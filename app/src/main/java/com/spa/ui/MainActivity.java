package com.spa.ui;

import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.reflect.TypeToken;
import com.spa.R;
import com.spa.app.App;
import com.spa.app.Req;
import com.spa.bean.AJson;
import com.spa.bean.LiveType;
import com.spa.bean.LiveTypeApk;
import com.spa.bean.LiveTypeIP;
import com.spa.event.DataMessage;
import com.spa.tools.Fragments;
import com.spa.tools.Update;
import com.spa.tools.WebInstaller;
import com.spa.ui.diy.Toas;
import com.spa.ui.live.LiveActivity;

public class MainActivity extends BaseActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            setContentView(R.layout.activity_main);

            Fragments.To(getFragmentManager(), new MainFr());

            init();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void init() {

        checkupdate();
    }




    private void checkupdate() {
        String url = App.requrl("getUpgrade", "&version=" + App.version);
//        Log.e(tag, url);
        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String json) {
                try {

                        final AJson<String> data = App.gson.fromJson(
                                json, new TypeToken<AJson<String>>() {
                                }.getType());
                        if (data.getData() != null) {
                            System.out.println("准备自动升级...");
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    new Update(MainActivity.this, data.getData()).downloadAndInstall();
                                }
                            });
                        }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
            }
        });
        request.setRetryPolicy(new DefaultRetryPolicy(
                5 * 1000,//链接超时时间
                0,//重新尝试连接次数
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        ));
        App.queue.add(request);
    }

    private Handler handler = new Handler();


    @Override
    public void onBackPressed() {
        if (getFragmentManager().getBackStackEntryCount() > 1) {
            getFragmentManager().popBackStack();
        } else {
            exit();
        }
    }

    private long exitTime = 0;


    private void exit() {
        if (System.currentTimeMillis() - exitTime > 2000) {
            exitTime = System.currentTimeMillis();
            handler.post(new Runnable() {
                @Override
                public void run() {
                    Toas toas = new Toas();
                    toas.setMsg(getString(R.string.exit));
                    toas.show(MainActivity.this);
                    toas = null;
                }
            });
        } else {
            finish();
//            System.exit(0);
        }
    }
}
