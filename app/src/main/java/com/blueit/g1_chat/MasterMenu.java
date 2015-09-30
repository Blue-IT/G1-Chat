package com.blueit.g1_chat;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;

import com.parse.ParseUser;

/**
 * Created by Alex on 2015-09-29.
 */
public class MasterMenu {

    Context context;

    public MasterMenu(Context context){
        this.context = context;

    }

    public void logout() {
        ParseUser.getCurrentUser().logOut();
        Intent intent = new Intent(context, LoginActivity.class);
        context.startActivity(intent);
    }

    public void newsflash(){
        Intent intent = new Intent(context, NewsflashActivity.class);
        context.startActivity(intent);
    }

    public void user(){
        Intent intent = new Intent(context, UserListActivity.class);
        context.startActivity(intent);
    }

    public void reguser(){
        Intent intent = new Intent(context, RegisterActivity.class);
        context.startActivity(intent);
    }

    public void chat(){
        Intent intent = new Intent(context, ChatActivity.class);
        context.startActivity(intent);
    }

    public void admin(){
        Intent intent = new Intent(context, AdminActivity.class);
        context.startActivity(intent);
    }
}
