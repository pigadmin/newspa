package com.spa.service;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.view.Gravity;

import com.google.gson.reflect.TypeToken;
import com.spa.app.App;
import com.spa.app.Req;
import com.spa.bean.AJson;
import com.spa.event.DataMessage;
import com.spa.service.msg.IScrollState;
import com.spa.service.msg.Marquee;
import com.spa.service.msg.MarqueeToast;
import com.spa.service.msg.TextSurfaceView;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import de.greenrobot.event.EventBus;

public class MyService extends Service implements IScrollState, Runnable {

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
        }
    };

    public MyService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        System.out.println("---MyService_onCreate()---");
        super.onCreate();
        EventBus.getDefault().register(this);

        getmarquee();
    }

    Timer marquee = new Timer();

    private void getmarquee() {
        marquee.schedule(new TimerTask() {
            @Override
            public void run() {
                Req.get(Req.marquee);
            }
        }, 0, 60 * 1000);
    }

    boolean runmarquee = false;

    public void onEvent(DataMessage event) {
        try {
            if (event.getApi().equals(Req.marquee)) {
//                System.out.println(". 2..");
                AJson<List<Marquee>> data = App.gson.fromJson(
                        event.getData(), new TypeToken<AJson<List<Marquee>>>() {
                        }.getType());
                if (data.getData() != null) {
                    marquees = data.getData();
                    if (!runmarquee) {
                        runmarquee = true;
                        handler.post(this);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private List<Marquee> marquees;
    public int currentmsg;
    private MarqueeToast toast;
    private TextSurfaceView Text;

    public void showMessage() {
        try {
            if (marquees != null && !marquees.isEmpty()) {
                if (marquees.size() <= currentmsg)
                    currentmsg = 0;
                if (toast != null)
                    toast.hid();
                toast = new MarqueeToast(getApplicationContext());
                Text = new TextSurfaceView(getApplicationContext(), this);
                Text.setOrientation(1);
                toast.setHeight(40);
                if (marquees.get(currentmsg).getContent().equals("")
                        && marquees.get(currentmsg).getContent() == null) {
                    Text.setContent("");
                } else {
                    Text.setContent(marquees.get(currentmsg).getContent());
                }
                toast.setView(Text);
                toast.setGravity(Gravity.TOP | Gravity.LEFT, 1280, 0, 0);
                toast.show();
                currentmsg++;
            }
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }

    }

    @Override
    public void onDestroy() {
        System.out.println("---MyService_onDestroy()---");
        super.onDestroy();
    }

    @Override
    public void start() {

//        System.out.println("---MyService_start()---");
    }

    @Override
    public void stop() {
        // TODO Auto-generated method stub

        try {
//            System.out.println("---MyService_stop()---");
            Text.setLoop(false);
            Looper.prepare();
            handler.post(this);
            Looper.loop();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        // TODO Auto-generated method stub
//        System.out.println("---MyService_run()---");
        showMessage();
    }

}
