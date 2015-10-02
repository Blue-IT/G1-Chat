package com.blueit.g1_chat;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.blueit.g1_chat.adapters.ChannelAdapter;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class ChannelListActivity extends AppCompatActivity{

    ParseUser currentUser = ParseUser.getCurrentUser();
    ArrayAdapter<String> adapter;
    List<String> channels = new ArrayList<String>();
    MasterMenu menu = new MasterMenu(ChannelListActivity.this);

    ListView lv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_channel_list);

        // Setup adapter
        lv = (ListView) findViewById(R.id.channel_listview);
        adapter = new ChannelAdapter(ChannelListActivity.this, R.layout.channel_item, channels);
        lv.setAdapter(adapter);

        // Register callback
        registerListClickCallback(channels);

        // Fetch channels from database
        refreshChannelList();

        // Setup click listener for Create Channel button
        findViewById(R.id.btn_add_channel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChannelListActivity.this.startActivity(new Intent(ChannelListActivity.this, CreateChannelActivity.class));
            }
        });

    }

    public void refreshChannelList() {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Channel");
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> channelList, ParseException e) {
                if (e != null) {
                    Toast.makeText(ChannelListActivity.this, "Error " + e, Toast.LENGTH_SHORT).show();
                } else {
                    // Clear existing data
                    channels.clear();

                    // Load new data
                    Log.d("G1CHAT", "channelList callback");
                    Log.d("G1CHAT", "there are " + channelList.size() + " results");
                    for(ParseObject channelObject : channelList) {
                        String channel = channelObject.getString("name");
                        Log.d("G1CHAT", "adding channel " + channel);
                        channels.add(channel);
                    }

                    // Update
                    Log.d("G1CHAT", "notifying changed");
                    adapter.notifyDataSetChanged();
                }
            }
        });
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        refreshChannelList();
    }

    public void registerListClickCallback(final List<String> data) {
        lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View viewClicked,
                                           int position, long id) {
                String requestedChannel = channels.get(position);
                Intent intent = new Intent(ChannelListActivity.this, ChatActivity.class);
                intent.putExtra("channel", requestedChannel);
                intent.addFlags(intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                ChannelListActivity.this.startActivity(intent);
                return false;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_user, menu);
        MenuItem item = menu.findItem(R.id.action_chat);
        item.setVisible(false);
        invalidateOptionsMenu();
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
