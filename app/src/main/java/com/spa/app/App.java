package com.spa.app;

import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.TextUtils;
import android.view.View;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.spa.R;
import com.spa.bean.LogoBg;
import com.spa.bean.User;
import com.spa.event.NetChange;
import com.spa.event.UpdateTime;
import com.spa.service.MyService;
import com.spa.service.SocketService;
import com.spa.ui.ad.bean.Mings;
import com.spa.ui.diy.Toas;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;
import okhttp3.OkHttpClient;

public class App extends Application {
    public static RequestQueue queue;

    public static class ScreenType {

        // 横屏
        public static final int HORIZONTAL = 1;
        // 竖屏
        public static final int VERTICAL = 2;
    }

    public static final String SHOWNAME = "SHOWNAME";
    public static final String HIDENAME = "HIDENAME";
    public static final String PALY = "PALY";
    public static final String PAUSE = "PAUSE";
    public static final String STOP = "STOP";
    public static final String FORWARD = "FORWARD";
    public static final String REWIND = "REWIND";
    public static final String Cancle = "Cancle";
    public static Gson gson;
    private SharedPreferences config;
    public static OkHttpClient client;

    private Toas toas = new Toas();

    public int getShowname() {
        return showname;
    }

    public void setShowname(int showname) {
        this.showname = showname;
    }

    private int showname;

    @Override
    public void onCreate() {
        super.onCreate();
        queue = Volley.newRequestQueue(this);

        config = getSharedPreferences("config", Context.MODE_PRIVATE);
        config();
        getip();

        mac();
        client = new OkHttpClient();
        gson = new GsonBuilder().setDateFormat(
                "yyyy-MM-dd'T'HH:mm:ss").create();
        IntentFilter filter = new IntentFilter();
        filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        filter.addAction(Intent.ACTION_TIME_TICK);
        filter.addAction(Intent.ACTION_TIME_CHANGED);
        registerReceiver(receiver, filter);


        startService(new Intent(this, MyService.class));
        startService(new Intent(this, SocketService.class));
    }

    public static int network_type = -1;
    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
//                ConnectivityManager connectivityManager = (ConnectivityManager) context
//                        .getSystemService(Context.CONNECTIVITY_SERVICE);
//                NetworkInfo activeNetworkInfo = connectivityManager
//                        .getActiveNetworkInfo();
//                network_type = activeNetworkInfo.getType();
//                System.out.println("网络类型&Net Type：" + network_type);
//
//                switch (network_type) {
//                    case -1:
//                        toas.setMsg(getString(R.string.disnetwork));
//                        break;
//                    case 0:
//                        toas.setMsg(getString(R.string.gps_network));
//                        break;
//                    case 1:
//                        toas.setMsg(getString(R.string.wifi_network));
//                        break;
//                    case 9:
//                        toas.setMsg(getString(R.string.eth_network));
//                        break;
//                }
//                EventBus.getDefault().post(new NetChange(network_type));
//                toas.show(App.this);
            } else if (intent.getAction().equals(Intent.ACTION_TIME_TICK)) {
                EventBus.getDefault().post(new UpdateTime(System.currentTimeMillis()));
            } else if (intent.getAction().equals(Intent.ACTION_TIME_CHANGED)) {
                System.out.println("更改系统时间");
            }

        }
    };

    public View getOld() {
        return old;
    }

    public void setOld(View old) {
        this.old = old;
    }

    private View old;

    private boolean fstart;
    //    private static String ip = "192.168.2.14";
    private static String ip = "192.168.2.25";
    //private static String ip = "192.168.2.89:8108";
    public static String version;

    private void config() {
        try {
            fstart = config.getBoolean("fstart", false);
            if (!fstart) {
                SharedPreferences.Editor editor = config.edit();
                editor.putBoolean("fstart", true);
                System.out.println("---fstart---");
                editor.putString("ip", ip);
                System.out.println("---ip---\n" + ip);
                editor.commit();
            }
            version = getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String headurl;
    public static String socketurl;

    private void getip() {
        String tmp = config.getString("ip", "");
        if (!tmp.equals("")) {
//            headurl = "http://" + tmp + ":8080/wisdom_spa/remote/";
            headurl = "http://" + tmp + ":8108/wisdom_spa/remote/";
            socketurl = "http://" + tmp + ":8000/tv";
            System.out.println("---headurl---\n" + headurl);
        }
    }

    public static String requrl(String api, String parm) {
        String url = headurl + api + "?mac=" + App.mac + parm;

        return url;
    }

    public static <T> T jsonToObject(String json, TypeToken<T> typeToken) {
        //  new TypeToken<AJson<Object>>() {}.getType()   对象参数
        // new TypeToken<AJson<List<Object>>>() {}.getType() 集合参数

        if (TextUtils.isEmpty(json) || json.equals("null"))
            return null;
        try {
            return gson.fromJson(json, typeToken.getType());
        } catch (Exception e) {
            return null;
        }
    }

    public static String mac = "error";

    private void mac() {
        try {
            Process pro = Runtime.getRuntime().exec(
                    "cat /sys/class/net/eth0/address");
            InputStreamReader inReader = new InputStreamReader(
                    pro.getInputStream());
            BufferedReader bReader = new BufferedReader(inReader);
            String line = null;
            while ((line = bReader.readLine()) != null) {
                mac = line.trim();
            }
            System.out.println("---mac---\n" + mac);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private  LogoBg logoBg = null;

    public LogoBg getLogoBg() {
        return logoBg;
    }

    public void setLogoBg(LogoBg logoBg) {
        this.logoBg = logoBg;
    }

    private Bitmap logo = null;

    public Bitmap getLogo() {
        return logo;
    }

    public void setLogo(Bitmap logo) {
        this.logo = logo;
    }

    private List<Bitmap> back = new ArrayList<>();

    public List<Bitmap> getBack() {
        return back;
    }

    public void setBack(List<Bitmap> back) {
        this.back = back;
    }

    public static boolean isInstall(Context context, String packageName) {
        try {
            PackageInfo pin = context.getPackageManager().getPackageInfo(
                    packageName, 0);
            if (pin != null) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean startApk(Context context, String pkgNmae, String className) {
        try {
            if (isInstall(context, pkgNmae)) {
                if (!className.trim().equals("")) {
                    Intent intent = new Intent();
                    intent.setComponent(new ComponentName(pkgNmae, className));
                    context.startActivity(intent);
                } else {
                    Intent intent = context.getPackageManager().getLaunchIntentForPackage(pkgNmae);
                    context.startActivity(intent);

                }
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();

        }
        return false;
    }

    private String Key = "";

    public String getKey() {
        return Key;
    }

    public void setKey(String key) {
        Key = key;
    }

    private long systiem = 0;

    public long getSystiem() {
        return systiem;
    }

    public void setSystiem(long systiem) {
        this.systiem = systiem;
    }

    public boolean isWeek() {
        return week;
    }

    public void setWeek(boolean week) {
        this.week = week;
    }

    public boolean week;

    public boolean isNowins() {
        return nowins;
    }

    public void setNowins(boolean nowins) {
        this.nowins = nowins;
    }

    public boolean nowins;


    public boolean isMing() {
        return ming;
    }

    public void setMing(boolean ming) {
        this.ming = ming;
    }

    public boolean ming;

    public Mings getMings() {
        return mings;
    }

    public void setMings(Mings mings) {
        this.mings = mings;
    }

    Mings mings = new Mings();


    public boolean isDownloadzip() {
        return downloadzip;
    }

    public void setDownloadzip(boolean downloadzip) {
        this.downloadzip = downloadzip;
    }

    boolean downloadzip;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    private User user;

    public boolean isCall() {
        return call;
    }

    public void setCall(boolean call) {
        this.call = call;
    }

    private boolean call;
}