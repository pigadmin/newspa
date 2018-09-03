package com.spa.ui.jishi;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
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
import com.spa.tools.Logger;
import com.spa.tools.StringUtils;
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

    private final static String TAG = "JishiStyleFr";

    private View view;
    private Activity activity;
    private App app;

    private TextView mChoice1;
    private TextView mChoice2;

    private LinearLayout mSpin1llt;
    private LinearLayout mSpin2llt;

    private ImageView mSpin1Src;
    private ImageView mSpin2Src;


    private long finshTime = 0;

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
        left_list.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
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
                try {
                    typeCls = "&typeCls=" + list.get(groupPosition).getId();
                    Req.get(Req.teach + typeCls);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                adapter.setSelecte(true);
                adapter.notifyDataSetChanged();
                return false;
            }
        });

        /**
         * 设置子选项点击监听事件
         */
        left_list.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                try {
                    services = "&services=" + list.get(groupPosition).getTechTypes().get(childPosition).getId();
                    Req.get(Req.teach + typeCls + services);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                adapter.setSelectedItem(groupPosition, childPosition);
                adapter.notifyDataSetChanged();
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
    public MyExAdater adapter;
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

        adapter = new MyExAdater(list, itemList, activity, R.layout.adapter_intro_1, R.layout.adapter_intro_2, left_list, activity);
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

            long beginTime = System.currentTimeMillis();
            if (beginTime - finshTime < 1000) {
                return;
            }
            showDialogStyle8();
            finshTime = beginTime;
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
        mSpin1Src = view.findViewById(R.id.spin1_src);
        mSpin2Src = view.findViewById(R.id.spin2_src);

        mSpin1llt = view.findViewById(R.id.spin1_llt);
        mSpin2llt = view.findViewById(R.id.spin2_llt);

        mChoice1 = view.findViewById(R.id.spin1);
        mChoice2 = view.findViewById(R.id.spin2);
        iniLiter();
    }

    private void Attribute1(View strView, final PopupWindow window) {
        final TextView textType1 = strView.findViewById(R.id.text_type_1);
        final TextView textType2 = strView.findViewById(R.id.text_type_2);
        final TextView textType3 = strView.findViewById(R.id.text_type_3);

        textType1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mChoice1.setText(textType1.getText().toString());
                sex = "";
                Req.get(Req.teach + keywd + sex + status);
                mSpin1Src.setImageResource(R.mipmap.dbx_1);
                window.dismiss();
            }
        });

        textType2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mChoice1.setText(textType2.getText().toString());
                sex = "&sex=1";
                Req.get(Req.teach + keywd + sex + status);
                mSpin1Src.setImageResource(R.mipmap.dbx_1);
                window.dismiss();
            }
        });

        textType3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mChoice1.setText(textType3.getText().toString());
                sex = "&sex=2";
                Req.get(Req.teach + keywd + sex + status);
                mSpin1Src.setImageResource(R.mipmap.dbx_1);
                window.dismiss();
            }
        });
    }

    private void Attribute2(View strView, final PopupWindow window) {
        final TextView textType1 = strView.findViewById(R.id.text_type_1);
        final TextView textType2 = strView.findViewById(R.id.text_type_2);
        final TextView textType3 = strView.findViewById(R.id.text_type_3);

        textType1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mChoice2.setText(textType1.getText().toString());
                status = "";
                Req.get(Req.teach + keywd + sex + status);
                mSpin2Src.setImageResource(R.mipmap.dbx_1);
                window.dismiss();
            }
        });

        textType2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mChoice2.setText(textType2.getText().toString());
                status = "&status=2";
                Req.get(Req.teach + keywd + sex + status);
                mSpin2Src.setImageResource(R.mipmap.dbx_1);
                window.dismiss();
            }
        });

        textType3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mChoice2.setText(textType3.getText().toString());
                status = "&status=1";
                Req.get(Req.teach + keywd + sex + status);
                mSpin2Src.setImageResource(R.mipmap.dbx_1);
                window.dismiss();
            }
        });
    }

    private void showDiag1() {
        mSpin1Src.setImageResource(R.mipmap.dbx);
        View strView = getActivity().getLayoutInflater().inflate(R.layout.serch_powin_layout_dialog1, null, false);
        final PopupWindow window = new PopupWindow(strView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        window.setOutsideTouchable(true);
        window.setBackgroundDrawable(new BitmapDrawable());
        window.showAsDropDown(mSpin1llt, 0, 0, 0);
        window.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                mSpin1Src.setImageResource(R.mipmap.dbx_1);
            }
        });
        Attribute1(strView, window);
    }

    private void showDiag2() {
        mSpin2Src.setImageResource(R.mipmap.dbx);
        View strView = getActivity().getLayoutInflater().inflate(R.layout.serch_powin_layout_dialog2, null, false);
        final PopupWindow window = new PopupWindow(strView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        window.setOutsideTouchable(true);
        window.setBackgroundDrawable(new BitmapDrawable());
        window.showAsDropDown(mSpin2llt, 0, 0, 0);
        window.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                mSpin2Src.setImageResource(R.mipmap.dbx_1);
            }
        });
        Attribute2(strView, window);
    }

    private void iniLiter() {
        mSpin1llt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDiag1();
            }
        });

        mSpin2llt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDiag2();
            }
        });
    }

    private void showDialogStyle8() {
        final JishiBtmDialogList dialog = new JishiBtmDialogList(activity, jishi);
        dialog.show();

        final List<String> tmp = new ArrayList<>();
        String str = jishi.getServices().replace(" ", "、");
        String[] s = str.split("、");
        for (int i = 0; i < s.length; i++) {
            tmp.add(s[i] + "、");
        }

        final List<String> resultList = new ArrayList<>();
        final GridView mGridView = dialog.findViewById(R.id.gridview_gvw1);
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int postion, long l) {
                if (jishi != null) {
                    if (!TextUtils.isEmpty(jishi.getServices().trim())) {
                        Logger.d(TAG, "选中的..." + mGridView.isItemChecked(postion) + "选中的值..." + tmp.get(postion));
                        if (mGridView.isItemChecked(postion)) {
                            resultList.add(tmp.get(postion));
                        } else {
                            resultList.remove(postion);
                        }
                        dialog.chooseAdapter.notifyDataSetChanged();
                    }
                }
            }
        });
        ok = dialog.findViewById(R.id.ok);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {

                if (jishi.getStatus() == 1) {
                    Toast.makeText(activity, jishi.getNumbering() + "号技师正在上钟，请返回重新下单...", Toast.LENGTH_LONG).show();
                } else if (jishi.getStatus() == 2) {
                    String result = StringUtils.ListToString(resultList);
                    Logger.d(TAG, "str.." + result);
                    if (!TextUtils.isEmpty(result)) {
                        result = result.substring(0, result.length() - 1);
                        showDialogStyle9(result);
                        dialog.dismiss();
                    } else {
                        Toast.makeText(activity, "选择服务项目", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
    }

    private TextView user_name;

    private void showDialogStyle9(String result) {
        final BtmDialog dialog = new BtmDialog(activity, R.layout.dialog_style9);
        dialog.show();
        jishi_no = dialog.findViewById(R.id.jishi_no);
        jishi_no.setText(jishi.getNumbering());
        user_name = dialog.findViewById(R.id.user_name);
        user_name.setText(app.getUser().getName());
        jishi_project = dialog.findViewById(R.id.jishi_project);
        jishi_project.setText(result);
        cancle = dialog.findViewById(R.id.cancle);
        cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                dialog.dismiss();
            }
        });

//        handler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                System.out.println("------------");
//                dialog.dismiss();
//                Toast.makeText(activity, "下单成功", Toast.LENGTH_LONG).show();
//            }
//        }, 2000);


        dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                System.out.println("@@@@@jishi" + keyCode);
                if (keyCode == KeyEvent.KEYCODE_ENTER) {
                    System.out.println("------------");
                    dialog.dismiss();
                    Toast.makeText(activity, "下单成功", Toast.LENGTH_LONG).show();
                }
                return false;
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
