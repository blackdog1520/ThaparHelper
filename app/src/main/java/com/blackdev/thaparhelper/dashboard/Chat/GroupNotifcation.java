package com.blackdev.thaparhelper.dashboard.Chat;

import android.telecom.Call;

import com.google.gson.JsonObject;

import retrofit.Callback;
import retrofit.http.GET;

public interface GroupNotifcation {
    @GET("http://your.api.url")
    Callback<JsonObject> getGenericJSON();
}
