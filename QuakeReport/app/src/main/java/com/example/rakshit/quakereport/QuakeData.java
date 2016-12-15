package com.example.rakshit.quakereport;

public class QuakeData
{
    private double mag;
    private String place;
    private String date;

    QuakeData(double mag, String place, String date)
    {
        this.mag = mag;
        this.place = place;
        this.date = date;
    }

    public double getMag()
    {
        return mag;
    }

    public String getPlace()
    {
        return place;
    }

    public String getDate()
    {
        return date;
    }
}
