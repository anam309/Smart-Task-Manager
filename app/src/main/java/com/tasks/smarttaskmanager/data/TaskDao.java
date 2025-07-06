package com.tasks.smarttaskmanager.data;

import androidx.lifecycle.LiveData;
import androidx.room.*;
import java.util.List;

@Dao
public interface TaskDao {
    @Query("SELECT * FROM Task ORDER BY deadline ASC")
    LiveData<List<Task>> getAllTasks();

    @Query("SELECT * FROM Task WHERE categoryId = :categoryId")
    LiveData<List<Task>> getTasksByCategory(int categoryId);

    @Insert
    void insert(Task task);

    @Update
    void update(Task task);

    @Delete
    void delete(Task task);

    @Query("SELECT * FROM Task WHERE id = :id LIMIT 1")
    LiveData<Task> getTaskById(int id);
}