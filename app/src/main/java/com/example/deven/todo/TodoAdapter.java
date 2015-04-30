package com.example.deven.todo;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Deven on 4/29/15.
 */
public class TodoAdapter extends ArrayAdapter<Todo> {
    private List<Todo> todos;

    public TodoAdapter(Context context, int resourceId, List<Todo> objects) {
        super(context, resourceId, objects);
        this.todos = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = super.getView(position, convertView, parent);
        Todo todo = todos.get(position);
        if (todo.isDone()) {
            view.setBackgroundColor(Color.rgb(0,168,83)); // green  bg if done
        } else {
            view.setBackgroundColor(Color.rgb(231,120,0)); // organge bg if todo
        }
        return view;
    }
}
