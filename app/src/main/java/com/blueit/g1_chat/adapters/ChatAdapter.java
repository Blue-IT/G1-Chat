package com.blueit.g1_chat.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.blueit.g1_chat.R;
import com.blueit.g1_chat.parseobjects.ChatMessage;

import java.util.List;

public class ChatAdapter extends ArrayAdapter<ChatMessage> {

    Context context;
    int layoutResourceId;
    List<ChatMessage> data = null;

    public ChatAdapter(Context context, int layoutResourceId, List<ChatMessage> data) {
        super(context, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.data = data;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        ChatMessageHolder holder = null;

        if(row == null)
        {
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);

            holder = new ChatMessageHolder();
            holder.author = (TextView)row.findViewById(R.id.item_author);
            holder.content = (TextView)row.findViewById(R.id.item_content);

            row.setTag(holder);
        }
        else
        {
            holder = (ChatMessageHolder) row.getTag();
        }

        ChatMessage msg = getItem(position);
        holder.author.setText(msg.getAuthor());
        holder.content.setText(msg.getContent());

        return row;
    }

    static class ChatMessageHolder
    {
        TextView author;
        TextView content;
    }
}