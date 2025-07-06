package com.tasks.smarttaskmanager.ui;

import com.tasks.smarttaskmanager.data.Task;

import java.util.Objects;

public class TaskDisplayItem {
    public final boolean isHeader;
    public final String categoryName;
    public final Task task;

    // Constructors
    public TaskDisplayItem(String categoryName) {
        this.isHeader = true;
        this.categoryName = categoryName;
        this.task = null;
    }
    public TaskDisplayItem(Task task) {
        this.isHeader = false;
        this.categoryName = null;
        this.task = task;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof TaskDisplayItem)) return false;
        TaskDisplayItem other = (TaskDisplayItem) obj;

        if (this.isHeader != other.isHeader) return false;

        if (isHeader) {
            return Objects.equals(this.categoryName, other.categoryName);
        } else {
            return this.task != null && other.task != null && this.task.id == other.task.id &&
                    Objects.equals(this.task.title, other.task.title) &&
                    Objects.equals(this.task.description, other.task.description) &&
                    Objects.equals(this.task.deadline, other.task.deadline) &&
                    this.task.completed == other.task.completed &&
                    this.task.categoryId == other.task.categoryId;
        }
    }

    @Override
    public int hashCode() {
        return Objects.hash(isHeader, categoryName, task != null ? task.id : -1);
    }
}
