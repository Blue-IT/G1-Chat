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

import java.util.List;

public class NewsflashAdapter extends ArrayAdapter<Newsflash> {

    Context context;
    int layoutResourceId;
    List<Newsflash> data = null;

    public NewsflashAdapter(Context context, int layoutResourceId, List<Newsflash> data) {
        super(context, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.data = data;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        NewsflashHolder holder = null;

        if(row == null)
        {
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);

            holder = new NewsflashHolder();
            holder.title = (TextView)row.findViewById(R.id.item_title);
            holder.content = (TextView)row.findViewById(R.id.item_content);

            row.setTag(holder);
        }
        else
        {
            holder = (NewsflashHolder) row.getTag();
        }

        Newsflash nf = getItem(position);
        holder.title.setText(nf.getTitle());
        holder.content.setText(nf.getContent());

        return row;
    }

    static class NewsflashHolder
    {
        TextView title;
        TextView content;
    }
}