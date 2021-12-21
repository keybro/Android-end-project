package com.hznu.lin.project.db;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.hznu.lin.project.dao.TestDao;
import com.hznu.lin.project.entity.Test;

@Database(entities = {Test.class}, version = 1)
public abstract class TestDataBase extends RoomDatabase {

    public abstract TestDao userDao();

}