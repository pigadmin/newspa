package com.spa.adaters;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.spa.R;
import com.spa.bean.VideoData;

import java.util.List;


public class MusicTypeAdapter extends BAdapter<VideoData> {

    private final static String TAG = "MusicTypeAdapter";
    public int mCurrentItem = 0;
    public Context context;

    public MusicTypeAdapter(Context context, int layoutId, List<VideoData> list) {
        super(context, layoutId, list);
        this.context = context;
    }

    @Override
    public void onInitView(View convertView, int position) {
        TextView musicName = get(convertView, R.id.music_name);
        TextView songName = get(convertView, R.id.music_song_name);
        VideoData type = getAllData().get(position);
        musicName.setText((position + 1) + ". " + type.getName());
        songName.setText(type.getAct());

        if (mCurrentItem == position) {
            musicName.setTextColor(context.getResources().getColor(R.color.colorAccent));
            songName.setTextColor(context.getResources().getColor(R.color.colorAccent));
        } else {
            musicName.setTextColor(context.getResources().getColor(R.color.white));
            songName.setTextColor(context.getResources().getColor(R.color.white));
        }
    }

    public void setCurrentItem(int currentItem) {
        this.mCurrentItem = currentItem;
    }
}
