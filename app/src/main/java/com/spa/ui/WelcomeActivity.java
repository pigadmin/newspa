package com.spa.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.VideoView;

import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.spa.R;
import com.spa.app.App;
import com.spa.app.Req;
import com.spa.bean.AJson;
import com.spa.bean.WelcomeAd;
import com.spa.event.DataMessage;
import com.spa.event.ErrorMessage;
import com.spa.tools.FULL;
import com.spa.tools.LtoDate;
import com.spa.ui.diy.Toas;

import java.util.List;

import static com.squareup.picasso.Picasso.with;

public class WelcomeActivity extends BaseActivity {

    private App app;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            setContentView(R.layout.activity_welcome);
            setMediaListene();
            app = (App) getApplication();
            find();
//        System.out.println(Req.check);
//        System.out.println(App.mac);
            reg();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private MediaPlayer mediaPlayer;

    private void setMediaListene() {
        mediaPlayer = new MediaPlayer();

        mediaPlayer.setOnPreparedListener(this);
        mediaPlayer.setOnErrorListener(this);
        mediaPlayer.setOnCompletionListener(this);

    }

    private void reg() {
        // TODO Auto-generated method stub
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(netreceiver, intentFilter);
    }

    private BroadcastReceiver netreceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            try {
                ConnectivityManager manager = (ConnectivityManager) context
                        .getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo ETHERNET = manager
                        .getNetworkInfo(ConnectivityManager.TYPE_ETHERNET);
                NetworkInfo WIFI = manager
                        .getNetworkInfo(ConnectivityManager.TYPE_WIFI);
                if (WIFI.isConnected() || ETHERNET.isConnected()) {
                    welcome_tips.setText("");
                    check();
                } else {
//                    welcome_tips.setText(getString(R.string.disnetwork));
                }
            } catch (Exception e) {
//                e.printStackTrace();
            }
        }
    };
    private TextView welcome_tips;
    private ImageView welcome_image;
    private VideoView welcome_video;
    private TextView welcome_time_tips;

    private void find() {
        welcome_tips = findViewById(R.id.welcome_tips);
        welcome_image = findViewById(R.id.welcome_image);
        welcome_video = findViewById(R.id.welcome_video);
        FULL.star(welcome_video);
        welcome_time_tips = findViewById(R.id.welcome_time_tips);
        welcome_video.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {

            }
        });
        welcome_video.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {

            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                mediaPlayer.start();
            }
        });
        welcome_video.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mediaPlayer, int i, int i1) {
                return true;
            }
        });
    }


    private void check() {
        Req.get(Req.check);
    }

    private void welcomead() {
        Req.get(Req.welcomead);
    }

    public void onEvent(DataMessage event) {
        try {
            if (event.getApi().equals(Req.check)) {
                final AJson<Long> data = App.gson.fromJson(event.getData(),
                        new TypeToken<AJson<Long>>() {
                        }.getType());
                //            System.out.println("@@@@@" + data.getData());
                if (data.getCode().equals("200")) {
                    //                        System.out.println(event.getData());
                    app.setSystiem(data.getData());
                    welcomead();
                    SystemClock.setCurrentTimeMillis(data.getData());


                } else {
                    //                System.out.println("===================================");
                    Toas toas = new Toas();
                    toas.setMsg(data.getErrorInfo());
                    toas.show(WelcomeActivity.this);
                    toas = null;
                    welcome_tips.setText(data.getErrorInfo());
                }
            } else if (event.getApi().equals(Req.welcomead)) {
                final AJson<List<WelcomeAd>> data = App.gson.fromJson(event.getData(),
                        new TypeToken<AJson<List<WelcomeAd>>>() {
                        }.getType());


                welcomeAds = data.getData();
                if (!welcomeAds.isEmpty()) {
                    for (WelcomeAd ad : welcomeAds) {
                        WELCOME_FINISH += ad.getInter();
                    }
                    handler.sendEmptyMessage(PlayWelcomead);
                }
                handler.sendEmptyMessage(WELCOME_TIMEOUT);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int WELCOME_FINISH;
    List<WelcomeAd> welcomeAds;
    final int WELCOME_TIMEOUT = 0;
    final int PlayWelcomead = 1;
    int cutad;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {


                case WELCOME_TIMEOUT:

                    if (WELCOME_FINISH == 0) {
                        tomain();
                    } else if (WELCOME_FINISH > 0) {
                        welcome_time_tips.setVisibility(View.VISIBLE);
                        welcome_time_tips.setText(WELCOME_FINISH + "");
                        WELCOME_FINISH--;
                        handler.sendEmptyMessageDelayed(WELCOME_TIMEOUT, 1 * 1000);
                    }
                    break;
                case PlayWelcomead:
                    try {
                        switch (welcomeAds.get(cutad).getType()) {
                            case 1:
                                playimg();
                                playmusic();
                                break;
                            case 2:
                                playvideo();
                                break;
                        }

                        if (cutad < welcomeAds.size() - 1) {
                            handler.sendEmptyMessageDelayed(PlayWelcomead, welcomeAds.get(cutad).getInter() * 1000);
                            cutad++;
                        }
                    } catch (Exception e) {
                    }

                    break;
            }
        }
    };

    private void playimg() {
        if (welcome_video.getVisibility() == View.VISIBLE) {
            welcome_video.setVisibility(View.GONE);
            if (welcome_video.isPlaying()) {
                welcome_video.stopPlayback();
            }
        }
        if (welcome_image.getVisibility() == View.GONE) {
            welcome_image.setVisibility(View.VISIBLE);
        }
        with(this).load(welcomeAds.get(cutad).getFilePath()).into(welcome_image);
    }


    private void playmusic() {
        // TODO Auto-generated method stub

        try {
            mediaPlayer.stop();
            mediaPlayer.reset();
            System.out.println(welcomeAds.get(cutad).getBgFile());
//            mediaPlayer.setDataSource(WelcomeActivity.this,
//                    Uri.parse(music.get(cutmusic).getBgFile()));
            mediaPlayer.setDataSource(WelcomeActivity.this,
                    Uri.parse(welcomeAds.get(cutad).getBgFile()));
            mediaPlayer.prepareAsync();
        } catch (Exception e) {
            // TODO: handle exception
        }

    }

    private void playvideo() {
        if (welcome_image.getVisibility() == View.VISIBLE) {
            welcome_image.setVisibility(View.GONE);
        }
        if (welcome_video.getVisibility() == View.GONE) {
            welcome_video.setVisibility(View.VISIBLE);
        }
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
            mediaPlayer.release();
        }

        welcome_video.setVideoURI(Uri.parse(welcomeAds.get(cutad).getFilePath()));

    }

    void tomain() {
        finish();
        startActivity(new Intent(WelcomeActivity.this, MainActivity.class)
                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));

    }

    public void onEvent(ErrorMessage event) {
        if (event.getApi().equals(Req.check)) {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    timer.start();
                }
            });
        }
    }

    private CountDownTimer timer = new CountDownTimer(60 * 1000, 1000) {
        @Override
        public void onTick(long l) {
//            welcome_tips.setText(getString(R.string.connect_error) + "\n" + (l / 1000) + "s后重新连接");
            welcome_tips.setText(getString(R.string.connect_error) + "（" + (l / 1000) + "）");
        }

        @Override
        public void onFinish() {
            check();
        }
    };

    @Override
    protected void onDestroy() {
        try {
            unregisterReceiver(netreceiver);
            timer.cancel();

            if (mediaPlayer != null) {
                mediaPlayer.stop();
                mediaPlayer.release();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.onDestroy();
    }
}
