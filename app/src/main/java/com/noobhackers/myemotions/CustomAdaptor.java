package com.noobhackers.myemotions;

import android.media.Image;
import android.widget.ArrayAdapter;
import android.content.Context;
import android.view.*;
import android.widget.ImageView;
import android.widget.TextView;

public class CustomAdaptor extends ArrayAdapter<String> {

    Context context;
    String[] moods;
    int[] images;

    public CustomAdaptor(Context context, String[] moods, int[] images) {
        super(context, R.layout.spinner_mood);
        this.context = context;
        this.moods = moods;
        this.images = images;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return getView(position, convertView, parent);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder mViewHolder = new ViewHolder();
        if (convertView == null) {
            LayoutInflater mInflater = (LayoutInflater) context.
                    getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.spinner_mood, parent, false);
            mViewHolder.moodView = (ImageView) convertView.findViewById(R.id.moodImage);
            mViewHolder.moodText = (TextView) convertView.findViewById(R.id.moodText);
            convertView.setTag(mViewHolder);
        } else {
            mViewHolder = (ViewHolder) convertView.getTag();
        }
        mViewHolder.moodView.setImageResource(images[position]);
        mViewHolder.moodText.setText(moods[position]);

        return convertView;
    }

    @Override
    public int getCount() {
        return moods.length;
    }

    private static class ViewHolder {
        ImageView moodView;
        TextView moodText;
    }
}
