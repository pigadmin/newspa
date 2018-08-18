package com.spa.views;

import android.app.Dialog;
import android.content.Context;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;

import com.spa.R;
import com.spa.adaters.TypeChooseAdapter;

import java.util.List;

/**
 * 自定义提示框
 */
public class BtmDialog extends Dialog {

    public GridView mGridView;

    public BtmDialog(Context context, int mLayout, int gravity, int width, int height) {
        super(context, R.style.CustomDialog);
        setContentView(mLayout);
        getWindow().setGravity(gravity);
        getWindow().setLayout(width, height);
    }

    public BtmDialog(Context context, int mLayout, int gravity, int width, int height, List<Integer> integers) {
        super(context, R.style.CustomDialog);
        setContentView(mLayout);
        getWindow().setGravity(gravity);
        getWindow().setLayout(width, height);

        mGridView = (GridView) findViewById(R.id.gridview_gvw);
        mGridView.setAdapter(new TypeChooseAdapter(context, R.layout.item_show_more_single, integers));

    }

    protected BtmDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    @Override
    public void show() {
        super.show();
    }

    @Override
    public void dismiss() {
        super.dismiss();
    }
}
