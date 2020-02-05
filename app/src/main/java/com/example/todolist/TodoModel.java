package com.example.todolist;

import java.util.Date;

import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class TodoModel extends RealmObject {
    @PrimaryKey
    private Integer todoId;
    private String todo;
    private Date created;
    private Boolean isComplete;

    public Integer getTodoId() {
        return this.todoId;
    }

    public void setTodoId(Integer todoId) {
        this.todoId = todoId;
    }

    public String getTodo() {
        return this.todo;
    }

    public void setTodo(String todo) {
        this.todo = todo;
    }

    public Date getCreated() {
        return this.created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public Boolean getIsComplete() {
        return this.isComplete;
    }

    public void setIsComplete(Boolean isComplete) {
        this.isComplete = isComplete;
    }

    public static Integer getNextTodoId() {
        Realm realm = Realm.getDefaultInstance();
        // 初期化
        Integer nextTodoId = 1;
        // userIdの最大値を取得
        Number maxTodoId = realm.where(TodoModel.class).max("todoId");
        // 1度もデータが作成されていない場合はNULLが返ってくるため、NULLチェックをする
        if(maxTodoId != null) {
            nextTodoId = maxTodoId.intValue() + 1;
        }
        return nextTodoId;
    }
}
