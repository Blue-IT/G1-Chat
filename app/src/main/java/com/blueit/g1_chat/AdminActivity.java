package com.blueit.g1_chat;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.parse.ParseUser;

public class AdminActivity extends AppCompatActivity {

    MasterMenu masterMenu = new MasterMenu(AdminActivity.this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        findViewById(R.id.btn_add_user).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                masterMenu.user();
            }
        });

        findViewById(R.id.btn_add_news).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                masterMenu.newsflash();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_admin, menu);
        MenuItem item = menu.findItem(R.id.action_admin);
        item.setVisible(false);
        invalidateOptionsMenu();
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if(id == R.id.action_logout) {
            masterMenu.logout();
        }else if(id == R.id.action_news){
            masterMenu.newsflash();
        }else if(id == R.id.action_user){
            masterMenu.user();
        }else if(id == R.id.action_chat){
            masterMenu.chat();
        }else if(id == R.id.action_admin){
            masterMenu.admin();
        }

        return super.onOptionsItemSelected(item);
    }
}