package com.blueit.g1_chat;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import com.blueit.g1_chat.security.SecurityUtil;

public class LoginActivity extends Activity
{
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    public void onClickLogin(View view) {
        // TODO: Should contact server to login and then proceed to main chat interface
        SecurityUtil.debug();
    }
}
