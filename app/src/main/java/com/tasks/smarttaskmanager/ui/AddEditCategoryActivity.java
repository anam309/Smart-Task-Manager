package com.tasks.smarttaskmanager.ui;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.tasks.smarttaskmanager.R;
import com.tasks.smarttaskmanager.data.AppDatabase;
import com.tasks.smarttaskmanager.data.Category;
import com.tasks.smarttaskmanager.viewmodels.TaskViewModel;

public class AddEditCategoryActivity extends AppCompatActivity {

    private EditText edtCategoryName;
    private Button btnSave;
    private int categoryId = -1;
    private TaskViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_category);

        edtCategoryName = findViewById(R.id.edit_category_name);
        btnSave = findViewById(R.id.btn_save_category);

        viewModel = new ViewModelProvider(this).get(TaskViewModel.class);

        if (getIntent().hasExtra("categoryId")) {
            categoryId = getIntent().getIntExtra("categoryId", -1);
            new Thread(() -> {
                Category category = AppDatabase.getInstance(getApplicationContext())
                        .categoryDao().getCategoryById(categoryId);
                runOnUiThread(() -> {
                    if (category != null) {
                        edtCategoryName.setText(category.getName());
                    }
                });
            }).start();
        }

        btnSave.setOnClickListener(v -> saveCategory());
    }

    private void saveCategory() {
        String name = edtCategoryName.getText().toString().trim();

        if (name.isEmpty()) {
            Toast.makeText(this, "Category name is required", Toast.LENGTH_SHORT).show();
            return;
        }

        if (categoryId != -1) {
            Category updated = new Category(categoryId, name);
            viewModel.updateCategory(updated);
        } else {
            Category newCategory = new Category(name);
            viewModel.insertCategory(newCategory);
        }

        Toast.makeText(this, "Category saved", Toast.LENGTH_SHORT).show();
        finish();
    }
}
