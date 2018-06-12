package com.spa.ui.diy;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
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
import com.spa.event.DataMessage;
import com.spa.event.UpdateTime;
import com.spa.bean.AJson;
import com.spa.bean.LogoBg;
import com.spa.bean.Menu;
import com.spa.ui.diy.wea.NewWea;
import com.spa.ui.diy.wea.Wea;
import com.spa.ui.diy.wea.WeaIcon;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.List;

import de.greenrobot.event.EventBus;

public class Head extends LinearLayout {
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
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
    }

    ImageView logo;
    TextView no;
    ImageView wea_icon;
    TextView time;
    TextView city;
    TextView date;

    private void init() {
        EventBus.getDefault().post(new UpdateTime(System.currentTimeMillis()));
    }

    private void find() {
        logo = findViewById(R.id.logo);

        no = findViewById(R.id.no);
        wea_icon = findViewById(R.id.wea_icon);
        time = findViewById(R.id.time);
        city = findViewById(R.id.city);
        date = findViewById(R.id.date);
    }

    public void onEvent(final UpdateTime event) {
        System.out.println("更新时间：" + event.getYmdhmse());
        handler.post(new Runnable() {
            @Override
            public void run() {
                time.setText(event.getHm());
                date.setText(event.getYmd());
            }
        });
    }

    public void onEvent(final DataMessage event) {
        try {
            if (event.getApi().equals(Req.wea)) {
                AJson<NewWea> data = App.gson.fromJson(event.getData(),
                        new TypeToken<AJson<NewWea>>() {
                        }.getType());
                final Wea wea = App.gson.fromJson(data.getData().getInfo().toString(), Wea.class);
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        city.setText(wea.getCity());
                        wea_icon.setImageResource(WeaIcon.parseIcon(wea.getData().getForecast().get(0).getType()));
                    }
                });
            } else if (event.getApi().equals(Req.logo)) {
                final AJson<LogoBg> data = App.gson.fromJson(event.getData(),
                        new TypeToken<AJson<LogoBg>>() {
                        }.getType());
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        Picasso.with(context).load(data.getData().getLogo().getLogoPath()).into(logo);

                    }
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        System.out.println("Head onDetachedFromWindow");
        EventBus.getDefault().unregister(this);
    }

}
