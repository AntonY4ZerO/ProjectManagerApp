package com.antapp.projectstasksapp.database;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * Класс для работы с конкретной записью в таблице проектов в базе данных
 * Описывает поля в таблице проектов в базе данных
 */
@Entity
public class ProjectEntity {

    // Поле id является уникальным первичным ключем,
    // autoGenerate говорит о том что при создании новый проект будет автоматически получать свой уникальный id
    @PrimaryKey(autoGenerate = true)
    public int id;
    private String title;
    private String deadline;
    private String description;

    public ProjectEntity() {
    }

    // конструктор со всеми поля кроме id, так как id таблица будет генерировать автоматически
    public ProjectEntity(String title, String deadline, String description) {
        this.title = title;
        this.deadline = deadline;
        this.description = description;
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
}
