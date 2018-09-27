package com.spa.ui;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.VideoView;

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
import com.spa.bean.Backs;
import com.spa.bean.LiveType;
import com.spa.bean.LiveTypeApk;
import com.spa.bean.LiveTypeIP;
import com.spa.bean.Menu;
import com.spa.bean.WelcomeAd;
import com.spa.event.DataMessage;
import com.spa.tools.FULL;
import com.spa.tools.Fragments;
import com.spa.tools.LtoDate;
import com.spa.tools.WebInstaller;
import com.spa.ui.dish.DishStyleFr;
import com.spa.ui.diy.Toas;
import com.spa.ui.diy.wea.NewWea;
import com.spa.ui.diy.wea.Wea;
import com.spa.ui.diy.wea.WeatherImage;
import com.spa.ui.game.GameFr;
import com.spa.ui.intro.IntroFr;
import com.spa.ui.jishi.JishiStyleFr;
import com.spa.ui.live.LiveActivity;
import com.spa.ui.video.VideoFr;
import com.spa.views.BtmDialog;
import com.spa.views.BtmDialogList;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;

import static com.squareup.picasso.Picasso.with;

public class MainFr extends BaseFr implements View.OnClickListener {

    private View view;
    private Activity activity;
    private App app;

    private FrameLayout main_menu1, main_menu2, main_menu3, main_menu4, main_menu5, main_menu6;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fr_main2, container, false);
        activity = getActivity();
        app = (App) activity.getApplication();
        find();
        init();
        reg();
        app.setShowname(1);

        activity.sendBroadcast(new Intent(App.SHOWNAME));
        return view;
    }


    private void reg() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_TIME_TICK);
        activity.registerReceiver(receiver, filter);
    }

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (Intent.ACTION_TIME_TICK.equals(intent.getAction())) {
                app.setSystiem(app.getSystiem() + 60 * 1000);
                updateTime();
            }
        }
    };

    private void updateTime() {
//        long time = app.getSystiem();
        long time = System.currentTimeMillis();
        main_week.setText(LtoDate.E(time));
        main_time.setText(LtoDate.Hm(time));
        main_date.setText(LtoDate.yMd(time));
    }


    @Override
    public void onDestroyView() {
//        app.setOld(activity.getWindow().getCurrentFocus());
//        System.out.println(app.getOld());
        try {
            activity.sendBroadcast(new Intent(App.HIDENAME));
            app.setShowname(0);
            System.out.println("main__oooooooooooooooooooon");
//            activity.unregisterReceiver(receiver);
            activity.unregisterReceiver(receiver);
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.onDestroyView();
    }

//    @Override
//    public void onResume() {
//        if (app.getOld() != null) {
//            app.getOld().requestFocus();
//        }
//        super.onResume();
//    }

    private void init() {
//        mainad();
        getmenu();
        getwea();
    }


    private void getmenu() {
        String url = App.requrl("tmenu", "");
//        Log.e(tag, url);
        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String json) {
                try {
                    System.out.println(json);
                    final AJson<List<Menu>> data = App.gson.fromJson(json,
                            new TypeToken<AJson<List<Menu>>>() {
                            }.getType());

                    menu = data.getData();
//                System.out.println(menu.size());
                    if (menu != null && !menu.isEmpty()) {
//                    ResetMenu();
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

    private void mainad() {
        Req.get(Req.welcomead);
    }

    private List<Menu> menu = new ArrayList<>();
    private Backs backs;
    public int WELCOME_FINISH;
    List<WelcomeAd> welcomeAds;
    final int WELCOME_TIMEOUT = 0;
    final int PlayWelcomead = 1;
    int cutad;


    private void HideAd(int sh) {
        if (sh == 1) {
            ad1.setVisibility(View.INVISIBLE);
            ad2.setVisibility(View.INVISIBLE);
            ad3.setVisibility(View.INVISIBLE);
            ad4.setVisibility(View.INVISIBLE);
            ad5.setVisibility(View.INVISIBLE);
            ad6.setVisibility(View.INVISIBLE);
            ad7.setVisibility(View.INVISIBLE);
            ad8.setVisibility(View.INVISIBLE);
            ad9.setVisibility(View.INVISIBLE);
            ad10.setVisibility(View.INVISIBLE);
            ad11.setVisibility(View.INVISIBLE);
        } else if (sh == 2) {
            ad1.setVisibility(View.VISIBLE);
            ad2.setVisibility(View.VISIBLE);
            ad3.setVisibility(View.VISIBLE);
            ad4.setVisibility(View.VISIBLE);
            ad5.setVisibility(View.VISIBLE);
            ad6.setVisibility(View.VISIBLE);
            ad7.setVisibility(View.VISIBLE);
            ad8.setVisibility(View.VISIBLE);
            ad9.setVisibility(View.VISIBLE);
            ad10.setVisibility(View.VISIBLE);
            ad11.setVisibility(View.VISIBLE);
            if (welcome_video.isPlaying()) {
                welcome_video.pause();
            }
        }
    }

//    private void ResetMenu() {
//        handler.post(new Runnable() {
//            @Override
//            public void run() {
//                try {
//                    main_menu1.setText(menu.get(0).getName());
//                    main_menu2.setText(menu.get(1).getName());
//                    main_menu3.setText(menu.get(2).getName());
//                    main_menu4.setText(menu.get(3).getName());
//                    main_menu5.setText(menu.get(4).getName());
//                    main_menu6.setText(menu.get(5).getName());
//
////                    loadWeb();
//
//                    for (int i = 0; i < menu.size(); i++) {
//                        int id = menu.get(i).getId();
//                        switch (i) {
//                            case 0:
//                                seticon(id, main_menu1);
//                                break;
//                            case 1:
//                                seticon(id, main_menu2);
//                                break;
//                            case 2:
//                                seticon(id, main_menu3);
//                                break;
//                            case 3:
//                                seticon(id, main_menu4);
//                                break;
//                            case 4:
//                                seticon(id, main_menu5);
//                                break;
//                            case 5:
//                                seticon(id, main_menu6);
//                                break;
//                        }
//                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//
//
//        });
//    }

//    private void seticon(int id, TextView button) {
//        try {
//            Drawable drawable = null;
//            switch (id) {
//                case 1:   //服务介绍
//                    drawable = activity.getResources().getDrawable(R.drawable.main_menu1);
//                    break;
//                case 2://电视直播
//                    drawable = getResources().getDrawable(R.drawable.main_menu2);
//                    break;
//                case 3://酒水饮料
//                    drawable = getResources().getDrawable(R.drawable.main_menu5);
//                    break;
//                case 4://技师服务
//                    drawable = getResources().getDrawable(R.drawable.main_menu4);
//                    break;
//                case 5://影音娱乐
//                    drawable = getResources().getDrawable(R.drawable.main_menu3);
//                    break;
//                case 6://游戏应用
//                    drawable = getResources().getDrawable(R.drawable.main_menu6);
//                    break;
//            }
//            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
//            button.setCompoundDrawables(null, drawable, null, null);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

    private TextView main_week, main_time, main_date;
    private ImageView ad1, ad2, ad4, ad5, ad6, ad7, ad8, ad9, ad10, ad11;
    private LinearLayout ad3;

    private void find() {
        ad1 = view.findViewById(R.id.ad1);
        ad2 = view.findViewById(R.id.ad2);
        ad3 = view.findViewById(R.id.ad3);
        ad4 = view.findViewById(R.id.ad4);
        ad5 = view.findViewById(R.id.ad5);
        ad6 = view.findViewById(R.id.ad6);
        ad7 = view.findViewById(R.id.ad7);
        ad8 = view.findViewById(R.id.ad8);
        ad9 = view.findViewById(R.id.ad9);
        ad10 = view.findViewById(R.id.ad10);
        ad11 = view.findViewById(R.id.ad11);


        main_week = view.findViewById(R.id.main_week);
        main_time = view.findViewById(R.id.main_time);
        main_date = view.findViewById(R.id.main_date);
        updateTime();

        main_menu1 = view.findViewById(R.id.main_menu1);
        main_menu2 = view.findViewById(R.id.main_menu2);
        main_menu3 = view.findViewById(R.id.main_menu3);
        main_menu4 = view.findViewById(R.id.main_menu4);
        main_menu5 = view.findViewById(R.id.main_menu5);
        main_menu6 = view.findViewById(R.id.main_menu6);
        main_menu1.setOnClickListener(this);
        main_menu2.setOnClickListener(this);
        main_menu3.setOnClickListener(this);
        main_menu4.setOnClickListener(this);
        main_menu5.setOnClickListener(this);
        main_menu6.setOnClickListener(this);

        wea_icon = view.findViewById(R.id.wea_icon);
        wea_tmp = view.findViewById(R.id.wea_tmp);
        wea_city = view.findViewById(R.id.wea_city);
        wea_maxtmp = view.findViewById(R.id.wea_maxtmp);
        wea_mintmp = view.findViewById(R.id.wea_mintmp);

        welcome_image = view.findViewById(R.id.welcome_image);
        welcome_video = view.findViewById(R.id.welcome_video);
        FULL.star(welcome_video);
        welcome_video.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {

            }
        });
        welcome_video.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {

            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                mediaPlayer.start();
            }
        });
        welcome_video.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mediaPlayer, int i, int i1) {
                return true;
            }
        });


        mediaPlayer = new MediaPlayer();

        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {

            }
        });
        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {

            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                mediaPlayer.start();
            }
        });
        mediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mediaPlayer, int i, int i1) {
                return true;
            }
        });

    }

    private MediaPlayer mediaPlayer;
    private ImageView welcome_image;
    private VideoView welcome_video;
    private ImageView wea_icon;
    private TextView wea_tmp, wea_city, wea_maxtmp, wea_mintmp;

    private void getwea() {

        String url = App.requrl("getWeather", "");
//        Log.e(tag, url);
        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String json) {
                try {
                    AJson<NewWea> data = App.gson.fromJson(json,
                            new TypeToken<AJson<NewWea>>() {
                            }.getType());
                    System.out.println(data.getData().getInfo().toString());
                    final Wea wea = App.gson.fromJson(data.getData().getInfo().toString(), Wea.class);
                    System.out.println("--" + data.getData().getInfo().toString());
                    if (wea != null) {
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                wea_city.setText(wea.getCity());
                                wea_tmp.setText(wea.getData().getWendu() + "℃");
                                wea_maxtmp.setText(wea.getData().getForecast().get(0).getHigh().substring(2, 5) + "℃");
                                wea_mintmp.setText(wea.getData().getForecast().get(0).getLow().substring(2, 5) + "℃");
                                wea_icon.setImageResource(WeatherImage.parseIcon(wea.getData().getForecast().get(0).getType()));


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


    @Override
    public void onClick(View v) {
        if (v == main_menu1) {
            ToActivity(0);
        } else if (v == main_menu2) {
            ToActivity(1);
        } else if (v == main_menu3) {
            ToActivity(2);
        } else if (v == main_menu4) {
            ToActivity(3);
        } else if (v == main_menu5) {
            ToActivity(4);
        } else if (v == main_menu6) {
            ToActivity(5);
        }
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case WELCOME_TIMEOUT:
                    System.out.println(WELCOME_FINISH + "@@@@@@@");
                    if (WELCOME_FINISH == 0) {


                        HideAd(2);
                        handler.removeMessages(WELCOME_TIMEOUT);
                    } else if (WELCOME_FINISH > 0) {
                        WELCOME_FINISH--;
                        handler.sendEmptyMessageDelayed(WELCOME_TIMEOUT, 1 * 1000);
                    }
                    break;
                case PlayWelcomead:
                    try {
                        System.out.println(welcomeAds.get(cutad).getType() + "@@@@");
                        switch (welcomeAds.get(cutad).getType()) {
                            case 1:
                                playimg();
                                playmusic();
                                break;
                            case 2:
                                playvideo();
                                break;
                        }
                        if (welcomeAds.size() > 1) {
                            if (cutad < welcomeAds.size() - 1) {
                                handler.sendEmptyMessageDelayed(PlayWelcomead, welcomeAds.get(cutad).getInter() * 1000);
                                cutad++;
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
            }
        }
    };

    private void playimg() {
        if (welcome_video.getVisibility() == View.VISIBLE) {
            welcome_video.setVisibility(View.GONE);
            if (welcome_video.isPlaying()) {
                welcome_video.stopPlayback();
            }
        }
        if (welcome_image.getVisibility() == View.GONE) {
            welcome_image.setVisibility(View.VISIBLE);
        }
        System.out.println(welcomeAds.get(cutad).getFilePath());

        Picasso.with(activity).load(welcomeAds.get(cutad).getFilePath()).into(welcome_image);
    }


    private void playmusic() {
        // TODO Auto-generated method stub

        try {
            mediaPlayer.stop();
            mediaPlayer.reset();
            System.out.println(welcomeAds.get(cutad).getBgFile());
//            mediaPlayer.setDataSource(WelcomeActivity.this,
//                    Uri.parse(music.get(cutmusic).getBgFile()));
            mediaPlayer.setDataSource(activity,
                    Uri.parse(welcomeAds.get(cutad).getBgFile()));
            mediaPlayer.prepareAsync();
        } catch (Exception e) {
            // TODO: handle exception
        }

    }

    private void playvideo() {
        if (welcome_image.getVisibility() == View.VISIBLE) {
            welcome_image.setVisibility(View.GONE);
        }
        if (welcome_video.getVisibility() == View.GONE) {
            welcome_video.setVisibility(View.VISIBLE);
        }
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
            mediaPlayer.release();
        }

        welcome_video.setVideoURI(Uri.parse(welcomeAds.get(cutad).getFilePath()));

    }

    private void ToActivity(int p) {
        try {
            System.out.println(menu);
            if (menu.isEmpty())
                return;
            if (menu.get(p).getStatus() == 0) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        Toas toas = new Toas();
                        toas.setMsg(getString(R.string.useless));
                        toas.show(activity);
                        toas = null;
                    }
                });
                return;
            }

            int id = menu.get(p).getId();
            Log.d("MainFr", "id.." + id);
            switch (id) {
                case 1://服务介绍
                    Fragments.To(getFragmentManager(), new IntroFr());
                    break;
                case 2://电视直播
                    Log.e("time", System.currentTimeMillis() + "");
                    getlive();
                    break;
                case 3://酒水饮料
                    Fragments.To(getFragmentManager(), new DishStyleFr());
                    break;
                case 4://技师服务
                    Fragments.To(getFragmentManager(), new JishiStyleFr());
                    break;
                case 5://点播资源
                    Fragments.To(getFragmentManager(), new VideoFr());
                    break;
                case 6://游戏应用
                    Fragments.To(getFragmentManager(), new GameFr());
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getlive() {
        String url = App.requrl("live", "&type=2");
//        Log.e(tag, url);
        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String json) {
                try {
                    AJson<LiveType> data = App.gson.fromJson(
                            json, new TypeToken<AJson<LiveType>>() {
                            }.getType());
                    LiveType liveType = data.getData();
                    System.out.println("直播类型：" + liveType.getType());
                    String tmp = App.gson.toJson(liveType.getData());
                    System.out.println(tmp);
                    switch (liveType.getType()) {
                        case 1:
                            try {
                                ComponentName componentName = new ComponentName(
                                        "com.mstar.tv.tvplayer.ui",
                                        "com.mstar.tv.tvplayer.ui.RootActivity");
                                activity.startActivity(new Intent().setComponent(componentName));
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            break;
                        case 2:
                            AJson<LiveTypeIP> ip = App.gson.fromJson(
                                    json, new TypeToken<AJson<LiveTypeIP>>() {
                                    }.getType());
                            Bundle bundle = new Bundle();
                            bundle.putSerializable("key", ip.getData());
                            activity.startActivity(new Intent(activity, LiveActivity.class).putExtras(bundle));
                            Log.e("time", System.currentTimeMillis() + "");
                            break;
                        case 3:
                            AJson<LiveTypeApk> apk = App.gson.fromJson(
                                    json, new TypeToken<AJson<LiveTypeApk>>() {
                                    }.getType());
                            final String pkgName = apk.getData().getData().get(0).getPackage_name();
                            final String url = apk.getData().getData().get(0).getPath();
                            System.out.println(pkgName + "@@@@@@@@@@@" + url);
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    if (App.isInstall(activity, pkgName)) {
                                        App.startApk(activity, pkgName, "");
                                    } else {
                                        new WebInstaller(activity, url)
                                                .downloadAndInstall(activity
                                                        .getString(R.string.Downloading_game));
                                    }
                                }
                            });

                            break;

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

    private void showDialogStyle5() {
        BtmDialog dialog = new BtmDialog(activity, R.layout.dialog_style5);
        dialog.show();
    }

    private void showDialogStyle6() {
        BtmDialog dialog = new BtmDialog(activity, R.layout.dialog_style6);
        dialog.show();
    }

    private void showDialogStyle7() {
        List<String> list = new ArrayList<>();
        for (int i = 1; i < 10; i++) {
            list.add(i + "");
        }

        BtmDialogList dialog = new BtmDialogList(activity, list);
        dialog.show();
    }
}
