package com.tasks.smarttaskmanager.ui;

import android.Manifest;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.tasks.smarttaskmanager.R;
import com.tasks.smarttaskmanager.data.Category;
import com.tasks.smarttaskmanager.data.Task;
import com.tasks.smarttaskmanager.utils.NotificationUtils;
import com.tasks.smarttaskmanager.viewmodels.TaskViewModel;

import java.util.*;

public class MainActivity extends AppCompatActivity {
    private static final int REQUEST_CODE_POST_NOTIFICATIONS = 101;

    private TaskViewModel taskViewModel;
    private TaskAdapter adapter;

    private List<Task> latestTasks = null;
    private List<Category> latestCategories = null;
    private static final String CHANNEL_ID = "task_notifications_channel";

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Task Notifications";
            String description = "Notifications for task deadlines and updates";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
//        NotificationUtils.createNotificationChannel(this);
//
//        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, NotificationUtils.CHANNEL_ID)
//                .setSmallIcon(R.drawable.ic_launcher_foreground)
//                .setContentTitle("Test Notification")
//                .setContentText("This is a test notification.")
//                .setPriority(NotificationCompat.PRIORITY_HIGH)
//                .setAutoCancel(true);
//
//        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//        manager.notify(1001, builder.build());

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        createNotificationChannel();

        checkExactAlarmPermission();

        Button addTaskButton = findViewById(R.id.btn_add_task);
        Button addCategoryButton = findViewById(R.id.btn_add_category);
        RecyclerView recyclerView = findViewById(R.id.recycler_tasks);

        // Open add task screen
        addTaskButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AddEditTaskActivity.class);
            startActivity(intent);
        });

        // Open add category screen
        addCategoryButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AddEditCategoryActivity.class);
            startActivity(intent);
        });

        // Request notification permission (Android 13+)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS)
                    != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.POST_NOTIFICATIONS}, REQUEST_CODE_POST_NOTIFICATIONS);
            }
        }

        // Set up RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new TaskAdapter();
        recyclerView.setAdapter(adapter);

        // ViewModel setup
        taskViewModel = new ViewModelProvider(this).get(TaskViewModel.class);

        // Handle task interactions
        adapter.setOnTaskClickListener(new TaskAdapter.OnTaskClickListener() {
            @Override
            public void onEdit(Task task) {
                Intent intent = new Intent(MainActivity.this, AddEditTaskActivity.class);
                intent.putExtra("TASK_ID", task.id);
                startActivity(intent);
            }

            @Override
            public void onDelete(Task task) {
                NotificationUtils.cancelDeadlineNotifications(MainActivity.this, task.id);
                taskViewModel.deleteTask(task);
                Toast.makeText(MainActivity.this, "Task deleted", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onCheckChanged(Task task, boolean isChecked) {
                task.completed = isChecked;
                taskViewModel.updateTask(task);
                Toast.makeText(MainActivity.this, "Task " + (isChecked ? "completed" : "marked incomplete"), Toast.LENGTH_SHORT).show();

            }
        });

        // Observe LiveData once, combine responses
        taskViewModel.getAllTasks().observe(this, tasks -> {
            latestTasks = tasks;
            maybeUpdateUI();
        });

        taskViewModel.getAllCategories().observe(this, categories -> {
            latestCategories = categories;
            maybeUpdateUI();
        });
    }

    private void maybeUpdateUI() {
        if (latestTasks != null && latestCategories != null) {
            List<TaskDisplayItem> items = groupTasksByCategory(latestTasks, latestCategories);
            adapter.submitList(items);
        }
    }

    private List<TaskDisplayItem> groupTasksByCategory(List<Task> tasks, List<Category> categories) {
        List<TaskDisplayItem> displayItems = new ArrayList<>();

        Map<Integer, String> categoryMap = new HashMap<>();
        for (Category cat : categories) {
            categoryMap.put(cat.getId(), cat.getName());
        }

        Map<Integer, List<Task>> tasksByCategory = new HashMap<>();
        for (Task task : tasks) {
            tasksByCategory.computeIfAbsent(task.categoryId, k -> new ArrayList<>()).add(task);
        }

        for (Category category : categories) {
            List<Task> tasksForCategory = tasksByCategory.get(category.getId());
            if (tasksForCategory != null && !tasksForCategory.isEmpty()) {
                displayItems.add(new TaskDisplayItem(category.getName()));
                for (Task t : tasksForCategory) {
                    displayItems.add(new TaskDisplayItem(t));
                }
            }
        }

        return displayItems;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_POST_NOTIFICATIONS) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted
            } else {
                Toast.makeText(this, "Notification permission denied. You may not receive reminders.", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void checkExactAlarmPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {  // Android 12+
            AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
            if (!alarmManager.canScheduleExactAlarms()) {
                // Permission NOT granted, show dialog to guide user to settings
                new AlertDialog.Builder(this)
                        .setTitle("Exact Alarm Permission Required")
                        .setMessage("This app needs permission to schedule exact alarms for timely notifications.")
                        .setPositiveButton("Grant", (dialog, which) -> {
                            Intent intent = new Intent();
                            intent.setAction(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM);
                            intent.setData(Uri.parse("package:" + getPackageName()));
                            startActivity(intent);
                        })
                        .setNegativeButton("Cancel", null)
                        .show();
            }
        }
    }
}
