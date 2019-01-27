package com.noobhackers.myemotions;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class ArrayAdapter extends android.widget.ArrayAdapter {


    int[] moodNames;
    String[] logMood;
    Context mContext;

    public ArrayAdapter(Context context, int[] moodList, String[] moodLogs) {
        super(context, R.layout.activity_listview);
        this.moodNames = moodList;
        this.logMood = moodLogs;
        this.mContext = context;
    }

    @Override
    public int getCount() {
        return moodNames.length;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder mViewHolder = new ViewHolder();
        if(convertView == null) {
            LayoutInflater mInflater = (LayoutInflater) mContext.
                    getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.activity_listview, parent, false);
            mViewHolder.mMood = (ImageView) convertView.findViewById(R.id.moodView);
            mViewHolder.mLog = (TextView) convertView.findViewById(R.id.textView);
            convertView.setTag(mViewHolder);
        }
        else {
            mViewHolder = (ViewHolder)convertView.getTag();
        }
            mViewHolder.mMood.setImageResource(moodNames[position]);
            mViewHolder.mLog.setText(logMood[position]);

        return convertView;
    }

    static class ViewHolder{
        ImageView mMood;
        TextView mLog;
    }
}
