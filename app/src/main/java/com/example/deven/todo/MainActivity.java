package com.example.deven.todo;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.parse.FindCallback;
import com.parse.Parse;

import com.parse.ParseAnalytics;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;


public class MainActivity extends ListActivity {

    private List<Todo> todos;
    private TodoAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.e("info", "onCreate started");
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);

        setContentView(R.layout.activity_main);
        MenuInflater inflater = getMenuInflater();

        Parse.initialize(this, "1AWLZWMg7EDT01XdQGkjKfuLdd6djw5ZU6yCdXhU", "xN0BbK8Nn4z669j5Wd8kMhZk1HkjHWrinOK0EeCX");
        // ParseAnalytics.trackAppOpened(getIntent());

        ParseUser currentUser = ParseUser.getCurrentUser();
        if (currentUser == null) {
            loadLoginView();
        }

        todos = new ArrayList<Todo>();
        adapter = new TodoAdapter(this, R.layout.list_item_layout, todos); // Todo.toString() -> each row
        setListAdapter(adapter);
        refreshTodos();
    }

    public void newTodo(View view) {
        Intent intent = new Intent(this, EditTodoActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        Log.d("info","resumed main!");
        super.onResume();
        refreshTodos();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        Log.d("info", "inflating menu!");
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id) {
            case R.id.action_refresh: {
                refreshTodos();
                break;
            }
            case R.id.action_help: {
                // Do something when user selects Settings from Action Bar overlay
                loadHelpView();
                break;
            }
            case R.id.action_logout: {
                ParseUser.logOut();
                loadLoginView();
                break;
            }
        }

        return super.onOptionsItemSelected(item);
    }

    public void refreshTodos() {
        Log.e("alert","refreshing todos!");

        ParseQuery<ParseObject> query = ParseQuery.getQuery("Todo");
        query.whereEqualTo("author", ParseUser.getCurrentUser());

        setProgressBarIndeterminateVisibility(true); // start spinner

        query.findInBackground(new FindCallback<ParseObject>() {

            @Override
            public void done(List<ParseObject> todoList, ParseException e) {
                if (e == null) {
                    // If there are results, update the list of posts
                    // and notify the adapter
                    todos.clear();
                    for (ParseObject todo : todoList) {
                        Todo newTodo = new Todo(todo.getObjectId(), todo.getString("content"), todo.getBoolean("done"));
                        todos.add(newTodo);
                    }
                    ((ArrayAdapter<Todo>) getListAdapter()).notifyDataSetChanged();
                    setProgressBarIndeterminateVisibility(false); // stop spinner
                } else {
                    Log.d(getClass().getSimpleName(), "Error: " + e.getMessage());
                }
            }
        });
    }

    private void loadLoginView() {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    private void loadHelpView() {
        Intent intent = new Intent(this, HelpActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

}
