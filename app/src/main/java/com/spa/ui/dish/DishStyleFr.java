package com.spa.ui.dish;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.spa.R;
import com.spa.app.App;
import com.spa.app.Req;
import com.spa.bean.AJson;
import com.spa.bean.Dish;
import com.spa.bean.DishStyle;
import com.spa.event.DataMessage;
import com.spa.tools.Fragments;
import com.spa.ui.BaseFr;
import com.spa.ui.adapter.DishAdapter;
import com.spa.ui.adapter.DishStyleAdapter;
import com.spa.ui.bottom.OrderFr;
import com.spa.ui.diy.Toas;
import com.spa.views.BtmDialog;
import com.squareup.picasso.Picasso;

import java.net.URLEncoder;
import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * 酒水饮料
 */
public class DishStyleFr extends BaseFr implements AdapterView.OnItemClickListener {

    private View view;
    private Activity activity;
    private App app;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_dish, container, false);
        activity = getActivity();
        app = (App) activity.getApplication();
        EventBus.getDefault().register(this);
        find();
        init();
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
    }

    private void init() {
        Req.get(Req.dishstyle);
    }

    private ListView left_list;
    private GridView right_grid;
    private ImageView zhangdanchaxun;

    private void find() {
        left_list = view.findViewById(R.id.left_list);
        left_list.setOnItemClickListener(this);
        right_grid = view.findViewById(R.id.right_grid);
        right_grid.setOnItemClickListener(this);
        zhangdanchaxun = view.findViewById(R.id.zhangdanchaxun);
        zhangdanchaxun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragments.Replace(activity.getFragmentManager(), new OrderFr());
            }
        });
    }

    private List<DishStyle> list;
    private DishStyleAdapter adapter;
    private Handler handler = new Handler();
    private List<Dish> grid;

    public void onEvent(DataMessage event) {
        try {
            if (event.getApi().equals(Req.dishstyle)) {
                AJson<List<DishStyle>> data = App.gson.fromJson(
                        event.getData(), new TypeToken<AJson<List<DishStyle>>>() {
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

            } else if (event.getApi().equals(Req.dish + styleId)) {
                AJson<List<Dish>> data = App.gson.fromJson(
                        event.getData(), new TypeToken<AJson<List<Dish>>>() {
                        }.getType());
                grid = data.getData();
                if (!grid.isEmpty()) {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            resetUI();
                        }
                    });
                }

            } else if (event.getApi().equals(Req.notice + notice)) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        dialog_dish.dismiss();
                        Toas toas = new Toas();
                        toas.setMsg(activity.getString(R.string.order_success));
                        toas.show(activity);
                        toas = null;
                    }
                });

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String styleId = "";

    private void resetList() {
        adapter = new DishStyleAdapter(activity, list);
        left_list.setAdapter(adapter);
        styleId = "&styleId=" + list.get(0).getId();
        Req.get(Req.dish + styleId);
    }

    private DishAdapter adapter2;

    private void resetUI() {
        adapter2 = new DishAdapter(activity, grid);
        right_grid.setAdapter(adapter2);
    }

    Dish dish;

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        try {
            final int p = position;
            if (parent == left_list) {
                styleId = "&styleId=" + list.get(p).getId();
                Req.get(Req.dish + styleId);
            } else if (parent == right_grid) {
                dish = grid.get(p);
                showOrder();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private AlertDialog dialog_dish;
    private ImageView dish_icon;
    private TextView dish_name, dish_no, dish_price;
    private Button order, cancle;
    private ImageButton dish_jia, dish_jian;
    private TextView dish_num;
    private int count;
    private String notice;
    private EditText edit_edt;
//    private TextView dish_des;

    private void showOrder() {

        dialog_dish = new AlertDialog.Builder(activity).create();
        dialog_dish.setView(new EditText(activity));
        if (dialog_dish.isShowing()) {
            dialog_dish.dismiss();
        } else {
            dialog_dish.show();
        }

        dialog_dish.setContentView(R.layout.dialog_dish);
        dish_icon = dialog_dish.findViewById(R.id.dish_icon);
        Picasso.with(activity).load(dish.getIcon()).into(dish_icon);
        dish_name = dialog_dish.findViewById(R.id.dish_name);
        dish_name.setText(getString(R.string.dish_name) + dish.getName());
        dish_no = dialog_dish.findViewById(R.id.dish_no);
        dish_no.setText(getString(R.string.dish_time) + dish.getSupply_time());
        dish_price = dialog_dish.findViewById(R.id.dish_price);
        dish_price.setText(getString(R.string.dish_price) + dish.getPrice() + "");

        order = dialog_dish.findViewById(R.id.order);
        cancle = dialog_dish.findViewById(R.id.cancle);
        edit_edt = dialog_dish.findViewById(R.id.edit_edt);

//        dish_des = dialog_dish.findViewById(R.id.dish_des);
//        dish_des.setText(dish.getDiscription());

        dish_jia = dialog_dish.findViewById(R.id.dish_jia);
        dish_num = dialog_dish.findViewById(R.id.dish_num);
        dish_jian = dialog_dish.findViewById(R.id.dish_jian);
        dish_jia.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                count = Integer.parseInt(dish_num.getText().toString());
                count++;
                dish_num.setText(count + "");
            }
        });

        dish_jian.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                count = Integer.parseInt(dish_num.getText().toString());
                if (count > 1) {
                    count--;
                } else {
                    count = 99;
                }
                dish_num.setText(count + "");

            }
        });
        order.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                String yaoqiu = edit_edt.getText().toString();
                if (!yaoqiu.equals("")) {
                    yaoqiu = "-要求：" + edit_edt.getText().toString();
                }

                notic = dish.getName() + "-数量：" + dish_num.getText().toString() + "份"
                        + yaoqiu;
                notice = "&notifyNews=" + URLEncoder.encode(notic);
//                Req.get(Req.notice + notice);
                dialog_dish.dismiss();
                showDialogStyle2();
            }
        });
        cancle.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                dialog_dish.dismiss();
            }
        });
    }

    private String notic;

    private void showDialogStyle2() {
        View view = LayoutInflater.from(activity).inflate(
                R.layout.dialog_style2, null);
        TextView namr = view.findViewById(R.id.namr);
        namr.setText(dish.getName());
        TextView num = view.findViewById(R.id.num);
        num.setText(dish_num.getText().toString());
        TextView total = view.findViewById(R.id.total);
        double tmp = dish.getPrice() * Double.parseDouble(dish_num.getText().toString());
        total.setText(tmp + "元");
        final BtmDialog dialog = new BtmDialog(activity, R.layout.dialog_style2);
        dialog.show();
        dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialogInterface, int i, KeyEvent keyEvent) {
                System.out.println("---------1");
                return false;
            }
        });
        view.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                System.out.println("---------2");
                return false;
            }
        });
        cancle = dialog.findViewById(R.id.cancle);
        cancle.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                dialog.dismiss();
            }
        });
    }

    private void showDialogStyle3() {
        View view = LayoutInflater.from(activity).inflate(
                R.layout.dialog_style3, null);
        BtmDialog dialog = new BtmDialog(activity, R.layout.dialog_style3);
        dialog.show();
    }

    private void showDialogStyle4() {
        View view = LayoutInflater.from(activity).inflate(
                R.layout.dialog_style4, null);
        BtmDialog dialog = new BtmDialog(activity, R.layout.dialog_style4);
        dialog.show();
    }
}
