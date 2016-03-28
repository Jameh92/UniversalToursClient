package com.example.jredpath.universaltoursclient.util;

import com.google.gson.GsonBuilder;

/**
 * Created by JRedpath on 17/03/2016.
 */
public class JSONBuilder {
    public static String getJson(Object obj){
        return new GsonBuilder().setDateFormat("yyyy-MM-dd'T'hh:mm:ss").serializeNulls().create().toJson(obj);
    }
}
