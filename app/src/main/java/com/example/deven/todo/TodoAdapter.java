package com.example.deven.todo;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.facebook.rebound.SimpleSpringListener;
import com.facebook.rebound.Spring;
import com.facebook.rebound.SpringListener;
import com.facebook.rebound.SpringSystem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Deven on 4/29/15.
 */
public class TodoAdapter extends ArrayAdapter<Todo> {

    private SpringSystem springSystem;
    private Spring spring;
    private List<Todo> todos;
    private View pressedTodoView;
    private Integer pressedTodoId;

    public TodoAdapter(Context context, int resourceId, List<Todo> objects) {
        super(context, resourceId, objects);

        this.todos = objects;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = super.getView(position, convertView, parent);
        Todo todo = todos.get(position);
        if (todo.isDone()) {
            view.setBackgroundColor(parent.getContext().getResources().getColor(R.color.green));
        } else {
            view.setBackgroundColor(parent.getContext().getResources().getColor(R.color.orange));
        }

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Todo todo = todos.get(position);
                Intent intent = new Intent(getContext(), EditTodoActivity.class);
                intent.putExtra("todoId", todo.getId());
                intent.putExtra("todoContent", todo.getContent());
                intent.putExtra("todoDone", todo.isDone());
                getContext().startActivity(intent);
            }
        });

        view.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(final View view) {
                toggleTodo(position);
                Todo todo = todos.get(position);
                Todo.editInParse(todo.getId(), todo.getContent(), todo.isDone(), (MainActivity)getContext());
                Toast.makeText(getContext().getApplicationContext(), "Saved!", Toast.LENGTH_SHORT).show();
                return false;
            }
        });
        return view;
    }

    private void toggleTodo(Integer position) {
        todos.get(position).toggleDone();
        this.notifyDataSetChanged();
    }
}
