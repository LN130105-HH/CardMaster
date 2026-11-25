package com.cardmaster.app.util;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;

import com.cardmaster.app.R;
import com.cardmaster.app.data.local.AppDatabase;
import com.cardmaster.app.data.local.entity.DeckEntity;
import com.cardmaster.app.data.local.entity.NotificationEntity;
import com.cardmaster.app.data.local.entity.StudyPlanEntity;
import com.cardmaster.app.ui.planner.StudyPlannerActivity;

import java.util.List;
import java.util.concurrent.Executors;

public class StudyReminderReceiver extends BroadcastReceiver {

    public static final String CHANNEL_ID = "cardmaster_study_reminders";
    public static final String EXTRA_PLAN_ID = "EXTRA_PLAN_ID";
    public static final String EXTRA_USER_ID = "EXTRA_USER_ID";
    public static final String EXTRA_DECK_ID = "EXTRA_DECK_ID";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent == null) return;
        long planId = intent.getLongExtra(EXTRA_PLAN_ID, -1L);
        if (planId <= 0) return;

        PendingResult pendingResult = goAsync();
        Executors.newSingleThreadExecutor().execute(() -> {
            try {
                AppDatabase db = AppDatabase.getInstance(context);
                StudyPlanEntity plan = db.studyPlanDao().getById(planId);

                // Kiểm tra lại trong DB: nếu đã xóa hoặc tắt nhắc nhở thì bỏ qua
                if (plan == null || !plan.isRemind() || StudyPlanEntity.SYNC_DELETED.equals(plan.getSyncStatus())) {
                    return;
                }

                String deckTitle = "Bộ thẻ ôn tập";
                List<DeckEntity> userDecks = db.deckDao().getDecks(plan.getUserId());
                if (userDecks != null) {
                    for (DeckEntity deck : userDecks) {
                        if (deck.getId() == plan.getDeckId()) {
                            deckTitle = deck.getTitle();
                            break;
                        }
                    }
                }

                String title = "Đến giờ ôn tập CardMaster!";
                String content = "Đã đến lịch học bộ thẻ: " + deckTitle;

                showSystemNotification(context, (int) (plan.getId() % Integer.MAX_VALUE), title, content);

                // Lưu lịch sử thông báo vào database trong ứng dụng
                db.notificationDao().insert(new NotificationEntity(
                        plan.getUserId(),
                        title,
                        content,
                        NotificationEntity.TYPE_REMINDER,
                        false,
                        System.currentTimeMillis()
                ));

                // Nếu là lịch lặp lại (Hàng ngày / Hàng tuần), tự động đặt báo thức cho kỳ tiếp theo
                if (StudyPlanEntity.REPEAT_DAILY.equals(plan.getRepeatMode())
                        || StudyPlanEntity.REPEAT_WEEKLY.equals(plan.getRepeatMode())) {
                    long nextTime = StudyReminderScheduler.computeNextTriggerTime(
                            plan.getScheduledTime() + 60_000L,
                            plan.getRepeatMode()
                    );
                    plan.setScheduledTime(nextTime);
                    db.studyPlanDao().update(plan);
                    StudyReminderScheduler.scheduleOrUpdate(context, plan);
                }
            } finally {
                pendingResult.finish();
            }
        });
    }

    private void showSystemNotification(Context context, int notificationId, String title, String content) {
        createNotificationChannel(context);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS)
                    != PackageManager.PERMISSION_GRANTED) {
                return;
            }
        }

        Intent openIntent = new Intent(context, StudyPlannerActivity.class);
        openIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent contentPendingIntent = PendingIntent.getActivity(
                context,
                notificationId,
                openIntent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_style)
                .setContentTitle(title)
                .setContentText(content)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(content))
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true)
                .setContentIntent(contentPendingIntent);

        NotificationManagerCompat.from(context).notify(notificationId, builder.build());
    }

    public static void createNotificationChannel(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationManager manager = context.getSystemService(NotificationManager.class);
            if (manager == null) return;

            NotificationChannel existing = manager.getNotificationChannel(CHANNEL_ID);
            if (existing == null) {
                NotificationChannel channel = new NotificationChannel(
                        CHANNEL_ID,
                        "Nhắc nhở lịch ôn tập",
                        NotificationManager.IMPORTANCE_HIGH
                );
                channel.setDescription("Thông báo nhắc nhở khi đến giờ học trên CardMaster");
                channel.enableVibration(true);
                manager.createNotificationChannel(channel);
            }
        }
    }
}