package com.spa.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.spa.R;
import com.spa.bean.Game;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;


public class GameAdapter extends BaseAdapter {
    private Context context;
    private List<Game> list = new ArrayList<>();

    public GameAdapter(Context context, List<Game> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(
                    R.layout.adapter_game, null);
            holder.game_bg = convertView
                    .findViewById(R.id.game_bg);
            holder.game_name = convertView
                    .findViewById(R.id.game_name);
            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.game_name.setText(list.get(position).getName());
        Picasso.with(context).load(list.get(position).getIcon()).into(holder.game_bg);
        return convertView;
    }

    public class ViewHolder {
        private ImageView game_bg;
        private TextView game_name;
    }
}
