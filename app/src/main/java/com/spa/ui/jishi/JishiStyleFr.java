package com.spa.ui.jishi;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.spa.R;
import com.spa.app.App;
import com.spa.app.Req;
import com.spa.bean.AJson;
import com.spa.bean.JishiData;
import com.spa.bean.TeachType;
import com.spa.bean.Tech;
import com.spa.event.DataMessage;
import com.spa.ui.BaseFr;
import com.spa.ui.adapter.JishiAdapter;
import com.spa.ui.adapter.TeachTypeAdapter;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.greenrobot.event.EventBus;

public class JishiStyleFr extends BaseFr implements AdapterView.OnItemClickListener, View.OnClickListener {

    private View view;
    private Activity activity;
    private App app;

    private Spinner spinner1;
    private Spinner spinner2;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.activity_jishi, container, false);
        activity = getActivity();
        app = (App) activity.getApplication();
        EventBus.getDefault().register(this);
        find();
        initView();
        init();
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
    }

    private void init() {
        Req.get(Req.teachtype);
    }

    private ListView left_list;
    private GridView right_grid;
    EditText keyword;
//    RadioButton jishi_all, jishi_sex1, jishi_sex2, status1, status2, status_all;

    private void find() {
        left_list = view.findViewById(R.id.left_list);
        left_list.setOnItemClickListener(this);
        right_grid = view.findViewById(R.id.right_grid);
        right_grid.setOnItemClickListener(this);
//        jishi_all = view.findViewById(R.id.jishi_all);
//        jishi_sex1 = view.findViewById(R.id.jishi_sex1);
//        jishi_sex2 = view.findViewById(R.id.jishi_sex2);
//        status_all = view.findViewById(R.id.status_all);
//        status1 = view.findViewById(R.id.status1);
//        status2 = view.findViewById(R.id.status2);
//        jishi_all.setOnClickListener(this);
//        jishi_sex1.setOnClickListener(this);
//        jishi_sex2.setOnClickListener(this);
//        status1.setOnClickListener(this);
//        status2.setOnClickListener(this);
//        status_all.setOnClickListener(this);

        keyword = view.findViewById(R.id.keyword);
        keyword.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

                handler.removeMessages(search);
                handler.sendEmptyMessageDelayed(search, 1000);
            }


        });

    }

    final int search = 0;
    String keywd = "";
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    if (!keyword.getText().toString().equals("")) {
                        keywd = "&keywd=" + keyword.getText().toString();
                        Req.get(Req.teach + keywd + sex + status);
                    } else {
                        Req.get(Req.teach + services);
                    }

                    break;
            }
        }
    };
    private List<TeachType> list;
    private TeachTypeAdapter adapter;
    private Tech grid;

    public void onEvent(DataMessage event) {
        try {
            if (event.getApi().equals(Req.teachtype)) {
                AJson<List<TeachType>> data = App.gson.fromJson(
                        event.getData(), new TypeToken<AJson<List<TeachType>>>() {
                        }.getType());
                list = data.getData();
                if (!list.isEmpty()) {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            resetList();
                        }
                    });
                }

            } else if (event.getApi().equals(Req.teach + services)) {
                AJson<Tech> data = App.gson.fromJson(
                        event.getData(), new TypeToken<AJson<Tech>>() {
                        }.getType());
                grid = data.getData();
//                if (!grid.getData().isEmpty()) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        resetUI();
                    }
                });
//                }

            } else if (event.getApi().equals(Req.teach + keywd + sex + status)) {
                AJson<Tech> data = App.gson.fromJson(
                        event.getData(), new TypeToken<AJson<Tech>>() {
                        }.getType());
                grid = data.getData();
//                if (!grid.getData().isEmpty()) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        resetUI();
                    }
                });
//                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String services;

    private void resetList() {
        adapter = new TeachTypeAdapter(activity, list);
        left_list.setAdapter(adapter);
        left_list.requestFocus();
        services = "&services=" + list.get(0).getId();
        Req.get(Req.teach + services);
    }


    private JishiAdapter adapter2;

    private void resetUI() {
        System.out.println(grid.getData().size());
        adapter2 = new JishiAdapter(activity, grid.getData());
        right_grid.setAdapter(adapter2);
    }

    JishiData jishi;

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        final int p = position;
        if (parent == left_list) {
            services = "&services=" + list.get(p).getId();
            Req.get(Req.teach + services);
        } else if (parent == right_grid) {
            jishi = grid.getData().get(p);
            showJishi();
        }


    }

    private AlertDialog dialog_jishi;
    private ImageView jishi_icon;
    private TextView jishi_no, jishi_sex, jishi_city, jishi_kg, jishi_height, jishi_status1, jishi_status2, jishi_project;
    private Button ok, cancle;

    private void showJishi() {
        dialog_jishi = new AlertDialog.Builder(activity).create();
        if (dialog_jishi.isShowing()) {
            dialog_jishi.dismiss();
        } else {
            dialog_jishi.show();
        }
        dialog_jishi.setContentView(R.layout.dialog_jishi);
        jishi_icon = dialog_jishi.findViewById(R.id.jishi_icon);
        Picasso.with(activity).load(jishi.getPic()).into(jishi_icon);
        jishi_no = dialog_jishi.findViewById(R.id.jishi_no);
        jishi_no.setText(getString(R.string.no) + jishi.getNumbering());
        jishi_sex = dialog_jishi.findViewById(R.id.jishi_sex);

        if (jishi.getSex() == 1) {
            jishi_sex.setText(getString(R.string.jishi_sex) + getString(R.string.jishi_sex1));
        } else if (jishi.getSex() == 2) {
            jishi_sex.setText(getString(R.string.jishi_sex) + getString(R.string.jishi_sex2));
        }
        jishi_city = dialog_jishi.findViewById(R.id.jishi_city);
        jishi_city.setText(getString(R.string.jishi_city) + jishi.getOrigin());

        jishi_kg = dialog_jishi.findViewById(R.id.jishi_kg);
        jishi_kg.setText(getString(R.string.jishi_kg) + jishi.getWeight());
        jishi_height = dialog_jishi.findViewById(R.id.jishi_height);
        jishi_height.setText(getString(R.string.jishi_height) + jishi.getHeight());

        jishi_status1 = dialog_jishi.findViewById(R.id.jishi_status1);
        if (jishi.getStatus() == 1) {
            jishi_status1.setText(getString(R.string.jishi_status) + getString(R.string.jishi_status1));
        } else if (jishi.getStatus() == 2) {
            jishi_status1.setText(getString(R.string.jishi_status) + getString(R.string.jishi_status2));
        }

        jishi_status2 = dialog_jishi.findViewById(R.id.jishi_status2);
        if (jishi.getOnduty() == 1) {
            jishi_status2.setText(getString(R.string.jishi_status3));
        } else if (jishi.getOnduty() == 2) {
            jishi_status2.setText(getString(R.string.jishi_status4));
        }
        jishi_project = dialog_jishi.findViewById(R.id.jishi_project);
        jishi_project.setText(jishi.getServices());

        ok = dialog_jishi.findViewById(R.id.ok);
        cancle = dialog_jishi.findViewById(R.id.cancle);
        ok.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                dialog_jishi.dismiss();
            }
        });
        cancle.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                dialog_jishi.dismiss();
            }
        });
    }


    String sex = "";
    String status = "";

    @Override
    public void onClick(View v) {

//        if (v == jishi_all) {
//            sex = "";
//            Req.get(Req.teach + keywd + sex + status);
//        } else if (v == jishi_sex1) {
//            sex = "&sex=1";
//            Req.get(Req.teach + keywd + sex + status);
//        } else if (v == jishi_sex2) {
//            sex = "&sex=2";
//            Req.get(Req.teach + keywd + sex + status);
//        } else if (v == status_all) {
//            status = "";
//            Req.get(Req.teach + keywd + sex + status);
//        } else if (v == status1) {
//            status = "&status=1";
//            Req.get(Req.teach + keywd + sex + status);
//        } else if (v == status2) {
        status = "&status=2";
        Req.get(Req.teach + keywd + sex + status);
//        }

    }

    private void initView() {
        spinner1 = view.findViewById(R.id.spin1);
        spinner2 = view.findViewById(R.id.spin2);
        iniLiter();
    }

    private void iniLiter() {
        ArrayAdapter<String> adapter1 = new ArrayAdapter<>(activity, android.R.layout.simple_spinner_item, activity.getResources().getStringArray(R.array.sex));
        //第三步：为适配器设置下拉列表下拉时的菜单样式。
        adapter1.setDropDownViewResource(R.layout.powin_layout);
        //第四步：将适配器添加到下拉列表上
        spinner1.setAdapter(adapter1);
        //第五步：为下拉列表设置各种事件的响应，这个事响应菜单被选中
        spinner1.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                TextView tv = (TextView) arg1;
                tv.setTextColor(getResources().getColor(R.color.white));    //设置颜色
            }

            public void onNothingSelected(AdapterView<?> arg0) {

            }
        });

        ArrayAdapter<String> adapter2 = new ArrayAdapter<>(activity, android.R.layout.simple_spinner_item, activity.getResources().getStringArray(R.array.free));
        //第三步：为适配器设置下拉列表下拉时的菜单样式。
        adapter2.setDropDownViewResource(R.layout.powin_layout);
        //第四步：将适配器添加到下拉列表上
        spinner2.setAdapter(adapter2);
        //第五步：为下拉列表设置各种事件的响应，这个事响应菜单被选中
        spinner2.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                TextView tv = (TextView) arg1;
                tv.setTextColor(getResources().getColor(R.color.white));    //设置颜色
            }

            public void onNothingSelected(AdapterView<?> arg0) {

            }
        });
    }
}
