package com.spa.ui.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.spa.R;
import com.spa.bean.DishStyle;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;


public class DishStyleAdapter extends BaseAdapter {
    private Context context;
    private List<DishStyle> list = new ArrayList<>();

    public DishStyleAdapter(Context context, List<DishStyle> list) {
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
                    R.layout.adapter_intro, null);
            holder.intro_name = convertView
                    .findViewById(R.id.intro_name);
            holder.icon = convertView
                    .findViewById(R.id.icon);


            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.intro_name.setText(list.get(position).getName());
        Picasso.with(context).load(Uri.parse(list.get(position).getIcon())).into(holder.icon);
        return convertView;
    }


    public class ViewHolder {
        private TextView intro_name;
        private ImageView icon;
    }
}
