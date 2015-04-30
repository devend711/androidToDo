package com.example.deven.todo;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

/**
 * Created by Deven on 4/28/15.
 */
public class Todo {
    private String id;
    private String content;
    private Boolean done;
    private Boolean success; // keep track of parse success/failure

    Todo(String newId, String newContent, Boolean newDone){
        id = newId;
        content = newContent;
        done = newDone;
    }

    Todo(String newId, String newContent){
        id = newId;
        content = newContent;
        done = false;
    }

    @Override
    public String toString() {
        return getContent();
    }

    // getters

    public String getId() {
        return id;
    }

    public Boolean isDone() {
        return done;
    }

    public String getContent() {
        return content;
    }

    // setters

    public void toggleDone() {
        this.done = !this.done;
    }

    public void setDone() {
        this.done = true;
    }

    public void setNotDone() {
        this.done = false;
    }

    public void setContent(String newContent) {
        this.content = newContent;
    }

    // constructor to upload a todo object to parse
    public static void buildForParse(final String newContent, final Boolean newDone, final Activity activity) {
        final ParseObject newTodo = new ParseObject("Todo");
        newTodo.put("content", newContent);
        newTodo.put("done", newDone);
        activity.setProgressBarIndeterminateVisibility(true);
        newTodo.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                activity.setProgressBarIndeterminateVisibility(false);
                if (e != null) {
                    Log.d(getClass().getSimpleName(), "Couldn't save todo");
                    Toast.makeText( activity.getApplicationContext(), "Couldn't save task", Toast.LENGTH_SHORT).show();
                } else {
                    ((EditTodoActivity) activity).setTodo(new Todo(newTodo.getObjectId(), newContent, newDone)); // update MainActivity's todo
                    Toast.makeText( activity.getApplicationContext(), "Saved!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public static void editInParse(String todoId, final String newContent, final Boolean newDone, final Activity activity) {
        ParseQuery<ParseObject> q = ParseQuery.getQuery("Todo");
        q.getInBackground(todoId, new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject todo, ParseException e) {
                if (e == null) {
                    todo.put("content", newContent);
                    todo.put("done", newDone);
                    activity.setProgressBarIndeterminateVisibility(true);
                    todo.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            activity.setProgressBarIndeterminateVisibility(false);
                            if (e == null) {
                                Toast.makeText( activity.getApplicationContext(), "Saved!", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText( activity.getApplicationContext(), "Couldn't save task", Toast.LENGTH_SHORT).show();
                            }
                            //success = true;
                        }
                    });
                }
            }
        });
    }
}
