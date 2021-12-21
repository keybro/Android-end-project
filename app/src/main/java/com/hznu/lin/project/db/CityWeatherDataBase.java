package com.hznu.lin.project.db;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.hznu.lin.project.dao.CityWeatherDao;
import com.hznu.lin.project.entity.CityWeatherData;

@Database(entities = {CityWeatherData.class},version = 1)
public abstract class CityWeatherDataBase extends RoomDatabase {
    public abstract CityWeatherDao cityWeatherDao();

}
