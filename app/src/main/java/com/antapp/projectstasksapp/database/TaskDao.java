package com.antapp.projectstasksapp.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

/**
 * Предоставляет интерфейс взаимодействия с таблицой задач в базе данных
 */
@Dao
public interface TaskDao {

    // Запрос конкретной задачи по id
    @Query("SELECT * FROM TaskEntity WHERE id = :id")
    TaskEntity getById(int id);

    // Запрос всех задач проекта по id проекта
    @Query("SELECT * FROM TaskEntity WHERE projectId = :projectId")
    List<TaskEntity> getByProjectId(int projectId);

    // Вставка новой задачи в таблицу
    @Insert
    void insert(TaskEntity taskEntity);

    // Обновление существующей задачи в таблице
    @Update
    void update(TaskEntity taskEntity);

    // Удаление конкретной задачи по id
    @Query("DELETE FROM TaskEntity WHERE id = :id")
    void deleteById(int id);

    // Удаление всех задач проекта по id проекта
    @Query("DELETE FROM TaskEntity WHERE projectId = :projectId")
    void deleteByProjectId(int projectId);
}