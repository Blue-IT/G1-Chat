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
import com.parse.ParseObject;
import com.parse.ParseSession;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.SignUpCallback;

public class RegisterActivity extends AppCompatActivity {

    private EditText name;
    private EditText username;
    private EditText password;

    MasterMenu menu = new MasterMenu(RegisterActivity.this);

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
                    // Store current session so that it can be restored after signup
                    final String adminSession = ParseUser.getCurrentUser().getSessionToken();

                    // Create new user
                    ParseUser user = new ParseUser();
                    user.put("name",Sname);
                    user.put("username",Susername);
                    user.put("password",Spassword);

                    // Signup
                    user.signUpInBackground(new SignUpCallback() {
                        @Override
                        public void done(ParseException e) {
                            if (e == null) {
                                ParseUser.becomeInBackground(adminSession); // Restore admin session
                                Toast.makeText(getApplicationContext(),
                                        "Successfully Registered",
                                        Toast.LENGTH_LONG).show();
                                finish();
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
        if(id == R.id.action_logout) {
            menu.logout();
        }else if(id == R.id.action_news){
            menu.newsflash();
        }else if(id == R.id.action_user){
            menu.user();
        }else if(id == R.id.action_chat){
            menu.chat();
        }else if(id == R.id.action_admin){
            menu.admin();
        }

        return super.onOptionsItemSelected(item);
    }
}
