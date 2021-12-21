package com.hznu.lin.project.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "cityWeatherData")
public class CityWeatherData {
    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "date")
    private String date;

    @ColumnInfo(name = "low")
    private String low;

    @ColumnInfo(name = "high")
    private String hign;

    @Ignore
    public CityWeatherData(int id, String date, String low, String hign) {
        this.id = id;
        this.date = date;
        this.low = low;
        this.hign = hign;
    }

    public CityWeatherData() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getLow() {
        return low;
    }

    public void setLow(String low) {
        this.low = low;
    }

    public String getHign() {
        return hign;
    }

    public void setHign(String hign) {
        this.hign = hign;
    }

    @Override
    public String toString() {
        return "CityWeatherData{" +
                "id=" + id +
                ", date='" + date + '\'' +
                ", low='" + low + '\'' +
                ", hign='" + hign + '\'' +
                '}';
    }
}
