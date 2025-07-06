package com.tasks.smarttaskmanager.data;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.ForeignKey;
import java.util.Date;

@Entity(foreignKeys = @ForeignKey(entity = Category.class,
        parentColumns = "id",
        childColumns = "categoryId"))
public class Task {
    @PrimaryKey(autoGenerate = true)
    public int id;

    public String title;
    public String description;
    public Date deadline;
    public boolean completed;
    public int categoryId;

    public Task(String title, String description, Date deadline, boolean completed, int categoryId) {
        this.title = title;
        this.description = description;
        this.deadline = deadline;
        this.completed = completed;
        this.categoryId = categoryId;
    }

    public Task(int id, String title, String description, Date deadline, boolean completed, int categoryId) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.deadline = deadline;
        this.completed = completed;
        this.categoryId = categoryId;
    }

    public Task() {
    }
}