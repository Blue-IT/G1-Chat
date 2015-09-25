package com.blueit.g1_chat;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseUser;

/**
 * Created by Alex on 2015-09-24.
 */
@ParseClassName("User")
public class User extends ParseObject {

    public String getName(){
        return getString("name");
    }
    public void setName(String name){
        put("name", name);
    }
    @Override
    public String toString(){
        return getString("name");
    }
}
