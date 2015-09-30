package com.blueit.g1_chat;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;

import com.parse.ParseUser;

/**
 * Created by Alex on 2015-09-29.
 */
public class MasterMenu extends AppCompatActivity {

    Context context;


    public MasterMenu(Context context){
        this.context = context;

    }

    public void logout() {
        ParseUser.getCurrentUser().logOut();
        Intent intent = new Intent(context, LoginActivity.class);
        startActivity(intent);
    }

    public void newsflash(){
        Intent intent = new Intent(context, NewsflashActivity.class);
        startActivity(intent);
    }

    public void user(){
        Intent intent = new Intent(context, UserListActivity.class);
        startActivity(intent);
    }
}
