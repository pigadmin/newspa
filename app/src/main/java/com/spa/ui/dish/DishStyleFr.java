package com.spa.ui.dish;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.os.Handler;
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
import com.spa.ui.BaseFr;
import com.spa.ui.adapter.DishAdapter;
import com.spa.ui.adapter.DishStyleAdapter;
import com.spa.ui.diy.Toas;
import com.squareup.picasso.Picasso;

import java.net.URLEncoder;
import java.util.List;

import de.greenrobot.event.EventBus;

public class DishStyleFr extends BaseFr implements AdapterView.OnItemClickListener {

    private View view;
    private Activity activity;
    private App app;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        view = inflater.inflate(R.layout.activity_dish, container, false);
        activity = getActivity();
        app = (App) activity.getApplication();
        EventBus.getDefault().register(this);
        find();
        init();
        return view;
    }


    private void init() {
        Req.get(Req.dishstyle);
    }

    private ListView left_list;
    private GridView right_grid;

    private void find() {
        left_list = view.findViewById(R.id.left_list);
        left_list.setOnItemClickListener(this);
        right_grid = view.findViewById(R.id.right_grid);
        right_grid.setOnItemClickListener(this);
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
        left_list.requestFocus();
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
    private EditText dish_req;
    private TextView dish_des;

    private void showOrder() {
        dialog_dish = new AlertDialog.Builder(activity).create();
        if (dialog_dish.isShowing()) {
            dialog_dish.dismiss();
        } else {
//            dialog_dish.setView(new EditText(activity));
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
        dish_req = dialog_dish.findViewById(R.id.dish_req);
        dish_des = dialog_dish.findViewById(R.id.dish_des);
        dish_des.setText(dish.getDiscription());

        dish_jia = dialog_dish.findViewById(R.id.dish_jia);
        dish_num = dialog_dish.findViewById(R.id.dish_num);
        dish_jian = dialog_dish.findViewById(R.id.dish_jian);
        dish_jia.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                count = Integer.parseInt(dish_num.getText().toString());
                count++;
                dish_num.setText(count + "");
            }
        });

        dish_jian.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
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
//                String notic = dish.getName() + "-数量：" + dish_num.getText().toString() + "份" + "-备注：" + dish_req.getText().toString();
                String notic = dish.getName() + "-数量：" + dish_num.getText().toString() + "份";
                notice = "&notifyNews=" + URLEncoder.encode(notic);
                Req.get(Req.notice + notice);
            }
        });
        cancle.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                dialog_dish.dismiss();
            }
        });
    }


}
