package com.antapp.projectstasksapp.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

/**
 * Предоставляет интерфейс взаимодействия с таблицой проектов в базе данных
 */
@Dao
public interface ProjectDao {

    // Запрос всех проектов
    @Query("SELECT * FROM ProjectEntity")
    List<ProjectEntity> getAll();

    // Запрос конкретного проекта по id
    @Query("SELECT * FROM ProjectEntity WHERE id = :id")
    ProjectEntity getById(int id);

    // Вставка нового проекта в таблицу
    @Insert
    void insert(ProjectEntity projectEntity);

    // Обновление существующего проекта в таблице
    @Update
    void update(ProjectEntity projectEntity);

    // Удаление конкретного проекта по id
    @Query("DELETE FROM ProjectEntity WHERE id = :id")
    void deleteById(int id);
}