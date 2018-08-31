package com.spa.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.spa.R;
import com.spa.bean.JishiData;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;


public class JishiAdapter extends BaseAdapter {
    private Context context;
    private List<JishiData> list = new ArrayList<>();

    public JishiAdapter(Context context, List<JishiData> list) {
        // TODO Auto-generated constructor stub
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub

        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(
                    R.layout.adapter_dish, null);
            holder.game_bg = convertView
                    .findViewById(R.id.game_bg);
            holder.game_name = convertView
                    .findViewById(R.id.game_name);
            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        if (list.get(position).getStatus() == 1) {
            holder.game_name.setText(list.get(position).getNumbering()+" "+"（上钟）");
        } else {
            holder.game_name.setText(list.get(position).getNumbering()+" "+"（空闲）");
        }

        Picasso.with(context).load(list.get(position).getPic()).into(holder.game_bg);

        return convertView;

    }


    public class ViewHolder {
        private ImageView game_bg;
        private TextView game_name;

    }

}
