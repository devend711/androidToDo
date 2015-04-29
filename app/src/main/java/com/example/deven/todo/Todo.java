package com.example.deven.todo;

/**
 * Created by Deven on 4/28/15.
 */
public class Todo {
    private String id;
    private String content;
    private Boolean done;

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

}
