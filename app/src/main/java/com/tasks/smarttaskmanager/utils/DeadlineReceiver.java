package com.tasks.smarttaskmanager.utils;

import android.content.*;
import android.app.*;
import androidx.core.app.NotificationCompat;
import com.tasks.smarttaskmanager.R;

public class DeadlineReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        int taskId = intent.getIntExtra("taskId", -1);
        String title = intent.getStringExtra("title");

        NotificationUtils.createNotificationChannel(context);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, NotificationUtils.CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle("Task Deadline Approaching")
                .setContentText("Task: " + title + " is due soon!")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true);

        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(taskId, builder.build());
    }
}