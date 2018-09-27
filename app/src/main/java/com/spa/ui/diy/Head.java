package com.spa.ui.diy;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.media.AudioManager;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.reflect.TypeToken;
import com.spa.R;
import com.spa.app.App;
import com.spa.app.Req;
import com.spa.bean.AJson;
import com.spa.bean.LogoBg;
import com.spa.bean.User;
import com.spa.event.BitmapMessage;
import com.spa.event.DataMessage;
import com.spa.event.UpdateTime;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;

public class Head extends LinearLayout {
    private final int bg = 0;
    private int currentbg = 0;
    private List<Bitmap> bgbitmap = new ArrayList<>();
    private String bgpath;


    private View view;
    private Context context;
    private App app;


    public Head(Context context, AttributeSet attrs) {
        super(context, attrs);
        try {
            this.context = context;
            view = LayoutInflater.from(context).inflate(R.layout.head, this);
            app = (App) context.getApplicationContext();
            find();
            init();
            audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
            maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
            currentVolume = audioManager
                    .getStreamVolume(AudioManager.STREAM_MUSIC);

            IntentFilter filter = new IntentFilter();
            filter.addAction(App.SHOWNAME);
            filter.addAction(App.HIDENAME);
            context.registerReceiver(receiver, filter);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(App.HIDENAME)) {
                name.setVisibility(View.GONE);
            } else if (intent.getAction().equals(App.SHOWNAME)) {
                name.setVisibility(View.VISIBLE);
            }

        }
    };


    private AudioManager audioManager;
    private int maxVolume;
    private int currentVolume;

    private void init() {
        try {

            if (app.getUser() == null) {
                getuser();
            }

            if (app.getLogoBg() == null) {
                getlogo();
            } else {
                setlogo();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private ImageView logo;
    private TextView name;

    private void find() {
        logo = findViewById(R.id.logo);
        name = findViewById(R.id.name);
    }

    private void setlogo() {
//        System.out.println("##########2" + app.getLogoBg().getLogo().getLogoPath());
        Picasso.with(context).load(app.getLogoBg().getLogo().getLogoPath()).error(R.mipmap.top_logo).into(logo);
    }


    private void getlogo() {
        String url = App.requrl("getLogo", "&type=3");
//        Log.e("@@@", url);
        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String json) {
                try {

                    AJson<LogoBg> logobg = App.gson.fromJson(json,
                            new TypeToken<AJson<LogoBg>>() {
                            }.getType());
                    LogoBg lg = logobg.getData();

                    if (lg != null) {
                        app.setLogoBg(lg);
//                        Log.e("@@@", lg.getLogo().getLogoPath());
                        setlogo();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
            }
        });
        request.setRetryPolicy(new DefaultRetryPolicy(
                5 * 1000,//链接超时时间
                0,//重新尝试连接次数
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        ));
        App.queue.add(request);
    }

    private void getuser() {
        String url = App.requrl("getUser", "");
//        Log.e(tag, url);
        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String json) {
                try {
                    System.out.println("-------" + json);
                    final AJson<User> data = App.gson.fromJson(json,
                            new TypeToken<AJson<User>>() {
                            }.getType());
                    if (data.getCode().equals("200")) {
                        if (data.getData() != null) {
                            app.setUser(data.getData());
                            name.setText(data.getData().getName());
                        } else {
                            name.setText(data.getMsg() + App.mac);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
            }
        });
        request.setRetryPolicy(new DefaultRetryPolicy(
                5 * 1000,//链接超时时间
                0,//重新尝试连接次数
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        ));
        App.queue.add(request);
    }


}
