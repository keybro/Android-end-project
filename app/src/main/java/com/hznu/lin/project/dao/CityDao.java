package com.hznu.lin.project.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.hznu.lin.project.entity.City;

import java.util.List;

@Dao
public interface CityDao {

    @Insert
    void insertAll(City... cities);

    @Query("SELECT name FROM city")
    List<String> getPreferCity();

    @Delete
    void delete(City city);


}
