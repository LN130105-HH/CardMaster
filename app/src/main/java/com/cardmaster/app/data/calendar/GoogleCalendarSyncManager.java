package com.cardmaster.app.data.calendar;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import com.cardmaster.app.data.local.AppDatabase;
import com.cardmaster.app.data.local.entity.DeckEntity;
import com.cardmaster.app.data.local.entity.NotificationEntity;
import com.cardmaster.app.data.local.entity.StudyPlanEntity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.Executors;

public class GoogleCalendarSyncManager {

    public interface SyncCallback {
        void onSyncComplete(int createdCount, int updatedCount, int conflictResolvedCount, int deletedCount);
        void onConflictDetected(StudyPlanEntity plan, long remoteTime, Runnable onKeepApp, Runnable onKeepRemote);
        void onError(String error);
    }

    // Mock/Simulated Remote Google Calendar Event Store to verify anti-duplication & 2-way sync
    public static class GoogleCalendarEvent {
        public String eventId;
        public String summary;
        public long startTime;
        public long updatedTime;

        public GoogleCalendarEvent(String eventId, String summary, long startTime, long updatedTime) {
            this.eventId = eventId;
            this.summary = summary;
            this.startTime = startTime;
            this.updatedTime = updatedTime;
        }
    }

    private static final Map<String, GoogleCalendarEvent> REMOTE_CALENDAR = new HashMap<>();

    private final Context context;
    private final AppDatabase db;
    private final Handler mainHandler = new Handler(Looper.getMainLooper());

    public GoogleCalendarSyncManager(Context context) {
        this.context = context.getApplicationContext();
        this.db = AppDatabase.getInstance(this.context);
    }

    /**
     * Perform two-way Google Calendar synchronization with conflict resolution and anti-duplication
     */
    public void syncTwoWay(long userId, SyncCallback callback) {
        Executors.newSingleThreadExecutor().execute(() -> {
            try {
                List<StudyPlanEntity> plans = db.studyPlanDao().getPendingSyncPlans(userId);
                int created = 0;
                int updated = 0;
                int conflictCount = 0;
                int deleted = 0;

                long now = System.currentTimeMillis();

                for (StudyPlanEntity plan : plans) {
                    DeckEntity deck = db.deckDao().getById(plan.getDeckId());
                    String deckTitle = deck != null ? deck.getTitle() : "Ôn tập CardMaster";

                    String eventId = plan.getCalendarEventId();

                    // CASE 1: Plan deleted in App -> delete from Google Calendar
                    if (StudyPlanEntity.SYNC_DELETED.equals(plan.getSyncStatus())) {
                        if (eventId != null && REMOTE_CALENDAR.containsKey(eventId)) {
                            REMOTE_CALENDAR.remove(eventId);
                            deleted++;
                        }
                        db.studyPlanDao().delete(plan);
                        continue;
                    }

                    // CASE 2: No calendarEventId -> CREATE new event (Anti-duplication rule)
                    if (eventId == null || eventId.isEmpty()) {
                        String newEventId = "gcal_" + UUID.randomUUID().toString().substring(0, 8);
                        plan.setCalendarEventId(newEventId);
                        plan.setSyncStatus(StudyPlanEntity.SYNC_SYNCED);
                        plan.setLastSyncedAt(now);
                        plan.setLastModifiedAt(now);

                        REMOTE_CALENDAR.put(newEventId, new GoogleCalendarEvent(newEventId, "Ôn tập: " + deckTitle, plan.getScheduledTime(), now));
                        db.studyPlanDao().update(plan);
                        created++;
                    } else {
                        // CASE 3: calendarEventId already exists -> UPDATE existing event, do NOT create duplicate
                        GoogleCalendarEvent remoteEvent = REMOTE_CALENDAR.get(eventId);

                        if (remoteEvent != null) {
                            // Check for conflict: both modified
                            if (StudyPlanEntity.SYNC_CONFLICT.equals(plan.getSyncStatus()) ||
                                    (remoteEvent.updatedTime > plan.getLastSyncedAt() && plan.getLastModifiedAt() > plan.getLastSyncedAt())) {

                                conflictCount++;
                                // Compare last modified times: latest update wins
                                if (remoteEvent.updatedTime > plan.getLastModifiedAt()) {
                                    // Remote is newer -> update App plan
                                    plan.setScheduledTime(remoteEvent.startTime);
                                    plan.setSyncStatus(StudyPlanEntity.SYNC_SYNCED);
                                    plan.setLastSyncedAt(now);
                                    plan.setLastModifiedAt(remoteEvent.updatedTime);
                                    db.studyPlanDao().update(plan);
                                } else {
                                    // App is newer -> update Remote event
                                    remoteEvent.startTime = plan.getScheduledTime();
                                    remoteEvent.summary = "Ôn tập: " + deckTitle;
                                    remoteEvent.updatedTime = now;
                                    plan.setSyncStatus(StudyPlanEntity.SYNC_SYNCED);
                                    plan.setLastSyncedAt(now);
                                    plan.setLastModifiedAt(now);
                                    db.studyPlanDao().update(plan);
                                }
                                updated++;
                            } else {
                                // Normal update
                                remoteEvent.startTime = plan.getScheduledTime();
                                remoteEvent.summary = "Ôn tập: " + deckTitle;
                                remoteEvent.updatedTime = now;

                                plan.setSyncStatus(StudyPlanEntity.SYNC_SYNCED);
                                plan.setLastSyncedAt(now);
                                plan.setLastModifiedAt(now);
                                db.studyPlanDao().update(plan);
                                updated++;
                            }
                        } else {
                            // Re-create if missing remotely
                            REMOTE_CALENDAR.put(eventId, new GoogleCalendarEvent(eventId, "Ôn tập: " + deckTitle, plan.getScheduledTime(), now));
                            plan.setSyncStatus(StudyPlanEntity.SYNC_SYNCED);
                            plan.setLastSyncedAt(now);
                            plan.setLastModifiedAt(now);
                            db.studyPlanDao().update(plan);
                            updated++;
                        }
                    }
                }

                // Add notification
                db.notificationDao().insert(new NotificationEntity(
                        userId,
                        "Đồng bộ Google Calendar thành công",
                        "Đã đồng bộ " + (created + updated) + " lịch ôn tập với Google Calendar (Tạo mới: " + created + ", Cập nhật: " + updated + ").",
                        NotificationEntity.TYPE_REMINDER,
                        false,
                        now
                ));

                final int fCreated = created;
                final int fUpdated = updated;
                final int fConflict = conflictCount;
                final int fDeleted = deleted;

                mainHandler.post(() -> callback.onSyncComplete(fCreated, fUpdated, fConflict, fDeleted));
            } catch (Exception e) {
                mainHandler.post(() -> callback.onError(e.getMessage()));
            }
        });
    }

    public static int getRemoteEventCount() {
        return REMOTE_CALENDAR.size();
    }
}
