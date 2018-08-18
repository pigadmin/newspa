package com.spa.adaters;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.spa.R;

import java.util.List;


public class TypeChooseAdapter extends BAdapter<Integer> {

    public TypeChooseAdapter(Context context, int layoutId, List<Integer> list) {
        super(context, layoutId, list);
    }

    @Override
    public void onInitView(View convertView, int position) {
        TextView tv = get(convertView, R.id.text_tvw);
        int type = getItem(position);
        tv.setText(type + "");
    }
}
