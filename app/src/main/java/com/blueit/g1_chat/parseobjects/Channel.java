package com.blueit.g1_chat.parseobjects;

import com.parse.ParseClassName;
import com.parse.ParseObject;

/**
 * Created by clindell on 2015-10-05.
 */
@ParseClassName("Channel")
public class Channel extends ParseObject {

    public String getName() {
        return getString("name");
    }
    public void setName(String value){
        put("name", value);
    }

}
