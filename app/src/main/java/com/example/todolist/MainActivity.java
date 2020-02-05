package com.example.todolist;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.todolist.adapter.TodoRecyclerAdapter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;
import io.realm.Sort;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Realm.init(this);
        final Realm realm = Realm.getDefaultInstance();

        Button button = findViewById(R.id.add_todo_button);
        final EditText editText = findViewById(R.id.todo_text_input);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                realm.beginTransaction();
                Log.d("TAG:", "テスト");
                TodoModel todoModel = realm.createObject(TodoModel.class, TodoModel.getNextTodoId());
                todoModel.setTodo(editText.getText().toString());
                todoModel.setIsComplete(false);
                todoModel.setCreated(new Date());
                editText.setText("");

                realm.commitTransaction();
            }
        });

        RecyclerView recyclerView = findViewById(R.id.todo_recycler_view);
        final TodoRecyclerAdapter adapter = new TodoRecyclerAdapter();
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, 1));
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter.setListener(new TodoRecyclerAdapter.ClickListener() {
            @Override
            public void checkboxListener(int id, boolean check) {
                // check status
                if(check) {
                    Realm realm = Realm.getDefaultInstance();

                    realm.beginTransaction();

                    RealmResults<TodoModel> finds = realm.where(TodoModel.class).equalTo("todoId", id).findAll();

                    if(finds.size() > 0) {
                        // 更新
                        finds.get(0).setIsComplete(check);
                    }
                    realm.commitTransaction();
                }
            }
        });

        realm.addChangeListener(new RealmChangeListener<Realm>() {
            @Override
            public void onChange(Realm realm) {
                Date now = new Date();
                Calendar before = Calendar.getInstance();
                before.setTime(now);
                before.add(Calendar.HOUR, -24);
                List<TodoModel> list = realm.where(TodoModel.class).equalTo("isComplete", false).between("created", before.getTime(), now).findAllSorted("created", Sort.DESCENDING);
                adapter.setItem(list);
                adapter.notifyDataSetChanged();
            }
        });
        Date now = new Date();
        Calendar before = Calendar.getInstance();
        before.setTime(now);
        before.add(Calendar.HOUR, -24);
        List<TodoModel> list = realm.where(TodoModel.class).equalTo("isComplete", false).between("created", before.getTime(), now).findAllSorted("created", Sort.DESCENDING);
        adapter.setItem(list);
        adapter.notifyDataSetChanged();
    }


    @Override
    protected void onDestroy() {

        super.onDestroy();
    }
}
