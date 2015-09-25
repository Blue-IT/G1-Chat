package com.blueit.g1_chat.parseobjects;

import com.parse.ParseObject;
import com.parse.ParseClassName;

@ParseClassName("Newsflash")
public class Newsflash extends ParseObject {

  public String getTitle() {
    return getString("title");
  }
  public void setTitle(String value) {
    put("title", value);
  }

  public String getContent() {
    return getString("content");
  }
  public void setContent(String value) {
    put("content", value);
  }


}
