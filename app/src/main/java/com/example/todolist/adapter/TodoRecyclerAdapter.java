package com.example.todolist.adapter;

import android.icu.text.SimpleDateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todolist.R;
import com.example.todolist.TodoModel;

import java.util.ArrayList;
import java.util.List;

public class TodoRecyclerAdapter extends RecyclerView.Adapter<TodoRecyclerAdapter.ViewHolder> {

    private List<TodoModel> list = new ArrayList<>();

    public interface ClickListener {
        void checkboxListener(int id, boolean check);
    }

    private ClickListener listener = null;

    public void setListener(ClickListener listener) {
        this.listener = listener;
    }

    public void setItem(List<TodoModel> list) {
        this.list.clear();
        this.list.addAll(list);
    }

    @NonNull
    @Override
    public TodoRecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull TodoRecyclerAdapter.ViewHolder holder, final int position) {
        TodoModel todoModel = list.get(position);
        holder.todoId = todoModel.getTodoId();
        holder.checkBox.setChecked(todoModel.getIsComplete());
        holder.textView.setText(todoModel.getTodo());
        SimpleDateFormat sdf = new SimpleDateFormat("作成日：yyyy年MM月dd日 HH時mm分ss秒");
        holder.dateTextView.setText(sdf.format(todoModel.getCreated()));
        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (listener != null) {
                    listener.checkboxListener(list.get(position).getTodoId(), isChecked);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        public CheckBox checkBox = null;
        public TextView textView = null;
        public TextView dateTextView = null;
        public Integer todoId = null;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            checkBox = itemView.findViewById(R.id.todo_list_checkbox);
            textView = itemView.findViewById(R.id.todo_list_text);
            dateTextView = itemView.findViewById(R.id.todo_list_date);
        }
    }
}
