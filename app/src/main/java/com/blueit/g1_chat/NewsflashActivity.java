package com.blueit.g1_chat;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;

public class NewsflashActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText newsflashText;

    private ArrayList<String> TEMP_newsflashTable;
    private ArrayAdapter<String> TEMP_newsflashTableAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set view
        setContentView(R.layout.activity_newsflash);

        // Initialize text field
        newsflashText = (EditText) findViewById(R.id.newsflash_text);
        newsflashText.setInputType(InputType.TYPE_CLASS_TEXT
                       | InputType.TYPE_TEXT_FLAG_MULTI_LINE);

        // Initialize placeholder database
        TEMP_newsflashTable = new ArrayList<String>();
        TEMP_newsflashTable.add("Lorem");
        TEMP_newsflashTable.add("Ipsum");

        // Configure the adapter which feeds database info into the list
        TEMP_newsflashTableAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, TEMP_newsflashTable);
        ListView listView = (ListView) findViewById(R.id.newsflash_list);
        listView.setAdapter(TEMP_newsflashTableAdapter);

        // Setup submit button
        setClick(R.id.newsflash_submit);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_newsflash, menu);
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
        // Get input
        String input = newsflashText.getText().toString();

        // Validate
        Log.d("G1CHAT", "CreateNewsflash");
        Log.d("G1CHAT", input);
        if (input == null || input == "") {

            Log.d("G1CHAT", "Early exit");
            return;
        }

        // Create and insert
        TEMP_newsflashTable.add(input);
        TEMP_newsflashTableAdapter.notifyDataSetChanged();
        Log.d("G1CHAT", "Newsflash created!");
        // Reset input
        newsflashText.setText("");
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
