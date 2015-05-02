package com.example.deven.todo;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;


public class EditTodoActivity extends Activity {
    private Todo todo;
    // editing controls
    private EditText editContent;
    private CheckBox editDone;
    private Button saveButton;
    private Button deleteButton;
    // store todo data
    private String newContent;
    private Boolean newDone;
    private Boolean alreadyMadeOne; // prevent multiple saves

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_todo);
        getActionBar().setDisplayHomeAsUpEnabled(true); // able to go back to main activity

        Intent intent = this.getIntent();

        editContent = (EditText) findViewById(R.id.todoContent);
        editDone = (CheckBox) findViewById(R.id.todoDone);
        saveButton = (Button) findViewById(R.id.saveTodoButton);
        deleteButton = (Button) findViewById(R.id.deleteTodoButton);

        alreadyMadeOne = false;

        // is this an existing note?
        if (intent.getExtras() != null) {
            getActionBar().setTitle(R.string.edit_todo_activity_title);
            deleteButton.setActivated(true);
            this.todo = new Todo(intent.getStringExtra("todoId"), intent.getStringExtra("todoContent"), intent.getBooleanExtra("todoDone", false));
            editContent.setText(todo.getContent());
            editDone.setChecked(todo.isDone());
            Log.e("info", "editing an existing note");
        } else {
            getActionBar().setTitle(R.string.add_todo_activity_title);
            deleteButton.setActivated(false);
            Log.e("info", "creating a new note");
        }

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteTodo();
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveTodo();
            }
        });

        setProgressBarIndeterminateVisibility(false);
    }

    private void saveTodo(){
        newContent = editContent.getText().toString().trim();
        newDone = editDone.isChecked();
        if (!newContent.isEmpty()) {
            if (todo==null && alreadyMadeOne == false) { // we're adding a new todo, not editing one
                alreadyMadeOne = true;
                Todo.buildForParse(newContent, newDone, this);
            } else { // we're editing this.todo
                Todo.editInParse(todo.getId(), newContent, newDone, this);
            }
        } else { // newContent was empty
            Toast.makeText(getApplicationContext(), "Add a description!", Toast.LENGTH_SHORT).show();
        }
    }

    private void deleteTodo(){
        ParseQuery<ParseObject> q = ParseQuery.getQuery("Todo");
        Log.d("info", "deleting a todo");
        q.getInBackground(todo.getId(), new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject todo, ParseException e) {
                if (e == null) {
                    todo.deleteEventually();
                    Toast.makeText(getApplicationContext(), "Deleted!", Toast.LENGTH_SHORT).show();
                } else {
                   Log.e("error deleting", e.getMessage());
                }
            }
        });
    }

    public void setTodo(Todo newTodo) {
        this.todo = newTodo;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == android.R.id.home) {
            onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
