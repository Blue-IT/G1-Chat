package com.blueit.g1_chat;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.AdapterView;
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
import java.util.Date;
import java.util.List;

public class ChatActivity extends AppCompatActivity {

    String DEFAULT_CHANNEL = "default";
    int MESSAGE_HISTORY_LIMIT = 10;
    int MESSAGE_REFRESH_MS = 1000;
    private EditText comment;

    String currentChannel;
    ArrayList<ChatMessage> chatMessages;
    ChatAdapter chatAdapter;
    ListView listView;

    Date latestMessageDate;
    boolean setupCompleted = false;

    MasterMenu menu = new MasterMenu(ChatActivity.this);

    private boolean isRunning;
    private static Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        Log.d("G1CHAT", "onCreate");

        // Prepare handler
        handler = new Handler();

        Log.d("G1CHAT", "handler instantiated");;

        // View references
        listView = (ListView) findViewById(R.id.chat_list);
        comment = (EditText) findViewById(R.id.chatText);
        final String currentUser = ParseUser.getCurrentUser().getUsername();

        // Initiate chat
        Log.d("G1CHAT", "onCreate calls switchChannel");
        switchChannel(DEFAULT_CHANNEL);

        // Setup send message click listener
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
                                    "Chat Error", Toast.LENGTH_LONG).show();
                        }
                        chatMessages.add(entry);
                        chatAdapter.notifyDataSetChanged();
                    }
                });

                comment.setText("");
            }
        });

        registerForContextMenu(listView);
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        isRunning = true;
        if(setupCompleted) {
            loadNewMessages();
        }
    }

    @Override
    protected void onPause()
    {
        super.onPause();
        isRunning = false;
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        if (v.getId()==R.id.chat_list) {
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.menu_chat_list_item, menu);
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch(item.getItemId()) {
                return true;
            case R.id.edit:
                // edit stuff here
                return true;
            case R.id.delete:
                // remove stuff here
                return true;
            default:
                return super.onContextItemSelected(item);
        }
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
        if (id == R.id.action_logout) {
            menu.logout();
        } else if (id == R.id.action_news) {
            menu.newsflash();
        } else if (id == R.id.action_user) {
            menu.user();
        } else if (id == R.id.action_chat) {
            menu.chat();
        } else if (id == R.id.action_admin) {
            menu.admin();
        }

        return true;
    }

            public void switchChannel(String newChannel) {
                Log.d("G1CHAT", "switchChannel");
                // Set channel
                currentChannel = newChannel;

                // Initiate / Reset message array
                if (chatMessages != null) {
                    chatMessages.clear();
                } else {
                    chatMessages = new ArrayList<ChatMessage>();
                }

                // Query database and retrieve messages
                Log.d("G1CHAT", "switchChannel calls loadNewMessages");
                loadNewMessages();
            }

            public void loadNewMessages() {
                Log.d("G1CHAT", "loadNewMessages");
                // Create query
                Log.d("G1CHAT", "Creating query");
                ParseQuery<ChatMessage> query = ParseQuery.getQuery(ChatMessage.class);
                query.orderByAscending("createdAt");
                query.whereEqualTo("channel", currentChannel);
                query.setLimit(MESSAGE_HISTORY_LIMIT);
                if(latestMessageDate != null) {
                    Log.d("G1CHAT", "Query has createdAt property");
                    query.whereGreaterThan("createdAt", latestMessageDate);
                }
                query.findInBackground(new FindCallback<ChatMessage>() {
                    public void done(List<ChatMessage> messages, ParseException e) {
                        Log.d("G1CHAT", "FindCallback");
                        if (e == null && !messages.isEmpty()) {
                            Log.d("G1CHAT", "New messages!");

                            // Prune duplicate messages
                            for(ChatMessage existingMessage : chatMessages) {
                                for(ChatMessage newMessage : messages) {
                                    // If an existing message has the same object id as a new one, they are the same.
                                    if(existingMessage.getObjectId().equals(newMessage.getObjectId())) {
                                        // Remove the duplicate new message from the new data list
                                        messages.remove(newMessage);

                                        // End this loop - if we have found a new message equal to an existing message
                                        // there should be no further duplicates of this existing message.
                                        break;
                                    }
                                }
                            }


                            // Insert all remaining entries into our data list
                            chatMessages.addAll(messages);

                            // Configure the adapter which feeds data into the view
                            if (chatAdapter != null) {
                                chatAdapter.notifyDataSetChanged();
                            } else {
                                chatAdapter = new ChatAdapter(ChatActivity.this,
                                        R.layout.chat_item, chatMessages);
                                listView.setAdapter(chatAdapter);
                            }

                            // Store latest date
                            if (!chatMessages.isEmpty()) {
                                Log.d("G1CHAT", "Storing latest date");
                                ChatMessage lastMessage = chatMessages.get(chatMessages.size() - 1);
                                latestMessageDate = lastMessage.getCreatedAt();
                                Log.d("G1CHAT", "New latest date ");
                            }

                            if(!setupCompleted) {
                                setupCompleted = true;
                            }

                        } else if(e != null) {
                            Log.e("G1CHAT", "Failed to retrieve chat objects from parse");
                            Log.e("G1CHAT", "" + e.getCode());
                        }

                        // Post next refresh
                        Log.d("G1CHAT", "loadNewMessages posts next runnable");
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                Log.d("G1CHAT", "runnable fires");
                                if (isRunning)
                                    Log.d("G1CHAT", "runnable calls loadNewMessages");
                                loadNewMessages();
                            }
                        }, MESSAGE_REFRESH_MS);
                    }
                });
            }
        }
