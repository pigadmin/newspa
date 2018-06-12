package com.spa.ui.video;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import com.spa.ui.adapter.IntroAdapter;
import com.spa.ui.adapter.JishiAdapter;
import com.spa.ui.adapter.VideoAdapter;
import com.spa.ui.adapter.VideoTypeAdapter;
import com.spa.bean.AJson;
import com.spa.bean.Tech;
import com.spa.bean.Video;
import com.spa.bean.VideoType;

import java.util.List;

public class VideoActivity extends BaseActivity implements AdapterView.OnItemClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);
        find();
        init();

    }

    private void init() {
        Req.get(Req.videotype);
    }

    private ListView left_list;
    private GridView right_grid;

    private void find() {
        left_list = findViewById(R.id.left_list);
        left_list.setOnItemClickListener(this);
        right_grid = findViewById(R.id.right_grid);
        right_grid.setOnItemClickListener(this);
    }

    private List<VideoType> list;
    private VideoTypeAdapter adapter;
    private Handler handler = new Handler();
    private Video grid;

    public void onEvent(DataMessage event) {
        try {
            if (event.getApi().equals(Req.videotype)) {
                AJson<List<VideoType>> data = App.gson.fromJson(
                        event.getData(), new TypeToken<AJson<List<VideoType>>>() {
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

            } else if (event.getApi().equals(Req.video)) {
                AJson<Video> data = App.gson.fromJson(
                        event.getData(), new TypeToken<AJson<List<Video>>>() {
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

    private String type;

    private void resetList() {
        adapter = new VideoTypeAdapter(this, list);
        left_list.setAdapter(adapter);

        type = "&type=" + list.get(0).getId();
        Req.get(Req.video + type);
    }

    private VideoAdapter adapter2;

    private void resetUI() {
        adapter2 = new VideoAdapter(this, grid.getData());
        right_grid.setAdapter(adapter2);
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        final int p = position;
        if (parent == left_list) {
            type = "&type=" + list.get(0).getId();
            Req.get(Req.video + type);

        } else if (parent == right_grid) {
        }


    }
}
