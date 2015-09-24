package com.blueit.g1_chat;

import android.app.Application;

import com.parse.Parse;

public class DBApp extends Application {
    @Override
    public void onCreate()
    {
        super.onCreate();
        Parse.initialize(this, "p3Pa3m5L8jl4KFR8Oal23cWHh2t9XgMljko2WnuE", "ESSjUu81p6jhvGfl3h6lS3TBpxvSKexpVCy5wTUX");
    }
}
