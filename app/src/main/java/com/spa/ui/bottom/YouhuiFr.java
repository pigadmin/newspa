package com.spa.ui.bottom;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.spa.bean.AJson;
import com.spa.bean.Info;
import com.spa.event.DataMessage;
import com.spa.ui.BaseFr;
import com.spa.ui.adapter.IntroAdapter;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.greenrobot.event.EventBus;

public class YouhuiFr extends BaseFr implements AdapterView.OnItemClickListener {

    private View view;
    private Activity activity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        view = inflater.inflate(R.layout.activity_intro, container, false);
        activity = getActivity();
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


    private void init() {
        Req.get(Req.youhui);
    }

    private ListView left_list;
    private TextView item_title;
    private ImageView item_icon;
    private WebView item_content;

    private void find() {
        left_list = view.findViewById(R.id.left_list);
        left_list.setOnItemClickListener(this);


        item_title = view.findViewById(R.id.item_title);
        item_icon = view.findViewById(R.id.item_icon);
        item_content = view.findViewById(R.id.item_content);

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
        adapter = new IntroAdapter(activity, list);
        left_list.setAdapter(adapter);
        resetUI(0);
    }

    private void resetUI(int p) {
        try {
            item_title.setText(list.get(p).getName());
            Picasso.with(activity).load(list.get(p).getPath()).into(item_icon);
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
