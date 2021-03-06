package com.spa.ui.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.spa.R;
import com.spa.bean.LiveIP;

import java.util.ArrayList;
import java.util.List;


public class LiveListAdapter extends BaseAdapter {
    private Activity activity;

    private List<LiveIP> list = new ArrayList<>();

    public LiveListAdapter(Activity activity, List<LiveIP> list) {
        this.activity = activity;
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
            convertView = LayoutInflater.from(activity).inflate(
                    R.layout.adapter_livelist, null);
            holder.livelist_no = convertView
                    .findViewById(R.id.livelist_no);
            holder.livelist_name = convertView
                    .findViewById(R.id.livelist_name);
            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.livelist_no.setText(liveno(position));
        holder.livelist_name.setText(list.get(position).getName());
        return convertView;
    }

    private String liveno(int position) {
        if ((position + 1) < 10)
            return "0" + (position + 1);
        return (position + 1) + "";
    }

    public class ViewHolder {
        private TextView livelist_no;
        private TextView livelist_name;
    }
}
