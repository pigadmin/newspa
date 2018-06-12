package com.spa.ui.jishi;

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
import com.spa.bean.AJson;
import com.spa.bean.TeachType;
import com.spa.bean.Tech;
import com.spa.event.DataMessage;
import com.spa.ui.BaseActivity;
import com.spa.ui.adapter.JishiAdapter;
import com.spa.ui.adapter.TeachTypeAdapter;

import java.util.List;

public class JishiTypeActivity extends BaseActivity implements AdapterView.OnItemClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);
        find();
        init();
    }

    private void init() {
        Req.get(Req.teachtype);
    }

    private ListView left_list;
    private GridView right_grid;

    private void find() {
        left_list = findViewById(R.id.left_list);
        left_list.setOnItemClickListener(this);
        right_grid = findViewById(R.id.right_grid);
        right_grid.setOnItemClickListener(this);
    }

    private List<TeachType> list;
    private TeachTypeAdapter adapter;
    private Handler handler = new Handler();
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

            } else if (event.getApi().equals(Req.teach)) {
                AJson<Tech> data = App.gson.fromJson(
                        event.getData(), new TypeToken<AJson<Tech>>() {
                        }.getType());
                grid = data.getData();
                if (!grid.getData().isEmpty()) {
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

    private String services;

    private void resetList() {
        adapter = new TeachTypeAdapter(this, list);
        left_list.setAdapter(adapter);

        services = "&services=" + list.get(0).getId();
        Req.get(Req.teach + services);
    }


    private JishiAdapter adapter2;

    private void resetUI() {
        System.out.println(grid.getData().size());
        adapter2 = new JishiAdapter(this, grid.getData());
        right_grid.setAdapter(adapter2);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        final int p = position;
        if (parent == left_list) {
            services = "&services=" + list.get(p).getId();
            Req.get(Req.teach + services);
        } else if (parent == right_grid) {
            Bundle bundle = new Bundle();
            bundle.putSerializable("key", grid.getData().get(p));
//            startActivity(new Intent(DishStyleActivity.this, DishActivity.class)
//                    .putExtras(bundle));
        }


    }

}
