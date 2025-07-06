package com.tasks.smarttaskmanager.ui;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import com.tasks.smarttaskmanager.data.*;
import com.tasks.smarttaskmanager.utils.NotificationUtils;
import com.tasks.smarttaskmanager.viewmodels.TaskViewModel;
import com.tasks.smarttaskmanager.R;
import java.text.SimpleDateFormat;
import java.util.*;

public class AddEditTaskActivity extends AppCompatActivity {
    private EditText edtTitle, edtDescription;
    private Spinner spnCategory;
    private Button btnDeadline, btnSave;
    private CheckBox chkCompleted;
    private Date selectedDate = null;
    private int taskId = -1;
    private List<Category> categories;
    private TaskViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_task);

        edtTitle = findViewById(R.id.edit_title);
        edtDescription = findViewById(R.id.edit_description);
        spnCategory = findViewById(R.id.spinner_category);
        btnDeadline = findViewById(R.id.btn_deadline);
        btnSave = findViewById(R.id.btn_save);
        chkCompleted = findViewById(R.id.checkbox_edit_completed);

        viewModel = new ViewModelProvider(this).get(TaskViewModel.class);

        viewModel.getAllCategories().observe(this, cats -> {
            categories = cats;
            ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                    android.R.layout.simple_spinner_item,
                    getCategoryNames(cats));
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spnCategory.setAdapter(adapter);
        });

        if (getIntent().hasExtra("TASK_ID")) {
            taskId = getIntent().getIntExtra("TASK_ID", -1);
            if (taskId != -1) {
                viewModel.getTaskById(taskId).observe(this, task -> {
                    if (task != null) {
                        loadTask(task);
                    }
                });
            }
        }

        btnDeadline.setOnClickListener(v -> showDatePickerDialog());
        btnSave.setOnClickListener(v -> saveTask());
    }

    private void loadTask(Task task) {
        edtTitle.setText(task.title);
        edtDescription.setText(task.description);
        selectedDate = task.deadline;
        btnDeadline.setText(selectedDate != null
                ? new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(selectedDate)
                : "Set Deadline");
        chkCompleted.setChecked(task.completed);

        if (categories != null) {
            for (int i = 0; i < categories.size(); i++) {
                if (categories.get(i).getId() == task.categoryId) {
                    spnCategory.setSelection(i);
                    break;
                }
            }
        }
    }

    private void showDatePickerDialog() {
        Calendar calendar = Calendar.getInstance();
        if (selectedDate != null) calendar.setTime(selectedDate);

        new DatePickerDialog(this, (view, year, month, dayOfMonth) -> {
            calendar.set(year, month, dayOfMonth);
            showTimePickerDialog(calendar);

        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    private void showTimePickerDialog(Calendar calendar) {
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        new TimePickerDialog(this, (view, hourOfDay, minute1) -> {
            calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
            calendar.set(Calendar.MINUTE, minute1);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND, 0);

            selectedDate = calendar.getTime();
            btnDeadline.setText(selectedDate != null
                    ? new SimpleDateFormat("yyyy-MM-dd hh:mm aa", Locale.getDefault()).format(selectedDate)
                    : "Set Deadline");

        }, hour, minute, true).show(); // true for 24-hour format, false for AM/PM
    }


    private void saveTask() {
        String title = edtTitle.getText().toString().trim();
        String desc = edtDescription.getText().toString().trim();
        boolean completed = chkCompleted.isChecked();
        int catPos = spnCategory.getSelectedItemPosition();

        if (title.isEmpty() || catPos == AdapterView.INVALID_POSITION) {
            Toast.makeText(this, "Title and Category required", Toast.LENGTH_SHORT).show();
            return;
        }

        int categoryId = categories.get(catPos).getId();
        Task task;

        if (taskId != -1) {
            task = new Task(taskId, title, desc, selectedDate, completed, categoryId);
            viewModel.updateTask(task);
        } else {
            task = new Task(title, desc, selectedDate, completed, categoryId);
            viewModel.insertTask(task);
        }
        if (selectedDate != null) {
            NotificationUtils.scheduleDeadlineNotifications(
                    this,
                    task.id,
                    task.title,
                    selectedDate.getTime()
            );
        } else {
            NotificationUtils.cancelDeadlineNotifications(this, task.id);
        }

        finish();
    }

    private List<String> getCategoryNames(List<Category> cats) {
        List<String> names = new ArrayList<>();
        for (Category c : cats) {
            names.add(c.getName());
        }
        return names;
    }
}
