package com.spa.ui.diy;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.media.AudioManager;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

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

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;

public class Head extends LinearLayout {
    private final int bg = 0;
    private int currentbg = 0;
    private List<Bitmap> bgbitmap = new ArrayList<>();
    private String bgpath;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case bg:
                    try {
                        if (!logobg.getData().getBacks().isEmpty()) {
                            if (bgbitmap.size() < logobg.getData().getBacks().size()) {
                                bgpath = logobg.getData().getBacks().get(currentbg).getPath();
                                Req.img(bgpath, bgpath);
                            } else {
                                ((Activity) context).getWindow().getDecorView().setBackground(new BitmapDrawable(bgbitmap.get(currentbg)));
                            }
                            handler.sendEmptyMessageDelayed(bg, logobg.getData().getBacks().get(currentbg).getInter() * 1000);
                            if (currentbg < logobg.getData().getBacks().size() - 1) {
                                currentbg++;
                            } else {
                                currentbg = 0;
                            }
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    break;
            }
        }
    };
    private View view;
    private Context context;
    private App app;


    public Head(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        view = LayoutInflater.from(context).inflate(R.layout.head, this);
        app = (App) context.getApplicationContext();
        EventBus.getDefault().register(this);
        find();
        init();
        audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        currentVolume = audioManager
                .getStreamVolume(AudioManager.STREAM_MUSIC);
    }


    private AudioManager audioManager;
    private int maxVolume;
    private int currentVolume;

    private void init() {
        EventBus.getDefault().post(new UpdateTime(System.currentTimeMillis()));
    }

    private ImageView logo;
    private TextView name;

    private void find() {
        logo = findViewById(R.id.logo);
        name = findViewById(R.id.name);

    }

    public void onEvent(final UpdateTime event) {
        System.out.println("更新时间：" + event.getYmdhmse());
        handler.post(new Runnable() {
            @Override
            public void run() {
            }
        });
    }

    private AJson<LogoBg> logobg;

    public void onEvent(final DataMessage event) {
        try {
//            if (event.getApi().equals(Req.wea)) {
//                AJson<NewWea> data = App.gson.fromJson(event.getData(),
//                        new TypeToken<AJson<NewWea>>() {
//                        }.getType());
//                final Wea wea = App.gson.fromJson(data.getData().getInfo().toString(), Wea.class);
//                if (wea != null) {
//                    handler.post(new Runnable() {
//                        @Override
//                        public void run() {
//
//                        }
//                    });
//                }
//            } else
//
            if (event.getApi().equals(Req.logo)) {
                try {
                    logobg = App.gson.fromJson(event.getData(),
                            new TypeToken<AJson<LogoBg>>() {
                            }.getType());
                    if (logobg.getData() != null) {
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
//                                Picasso.with(context).load(logobg.getData().getLogo().getLogoPath()).into(logo);
                                handler.sendEmptyMessageDelayed(bg, 1000);
                            }
                        });
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            } else if (event.getApi().equals(Req.user)) {
                System.out.println("-------" + event.getData());
                final AJson<User> data = App.gson.fromJson(event.getData(),
                        new TypeToken<AJson<User>>() {
                        }.getType());
                if (data.getCode().equals("200")) {
                    if (data.getData() != null) {
                        app.setUser(data.getData());
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                name.setText(data.getData().getName());
                            }
                        });
                    }
                } else {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            name.setText(data.getMsg() + App.mac);
                        }
                    });
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void onEvent(final BitmapMessage event) {
        if (event.getApi().equals(bgpath)) {
            bgbitmap.add(event.getBitmap());
            handler.post(new Runnable() {
                @Override
                public void run() {
                    ((Activity) context).getWindow().getDecorView().setBackground(new BitmapDrawable(event.getBitmap()));
                }
            });
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        System.out.println("Head onDetachedFromWindow");
        EventBus.getDefault().unregister(this);
    }
}
