package com.spa.adaters;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.widget.TextView;

import com.spa.R;
import com.spa.views.MyGridView;

import java.util.List;


public class TypeChooseAdapter extends BAdapter<String> {

    public MyGridView mGridView;
    public Context context;

    public TypeChooseAdapter(Context context, int layoutId, List<String> list, MyGridView mGridView) {
        super(context, layoutId, list);
        this.mGridView = mGridView;
        this.context = context;
    }

    @Override
    public void onInitView(View convertView, int position) {
        TextView tv = get(convertView, R.id.text_tvw);
        String type = getItem(position);
        tv.setText(type);

        //判断position位置是否被选中，改变颜色
        if (mGridView.isItemChecked(position)) {
            convertView.setBackgroundColor(context.getResources().getColor(R.color.zz_ys));
        } else {
            convertView.setBackgroundColor(Color.TRANSPARENT);
        }
    }
}
