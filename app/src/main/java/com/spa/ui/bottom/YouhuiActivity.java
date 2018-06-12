package com.spa.ui.bottom;

import android.graphics.Color;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
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

import java.util.List;

public class YouhuiActivity extends BaseActivity implements AdapterView.OnItemClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);
        find();
        init();

    }

    private void init() {
        Req.get(Req.youhui);
    }

    private ListView left_list;
    private TextView item_title;
    private ImageView item_icon;
    private WebView item_content;

    private void find() {
        left_list = findViewById(R.id.left_list);
        left_list.setOnItemClickListener(this);
        item_title = findViewById(R.id.item_title);
        item_icon = findViewById(R.id.item_icon);
        item_content = findViewById(R.id.item_content);

        WebSettings websettings = item_content.getSettings();
        websettings.setJavaScriptEnabled(true);
        websettings.setBuiltInZoomControls(true);
        item_content.setBackgroundColor(Color.TRANSPARENT);
        item_content.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });

    }

    private List<Info> list;
    private IntroAdapter adapter;
    private Handler handler = new Handler();

    public void onEvent(DataMessage event) {
        try {
            if (event.getApi().equals(Req.youhui)) {
                AJson<List<Info>> data = App.gson.fromJson(
                        event.getData(), new TypeToken<AJson<List<Info>>>() {
                        }.getType());
                list = data.getData();
                if (!list.isEmpty()) {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            resetList();
                        }
                    });
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void resetList() {
        adapter = new IntroAdapter(this, list);
        left_list.setAdapter(adapter);
        resetUI(0);
    }

    private void resetUI(int p) {
        try {
            item_title.setText(list.get(p).getName());
            Picasso.with(this).load(list.get(p).getPath()).into(item_icon);
            item_content.loadDataWithBaseURL(null, list.get(p).getContent(),
                    "text/html", "utf-8", null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        final int p = position;
        handler.post(new Runnable() {
            @Override
            public void run() {
                resetUI(p);
            }
        });
    }
}
