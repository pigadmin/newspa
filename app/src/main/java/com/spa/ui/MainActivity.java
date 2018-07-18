package com.spa.ui;

import android.os.Bundle;
import android.os.Handler;

import com.google.gson.reflect.TypeToken;
import com.spa.R;
import com.spa.app.App;
import com.spa.app.Req;
import com.spa.bean.AJson;
import com.spa.event.DataMessage;
import com.spa.tools.Fragments;
import com.spa.tools.Update;
import com.spa.ui.diy.Toas;

public class MainActivity extends BaseActivity {

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        //    super.onSaveInstanceState(outState);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Fragments.To(getFragmentManager(), new MainFr());

        init();
    }


    private void init() {
        getlogo();
        getwea();
        checkupdate();

    }

    private void getlogo() {
        Req.get(Req.logo);
    }


    private void getwea() {
        Req.get(Req.wea);
    }

    private void checkupdate() {
        Req.get(Req.update);
    }

    private Handler handler = new Handler();

    public void onEvent(final DataMessage event) {
        try {

            if (event.getApi().equals(Req.menu)) {
                if (event.getApi().equals(Req.update)) {
                    final AJson<String> data = App.gson.fromJson(
                            event.getData(), new TypeToken<AJson<String>>() {
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
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

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
        // TODO Auto-generated method stub
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
            System.exit(0);
        }
    }
}
