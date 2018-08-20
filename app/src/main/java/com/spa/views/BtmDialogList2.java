package com.spa.views;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.widget.GridView;
import android.widget.LinearLayout;

import com.spa.R;
import com.spa.adaters.TypeChooseAdapter;
import com.spa.adaters.TypeChooseAdapter2;
import com.spa.bean.EvaluateBean;

import java.util.List;

/**
 * 自定义提示框
 */
public class BtmDialogList2 extends Dialog {

    public GridView mGridViewAmount;
    public TypeChooseAdapter chooseAdapter;

    public GridView mGridViewService;
    public TypeChooseAdapter2 chooseAdapter2;

    public BtmDialogList2(Context context, List<String> list, List<EvaluateBean> list2) {
        super(context, R.style.CustomDialog);
        setContentView(R.layout.dialog_style18);
        getWindow().setGravity(Gravity.CENTER);
        getWindow().setLayout(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        initView(context, list, list2);
    }

    private void initView(Context context, List<String> list, List<EvaluateBean> list2) {
        mGridViewAmount = findViewById(R.id.gridview_gvw3);
        chooseAdapter = new TypeChooseAdapter(context, R.layout.item_show_more_single, list);
        mGridViewAmount.setAdapter(chooseAdapter);

        mGridViewService = findViewById(R.id.gridview_gvw4);
        chooseAdapter2 = new TypeChooseAdapter2(context, R.layout.item_show_more_single2, list2);
        mGridViewService.setAdapter(chooseAdapter2);
    }

    protected BtmDialogList2(Context context, boolean cancelable, OnCancelListener cancelListener) {
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
