package com.antapp.projectstasksapp.ui.tasklist;

public class Task {

    private int id;
    private String title;
    private String deadline;
    private String description;
    private boolean isCompleted;

    public Task(int id, String title, String deadline, String description, boolean isCompleted) {
        this.id = id;
        this.title = title;
        this.deadline = deadline;
        this.description = description;
        this.isCompleted = isCompleted;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDeadline() {
        return deadline;
    }

    public void setDeadline(String deadline) {
        this.deadline = deadline;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    public void setCompleted(boolean completed) {
        isCompleted = completed;
    }
}
