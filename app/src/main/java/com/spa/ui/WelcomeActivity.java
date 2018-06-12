package com.spa.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;

import com.google.gson.reflect.TypeToken;
import com.spa.R;
import com.spa.app.App;
import com.spa.app.Req;
import com.spa.event.DataMessage;
import com.spa.event.NetChange;
import com.spa.bean.AJson;
import com.spa.ui.diy.Toas;

public class WelcomeActivity extends BaseActivity {
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        if (App.network_type > 0) {
            check();
        }
    }

    public void onEvent(NetChange event) {
        check();
    }

    private void test() {
        startActivity(new Intent(this, MainActivity.class)
                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));

    }


    private void check() {
        Req.get(Req.check);
    }

    public void onEvent(DataMessage event) {
        if (event.getApi().equals(Req.check)) {
            AJson<Long> data = App.gson.fromJson(event.getData(),
                    new TypeToken<AJson<Long>>() {
                    }.getType());
            System.out.println(data.getData());
            if (data.getCode().equals("200")) {
//                        System.out.println(event.getData());
                SystemClock.setCurrentTimeMillis(data.getData());
                startActivity(new Intent(WelcomeActivity.this, MainActivity.class)
                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));

            } else {
                Toas toas = new Toas();
                toas.setMsg(data.getErrorInfo());
                toas.show(WelcomeActivity.this);
                toas = null;
            }
        }
    }

    @Override
    protected void onDestroy() {
//        System.out.println("onDestroy");
        super.onDestroy();
    }
}
