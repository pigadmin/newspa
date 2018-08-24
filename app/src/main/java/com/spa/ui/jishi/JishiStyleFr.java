package com.spa.ui.jishi;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.reflect.TypeToken;
import com.spa.R;
import com.spa.adaters.MyExAdater;
import com.spa.app.App;
import com.spa.app.Req;
import com.spa.bean.AJson;
import com.spa.bean.JishiData;
import com.spa.bean.TeachType;
import com.spa.bean.Tech;
import com.spa.bean.TechTypes;
import com.spa.event.DataMessage;
import com.spa.ui.BaseFr;
import com.spa.ui.adapter.JishiAdapter;
import com.spa.views.BtmDialog;
import com.spa.views.JishiBtmDialogList;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * 技师服务
 */
public class JishiStyleFr extends BaseFr implements AdapterView.OnItemClickListener, View.OnClickListener {

    private View view;
    private Activity activity;
    private App app;

    private Spinner spinner1;
    private Spinner spinner2;

    private String[] SexArray = new String[]{"全部", "男", "女"};
    private String[] FreeArray = new String[]{"全部", "空闲", "上钟"};

    public StringBuilder mBuilder;

    public String mResult;

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

    private ExpandableListView left_list;
    private GridView right_grid;
    EditText keyword;
//    RadioButton jishi_all, jishi_sex1, jishi_sex2, status1, status2, status_all;

    private void find() {
        left_list = view.findViewById(R.id.view_exlist);
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

        /**
         * 设置分组项的点击监听事件
         */

        left_list.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                Log.d("onGroupClick..", "groupPosition.." + groupPosition);
                try {
                    typeCls = "&typeCls=" + list.get(groupPosition).getId();
                    Req.get(Req.teach + typeCls);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return false;
            }
        });

        /**
         * 设置子选项点击监听事件
         */

        left_list.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                Log.d("onChildClick..", "groupPosition.." + groupPosition + "..childPosition.." + childPosition);
                try {
                    services = "&services=" + list.get(groupPosition).getTechTypes().get(childPosition).getId();
                    Req.get(Req.teach + typeCls + services);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                return false;
            }
        });
    }

    private String typeCls = "";
    private String services = "";

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
                        Req.get(Req.teach + typeCls + services);
                    }

                    break;
            }
        }
    };
    private List<TeachType> list;
    private MyExAdater adapter;
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

            } else if (event.getApi().equals(Req.teach + typeCls) || event.getApi().equals(Req.teach + typeCls + services)
                    || event.getApi().equals(Req.teach + keywd + sex + status)) {
                AJson<Tech> data = App.gson.fromJson(
                        event.getData(), new TypeToken<AJson<Tech>>() {
                        }.getType());
                grid = data.getData();
//                System.out.println(grid.getData().size() + "@@@@@@@@@@@@");
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

    public List<List> itemList;

    private void resetList() {
        itemList = new ArrayList<>();
        for (TeachType type : list) {
            ArrayList ar2 = new ArrayList();
            for (TechTypes types : type.getTechTypes()) {
                if (types != null) {
                    ar2.add(types);
                }
            }
            itemList.add(ar2);
        }

        adapter = new MyExAdater(list, itemList, activity, R.layout.adapter_intro, R.layout.adapter_intro_1);
        left_list.setAdapter(adapter);
        typeCls = "&typeCls=" + list.get(0).getId();
        Req.get(Req.teach + typeCls);
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
//            services = "&services=" + list.get(p).getId();
//            Req.get(Req.teach + services);
        } else if (parent == right_grid) {
            jishi = grid.getData().get(p);
//            showJishi();
            showDialogStyle8();
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
//        status = "&status=2";
//        Req.get(Req.teach + keywd + sex + status);
//        }

    }

    private void initView() {
        spinner1 = view.findViewById(R.id.spin1);
        spinner2 = view.findViewById(R.id.spin2);
        iniLiter();
    }

    private void iniLiter() {
        ArrayAdapter<String> adapter1 = new ArrayAdapter<>(activity, android.R.layout.simple_spinner_item, SexArray);
        //第三步：为适配器设置下拉列表下拉时的菜单样式。
        adapter1.setDropDownViewResource(R.layout.powin_layout);
        //第四步：将适配器添加到下拉列表上
        spinner1.setAdapter(adapter1);
        //第五步：为下拉列表设置各种事件的响应，这个事响应菜单被选中
        spinner1.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                if (arg1 != null) {
                    TextView tv = (TextView) arg1;
                    tv.setTextColor(getResources().getColor(R.color.white));    //设置颜色
                }
                switch (arg2) {
                    case 0:
                        sex = "";
                        break;
                    case 1:
                        sex = "&sex=1";
                        break;
                    case 2:
                        sex = "&sex=2";
                        break;
                }

                Req.get(Req.teach + keywd + sex + status);
            }

            public void onNothingSelected(AdapterView<?> arg0) {

            }
        });

        ArrayAdapter<String> adapter2 = new ArrayAdapter<>(activity, android.R.layout.simple_spinner_item, FreeArray);
        //第三步：为适配器设置下拉列表下拉时的菜单样式。
        adapter2.setDropDownViewResource(R.layout.powin_layout);
        //第四步：将适配器添加到下拉列表上
        spinner2.setAdapter(adapter2);
        //第五步：为下拉列表设置各种事件的响应，这个事响应菜单被选中
        spinner2.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                if (arg1 != null) {
                    TextView tv = (TextView) arg1;
                    tv.setTextColor(getResources().getColor(R.color.white));    //设置颜色
                }
                switch (arg2) {
                    case 0:
                        status = "";
                        break;
                    case 1:
                        status = "&status=2";
                        break;
                    case 2:
                        status = "&status=1";
                        break;
                }

                Req.get(Req.teach + keywd + sex + status);
            }

            public void onNothingSelected(AdapterView<?> arg0) {

            }
        });
    }


    private void showDialogStyle8() {

        mBuilder = new StringBuilder();
        final JishiBtmDialogList dialog = new JishiBtmDialogList(activity, jishi);
        dialog.show();
        ok = dialog.findViewById(R.id.ok);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                if (!TextUtils.isEmpty(mBuilder.toString())) {
                    dialog.dismiss();
                    showDialogStyle9();
                } else {
                    Toast.makeText(activity, "选择服务项目", Toast.LENGTH_LONG).show();
                }
            }
        });

        final List<String> tmp = new ArrayList<>();
        String str = jishi.getServices().replace(" ", "、");
        String[] s = str.split("、");
        for (int i = 0; i < s.length; i++) {
            tmp.add(s[i] + "、");
        }
        final GridView mGridView = dialog.findViewById(R.id.gridview_gvw1);
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int postion, long l) {
                if (jishi != null) {
                    if (!TextUtils.isEmpty(jishi.getServices().trim())) {
                        if (mGridView.isItemChecked(postion)) {
                            mBuilder.append(tmp.get(postion));
                        }
                        String result = mBuilder.toString();
                        mResult = result.substring(0, result.length() - 1);
                        dialog.chooseAdapter.notifyDataSetChanged();
                    }
                }
            }
        });
    }

    private TextView user_name;

    private void showDialogStyle9() {
        final BtmDialog dialog = new BtmDialog(activity, R.layout.dialog_style9);
        dialog.show();
        jishi_no = dialog.findViewById(R.id.jishi_no);
        jishi_no.setText(jishi.getNumbering());
        user_name = dialog.findViewById(R.id.user_name);
        user_name.setText(app.getUser().getName());
        jishi_project = dialog.findViewById(R.id.jishi_project);
        jishi_project.setText(mResult);
        cancle = dialog.findViewById(R.id.cancle);
        cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                dialog.dismiss();
            }
        });
    }

    private void showDialogStyle10() {
        BtmDialog dialog = new BtmDialog(activity, R.layout.dialog_style10);
        dialog.show();
    }

    private void showDialogStyle11() {
        BtmDialog dialog = new BtmDialog(activity, R.layout.dialog_style11);
        dialog.show();
    }
}
