package com.tasks.smarttaskmanager.viewmodels;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import com.tasks.smarttaskmanager.data.*;
import java.util.List;

public class TaskViewModel extends AndroidViewModel {
    private TaskRepository repository;

    public TaskViewModel(@NonNull Application application) {
        super(application);
        repository = new TaskRepository(application);
    }

    public LiveData<List<Task>> getAllTasks() {
        return repository.getAllTasks();
    }

    public LiveData<Task> getTaskById(int id){
        return repository.getTaskById(id);
    }
    public LiveData<List<Task>> getTasksByCategory(int categoryId) {
        return repository.getTasksByCategory(categoryId);
    }
    public LiveData<List<Category>> getAllCategories() {
        return repository.getAllCategories();
    }
    public void insertTask(Task task) {
        repository.insertTask(task);
    }
    public void updateTask(Task task) {
        repository.updateTask(task);
    }
    public void deleteTask(Task task) {
        repository.deleteTask(task);
    }
    public void insertCategory(Category category) {
        repository.insertCategory(category);
    }
    public void updateCategory(Category category) {
        repository.updateCategory(category);
    }
}