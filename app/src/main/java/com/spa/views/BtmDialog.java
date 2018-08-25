package com.spa.views;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.widget.LinearLayout;

import com.spa.R;

/**
 * 自定义提示框
 */
public class BtmDialog extends Dialog {

    public BtmDialog(Context context, int layout, int gravity, int width, int height) {
        super(context, R.style.CustomDialog);
        setContentView(layout);
        getWindow().setGravity(gravity);
        getWindow().setLayout(width, height);
    }

    public BtmDialog(Context context, int layout) {
        super(context, R.style.CustomDialog);
        initConfigure(layout);
    }

    public BtmDialog(Context context, int themeResId, int layout) {
        super(context, themeResId);
        initConfigure(layout);
    }

    private void initConfigure(int layout) {
        setContentView(layout);
        getWindow().setGravity(Gravity.CENTER);
        getWindow().setLayout(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        setCancelable(false);
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
