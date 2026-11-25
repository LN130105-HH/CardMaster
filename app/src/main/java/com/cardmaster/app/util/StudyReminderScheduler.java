package com.cardmaster.app.util;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import com.cardmaster.app.data.local.entity.StudyPlanEntity;

import java.util.Calendar;

public class StudyReminderScheduler {

    public static void scheduleOrUpdate(Context context, StudyPlanEntity plan) {
        if (context == null || plan == null || plan.getId() <= 0) return;

        // Nếu tắt nhắc nhở hoặc lịch đã bị đánh dấu xóa -> hủy báo thức ngay
        if (!plan.isRemind() || StudyPlanEntity.SYNC_DELETED.equals(plan.getSyncStatus())) {
            cancel(context, plan.getId());
            return;
        }

        long triggerTime = computeNextTriggerTime(plan.getScheduledTime(), plan.getRepeatMode());
        if (triggerTime <= System.currentTimeMillis()) {
            // Lịch một lần (ONCE) đã qua thời điểm hiện tại -> không đặt báo thức quá khứ
            cancel(context, plan.getId());
            return;
        }

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        if (alarmManager == null) return;

        PendingIntent pendingIntent = buildPendingIntent(context, plan.getId(), plan.getUserId(), plan.getDeckId());

        // Hủy báo thức cũ (nếu có) trước khi đặt lại để đảm bảo không bao giờ trùng lặp
        alarmManager.cancel(pendingIntent);

        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                if (alarmManager.canScheduleExactAlarms()) {
                    alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, triggerTime, pendingIntent);
                } else {
                    alarmManager.setAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, triggerTime, pendingIntent);
                }
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, triggerTime, pendingIntent);
            } else {
                alarmManager.setExact(AlarmManager.RTC_WAKEUP, triggerTime, pendingIntent);
            }
        } catch (SecurityException e) {
            alarmManager.setAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, triggerTime, pendingIntent);
        }
    }

    public static void cancel(Context context, long planId) {
        if (context == null || planId <= 0) return;
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        if (alarmManager == null) return;

        PendingIntent pendingIntent = buildPendingIntent(context, planId, -1L, -1L);
        alarmManager.cancel(pendingIntent);
        pendingIntent.cancel();
    }

    private static PendingIntent buildPendingIntent(Context context, long planId, long userId, long deckId) {
        Intent intent = new Intent(context, StudyReminderReceiver.class);
        intent.putExtra(StudyReminderReceiver.EXTRA_PLAN_ID, planId);
        intent.putExtra(StudyReminderReceiver.EXTRA_USER_ID, userId);
        intent.putExtra(StudyReminderReceiver.EXTRA_DECK_ID, deckId);

        int requestCode = (int) (planId % Integer.MAX_VALUE);
        int flags = PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE;
        return PendingIntent.getBroadcast(context, requestCode, intent, flags);
    }

    public static long computeNextTriggerTime(long scheduledTime, String repeatMode) {
        long now = System.currentTimeMillis();
        if (scheduledTime > now) {
            return scheduledTime;
        }

        if (StudyPlanEntity.REPEAT_DAILY.equals(repeatMode)) {
            Calendar cal = Calendar.getInstance();
            cal.setTimeInMillis(scheduledTime);
            while (cal.getTimeInMillis() <= now) {
                cal.add(Calendar.DAY_OF_YEAR, 1);
            }
            return cal.getTimeInMillis();
        }

        if (StudyPlanEntity.REPEAT_WEEKLY.equals(repeatMode)) {
            Calendar cal = Calendar.getInstance();
            cal.setTimeInMillis(scheduledTime);
            while (cal.getTimeInMillis() <= now) {
                cal.add(Calendar.WEEK_OF_YEAR, 1);
            }
            return cal.getTimeInMillis();
        }

        return scheduledTime;
    }
}