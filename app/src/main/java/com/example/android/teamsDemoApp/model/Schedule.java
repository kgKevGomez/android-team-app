package com.example.android.teamsDemoApp.model;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

import java.util.Date;

/**
 * Created by Kevin on 9/4/2017.
 */

@Parcel
public class Schedule {
    @SerializedName("date")
    Date date;
    @SerializedName("stadium")
    String stadium;

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getStadium() {
        return stadium;
    }

    public void setStadium(String stadium) {
        this.stadium = stadium;
    }
}