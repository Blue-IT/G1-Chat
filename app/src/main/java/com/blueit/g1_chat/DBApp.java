package com.blueit.g1_chat;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseObject;

public class DBApp extends Application {
    @Override
    public void onCreate()
    {
        super.onCreate();

        ParseObject.registerSubclass(User.class);
        Parse.initialize(this, "p3Pa3m5L8jl4KFR8Oal23cWHh2t9XgMljko2WnuE", "ESSjUu81p6jhvGfl3h6lS3TBpxvSKexpVCy5wTUX");
    }
}
