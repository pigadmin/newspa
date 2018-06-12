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
        EventBus.getDefault().register(this);
        app = (App) getApplication();
        System.out.println("BaseActivity");
        activities.add(this);
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

    public void onEvent(ErrorMessage event) {
        final ErrorMessage errorMessage = event;
        System.out.println(event.getApi());

        handler.post(new Runnable() {
            @Override
            public void run() {
                Toas toas = new Toas();
                toas.setMsg(errorMessage.getApi() + "：" + errorMessage.getCode());
                toas.show(BaseActivity.this);
                toas = null;
            }
        });

    }

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
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    public void onEvent(final NetChange event) {

        handler.post(new Runnable() {
            @Override
            public void run() {
                Toas toas = new Toas();
                switch (event.getType()) {
                    case -1:
                        toas.setMsg(getString(R.string.disnetwork));
                        break;
                    case 0:
                        toas.setMsg(getString(R.string.gps_network));
                        break;
                    case 1:
                        toas.setMsg(getString(R.string.wifi_network));
                        break;
                    case 9:
                        toas.setMsg(getString(R.string.eth_network));
                        break;
                }
                toas.show(BaseActivity.this);
                toas = null;
            }
        });


    }

    private List<Backs> backs;

    public void onEvent(DataMessage event) {
//        System.out.println("-----------------" + event.getApi());
        try {   System.out.println("BGBGBGBGBGBG");
            if (event.getApi().equals(Req.logo)) {
                final AJson<LogoBg> data = App.gson.fromJson(event.getData(),
                        new TypeToken<AJson<LogoBg>>() {
                        }.getType());
                backs = data.getData().getBacks();
                System.out.println(backs.size() + "@@@@@@@@@@@@@@");
                if (!backs.isEmpty()) {
                    handler.sendEmptyMessage(backsmsg);
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
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
