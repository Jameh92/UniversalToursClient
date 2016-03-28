package com.example.jredpath.universaltoursclient.model;

import com.google.gson.annotations.SerializedName;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by JRedpath on 22/03/2016.
 */
public class UserModel {

    @SerializedName("id")
    private Integer id;

    @SerializedName("firstname")
    private String firstName;

    @SerializedName("lastname")
    private String lastName;

    @SerializedName("email")
    private String email;

    @SerializedName("password")
    private String password;

    @SerializedName("favourites")
    private Set<TourModel> favourites;

    public UserModel(String firstName, String lastName, String email, String password, Set<TourModel> favourites) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.favourites = favourites;
    }

    public UserModel(){

    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Set<TourModel> getFavourites() {
        return favourites;
    }

    public void setFavourites(Set<TourModel> favourites) {
        this.favourites = favourites;
    }
}
