package com.spa.ui.dish;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;

import com.google.gson.reflect.TypeToken;
import com.spa.R;
import com.spa.app.App;
import com.spa.app.Req;
import com.spa.event.DataMessage;
import com.spa.ui.BaseActivity;
import com.spa.ui.adapter.DishAdapter;
import com.spa.ui.adapter.DishStyleAdapter;
import com.spa.bean.AJson;
import com.spa.bean.Dish;
import com.spa.bean.DishStyle;

import java.util.List;

public class DishStyleActivity extends BaseActivity implements AdapterView.OnItemClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dish);
        find();
        init();

    }

    private void init() {
        Req.get(Req.dishstyle);
    }

    private ListView left_list;
    private GridView right_grid;

    private void find() {
        left_list = findViewById(R.id.left_list);
        left_list.setOnItemClickListener(this);
        right_grid = findViewById(R.id.right_grid);
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

            } else if (event.getApi().equals(Req.dishstyle + styleId)) {
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

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String styleId = "";

    private void resetList() {
        adapter = new DishStyleAdapter(this, list);
        left_list.setAdapter(adapter);
        styleId = "&styleId=" + list.get(0).getId();
        Req.get(Req.dish + styleId);
    }

    private DishAdapter adapter2;

    private void resetUI() {
        adapter2 = new DishAdapter(this, grid);
        right_grid.setAdapter(adapter2);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        final int p = position;
        if (parent == left_list) {
            styleId = "&styleId=" + list.get(p).getId();
            Req.get(Req.dish + styleId);
        } else if (parent == right_grid) {
            Bundle bundle = new Bundle();
            bundle.putSerializable("key", grid.get(p));
            startActivity(new Intent(DishStyleActivity.this, DishActivity.class)
                    .putExtras(bundle));
        }


    }
}
