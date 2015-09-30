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

import com.blueit.g1_chat.adapters.UserListAdapter;
import com.parse.FindCallback;
import com.parse.FunctionCallback;
import com.parse.ParseCloud;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class UserListActivity extends AppCompatActivity{

    List<ParseUser> users = new ArrayList<ParseUser>();
    String user = ParseUser.getCurrentUser().getUsername();
    ArrayAdapter<ParseUser> adapter;

    MasterMenu menu = new MasterMenu(UserListActivity.this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_list);

        findViewById(R.id.btn_reg_user).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                menu.reguser();
            }
        });

        ParseQuery<ParseUser> query = ParseUser.getQuery().whereNotEqualTo("username", user);
        query.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> userList, ParseException e) {
                if (e != null){
                    Toast.makeText(UserListActivity.this, "Error " + e, Toast.LENGTH_SHORT).show();
                }
                users.addAll(userList);
                populateListView(users);
                registerClickCallback(users);

            }
        });




    }

    public void populateListView(List<ParseUser> data){
        adapter = new UserListAdapter(UserListActivity.this, R.layout.userlist_item, data);
        ListView lv = (ListView) findViewById(R.id.user_listview);
        lv.setAdapter(adapter);
    }

    public void registerClickCallback(final List<ParseUser> data) {

        ListView list = (ListView)findViewById(R.id.user_listview);
        list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View viewClicked,
                                           int position, long id) {
                HashMap<String, String> params = new HashMap<String, String>();
                params.put("userId", data.get(position).getObjectId());
                ParseCloud.callFunctionInBackground("deleteUserWithId", params, new FunctionCallback<String>() {
                    public void done(String id, ParseException e) {
                        if (e == null) {
                            Toast.makeText(getApplicationContext(), "The user is deleted.", Toast.LENGTH_SHORT).show();
                        } else {
                            Log.e("Error", e.getMessage());
                            Toast.makeText(getApplicationContext(), "Error occured", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                data.remove(position);
                adapter.notifyDataSetChanged();
                return false;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_admin, menu);
        MenuItem item = menu.findItem(R.id.action_user);
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
