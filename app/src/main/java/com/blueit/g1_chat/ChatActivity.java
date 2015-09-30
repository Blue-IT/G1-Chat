package com.blueit.g1_chat;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.blueit.g1_chat.adapters.ChatAdapter;
import com.blueit.g1_chat.adapters.NewsflashAdapter;
import com.blueit.g1_chat.parseobjects.ChatMessage;
import com.blueit.g1_chat.parseobjects.Newsflash;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

public class ChatActivity extends AppCompatActivity {

    String DEFAULT_CHANNEL = "default";
    int MESSAGE_HISTORY_LIMIT = 10;

    String currentChannel;
    ArrayList<ChatMessage> chatMessages;
    ChatAdapter chatAdapter;
    ListView listView;

    MasterMenu menu = new MasterMenu(ChatActivity.this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        // View references
        listView = (ListView) findViewById(R.id.chat_list);

        // Initiate chat
        switchChannel(DEFAULT_CHANNEL);
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

    public void switchChannel(String newChannel) {
        // Set channel
        currentChannel = newChannel;

        // Initiate / Reset message array
        if(chatMessages != null) {
            chatMessages.clear();
        }
        else {
            chatMessages = new ArrayList<ChatMessage>();
        }
        
        // Query database and retrieve messages
        ParseQuery<ChatMessage> query = ParseQuery.getQuery(ChatMessage.class);
        query.orderByAscending("createdAt");
        query.whereEqualTo("channel", currentChannel);
        query.setLimit(MESSAGE_HISTORY_LIMIT);
        query.findInBackground(new FindCallback<ChatMessage>() {
            public void done(List<ChatMessage> messages, ParseException e) {
                if (e == null) {
                    // Insert all entries into our data list
                    chatMessages.addAll(messages);

                    // Configure the adapter which feeds data into the view
                    if(chatAdapter != null) {
                        chatAdapter.notifyDataSetChanged();
                    }
                    else {
                        chatAdapter = new ChatAdapter(ChatActivity.this,
                                R.layout.chat_item, chatMessages);
                        ListView listView = (ListView) findViewById(R.id.chat_list);
                        listView.setAdapter(chatAdapter);
                    }
                } else {
                    Log.e("G1CHAT", "Failed to retrieve chat objects from parse");
                }
            }
        });

    }
}
