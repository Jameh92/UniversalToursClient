package com.example.jredpath.universaltoursclient.model;

/**
 * Created by JRedpath on 22/03/2016.
 */
public class CurrentAccount {
    private static UserModel instance = null;

    public synchronized static UserModel getInstance() {
        return instance;
    }

    private CurrentAccount() {
    }

    public static void setCurrentAccount(UserModel user){
        instance = user;
    }
}
