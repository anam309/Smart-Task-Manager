package com.tasks.smarttaskmanager.utils;

import android.app.*;
import android.content.*;
import android.os.Build;

import androidx.core.app.NotificationCompat;

import com.tasks.smarttaskmanager.R;

public class NotificationUtils {
    public static final String CHANNEL_ID = "task_deadline_channel";

    public static void createNotificationChannel(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Task Deadlines";
            String description = "Notifications for task deadlines";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    public static void scheduleDeadlineNotifications(Context context, int taskId, String title, long deadlineMillis) {
        long oneDayBefore = deadlineMillis - 24 * 60 * 60 * 1000; // 1 day before
        long oneHourBefore = deadlineMillis - 60 * 60 * 1000; // 1 hour before

        if (oneDayBefore > System.currentTimeMillis()) {
            scheduleSingleNotification(context, taskId * 10 + 1, title + " (Due in 1 day)", oneDayBefore);
        }

        if (oneHourBefore > System.currentTimeMillis()) {
            scheduleSingleNotification(context, taskId * 10 + 2, title + " (Due in 1 hour)", oneHourBefore);
        }
    }

    private static void scheduleSingleNotification(Context context, int notificationId, String contentText, long timeMillis) {
        Intent intent = new Intent(context, DeadlineReceiver.class);
        intent.putExtra("taskId", notificationId);
        intent.putExtra("title", contentText);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                context, notificationId, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, timeMillis, pendingIntent);
    }


    public static void cancelDeadlineNotifications(Context context, int taskId) {
        cancelSingleNotification(context, taskId * 10 + 1);
        cancelSingleNotification(context, taskId * 10 + 2);
    }

    private static void cancelSingleNotification(Context context, int notificationId) {
        Intent intent = new Intent(context, DeadlineReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                context, notificationId, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(pendingIntent);
    }

}