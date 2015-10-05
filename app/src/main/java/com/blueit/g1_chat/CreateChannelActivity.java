package com.blueit.g1_chat;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.blueit.g1_chat.parseobjects.Channel;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

import java.util.List;

public class CreateChannelActivity extends AppCompatActivity implements View.OnClickListener{


    private EditText inputTitle;

    MasterMenu menu = new MasterMenu(CreateChannelActivity.this);

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
            Toast.makeText(CreateChannelActivity.this, R.string.err_fields_empty, Toast.LENGTH_LONG)
                    .show();
            return;
        }

        // Verify does not already exist
        ParseQuery<Channel> query = ParseQuery.getQuery(Channel.class);
        query.findInBackground(new FindCallback<Channel>() {
            @Override
            public void done(List<Channel> channelList, ParseException e) {
                if (e != null) {
                    Toast.makeText(CreateChannelActivity.this, "Error " + e, Toast.LENGTH_SHORT).show();
                } else {
                    Log.d("G1CHAT", "Verification Channel List callback");
                    Log.d("G1CHAT", "there are " + channelList.size() + " results");
                    for (Channel channelObject : channelList) {
                        String channel = channelObject.getName();
                        Log.d("G1CHAT", "checking channel " + channel);
                        if (channel.equals(title)) {
                            Toast.makeText(CreateChannelActivity.this, R.string.err_fields_illegal, Toast.LENGTH_LONG)
                                    .show();
                            return;
                        }
                    }

                    Log.d("G1CHAT", "Channel verified");

                    // Create parseobject
                    Log.d("G1CHAT", "Creating channel " + title);
                    Channel entry = new Channel();
                    entry.setName(title);

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
