package com.example.rakshit.quakereport;

public class QuakeData
{
    private double mag;
    private String place;
    private long time;
    private String url;

    QuakeData(double mag, String place, long time, String url)
    {
        this.mag = mag;
        this.place = place;
        this.time = time;
        this.url = url;
    }

    public double getMag()
    {
        return mag;
    }

    public String getPlace()
    {
        return place;
    }

    public long getTime()
    {
        return time;
    }

    public String getUrl()
    {
        return url;
    }
}
