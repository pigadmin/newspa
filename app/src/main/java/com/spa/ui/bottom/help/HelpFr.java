package com.spa.ui.bottom.help;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.spa.R;
import com.spa.app.App;
import com.spa.app.Req;
import com.spa.bean.AJson;
import com.spa.bean.Info;
import com.spa.event.DataMessage;
import com.spa.ui.BaseFr;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.List;

import de.greenrobot.event.EventBus;

public class HelpFr extends BaseFr {

    private View view;
    private Activity activity;
    private App app;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        view = inflater.inflate(R.layout.activity_help, container, false);
        activity = getActivity();
        app = (App) activity.getApplication();
        EventBus.getDefault().register(this);
        find();
        init();
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
    }

    private WebView help_web;
    private TextView help_title;

    private void find() {
        help_title = view.findViewById(R.id.help_title);
        help_web = view.findViewById(R.id.help_web);


        WebSettings websettings = help_web.getSettings();
        websettings.setJavaScriptEnabled(true);
        websettings.setBuiltInZoomControls(true);
        help_web.setBackgroundColor(Color.TRANSPARENT);
        help_web.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
    }


    private void init() {
        Req.get(Req.help);
    }


    private List<Info> list;
    private Handler handler = new Handler();

    public void onEvent(DataMessage event) {
        try {
            System.out.println(Req.help);
            if (event.getApi().equals(Req.help)) {
                AJson<List<Info>> data = App.gson.fromJson(
                        event.getData(), new TypeToken<AJson<List<Info>>>() {
                        }.getType());
                list = data.getData();
                if (!list.isEmpty()) {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            resetUI();
                        }
                    });
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void resetUI() {
        help_title.setText(list.get(0).getName());
        System.out.println(list.get(0).getContent().replace
                ("background-color: rgb(255, 255, 255);", "background-color: transparent;"));
        help_web.loadDataWithBaseURL(null, list.get(0).getContent().replace
                        ("background-color: rgb(255, 255, 255);", "background-color: transparent;"),
                "text/html", "utf-8", null);

        handler.post(new Runnable() {
            @Override
            public void run() {
                System.out.println(list.get(0).getPath());
                Picasso.with(activity).load(list.get(0).getPath()).into(new Target() {
                    @Override
                    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom loadedFrom) {
                        help_web.setBackground(new BitmapDrawable(bitmap));
                    }

                    @Override
                    public void onBitmapFailed(Drawable drawable) {

                    }

                    @Override
                    public void onPrepareLoad(Drawable drawable) {

                    }
                });
            }
        });
    }


}
