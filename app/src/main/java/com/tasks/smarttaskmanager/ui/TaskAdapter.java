package com.tasks.smarttaskmanager.ui;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;
import com.tasks.smarttaskmanager.R;
import com.tasks.smarttaskmanager.data.Task;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class TaskAdapter extends ListAdapter<TaskDisplayItem, RecyclerView.ViewHolder> {

    private static final int VIEW_TYPE_HEADER = 0;
    private static final int VIEW_TYPE_TASK = 1;

    public interface OnTaskClickListener {
        void onEdit(Task task);
        void onDelete(Task task);
        void onCheckChanged(Task task, boolean isChecked);
    }

    private OnTaskClickListener listener;

    public void setOnTaskClickListener(OnTaskClickListener listener) {
        this.listener = listener;
    }

    public TaskAdapter() {
        super(DIFF_CALLBACK);
    }

    private static final DiffUtil.ItemCallback<TaskDisplayItem> DIFF_CALLBACK =
            new DiffUtil.ItemCallback<>() {
                @Override
                public boolean areItemsTheSame(@NonNull TaskDisplayItem oldItem, @NonNull TaskDisplayItem newItem) {
                    if (oldItem.isHeader && newItem.isHeader)
                        return oldItem.categoryName.equals(newItem.categoryName);
                    if (!oldItem.isHeader && !newItem.isHeader)
                        return oldItem.task.id == newItem.task.id;
                    return false;
                }

                @Override
                public boolean areContentsTheSame(@NonNull TaskDisplayItem oldItem, @NonNull TaskDisplayItem newItem) {
                    return oldItem.equals(newItem);
                }
            };

    @Override
    public int getItemViewType(int position) {
        return getItem(position).isHeader ? VIEW_TYPE_HEADER : VIEW_TYPE_TASK;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        if (viewType == VIEW_TYPE_HEADER) {
            View view = inflater.inflate(R.layout.item_category_header, parent, false);
            return new HeaderViewHolder(view);
        } else {
            View view = inflater.inflate(R.layout.item_task, parent, false);
            return new TaskViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        TaskDisplayItem item = getItem(position);

        if (item.isHeader) {
            HeaderViewHolder vh = (HeaderViewHolder) holder;
            vh.categoryName.setText(item.categoryName);
        } else {
            TaskViewHolder vh = (TaskViewHolder) holder;
            Task task = item.task;

            vh.title.setText(task.title);
            vh.description.setText(task.description);

            vh.title.setText(task.title);
            vh.description.setText(task.description);

            vh.deadline.setText(task.deadline != null
                    ? new SimpleDateFormat("yyyy-MM-dd hh:mm aa", Locale.getDefault()).format(task.deadline)
                    : "No deadline");
            vh.checkbox.setChecked(task.completed);
            vh.itemView.setAlpha(task.completed ? 0.5f : 1f);

            vh.checkbox.setOnCheckedChangeListener(null);
            vh.checkbox.setChecked(task.completed);
            vh.checkbox.setOnCheckedChangeListener((btn, checked) -> {
                if (listener != null) listener.onCheckChanged(task, checked);
            });

            vh.btnEdit.setOnClickListener(v -> {
                if (listener != null) listener.onEdit(task);
            });

            vh.btnDelete.setOnClickListener(v -> {
                if (listener != null) listener.onDelete(task);
            });
        }
    }

    static class HeaderViewHolder extends RecyclerView.ViewHolder {
        TextView categoryName;

        public HeaderViewHolder(@NonNull View itemView) {
            super(itemView);
            categoryName = itemView.findViewById(R.id.text_category_header);
        }
    }

    static class TaskViewHolder extends RecyclerView.ViewHolder {
        TextView title, description, deadline;
        CheckBox checkbox;
        ImageButton btnEdit, btnDelete;

        public TaskViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.text_title);
            description = itemView.findViewById(R.id.text_description);
            deadline = itemView.findViewById(R.id.text_deadline);
            checkbox = itemView.findViewById(R.id.checkbox_completed);
            btnEdit = itemView.findViewById(R.id.btn_edit);
            btnDelete = itemView.findViewById(R.id.btn_delete);
        }
    }
}
