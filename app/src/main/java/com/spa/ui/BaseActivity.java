package com.spa.ui;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;

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
import com.spa.bean.Backs;
import com.spa.bean.LogoBg;
import com.spa.event.DataMessage;
import com.spa.event.ErrorMessage;
import com.spa.event.NetChange;
import com.spa.ui.diy.Toas;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;

public class BaseActivity extends Activity implements MediaPlayer.OnPreparedListener, MediaPlayer.OnErrorListener, MediaPlayer.OnCompletionListener {
    private App app;
    private static List<Activity> activities = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        EventBus.getDefault().register(this);
        app = (App) getApplication();
        activities.add(this);

        init();
    }

    private void init() {
        try {
            if (app.getLogoBg() == null) {
                getlogo();
            } else {
                setbg();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void close() {

        for (Activity activity : activities) {
            activity.finish();
        }
        activities.clear();
        // System.exit(0);
        // android.os.Process.killProcess(android.os.Process.myPid());
    }

    public static void back() {
        try {
            activities.get(activities.size() - 1).finish();
            activities.remove(activities.size() - 1);
        } catch (Exception e) {
            // TODO: handle exception
        }

    }

    MediaPlayer mediaPlayer;

    private void setMediaListene() {
        mediaPlayer = new MediaPlayer();

        mediaPlayer.setOnPreparedListener(this);
        mediaPlayer.setOnErrorListener(this);
        mediaPlayer.setOnCompletionListener(this);

    }

    private void playmusic() {
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_TV:
//                startActivity(new Intent(BaseActivity.this, MainActivity.class)
//                        .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                break;
        }
        return super.onKeyDown(keyCode, event);
    }

//    public void onEvent(ErrorMessage event) {
//        final ErrorMessage errorMessage = event;
//        System.out.println(event.getApi());
//
//        handler.post(new Runnable() {
//            @Override
//            public void run() {
//                Toas toas = new Toas();
//                toas.setMsg(errorMessage.getApi() + "：" + errorMessage.getCode());
//                toas.show(BaseActivity.this);
//                toas = null;
//            }
//        });
//
//    }

    private final int backsmsg = 0;
    int cutbg = 0;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case backsmsg:
                    try {
//                        if (!app.getBack().isEmpty()) {
//                            getWindow().getDecorView().setBackground(
//                                    new BitmapDrawable(app.getBack().get(cutbg)));
//                        } else {
//                            System.out.println(backs.get(cutbg).getPath());
                        Picasso.with(BaseActivity.this).load(backs.get(cutbg).getPath()).into(new Target() {
                            @Override
                            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom loadedFrom) {
                                getWindow().getDecorView().setBackground(
                                        new BitmapDrawable(bitmap));
                                app.getBack().add(bitmap);
                            }

                            @Override
                            public void onBitmapFailed(Drawable drawable) {

                            }

                            @Override
                            public void onPrepareLoad(Drawable drawable) {

                            }
                        });
//                        }
                        if (backs.size() > 1) {
                            System.out.println("cutbg：" + cutbg + "----s：" + backs.get(cutbg).getInter());
                            handler.sendEmptyMessageDelayed(backsmsg, backs.get(cutbg).getInter() * 1000);
                            if (cutbg < backs.size() - 1) {
                                cutbg++;
                            } else {
                                cutbg = 0;
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    break;
            }
        }
    };

    @Override
    protected void onDestroy() {
        System.out.println("Base onDestroy");
//        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    private void getlogo() {
        String url = App.requrl("getLogo", "&type=3");
//        Log.e("@@@", url);
        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String json) {
                try {
//                    System.out.println("##########1" + json);
                    AJson<LogoBg> logobg = App.gson.fromJson(json,
                            new TypeToken<AJson<LogoBg>>() {
                            }.getType());
                    LogoBg lg = logobg.getData();

                    if (lg != null) {
                        app.setLogoBg(lg);
                        setbg();
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

    private void setbg() {
        try {
            backs = app.getLogoBg().getBacks();
            if (backs != null && !backs.isEmpty()) {
                System.out.println(backs.size() + "@@@@@@@@@@@@@@");
                handler.sendEmptyMessage(backsmsg);
            } else {
//                System.out.println("没有背景@@@@@@@@@@@@@@");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private List<Backs> backs;

    public void onEvent(DataMessage event) {

    }


    @Override
    public void onCompletion(MediaPlayer mp) {

    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        return true;
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        mp.start();
    }
}
