package com.hznu.lin.project.db;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.hznu.lin.project.dao.WeatherDataDao;
import com.hznu.lin.project.entity.WeatherData;


@Database(entities = {WeatherData.class}, version = 1)
public abstract class WeatherDataBase extends RoomDatabase {
    public abstract WeatherDataDao weatherDataDao();

}
