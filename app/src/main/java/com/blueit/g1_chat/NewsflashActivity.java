package com.blueit.g1_chat;

import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;


import java.util.ArrayList;
import java.util.List;

import com.blueit.g1_chat.adapters.NewsflashAdapter;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import com.blueit.g1_chat.parseobjects.Newsflash;

public class NewsflashActivity extends AppCompatActivity implements View.OnClickListener {

    static final int CREATE_NEWSFLASH_REQUEST = 1;  // The request code
    static final int EDIT_MESSAGE_REQUEST = 2;

    private ArrayList<Newsflash> newsflashArrayList;
    private ArrayAdapter<Newsflash> newsflashArrayAdapter;
    ParseUser currentUser = ParseUser.getCurrentUser();

    int position;
    String newComment;

    MasterMenu menu = new MasterMenu(NewsflashActivity.this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set default layout
        setContentView(R.layout.activity_newsflash);

        // Construct view
        if (currentUser != null) {

            // Append admin view
            if(currentUser.getBoolean("isAdmin")) {

                // Get reference to our current layout
                RelativeLayout layout = (RelativeLayout)findViewById(R.id.newsflash_layout);

                // "Inflate" the admin layout without attaching it to our current view
                ViewGroup adminLayout = (ViewGroup)getLayoutInflater().inflate(R.layout.activity_newsflash_admin, layout, false);

                // For each child of the admin layout
                for(int i=0; i<(adminLayout).getChildCount(); ++i) {
                    // Get reference to the child
                    View child = (adminLayout).getChildAt(i);

                    // Remove child from admin layout
                    adminLayout.removeView(child);

                    // Add child to the current layout
                    layout.addView(child);
                }

                // Set the list element from the main layout to be drawn relatively to the create newsflash button from the admin layout
                ListView list = (ListView) findViewById(R.id.newsflash_list);
                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                        RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                params.addRule(RelativeLayout.ABOVE, R.id.newsflash_submit);
                list.setLayoutParams(params);

                // Setup admin create newsflash button
                setClick(R.id.newsflash_submit);


            }

        }
        else {
            Log.e("G1CHAT", "No currentUser");
        }

        // Initialize list database
        newsflashArrayList = new ArrayList<Newsflash>();

        // Query database and retrieve entries
        ParseQuery<Newsflash> query = ParseQuery.getQuery(Newsflash.class);
        query.orderByDescending("createdAt");
        query.findInBackground(new FindCallback<Newsflash>() {
           public void done(List<Newsflash> objects, ParseException e) {
               if (e == null) {
                   // Insert all entries into our data list
                   newsflashArrayList.addAll(objects);

                   // Configure the adapter which feeds data into the view
                   newsflashArrayAdapter = new NewsflashAdapter(NewsflashActivity.this,
                           R.layout.newsflash_item, newsflashArrayList);
                   ListView listView = (ListView) findViewById(R.id.newsflash_list);
                   listView.setHeaderDividersEnabled(true);
                   listView.setDividerHeight(1);
                   listView.setAdapter(newsflashArrayAdapter);

                   if(currentUser.getBoolean("isAdmin")) {
                       registerForContextMenu(listView);
                       //deleteNews(newsflashArrayList);
                   }
               } else {
                   Log.e("G1CHAT", "Failed to retrieve newsflash objects from parse");
               }
           }
        });

        // Setup submit button
        setClick(R.id.newsflash_submit);

        // Setup parse
        ParseObject.registerSubclass(Newsflash.class);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        AdapterView.AdapterContextMenuInfo info =
                (AdapterView.AdapterContextMenuInfo) menuInfo;


            if (v.getId() == R.id.newsflash_list) {
                MenuInflater inflater = getMenuInflater();
                inflater.inflate(R.menu.menu_list_item, menu);
            }

    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        position = info.position;
        switch(item.getItemId()) {
            case R.id.edit:
                //Edit message
                Intent intent = new Intent(NewsflashActivity.this, EditMessageActivity.class);
                intent.putExtra("content", newsflashArrayList.get(position).getString("content").toString());
                startActivityForResult(intent, EDIT_MESSAGE_REQUEST);
                return true;
            case R.id.delete:
                //Delete with notification
                newsflashArrayList.get(position).deleteInBackground();
                newsflashArrayList.remove(position);
                newsflashArrayAdapter.notifyDataSetChanged();
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Request
        if (requestCode == CREATE_NEWSFLASH_REQUEST) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                String id = data.getStringExtra("id");

                ParseQuery<Newsflash> query = ParseQuery.getQuery(Newsflash.class);
                query.whereEqualTo("objectId", id);
                query.getFirstInBackground(new GetCallback<Newsflash>() {
                    @Override
                    public void done(Newsflash parseObject, ParseException e) {
                        newsflashArrayList.add(0, parseObject);
                        newsflashArrayAdapter.notifyDataSetChanged();
                    }
                });
            }
            // Could not create newsflash
            else {
                Toast.makeText(NewsflashActivity.this, R.string.err_create_newsflash, Toast.LENGTH_LONG)
                        .show();
            }
        }else if(requestCode == EDIT_MESSAGE_REQUEST){
            if (resultCode == RESULT_OK){

                newComment = data.getStringExtra("comment");

                ParseQuery<ParseObject> query = ParseQuery.getQuery("Newsflash");
                query.getInBackground(newsflashArrayList.get(position).getObjectId(), new GetCallback<ParseObject>() {
                    public void done(ParseObject currentMessage, ParseException e) {
                        if (e == null) {
                            currentMessage.put("content",newComment);
                            currentMessage.saveInBackground();
                            Toast.makeText(getApplicationContext(), "Edited", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }
        }
        else {
            Toast.makeText(getApplicationContext(), "Error occured", Toast.LENGTH_SHORT).show();
        }
        newsflashArrayList.get(position).setContent(newComment);
        newsflashArrayAdapter.notifyDataSetChanged();
    }

    //Delete news with a longclick
    /*public void deleteNews(final ArrayList<Newsflash> data){
        ListView list = (ListView)findViewById(R.id.newsflash_list);
        list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View viewClicked,
                                           int position, long id) {
                ParseQuery<ParseObject> query = ParseQuery.getQuery("Newsflash");
                query.getInBackground(data.get(position).getObjectId(), new GetCallback<ParseObject>() {
                    public void done(ParseObject currentNews, ParseException e) {
                        if (e == null) {
                            currentNews.deleteInBackground();
                            Toast.makeText(getApplicationContext(), "Deleted", Toast.LENGTH_SHORT).show();
                        } else {
                            Log.e("Error", e.getMessage());
                            Toast.makeText(getApplicationContext(), "Error occured", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                newsflashArrayList.remove(position);//arg2 is position of the clicked item
                newsflashArrayAdapter.notifyDataSetChanged();
                return false;
            }
        });
    }*/
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if(ParseUser.getCurrentUser().getBoolean("isAdmin")) {
            getMenuInflater().inflate(R.menu.menu_admin, menu);
            MenuItem item = menu.findItem(R.id.action_news);
            item.setVisible(false);
            invalidateOptionsMenu();
        }else{
            getMenuInflater().inflate(R.menu.menu_user, menu);
            MenuItem item = menu.findItem(R.id.action_news);
            item.setVisible(false);
            invalidateOptionsMenu();
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

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

        if (id == R.id.newsflash_submit)
        {
            createNewsflash();
        }

    }

    public void createNewsflash()
    {
        // Start create newsflash activity
        Intent intent = new Intent(NewsflashActivity.this, CreateNewsflashActivity.class);
        startActivityForResult(intent, CREATE_NEWSFLASH_REQUEST);
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
