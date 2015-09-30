package com.blueit.g1_chat;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.blueit.g1_chat.parseobjects.Newsflash;
import com.parse.ParseException;
import com.parse.SaveCallback;

public class CreateNewsflashActivity extends AppCompatActivity implements View.OnClickListener{


    private EditText inputTitle;
    private EditText inputContent;

    MasterMenu menu = new MasterMenu(CreateNewsflashActivity.this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_newsflash);

        inputTitle =  (EditText) findViewById(R.id.create_newsflash_title);
        inputContent =  (EditText) findViewById(R.id.create_newsflash_content);
        inputContent.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_MULTI_LINE);
        setClick(R.id.create_newsflash_submit);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_admin, menu);
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

        if (id == R.id.create_newsflash_submit)
        {
            createNewsflash();
        }

    }

    public void createNewsflash()
    {
        // Get input
        String title = inputTitle.getText().toString();
        String content = inputContent.getText().toString();

        // Verify
        if (title.equals("") || content.equals("")) {
            Toast.makeText(CreateNewsflashActivity.this, R.string.err_fields_empty, Toast.LENGTH_LONG)
                    .show();
            return;
        }

        // Create parseobject
        final Newsflash entry = new Newsflash();
        entry.setTitle(title);
        entry.setContent(content);

        // Save
        entry.saveInBackground(new SaveCallback() {
            public void done(ParseException e) {
                // Send return data
                if (e == null) {
                    setResult(Activity.RESULT_OK,
                            new Intent().putExtra("id", entry.getObjectId()));
                    finish();
                } else {
                    CancelActivity();
                }
            }
        });
    }

    public void CancelActivity()
    {
        setResult(Activity.RESULT_CANCELED);
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
