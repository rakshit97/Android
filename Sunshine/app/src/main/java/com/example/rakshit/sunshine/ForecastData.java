package com.example.rakshit.sunshine;


public class ForecastData
{
    private long time;
    private double temp_min;
    private double temp_max;
    private String condition;

    ForecastData(long time, double temp_max, double temp_min, String condition)
    {
        this.time = time;
        this.temp_max = temp_max;
        this.temp_min = temp_min;
        this.condition = condition;
    }

    public long getTime()
    {
        return time;
    }

    public double getTemp_min()
    {
        return temp_min;
    }

    public double getTemp_max()
    {
        return temp_max;
    }

    public String getCondition()
    {
        return condition;
    }
}
