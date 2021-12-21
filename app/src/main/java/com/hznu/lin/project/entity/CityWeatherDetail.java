package com.hznu.lin.project.entity;

public class CityWeatherDetail {
    //气温
    private String temperature;
    //湿度
    private String humidity;
    //天气
    private String weather;
    //风
    private String wind;
    //风级
    private String winp;
    private String temp_high;
    private String temp_low;

    public CityWeatherDetail(String temperature, String humidity, String weather, String wind, String winp, String temp_high, String temp_low) {
        this.temperature = temperature;
        this.humidity = humidity;
        this.weather = weather;
        this.wind = wind;
        this.winp = winp;
        this.temp_high = temp_high;
        this.temp_low = temp_low;
    }

    public String getTemp_high() {
        return temp_high;
    }

    public void setTemp_high(String temp_high) {
        this.temp_high = temp_high;
    }

    public String getTemp_low() {
        return temp_low;
    }

    public void setTemp_low(String temp_low) {
        this.temp_low = temp_low;
    }

    public String getTemperature() {
        return temperature;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }

    public String getHumidity() {
        return humidity;
    }

    public void setHumidity(String humidity) {
        this.humidity = humidity;
    }

    public String getWeather() {
        return weather;
    }

    public void setWeather(String weather) {
        this.weather = weather;
    }

    public String getWind() {
        return wind;
    }

    public void setWind(String wind) {
        this.wind = wind;
    }

    public String getWinp() {
        return winp;
    }

    public void setWinp(String winp) {
        this.winp = winp;
    }

    @Override
    public String toString() {
        return "CityWeatherDetail{" +
                "temperature='" + temperature + '\'' +
                ", humidity='" + humidity + '\'' +
                ", weather='" + weather + '\'' +
                ", wind='" + wind + '\'' +
                ", winp='" + winp + '\'' +
                ", temp_high='" + temp_high + '\'' +
                ", temp_low='" + temp_low + '\'' +
                '}';
    }
}
