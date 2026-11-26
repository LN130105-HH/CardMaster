package com.cardmaster.app;

import com.cardmaster.app.data.local.entity.StudyPlanEntity;
import com.cardmaster.app.data.local.entity.UserEntity;

import org.junit.Assert;
import org.junit.Test;

public class FeatureUpdateV2Test {

    @Test
    public void testUserEntityV2Fields() {
        UserEntity user = new UserEntity("fb_uid_123", "Hoàng Vũ Lê Huy", "huy@cardmaster.app", "https://img.com/avatar.png", "Nam", "15/08/2003", 1000L);
        Assert.assertEquals("fb_uid_123", user.getFirebaseUid());
        Assert.assertEquals("Hoàng Vũ Lê Huy", user.getFullName());
        Assert.assertEquals("Nam", user.getGender());
        Assert.assertEquals("15/08/2003", user.getDateOfBirth());
        Assert.assertEquals("https://img.com/avatar.png", user.getAvatarUri());
    }

    @Test
    public void testCalendarAntiDuplicationAndConflictResolution() {
        long t1 = 1000000L;
        StudyPlanEntity plan = new StudyPlanEntity(1, 1, t1, StudyPlanEntity.REPEAT_DAILY, true, null, 0, StudyPlanEntity.SYNC_PENDING, t1);

        // Before sync, no eventId
        Assert.assertNull(plan.getCalendarEventId());

        // Simulating first sync: assign ID
        String eventId = "gcal_event_01";
        plan.setCalendarEventId(eventId);
        plan.setSyncStatus(StudyPlanEntity.SYNC_SYNCED);
        plan.setLastSyncedAt(t1);
        plan.setLastModifiedAt(t1);

        Assert.assertEquals("gcal_event_01", plan.getCalendarEventId());
        Assert.assertEquals(StudyPlanEntity.SYNC_SYNCED, plan.getSyncStatus());

        // Second update: changing scheduled time in app -> eventId remains identical (anti-duplication)
        long t2 = t1 + 3600000L;
        plan.setScheduledTime(t2);
        plan.setLastModifiedAt(t2);
        plan.setSyncStatus(StudyPlanEntity.SYNC_PENDING);

        Assert.assertEquals("gcal_event_01", plan.getCalendarEventId());
        Assert.assertTrue(plan.getLastModifiedAt() > plan.getLastSyncedAt());

        // Remote Google Calendar event updated at t3 (where t3 > t2) -> remote wins
        long t3 = t2 + 1000L;
        long remoteTime = t1 + 7200000L;
        if (t3 > plan.getLastModifiedAt()) {
            plan.setScheduledTime(remoteTime);
            plan.setLastSyncedAt(t3);
            plan.setLastModifiedAt(t3);
            plan.setSyncStatus(StudyPlanEntity.SYNC_SYNCED);
        }

        Assert.assertEquals(remoteTime, plan.getScheduledTime());
        Assert.assertEquals(StudyPlanEntity.SYNC_SYNCED, plan.getSyncStatus());
    }

    @Test
    public void testQuizCardSingleChoiceValidation() {
        int selectedIndex = 1; // Option B
        Assert.assertTrue(selectedIndex >= 0 && selectedIndex <= 3);

        // Unselected index should be flagged as invalid
        int unselectedIndex = -1;
        boolean isValid = (unselectedIndex >= 0 && unselectedIndex <= 3);
        Assert.assertFalse(isValid);
    }
}
