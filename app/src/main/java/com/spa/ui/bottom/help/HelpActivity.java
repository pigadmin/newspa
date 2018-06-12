package com.spa.ui.bottom.help;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.spa.R;
import com.spa.app.App;
import com.spa.app.Req;
import com.spa.event.DataMessage;
import com.spa.ui.BaseActivity;
import com.spa.ui.adapter.IntroAdapter;
import com.spa.bean.AJson;
import com.spa.bean.Info;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.List;

public class HelpActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);

        find();
        init();
    }

    private WebView help_web;
    private TextView help_title;

    private void find() {
        help_title = findViewById(R.id.help_title);
        help_web = findViewById(R.id.help_web);


        WebSettings websettings = help_web.getSettings();
        websettings.setJavaScriptEnabled(true);
        websettings.setBuiltInZoomControls(true);
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
        help_web.loadDataWithBaseURL(null, list.get(0).getContent(),
                "text/html", "utf-8", null);

        Picasso.with(this).load(list.get(0).getPath()).into(new Target() {
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

}
