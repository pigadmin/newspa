package com.spa.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.spa.R;
import com.spa.app.App;
import com.spa.app.Req;
import com.spa.bean.AJson;
import com.spa.event.DataMessage;
import com.spa.event.ErrorMessage;
import com.spa.ui.diy.Toas;

public class WelcomeActivity extends BaseActivity {
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
        }
    };
    private App app;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        app = (App) getApplication();
        find();
//        System.out.println(Req.check);
//        System.out.println(App.mac);
        reg();
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
                    welcome_tips.setText(getString(R.string.disnetwork));
                }
            } catch (Exception e) {
//                e.printStackTrace();
            }
        }
    };
    private TextView welcome_tips;

    private void find() {
        welcome_tips = findViewById(R.id.welcome_tips);
    }


    private void check() {
        Req.get(Req.check);
    }

    public void onEvent(DataMessage event) {
        if (event.getApi().equals(Req.check)) {
            final AJson<Long> data = App.gson.fromJson(event.getData(),
                    new TypeToken<AJson<Long>>() {
                    }.getType());
            System.out.println(data.getData());
            if (data.getCode().equals("200")) {
//                        System.out.println(event.getData());
                app.setSystiem(data.getData());
                SystemClock.setCurrentTimeMillis(data.getData());
                startActivity(new Intent(WelcomeActivity.this, MainActivity.class)
                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));

            } else {
                System.out.println("===================================");
                Toas toas = new Toas();
                toas.setMsg(data.getErrorInfo());
                toas.show(WelcomeActivity.this);
                toas = null;
                welcome_tips.setText(data.getErrorInfo());
            }
        }
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
            welcome_tips.setText(getString(R.string.connect_error) + "\n" + (l / 1000) + "s后重新连接");
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
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.onDestroy();
    }
}
