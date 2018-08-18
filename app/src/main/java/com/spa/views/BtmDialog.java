package com.spa.views;

import android.app.Dialog;
import android.content.Context;

import com.spa.R;

/**
 * 自定义提示框
 */
public class BtmDialog extends Dialog {

    public BtmDialog(Context context, int mLayout, int gravity, int width, int height) {
        super(context, R.style.CustomDialog);
        setContentView(mLayout);
        getWindow().setGravity(gravity);
        getWindow().setLayout(width, height);
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
