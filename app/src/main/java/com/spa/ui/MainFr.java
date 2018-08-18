package com.spa.ui;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

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
import com.spa.event.DataMessage;
import com.spa.tools.Fragments;
import com.spa.tools.LtoDate;
import com.spa.tools.WebInstaller;
import com.spa.ui.dish.DishStyleFr;
import com.spa.ui.diy.Toas;
import com.spa.ui.game.GameFr;
import com.spa.ui.intro.IntroFr;
import com.spa.ui.jishi.JishiStyleFr;
import com.spa.ui.live.LiveActivity;
import com.spa.ui.video.VideoFr;
import com.spa.views.BtmDialog;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;

public class MainFr extends BaseFr implements View.OnClickListener {

    private View view;
    private Activity activity;
    private App app;

    private ImageView main_menu1, main_menu2, main_menu3, main_menu4, main_menu5, main_menu6;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fr_main, container, false);
            activity = getActivity();
            app = (App) activity.getApplication();
            EventBus.getDefault().register(this);
            find();
            init();
            reg();
        }

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
        long time = app.getSystiem();
        main_week.setText(LtoDate.E(time));
        main_time.setText(LtoDate.Hm(time));
        main_date.setText(LtoDate.yMd(time));
    }


    @Override
    public void onDestroyView() {
//        app.setOld(activity.getWindow().getCurrentFocus());
//        System.out.println(app.getOld());
        activity.unregisterReceiver(receiver);
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
        getmenu();
    }


    private void getmenu() {
        Req.get(Req.menu);
    }


    private List<Menu> menu = new ArrayList<>();
    private Backs backs;

    public void onEvent(final DataMessage event) {
        try {


            if (event.getApi().equals(Req.menu)) {
                System.out.println(event.getData());
                final AJson<List<Menu>> data = App.gson.fromJson(event.getData(),
                        new TypeToken<AJson<List<Menu>>>() {
                        }.getType());

                menu = data.getData();
//                System.out.println(menu.size());
                if (menu != null && !menu.isEmpty()) {
//                    ResetMenu();
                }
            } else if (event.getApi().equals(Req.singlelive)) {
                AJson<LiveType> data = App.gson.fromJson(
                        event.getData(), new TypeToken<AJson<LiveType>>() {
                        }.getType());
                LiveType liveType = data.getData();
                System.out.println("直播类型：" + liveType.getType());
                String json = App.gson.toJson(liveType.getData());
                System.out.println(json);
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
                                event.getData(), new TypeToken<AJson<LiveTypeIP>>() {
                                }.getType());
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("key", ip.getData());
                        activity.startActivity(new Intent(activity, LiveActivity.class).putExtras(bundle));
                        Log.e("time", System.currentTimeMillis() + "");
                        break;
                    case 3:
                        AJson<LiveTypeApk> apk = App.gson.fromJson(
                                event.getData(), new TypeToken<AJson<LiveTypeApk>>() {
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

            }
        } catch (Exception e) {
            e.printStackTrace();
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

    private void find() {
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
    }

//    private void loadWeb() {
//        for (int i = 0; i < menu.size(); i++) {
//            final int p = i;
//            new Thread(new Runnable() {
//                @Override
//                public void run() {
//                    try {
//                        System.out.println(menu.get(p).getIcon() + "------------");
//                        Request request = new Request.Builder().url(menu.get(p).getIcon()).build();
//                        Response response = App.client.newCall(request).execute();
//                        if (response.code() == 200) {
//                            final byte[] b = response.body().bytes();
//                            handler.post(new Runnable() {
//                                @Override
//                                public void run() {
//                                    Bitmap bitmap = BitmapFactory.decodeByteArray(b, 0, b.length);
//                                    Drawable drawable = new BitmapDrawable(bitmap);
//                                    StateListDrawable stateListDrawable = new StateListDrawable();
//                                    stateListDrawable.addState(new int[]{android.R.attr.state_focused}, null);
//                                    stateListDrawable.addState(new int[]{}, drawable);
//                                    stateListDrawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
//                                    switch (p) {
//                                        case 0:
//                                            main_menu1.setCompoundDrawables(null, stateListDrawable, null, null);
//                                            break;
//                                        case 1:
//                                            main_menu2.setCompoundDrawables(null, stateListDrawable, null, null);
//                                            break;
//                                        case 2:
//                                            main_menu3.setCompoundDrawables(null, stateListDrawable, null, null);
//                                            break;
//                                        case 3:
//                                            main_menu4.setCompoundDrawables(null, stateListDrawable, null, null);
//                                            break;
//                                        case 4:
//                                            main_menu5.setCompoundDrawables(null, stateListDrawable, null, null);
//                                            break;
//                                        case 5:
//                                            main_menu6.setCompoundDrawables(null, stateListDrawable, null, null);
//                                            break;
//                                    }
//
//                                }
//                            });
//                        }
//                    } catch (Exception e) {
//                    }
//                }
//            }).start();
//        }
//
//    }

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

    private Handler handler = new Handler();

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
                    Req.get(Req.singlelive);
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

    private void showDialogStyle5() {
        BtmDialog dialog = new BtmDialog(activity, R.layout.dialog_style5, Gravity.CENTER, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        dialog.show();
    }

    private void showDialogStyle6() {
        BtmDialog dialog = new BtmDialog(activity, R.layout.dialog_style6, Gravity.CENTER, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        dialog.show();
    }

    private void showDialogStyle7() {
        List<Integer> list = new ArrayList<>();
        for (int i = 1; i < 50; i++) {
            list.add(i);
        }

        BtmDialog dialog = new BtmDialog(activity, R.layout.dialog_style7, Gravity.CENTER, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, list);
        dialog.show();
    }
}
