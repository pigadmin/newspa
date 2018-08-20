package com.spa.adaters;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.spa.R;
import com.spa.bean.EvaluateBean;

import java.util.List;


public class TypeChooseAdapter2 extends BAdapter<EvaluateBean> {

    public TypeChooseAdapter2(Context context, int layoutId, List<EvaluateBean> list) {
        super(context, layoutId, list);
    }

    @Override
    public void onInitView(View convertView, int position) {
        ImageView src = get(convertView, R.id.src);
        TextView tv = get(convertView, R.id.text_tvw2);
        EvaluateBean type = getItem(position);
        src.setImageResource(type.src);
        tv.setText(type.name);
    }
}
