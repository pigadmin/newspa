package com.spa.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.spa.R;
import com.spa.bean.Info;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;


public class IntroAdapter extends BaseAdapter {
    private Context context;
    private List<Info> list = new ArrayList<>();

    public IntroAdapter(Context context, List<Info> list) {
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
            convertView = LayoutInflater.from(context).inflate(R.layout.adapter_intro, null);
            holder.icon = convertView.findViewById(R.id.icon);
            holder.intro_name = convertView.findViewById(R.id.intro_name);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        Picasso.with(context).load(list.get(position).getPath()).into(holder.icon);
        holder.intro_name.setText(list.get(position).getName());
        holder.intro_name.setSelected(true);

        return convertView;
    }

    public class ViewHolder {
        private ImageView icon;
        private TextView intro_name;
    }
}
