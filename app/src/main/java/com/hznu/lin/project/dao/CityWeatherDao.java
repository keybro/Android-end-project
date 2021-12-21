package com.hznu.lin.project.dao;

import androidx.room.Dao;
import androidx.room.Insert;

import com.hznu.lin.project.entity.CityWeatherData;

@Dao
public interface CityWeatherDao {

    @Insert
    void insertAll(CityWeatherData... cityWeatherData);

}
