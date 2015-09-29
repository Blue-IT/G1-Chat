package com.blueit.g1_chat.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.blueit.g1_chat.R;
import com.parse.ParseUser;

import java.util.List;


/**
 * Created by Alex on 2015-09-27.
 */
public class UserListAdapter extends ArrayAdapter<ParseUser> {

    Context context;
    int layoutResourceId;
    List<ParseUser> users = null;

    public UserListAdapter(Context con, int layResID, List<ParseUser> data){
        super(con, layResID, data);
        this.context = con;
        this.layoutResourceId = layResID;
        this.users = data;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View itemView = convertView;
        ViewHolder holder;

        if(itemView == null){
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            itemView = inflater.inflate(layoutResourceId, parent, false);

            holder = new ViewHolder();
            holder.name = (TextView)itemView.findViewById(R.id.item_name);
            holder.username = (TextView)itemView.findViewById(R.id.item_username);
            itemView.setTag(holder);

        }else{
            holder = (ViewHolder)itemView.getTag();
        }

        //Find the user to work with
        ParseUser currentUser = users.get(position);

        //Fill the listView{
        holder.name.setText(currentUser.getString("name"));
        holder.username.setText(currentUser.getString("username"));

        return itemView;

    }

    public static class ViewHolder {
        TextView name;
        TextView username;
    }

}
