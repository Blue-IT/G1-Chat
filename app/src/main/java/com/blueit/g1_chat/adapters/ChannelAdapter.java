package com.blueit.g1_chat.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.blueit.g1_chat.R;
import com.blueit.g1_chat.parseobjects.Newsflash;
import com.parse.ParseObject;

import java.util.List;

public class ChannelAdapter extends ArrayAdapter<String> {

    Context context;
    int layoutResourceId;
    List<String> data = null;

    public ChannelAdapter(Context context, int layoutResourceId, List<String> data) {
        super(context, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.data = data;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        ChannelHolder holder = null;

        if(row == null)
        {
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);

            holder = new ChannelHolder();
            holder.title = (TextView)row.findViewById(R.id.item_title);

            row.setTag(holder);
        }
        else
        {
            holder = (ChannelHolder) row.getTag();
        }

        String title = getItem(position);
        holder.title.setText(title);

        return row;
    }

    static class ChannelHolder
    {
        TextView title;
    }
}