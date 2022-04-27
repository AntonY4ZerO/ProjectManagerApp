package com.antapp.projectstasksapp.ui.projectslist;

public class Project {

    private int id;
    private String title;
    private String deadline;
    private String description;
    private int tasksCount;

    public Project(int id, String title, String deadline, String description, int tasksCount) {
        this.id = id;
        this.title = title;
        this.deadline = deadline;
        this.description = description;
        this.tasksCount = tasksCount;
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

    public int getTasksCount() {
        return tasksCount;
    }

    public void setTasksCount(int tasksCount) {
        this.tasksCount = tasksCount;
    }
}
