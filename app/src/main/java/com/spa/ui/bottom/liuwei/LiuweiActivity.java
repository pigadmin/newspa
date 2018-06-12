package com.spa.ui.bottom.liuwei;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.widget.TextView;

import com.spa.R;
import com.spa.app.App;
import com.spa.ui.BaseActivity;

import java.util.Timer;

public class LiuweiActivity extends BaseActivity {
    private App app;

    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    if (key_temp.length() > 8) {
                        System.out.println(key_temp);
                    }
                    handler.sendEmptyMessage(2);

                    key_temp = "";
                    break;

                case 1:
                    seconds--;

                    if (seconds / 60 < 1) {
                        // txtTip.setText(seconds2Str(seconds));
                        liuwei_time.setText(seconds2Str(seconds));
                        key.setText("秒后自动进入待机状态");
                        txtTip.setText("解锁请刷手牌");
                    } else {
                        // txtTip.setText(seconds2Str(seconds));
                        liuwei_time.setText(seconds2Str(seconds));
                        key.setText("分钟后自动进入待机状态");
                        txtTip.setText("解锁请刷手牌");
                    }
                    if (seconds > 0) {
                        handler.sendEmptyMessageDelayed(1, 1 * 1000);
                    }

                    break;
                case 2:
                    finish();
                    break;

                default:
                    break;
            }
        }
    };

    @Override
    protected void onStop() {
        // TODO Auto-generated method stub
        updateTime.cancel();
        super.onStop();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        app = (App) getApplication();

        setContentView(R.layout.activity_liuwei);
        try {
//            seconds = Integer.parseInt(getIntent().getStringExtra("time")) * 60;

            seconds = 15 * 60;
        } catch (Exception e) {
            // TODO: handle exception
        }
        initview();
        setvalue();
    }

    private int seconds = 15 * 60;
    private Timer updateTime = new Timer();

    private void setvalue() {
        // TODO Auto-generated method stub
        // key.setText("使用手牌"
        // + apc.getKey().substring(0, 3)
        // + "****"
        // + apc.getKey().substring(apc.getKey().length() - 3,
        // apc.getKey().length()) + "立即解锁");

        key.setText("15分钟后自动进入待机状态");
        handler.sendEmptyMessage(1);
        handler.sendEmptyMessageDelayed(2, seconds * 1000);

    }

    private String key_temp = "";

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        if (keyCode >= KeyEvent.KEYCODE_0 && keyCode <= KeyEvent.KEYCODE_9) {

            key_temp += keyCode - 7;
            // System.out.println(key_temp);

            handler.removeMessages(0);
            handler.sendEmptyMessageDelayed(0, 100);
        }
        return true;
    }

    private String seconds2Str(int seconds) {
        int minute = seconds / 60;
        int second = seconds % 60;

        if (minute < 10 && second < 10) {
            return "0" + minute + ":" + "0" + second;
        }
        if (minute < 10) {
            return "0" + minute + ":" + second;
        }
        if (second < 10) {
            return minute + ":" + "0" + second;
        }
        return minute + ":" + second;

    }

    private TextView liuwei_time, key, txtTip;

    private void initview() {
        // TODO Auto-generated method stub
        liuwei_time = (TextView) findViewById(R.id.liuwei_time);
        key = (TextView) findViewById(R.id.key);
        txtTip = (TextView) findViewById(R.id.tip);
    }

}
