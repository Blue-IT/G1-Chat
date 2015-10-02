package com.blueit.g1_chat;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

public class RegisterActivity extends AppCompatActivity {

    private EditText name;
    private EditText username;
    private EditText password;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        name = (EditText)findViewById(R.id.name1_text);
        username = (EditText) findViewById(R.id.name_text);
        password = (EditText) findViewById(R.id.pass_text);

        // Set up the register click listener
        findViewById(R.id.register_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Validate the register data
                String Sname = name.getText().toString();
                String Susername = username.getText().toString();
                String Spassword = password.getText().toString();

                // Don't let the user skip any part
                if(Sname.equals("") && Susername.equals("") && Spassword.equals("")){
                    Toast.makeText(getApplicationContext(),
                            "Fill in all the fields", Toast.LENGTH_LONG).show();
                }else{
                   ParseUser user = new ParseUser();
                    user.put("name",Sname);
                    user.put("username",Susername);
                    user.put("password",Spassword);
                    user.signUpInBackground(new SignUpCallback() {
                        @Override
                        public void done(ParseException e) {
                            if(e == null){
                                Toast.makeText(getApplicationContext(),
                                        "Successfully Registered",
                                        Toast.LENGTH_LONG).show();
                                        user();
                            } else {
                                Toast.makeText(getApplicationContext(),
                                        "Register Error", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }
            }
        });

    }
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_admin_newsflash, menu);
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
        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
        startActivity(intent);
    }

    public void newsflash(){
        Intent intent = new Intent(RegisterActivity.this, NewsflashActivity.class);
        startActivity(intent);
    }

    public void user(){
        Intent intent = new Intent(RegisterActivity.this, UserListActivity.class);
        startActivity(intent);
    }
}
