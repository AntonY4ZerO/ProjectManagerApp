package com.antapp.projectstasksapp.database;

import android.content.Context;

import androidx.room.Room;

/**
 * Вспомогательный класс для удобного доступа к базе данных с любого экрана
 */
public class DatabaseHelper {

    // Сам объект базы данных
    // изначально null, но при первом запросе getDatabase будет проинициализирован
    private static AppDatabase database = null;

    // Метод для доступа к database, который инициализирует переменную database при необходимости, при первом вызове
    public static AppDatabase getDatabase(Context context){
        if (database == null) {
            // вызов databaseBuilder подключает к проекту базу данных, которая лежит в файле "database" в защищенной папке проекта,
            // если такого файла нет, то создаёт его
            database = Room.databaseBuilder(context, AppDatabase.class, "database")
                    .allowMainThreadQueries()
                    .build();
        }
        return database;
    }
}