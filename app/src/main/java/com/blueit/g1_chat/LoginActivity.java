package com.blueit.g1_chat;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

import com.blueit.g1_chat.security.SecurityUtil;

public class LoginActivity extends AppCompatActivity
{
    private EditText username;
    private EditText password;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Set up the login form.
        username = (EditText) findViewById(R.id.name_text);
        password = (EditText) findViewById(R.id.pass_text);

        // Set up the submit button click handler
        findViewById(R.id.sign_in_button).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                // Validate the log in data
                final String u = username.getText().toString();
                final String p = password.getText().toString();
                if (u.length() == 0 || p.length() == 0)
                {
                        Toast.makeText(LoginActivity.this, R.string.err_fields_empty, Toast.LENGTH_LONG)
                                .show();
                        return;
                }

                // Set up a progress dialog
                final ProgressDialog dlg = ProgressDialog.show(LoginActivity.this, null,
                        getString(R.string.alert_wait));
                // Call the Parse login method
                ParseUser.logInInBackground(u, p, new LogInCallback() {

                    @Override
                    public void done(ParseUser user, ParseException e) {
                        dlg.dismiss();
                        if (e != null) {
                            // Show the error message
                            Toast.makeText(LoginActivity.this, R.string.err_login, Toast.LENGTH_LONG).show();
                        }
                        else if(ParseUser.getCurrentUser().getBoolean("isAdmin")){
                            Intent adminIntent = new Intent(LoginActivity.this, AdminActivity.class);
                            adminIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(adminIntent);
                        }
                        else {
                            // Start an intent for the dispatch activity
                            Intent newsIntent = new Intent(LoginActivity.this, NewsflashActivity.class);
                            newsIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(newsIntent);
                        }
                    }
                });
            }
        });
    }
}

