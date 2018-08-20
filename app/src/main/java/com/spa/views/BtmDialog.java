package com.spa.views;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;

import com.spa.R;

/**
 * 自定义提示框
 */
public class BtmDialog extends Dialog {

    public BtmDialog(Context context, View view, int gravity, int width, int height) {
        super(context, R.style.CustomDialog);
        setContentView(view);
        getWindow().setGravity(gravity);
        getWindow().setLayout(width, height);
    }

    public BtmDialog(Context context, View view) {
        super(context, R.style.CustomDialog);
        initConfigure(view);
    }

    public BtmDialog(Context context, int themeResId, View view) {
        super(context, themeResId);
        initConfigure(view);
    }

    private void initConfigure(View view) {
        setContentView(view);
        getWindow().setGravity(Gravity.CENTER);
        getWindow().setLayout(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
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
