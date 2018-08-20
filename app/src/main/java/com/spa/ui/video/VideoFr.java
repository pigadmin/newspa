package com.spa.ui.video;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ListView;

import com.google.gson.reflect.TypeToken;
import com.spa.R;
import com.spa.app.App;
import com.spa.app.Req;
import com.spa.bean.AJson;
import com.spa.bean.Video;
import com.spa.bean.VideoData;
import com.spa.bean.VideoType;
import com.spa.event.DataMessage;
import com.spa.ui.BaseFr;
import com.spa.ui.adapter.TeleplayGridAdapter;
import com.spa.ui.adapter.VideoAdapter;
import com.spa.ui.adapter.VideoTypeAdapter;

import java.util.List;

import de.greenrobot.event.EventBus;

public class VideoFr extends BaseFr implements AdapterView.OnItemClickListener {

    private View view;
    private Activity activity;
    private App app;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_video, container, false);
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
        Req.get(Req.videotype);
    }

    private ListView left_list;
    private GridView right_grid;
    private EditText video_keyword;

    private void find() {
        left_list = view.findViewById(R.id.left_list);
        left_list.setOnItemClickListener(this);
        right_grid = view.findViewById(R.id.right_grid);
        right_grid.setOnItemClickListener(this);
        video_keyword = view.findViewById(R.id.video_keyword);
        video_keyword.addTextChangedListener(new TextWatcher() {

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
    String keywd;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    keywd = "&keywd=" + video_keyword.getText().toString();
                    Req.get(Req.video + keywd);

                    break;
            }
        }
    };


    private List<VideoType> list;
    private VideoTypeAdapter adapter;

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

            } else if (event.getApi().equals(Req.video + type)) {
                AJson<Video> data = App.gson.fromJson(
                        event.getData(), new TypeToken<AJson<Video>>() {
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

            } else if (event.getApi().equals(Req.video + keywd)) {
                AJson<Video> data = App.gson.fromJson(
                        event.getData(), new TypeToken<AJson<Video>>() {
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

    private String type;

    private void resetList() {
        adapter = new VideoTypeAdapter(activity, list);
        left_list.setAdapter(adapter);

        type = "&type=" + list.get(0).getId();
        Req.get(Req.video + type);
    }

    private VideoAdapter adapter2;

    private void resetUI() {
        adapter2 = new VideoAdapter(activity, grid.getData());
        right_grid.setAdapter(adapter2);
    }

    VideoData videoData;

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        final int p = position;
        if (parent == left_list) {
            type = "&type=" + list.get(p).getId();
            Req.get(Req.video + type);

        } else if (parent == right_grid) {
            videoData = grid.getData().get(p);
            if (videoData.getDetails().size() > 1) {
                teledialog();
            } else {
                play(0);
            }
        }
    }

    static AlertDialog teleplay_dialog;
    GridView teleplay_grid;
    TeleplayGridAdapter teleplayadapter;;

    public void teledialog() {
        // TODO Auto-generated method stub
        teleplay_dialog = new AlertDialog.Builder(activity).create();
        if (teleplay_dialog.isShowing()) {
            teleplay_dialog.dismiss();
        } else {
            teleplay_dialog.show();
        }

        teleplay_dialog.setContentView(R.layout.dialog_teleplay);

        teleplay_grid = teleplay_dialog.findViewById(R.id.teleplay_grid);
        teleplayadapter = new TeleplayGridAdapter(getActivity(), videoData.getDetails());
        teleplay_grid.setAdapter(teleplayadapter);
        teleplay_grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                play(i);
            }
        });
    }

    private void play(int position) {
        try {
            String path = videoData.getDetails().get(position).getFilePath();

            Intent intent = new Intent();
            Bundle bundle = new Bundle();
            bundle.putSerializable("key", videoData.getDetails().get(position));
            intent.setClass(activity, PlayerActivity.class);
            startActivity(intent.putExtras(bundle));
        } catch (Exception e) {
//            AppTool.toast(context, getString(R.string.res_error), 0, Gravity.CENTER, 0, 0);
            e.printStackTrace();
        }
    }
}
