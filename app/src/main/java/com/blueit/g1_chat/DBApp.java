package com.blueit.g1_chat;

import android.app.Application;

import com.parse.Parse;

public class DBApp extends Application {
    @Override
    public void onCreate()
    {
        super.onCreate();
        Parse.initialize(this, "ceIh2sSWp9QM5N5NQ21cVmxK3YOYqMjx1a2QbRQz", "zWF2jtsszzCrmOWw4yy4nuksg10cfaAhSMeWMeWE");
    }
}
