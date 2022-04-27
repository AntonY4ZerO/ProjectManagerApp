package com.antapp.projectstasksapp.database;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * Класс для работы с конкретной записью в таблице задач в базе данных
 * Описывает поля в таблице задач в базе данных
 */
@Entity
public class TaskEntity {

    // Поле id является уникальным первичным ключем,
    // autoGenerate говорит о том что при создании новая задача будет автоматически получать свой уникальный id
    @PrimaryKey(autoGenerate = true)
    public int id;
    // id проекта, к которому отсносится данная задача
    public int projectId;
    private String title;
    private String deadline;
    private String description;
    private boolean isCompleted;

    public TaskEntity() {
    }

    // конструктор со всеми поля кроме id, так как id таблица будет генерировать автоматически
    public TaskEntity(int projectId, String title, String deadline, String description, boolean isCompleted) {
        this.projectId = projectId;
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

    public int getProjectId() {
        return projectId;
    }

    public void setProjectId(int projectId) {
        this.projectId = projectId;
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
