package com.hznu.lin.project.db;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.hznu.lin.project.dao.CityDao;
import com.hznu.lin.project.entity.City;

@Database(entities = {City.class},version = 1)
public abstract class CityDataBase extends RoomDatabase {
    public abstract CityDao cityDao();
}
