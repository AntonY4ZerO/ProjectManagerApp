package com.antapp.projectstasksapp.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

/**
 * Класс самой базы данных, описывающий все её возможности
 * внутри массива entities перечисляются все таблицы внутри неё
 */
@Database(entities = {ProjectEntity.class, TaskEntity.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {

    // метод для доступа к интерфейсу таблицы проектов
    public abstract ProjectDao projectDao();

    // метод для доступа к интерфейсу таблицы задач
    public abstract TaskDao taskDao();
}