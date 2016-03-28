package com.example.jredpath.universaltoursclient.model;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

/**
 * Created by JRedpath on 17/03/2016.
 */
public class TourModel {

    @SerializedName("id")
    private Integer id;

    @SerializedName("organiser")
    private String organiser;

    @SerializedName("tourname")
    private String tourName;

    @SerializedName("tourdate")
    private String tourDate;

    @SerializedName("longitude")
    private Double longitude;

    @SerializedName("latitude")
    private Double latitude;

    @SerializedName("costperperson")
    private Double cost;

    @SerializedName("maxpeople")
    private Integer maxPeople;

    @SerializedName("description")
    private String description;


    public TourModel(String organiser, String tourName, String tourDate, Double longitude, Double latitude, Double cost, Integer maxPeople, String description) {
        this.organiser = organiser;
        this.tourName = tourName;
        this.tourDate = tourDate;
        this.longitude = longitude;
        this.latitude = latitude;
        this.cost = cost;
        this.maxPeople = maxPeople;
        this.description = description;
    }

    public TourModel(){

    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getOrganiser() {
        return organiser;
    }

    public void setOrganiser(String organiser) {
        this.organiser = organiser;
    }

    public String getTourName() {
        return tourName;
    }

    public void setTourName(String tourName) {
        this.tourName = tourName;
    }

    public String getTourDate() {
        return tourDate;
    }

    public void setTourDate(String tourDate) {
        this.tourDate = tourDate;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getCost() {
        return cost;
    }

    public void setCost(Double cost) {
        this.cost = cost;
    }

    public Integer getMaxPeople() {
        return maxPeople;
    }

    public void setMaxPeople(Integer maxPeople) {
        this.maxPeople = maxPeople;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}

