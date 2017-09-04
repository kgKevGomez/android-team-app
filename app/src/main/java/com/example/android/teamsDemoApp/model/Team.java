package com.example.android.teamsDemoApp.model;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

import java.util.ArrayList;
import java.util.Date;


/**
 * Created by kevin on 9/2/2017.
 */

@Parcel
public class Team {
    @SerializedName("id")
    private int id;

    @SerializedName("team_name")
    private String name;

    @SerializedName("img_logo")
    private String logoUrl;

    @SerializedName("description")
    private String description;

    @SerializedName("address")
    private String address;

    @SerializedName("video_url")
    private String videoUrl;

    @SerializedName("latitude")
    private double latitude;

    @SerializedName("longitude")
    private double longitude;

    @SerializedName("phone_number")
    private String phoneNumber;

    @SerializedName("schedule_games")
    private ArrayList<Schedule> scheduleGames;

    public ArrayList<Schedule> getScheduleGames() {
        return scheduleGames;
    }

    public void setScheduleGames(ArrayList<Schedule> scheduleGames) {
        this.scheduleGames = scheduleGames;
    }

    public String getLogoUrl() {
        return logoUrl;
    }

    public void setLogoUrl(String logoUrl) {
        this.logoUrl = logoUrl;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Team() {}

    public Team(int id, String name, String description, String address) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.address = address;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public String toString() {
        return name;
    }

    @Parcel
    public class Schedule {
        @SerializedName("date")
        private Date date;
        @SerializedName("stadium")
        private String stadium;

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
}
