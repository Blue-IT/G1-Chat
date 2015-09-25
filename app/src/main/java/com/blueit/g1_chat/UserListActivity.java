package com.blueit.g1_chat;

import android.app.ListActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

public class UserListActivity extends ListActivity {

    List<User> users = new ArrayList<User>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_list);

        ParseQuery<User> query = new ParseQuery<User>(User.class);
        query.findInBackground(new FindCallback<User>() {
            @Override
            public void done(List<User> list, ParseException e) {
                if (e != null){
                    Toast.makeText(UserListActivity.this, "Error " + e, Toast.LENGTH_SHORT).show();
                }
                for (User u : list){
                    User newUser = new User();
                    newUser.setName(u.getName());
                    users.add(newUser);
                }

                ArrayAdapter<User> adapter = new ArrayAdapter<User>(UserListActivity.this, android.R.layout.simple_list_item_1, users);
                setListAdapter(adapter);
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_user_list, menu);
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
}
