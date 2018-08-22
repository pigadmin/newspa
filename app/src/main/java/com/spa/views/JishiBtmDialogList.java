package com.spa.views;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.spa.R;
import com.spa.adaters.TypeChooseAdapter;
import com.spa.bean.JishiData;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * 自定义提示框
 */
public class JishiBtmDialogList extends Dialog {

    public GridView mGridView;
    public TypeChooseAdapter chooseAdapter;
    private ImageView jishi_icon;
    private TextView jishi_no, jishi_sex, jishi_city, jishi_kg, jishi_height, jishi_status1, jishi_status2, jishi_project;
    private Button ok, cancle;

    public JishiBtmDialogList(Context context, JishiData jishi) {
        super(context, R.style.CustomDialog);
        setContentView(R.layout.dialog_style8);
        getWindow().setGravity(Gravity.CENTER);
        getWindow().setLayout(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        mGridView = findViewById(R.id.gridview_gvw1);


        jishi_icon = findViewById(R.id.jishi_icon);
        Picasso.with(context).load(jishi.getPic()).into(jishi_icon);
        jishi_no = findViewById(R.id.jishi_no);
        jishi_no.setText(jishi.getNumbering());
        jishi_sex = findViewById(R.id.jishi_sex);

        if (jishi.getSex() == 1) {
            jishi_sex.setText(context.getString(R.string.jishi_sex1));
        } else if (jishi.getSex() == 2) {
            jishi_sex.setText(context.getString(R.string.jishi_sex2));
        }
        jishi_city = findViewById(R.id.jishi_city);
        jishi_city.setText(jishi.getOrigin());

        jishi_kg = findViewById(R.id.jishi_kg);
        jishi_kg.setText(jishi.getWeight());
        jishi_height = findViewById(R.id.jishi_height);
        jishi_height.setText(jishi.getHeight());

        jishi_status1 = findViewById(R.id.jishi_status1);
        if (jishi.getStatus() == 1) {
            jishi_status1.setText(context.getString(R.string.jishi_status1));
        } else if (jishi.getStatus() == 2) {
            jishi_status1.setText(context.getString(R.string.jishi_status2));
        }

        jishi_status2 = findViewById(R.id.jishi_status2);
        if (jishi.getOnduty() == 1) {
            jishi_status2.setText(context.getString(R.string.jishi_status3));
        } else if (jishi.getOnduty() == 2) {
            jishi_status2.setText(context.getString(R.string.jishi_status4));
        }

//        ok = findViewById(R.id.ok);
//
//        ok.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View arg0) {
//                dismiss();
//            }
//        });
//
        cancle = findViewById(R.id.cancle);

        cancle.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                dismiss();
            }
        });


        List<String> tmp = new ArrayList<>();
        String[] s = jishi.getServices().split(" ");
        for (int i = 0; i < s.length; i++) {
            tmp.add(s[i]);
        }

        chooseAdapter = new TypeChooseAdapter(context, R.layout.item_show_more_single, tmp);
        mGridView.setAdapter(chooseAdapter);


    }

    protected JishiBtmDialogList(Context context, boolean cancelable, OnCancelListener cancelListener) {
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
