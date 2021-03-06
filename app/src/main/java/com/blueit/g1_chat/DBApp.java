package com.blueit.g1_chat;

import android.app.Application;

import com.blueit.g1_chat.parseobjects.Channel;
import com.blueit.g1_chat.parseobjects.ChatMessage;
import com.blueit.g1_chat.parseobjects.Newsflash;
import com.parse.Parse;
import com.parse.ParseObject;

public class DBApp extends Application {
    @Override
    public void onCreate()
    {
        super.onCreate();
        ParseObject.registerSubclass(ChatMessage.class);
        ParseObject.registerSubclass(Newsflash.class);
        ParseObject.registerSubclass(Channel.class);
        Parse.initialize(this, "p3Pa3m5L8jl4KFR8Oal23cWHh2t9XgMljko2WnuE", "ESSjUu81p6jhvGfl3h6lS3TBpxvSKexpVCy5wTUX");
    }
}
