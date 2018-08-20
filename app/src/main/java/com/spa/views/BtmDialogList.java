package com.spa.views;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.widget.GridView;
import android.widget.LinearLayout;

import com.spa.R;
import com.spa.adaters.TypeChooseAdapter;

import java.util.List;

/**
 * 自定义提示框
 */
public class BtmDialogList extends Dialog {

    public GridView mGridView;
    public TypeChooseAdapter chooseAdapter;

    public BtmDialogList(Context context, int layout, int id, List<String> list) {
        super(context, R.style.CustomDialog);
        setContentView(layout);
        getWindow().setGravity(Gravity.CENTER);
        getWindow().setLayout(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        mGridView = findViewById(id);
        chooseAdapter = new TypeChooseAdapter(context, R.layout.item_show_more_single, list);
        mGridView.setAdapter(chooseAdapter);
    }

    protected BtmDialogList(Context context, boolean cancelable, OnCancelListener cancelListener) {
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
