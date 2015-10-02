package com.blueit.g1_chat;

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
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import junit.framework.Assert;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ChatActivity extends AppCompatActivity {

    private static final String TAG = "ChatActivity";

    String DEFAULT_CHANNEL = "default";
    int MESSAGE_HISTORY_LIMIT = 10;
    int MESSAGE_REFRESH_MS = 5000;
    private EditText comment;
    static final int EDIT_MESSAGE_REQUEST = 1;
    int position;
    String newComment;

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

        Log.d(TAG, "onCreate");

        // Prepare handler
        handler = new Handler();

        // View references
        listView = (ListView) findViewById(R.id.chat_list);
        comment = (EditText) findViewById(R.id.chatText);
        final String currentUser = ParseUser.getCurrentUser().getString("name");

        // Setup send message click listener
        findViewById(R.id.chatButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Retrieve input
                String Scomment = comment.getText().toString();

                // Process input
                Scomment = Scomment.trim(); // Trim leading/trailing whitespace.admin

                // Verify content
                if (Scomment.equals("")) {
                    Toast.makeText(ChatActivity.this, R.string.err_fields_empty, Toast.LENGTH_LONG)
                            .show();
                    return;
                } else if (Scomment.contains("<") || Scomment.contains(">")) {
                    Toast.makeText(ChatActivity.this, R.string.err_fields_illegal, Toast.LENGTH_LONG)
                            .show();
                    return;
                }

                // Create chat message
                final ChatMessage entry = new ChatMessage();
                entry.setContent(Scomment);
                entry.setAuthor(currentUser);
                entry.setChannel(currentChannel);

                // Send
                entry.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e != null) {
                            Toast.makeText(getApplicationContext(),
                                    R.string.err_chat_send, Toast.LENGTH_LONG).show();
                        }
                        chatMessages.add(entry);
                        chatAdapter.notifyDataSetChanged();
                    }
                });

                comment.setText("");
            }
        });

        // Setup context menu
        registerForContextMenu(listView);

        // Initiate chat
        initChat();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Log.d(TAG, "onNewIntent");
        // Reset some important variables
        if(isRunning) {
            isRunning = false; // Let's hope we don't query the old channel whilst initiating a new one.
        }
        latestMessageDate = null; // Clear this so that we don't use the previous channel's date when querying

        // Relaunch chat
        initChat();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume");
        isRunning = true; // Resume querying the database
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause");
        isRunning = false; // Do not query database while activity is not active
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        AdapterView.AdapterContextMenuInfo info =
                (AdapterView.AdapterContextMenuInfo) menuInfo;
        String author = chatMessages.get(info.position).getAuthor();
        //Context menu pops up only for the current user.
        if (author.equals(ParseUser.getCurrentUser().getString("name"))) {

            if (v.getId() == R.id.chat_list) {
                MenuInflater inflater = getMenuInflater();
                inflater.inflate(R.menu.menu_list_item, menu);
            }
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        position = info.position;
        switch(item.getItemId()) {
            case R.id.edit:
                //Edit message
                Intent intent = new Intent(ChatActivity.this, EditMessageActivity.class);
                intent.putExtra("content", chatMessages.get(position).getString("content").toString());
                startActivityForResult(intent, EDIT_MESSAGE_REQUEST);
                return true;
            case R.id.delete:
                //Delete with notification
                chatMessages.get(position).deleteInBackground();
                chatMessages.remove(position);
                chatAdapter.notifyDataSetChanged();
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Handle edit message request
        if (requestCode == EDIT_MESSAGE_REQUEST) {
            if (resultCode == RESULT_OK) {
                // Get edited text
                newComment = data.getStringExtra("comment");

                // Query
                ParseQuery<ChatMessage> query = ParseQuery.getQuery(ChatMessage.class);
                query.getInBackground(chatMessages.get(position).getObjectId(), new GetCallback<ChatMessage>() {
                    public void done(ChatMessage currentMessage, ParseException e) {
                        // Success
                        if (e == null) {
                            // Save
                            currentMessage.setContent(newComment);
                            currentMessage.saveInBackground();
                            Toast.makeText(getApplicationContext(), "Edited", Toast.LENGTH_SHORT).show();
                        }
                        // Error
                        else {
                            Log.e("Error", e.getMessage());
                            Toast.makeText(getApplicationContext(), "Error occured", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                // Update view right away
                chatMessages.get(position).setContent(newComment);
                chatAdapter.notifyDataSetChanged();
            }
        }
    }

    /**
     * Handles the process of starting up the chat.
     * It also checks the intent for a requested chat channel to use.
     */
    private void initChat() {
        Log.d(TAG, "initChat");
        Intent intent = getIntent();
        String requestedChannel = intent.getStringExtra("channel");
        if(requestedChannel != null || !requestedChannel.equals("")) {
            Log.d(TAG, "Using requested channel " + requestedChannel);
            switchChannel(requestedChannel);
        }
        else {
            Log.d(TAG, "Using default channel " + DEFAULT_CHANNEL);
            switchChannel(DEFAULT_CHANNEL);
        }
    }

    /**
     * Selects the chat channel to use, loading the messages and preparing the view.
     * @param newChannel
     */
    private void switchChannel(String newChannel) {
        Log.d(TAG, "switchChannel to " + newChannel);
        // Set channel
        currentChannel = newChannel;

        // Initiate / Reset message array
        if (chatMessages != null) {
            chatMessages.clear();
        } else {
            chatMessages = new ArrayList<ChatMessage>();
        }

        // Configure the adapter which feeds data into the view
        if (chatAdapter != null) {
            chatAdapter.notifyDataSetChanged();
        } else {
            chatAdapter = new ChatAdapter(ChatActivity.this,
                    R.layout.chat_item, chatMessages);
            listView.setAdapter(chatAdapter);
        }

        // Query database and retrieve messages
        Log.d(TAG, "switchChannel calls loadNewMessages");
        loadNewMessages();
    }

    /**
     * Fetches messages for the currentChannel from the database and adds them to the data adapter.
     */
    private void loadNewMessages() {
        Log.d(TAG, "loadNewMessages");
        // Create query
        ParseQuery<ChatMessage> query = ParseQuery.getQuery(ChatMessage.class);
        query.orderByAscending("createdAt");
        query.whereEqualTo("channel", currentChannel);
        query.setLimit(MESSAGE_HISTORY_LIMIT);
        if(latestMessageDate != null) {
            Log.d(TAG, "Query with createdAt property: " + latestMessageDate);
            query.whereGreaterThan("createdAt", latestMessageDate);
        }
        query.findInBackground(new FindCallback<ChatMessage>() {
            public void done(List<ChatMessage> messages, ParseException e) {
                Log.d(TAG, "loadNewMessages FindCallback");
                if (e == null && !messages.isEmpty()) {
                    Log.d(TAG, "Retrieved " + messages.size() + " messages from the database.");

                    // Prune duplicate messages
                    for (ChatMessage existingMessage : chatMessages) { // Messages currently in the View
                        for (ChatMessage newMessage : messages) { // Messages just retrieved from the database
                            // If an existing message has the same object id as a new one, they are the same.
                            if (existingMessage.getObjectId().equals(newMessage.getObjectId())) {
                                Log.d(TAG, "Pruning duplicate message with objectId " + existingMessage.getObjectId());
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
                    chatAdapter.notifyDataSetChanged();

                    // Store latest date
                    if (!chatMessages.isEmpty()) {
                        ChatMessage lastMessage = chatMessages.get(chatMessages.size() - 1);
                        latestMessageDate = lastMessage.getCreatedAt();
                        Log.d(TAG, "New latestMessageDate to be used in future queries: " + latestMessageDate);
                    }

                    // Workaround to prevent some issues from onResume
                    if (!setupCompleted) {
                        setupCompleted = true;
                    }

                } else if (e != null) {
                    Log.e(TAG, "Failed to retrieve chat objects from parse");
                    Log.e(TAG, "" + e.getCode());
                    Toast.makeText(getApplicationContext(),
                            R.string.err_chat_refresh, Toast.LENGTH_LONG).show();
                }

                // Post next refresh
                Log.d(TAG, "loadNewMessages posts next runnable");
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Log.d(TAG, "Runnable fires");
                        if (isRunning) {
                            Log.d(TAG, "Runnable calls loadNewMessages");
                            loadNewMessages();
                        }
                    }
                }, MESSAGE_REFRESH_MS);
            }
        });
    }
}
