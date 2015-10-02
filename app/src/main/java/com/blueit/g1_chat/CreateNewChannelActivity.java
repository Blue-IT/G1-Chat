package com.blueit.g1_chat;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.blueit.g1_chat.parseobjects.Newsflash;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

import java.util.List;

public class CreateNewChannelActivity extends AppCompatActivity implements View.OnClickListener{


    private EditText inputTitle;

    MasterMenu menu = new MasterMenu(CreateNewChannelActivity.this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_channel);

        inputTitle =  (EditText) findViewById(R.id.edit_message);
        setClick(R.id.send_button);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_user, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
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



    public void onClick(View v)
    {
        int id = v.getId();

        if (id == R.id.send_button)
        {
            createChannel();
        }

    }

    public void createChannel()
    {
        // Get input
        final String title = inputTitle.getText().toString();

        // Verify
        if (title.equals("")) {
            Toast.makeText(CreateNewChannelActivity.this, R.string.err_fields_empty, Toast.LENGTH_LONG)
                    .show();
            return;
        }

        // Verify does not already exist
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Channel");
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> channelList, ParseException e) {
                if (e != null) {
                    Toast.makeText(CreateNewChannelActivity.this, "Error " + e, Toast.LENGTH_SHORT).show();
                } else {
                    Log.d("G1CHAT", "Verification Channel List callback");
                    Log.d("G1CHAT", "there are " + channelList.size() + " results");
                    for (ParseObject channelObject : channelList) {
                        String channel = channelObject.getString("name");
                        Log.d("G1CHAT", "checking channel " + channel);
                        if (channel.equals(title)) {
                            Toast.makeText(CreateNewChannelActivity.this, R.string.err_fields_illegal, Toast.LENGTH_LONG)
                                    .show();
                            return;
                        }
                    }

                    Log.d("G1CHAT", "Channel verified");

                    // Create parseobject
                    Log.d("G1CHAT", "Creating channel " + title);
                    ParseObject entry = new ParseObject("Channel");
                    entry.put("name", title);

                    // Save
                    entry.saveInBackground(new SaveCallback() {
                        public void done(ParseException e) {

                            Log.d("G1CHAT", "Channel " + title + " saved");
                            finish();
                        }
                    });
                }
            }
        });

    }

    public void CancelActivity()
    {
        finish();
    }

    /**
     * Sets the click listener for a view with given id.
     *
     * @param id
     *            the id
     * @return the view on which listener is applied
     */
    public View setClick(int id)
    {
        View v = findViewById(id);
        if (v != null)
            v.setOnClickListener(this);
        return v;
    }
}
