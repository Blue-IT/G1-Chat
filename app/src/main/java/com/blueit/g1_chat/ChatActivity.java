package com.blueit.g1_chat;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ListView;
import android.view.View;
import android.widget.Toast;

import com.blueit.g1_chat.adapters.ChatAdapter;
import com.blueit.g1_chat.parseobjects.ChatMessage;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.List;

public class ChatActivity extends AppCompatActivity {

    String DEFAULT_CHANNEL = "default";
    int MESSAGE_HISTORY_LIMIT = 10;
    private EditText comment;

    String currentChannel;
    ArrayList<ChatMessage> chatMessages;
    ChatAdapter chatAdapter;
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        // View references
        listView = (ListView) findViewById(R.id.chat_list);
        comment = (EditText) findViewById(R.id.chatText);
        final String currentUser = ParseUser.getCurrentUser().getUsername();
        // Initiate chat
        switchChannel(DEFAULT_CHANNEL);

        ParseObject.registerSubclass(ChatMessage.class);

        findViewById(R.id.chatButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Scomment = comment.getText().toString();

                final ChatMessage entry = new ChatMessage();
                entry.setContent(Scomment);
                entry.setAuthor(currentUser);
                entry.setChannel(currentChannel);

                entry.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e != null) {
                            Toast.makeText(getApplicationContext(),
                                    "Register Error", Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });
        }

            @Override
            public boolean onCreateOptionsMenu(Menu menu) {
                // Inflate the menu; this adds items to the action bar if it is present.
                getMenuInflater().inflate(R.menu.menu_chat, menu);
                return true;
            }

            @Override
            public boolean onOptionsItemSelected(MenuItem item) {
                // Handle action bar item clicks here. The action bar will
                // automatically handle clicks on the Home/Up button, so long
                // as you specify a parent activity in AndroidManifest.xml.
                int id = item.getItemId();

                //noinspection SimplifiableIfStatement
                if (id == R.id.action_settings) {
                    return true;
                }

                return super.onOptionsItemSelected(item);
            }

            public void switchChannel(String newChannel) {
                // Set channel
                currentChannel = newChannel;

                // Initiate / Reset message array
                if (chatMessages != null) {
                    chatMessages.clear();
                } else {
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
                            if (chatAdapter != null) {
                                chatAdapter.notifyDataSetChanged();
                            } else {
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
