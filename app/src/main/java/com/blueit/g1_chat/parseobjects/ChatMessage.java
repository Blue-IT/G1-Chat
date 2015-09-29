package com.blueit.g1_chat.parseobjects;

import com.parse.ParseClassName;
import com.parse.ParseObject;

@ParseClassName("ChatMessage")
public class ChatMessage extends ParseObject {

  public String getAuthor() {
    return getString("author");
  }
  public void setAuthor(String value) {
    put("author", value);
  }

  public String getContent() {
    return getString("content");
  }
  public void setContent(String value) {
    put("content", value);
  }


}
