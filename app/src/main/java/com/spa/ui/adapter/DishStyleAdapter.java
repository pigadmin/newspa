package com.spa.ui.adapter;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.spa.R;
import com.spa.bean.DishStyle;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;


public class DishStyleAdapter extends BaseAdapter {
    private Context context;
    private List<DishStyle> list = new ArrayList<>();

    ListView left_list;

    public DishStyleAdapter(Context context, List<DishStyle> list, ListView left_list) {
        this.context = context;
        this.list = list;
        this.left_list = left_list;
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
        holder.intro_name.setSelected(true);
        Picasso.with(context).load(Uri.parse(list.get(position).getIcon())).into(holder.icon);

        if (left_list.isItemChecked(position)) {
            convertView.setBackgroundResource(R.mipmap.youce_k_1);
        } else {
            convertView.setBackgroundColor(Color.TRANSPARENT);
        }

        return convertView;
    }

    public class ViewHolder {
        private TextView intro_name;
        private ImageView icon;
    }
}
