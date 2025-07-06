package com.tasks.smarttaskmanager.data;

import android.app.Application;
import androidx.lifecycle.LiveData;
import java.util.List;
import java.util.concurrent.Executors;

public class TaskRepository {
    private TaskDao taskDao;
    private CategoryDao categoryDao;
    private LiveData<List<Task>> allTasks;
    private LiveData<List<Category>> allCategories;

    public TaskRepository(Application application) {
        AppDatabase db = AppDatabase.getInstance(application);
        taskDao = db.taskDao();
        categoryDao = db.categoryDao();
        allTasks = taskDao.getAllTasks();
        allCategories = categoryDao.getAllCategories();
    }

    public LiveData<List<Task>> getAllTasks() {
        return allTasks;
    }
    public LiveData<List<Task>> getTasksByCategory(int categoryId) {
        return taskDao.getTasksByCategory(categoryId);
    }

    public LiveData<Task> getTaskById(int id) {
        return taskDao.getTaskById(id);
    }
    public LiveData<List<Category>> getAllCategories() {
        return allCategories;
    }
    public void insertTask(Task task) {
        Executors.newSingleThreadExecutor().execute(() -> taskDao.insert(task));
    }
    public void updateTask(Task task) {
        Executors.newSingleThreadExecutor().execute(() -> taskDao.update(task));
    }
    public void deleteTask(Task task) {
        Executors.newSingleThreadExecutor().execute(() -> taskDao.delete(task));
    }
    public void insertCategory(Category category) {
        Executors.newSingleThreadExecutor().execute(() -> categoryDao.insert(category));
    }

    public void updateCategory(Category category) {
        Executors.newSingleThreadExecutor().execute(() -> categoryDao.update(category));
    }
}