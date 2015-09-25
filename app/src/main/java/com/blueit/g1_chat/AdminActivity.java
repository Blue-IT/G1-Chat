package com.blueit.g1_chat;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.parse.ParseUser;

public class AdminActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        findViewById(R.id.btn_add_user).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        findViewById(R.id.btn_add_news).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminActivity.this, NewsflashActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_admin, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }else if(id == R.id.action_logout) {
            logout();
        }else if(id == R.id.action_news){
            newsflash();
        }else if(id == R.id.action_user){
            user();
        }

        return super.onOptionsItemSelected(item);
    }

    public void logout() {
        ParseUser.getCurrentUser().logOut();
        Intent intent = new Intent(AdminActivity.this, LoginActivity.class);
        startActivity(intent);
    }

    public void newsflash(){
        Intent intent = new Intent(AdminActivity.this, NewsflashActivity.class);
        startActivity(intent);
    }

   public void user(){
        Intent intent = new Intent(AdminActivity.this, UserListActivity.class);
        startActivity(intent);
    }

}
;