package com.example.rakshit.sunshine;


import android.os.Parcel;
import android.os.Parcelable;

public class ForecastData implements Parcelable
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

    //Interface methods
    public ForecastData(Parcel in)
    {
        readFromParcel(in);
    }

    private void readFromParcel(Parcel in)
    {
        time = in.readLong();
        temp_max = in.readDouble();
        temp_min = in.readDouble();
        condition = in.readString();
    }

    @Override
    public int describeContents()
    {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i)
    {
        parcel.writeLong(time);
        parcel.writeDouble(temp_max);
        parcel.writeDouble(temp_min);
        parcel.writeString(condition);
    }

    public static final Creator CREATOR = new Creator()
    {
        @Override
        public ForecastData createFromParcel(Parcel parcel)
        {
            return new ForecastData(parcel);
        }

        @Override
        public ForecastData[] newArray(int i) {
            return new ForecastData[i];
        }
    };
}
