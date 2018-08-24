package com.spa.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.view.Gravity;

import com.google.gson.reflect.TypeToken;
import com.spa.app.App;
import com.spa.app.Req;
import com.spa.bean.AJson;
import com.spa.bean.Calendar;
import com.spa.bean.MaterialVO;
import com.spa.bean.ProgramContentVO;
import com.spa.bean.ProgramListVO;
import com.spa.bean.ProgramVO;
import com.spa.download.BPRDownloading;
import com.spa.download.ZipUtil;
import com.spa.event.DataMessage;
import com.spa.service.msg.IScrollState;
import com.spa.service.msg.Marquee;
import com.spa.service.msg.MarqueeToast;
import com.spa.service.msg.TextSurfaceView;
import com.spa.tools.LtoDate;
import com.spa.ui.ad.WeekActivity;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import de.greenrobot.event.EventBus;

public class MyService extends Service implements IScrollState, Runnable {

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
        }
    };

    public MyService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    App app;

    @Override
    public void onCreate() {
        System.out.println("---MyService_onCreate()---");
        super.onCreate();
        app = (App) getApplication();
        EventBus.getDefault().register(this);

        getmarquee();
        regtime();
    }

    private void regtime() {
        // TODO Auto-generated method stub
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_TIME_TICK);
        filter.addAction(Intent.ACTION_TIME_CHANGED);
        registerReceiver(receiver, filter);
    }

    Timer marquee = new Timer();

    private void getmarquee() {
        marquee.schedule(new TimerTask() {
            @Override
            public void run() {
                Req.get(Req.marquee);
                Req.get(Req.clendar);
            }
        }, 0, 60 * 1000);
    }

    String head = new SimpleDateFormat("yyyy-MM-dd ").format(new Date(System
            .currentTimeMillis()));
    Calendar calendar;
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.ACTION_TIME_TICK.equals(intent.getAction())) {
                System.out.println("update time");
                try {
                    long cur = System.currentTimeMillis();

                    if (calendar != null) {
                        //一周安排
                        long begin = (long) sdf.parse(head
                                + calendar.getTime_begin()).getTime();

                        long end = (long) sdf.parse(head
                                + calendar.getTime_end()).getTime();

                        System.out.println("周任务：" + LtoDate.yMdHmE(begin) + "====" + LtoDate.yMdHmE(end));
                        if (cur > begin && cur < end && !app.isWeek()) {
                            app.setWeek(true);
                            Bundle bundle = new Bundle();
                            bundle.putSerializable("key", calendar);
                            Intent week = new Intent(getApplicationContext(),
                                    WeekActivity.class);
                            week.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            week.putExtras(bundle);
                            startActivity(week);
                        }

                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    };

    boolean runmarquee = false;

    public void onEvent(DataMessage event) {
        try {
            if (event.getApi().equals(Req.marquee)) {
//                System.out.println(". 2..");
                AJson<List<Marquee>> data = App.gson.fromJson(
                        event.getData(), new TypeToken<AJson<List<Marquee>>>() {
                        }.getType());
                if (data.getData() != null) {
                    marquees = data.getData();
                    if (!runmarquee) {
                        runmarquee = true;
                        handler.post(this);
                    }
                }
            } else if (event.getApi().equals(Req.clendar)) {
                try {

                    System.out.println("=============================================" + event.getData());
                    AJson<List<Calendar>> json = App.gson.fromJson(
                            event.getData(), new TypeToken<AJson<List<Calendar>>>() {
                            }.getType());
                    if (!json.getData().isEmpty()) {
                        calendar = json.getData().get(0);


                        if (app.isDownloadzip()) {
                            return;
                        }

                        final String url = calendar.getProgramURL();
                        final String filename = url.substring(url.lastIndexOf('/') + 1);
                        String old = getSharedPreferences("task", Context.MODE_PRIVATE).getString("url", "");
                        System.out.println("已下载:" + old);
                        System.out.println("最新的:" + url);
                        if (!url.equals(old)) {
                            System.out.println("开始下载：------------------" + calendar.getName() + "\n" + url);
                            app.setWeek(true);
                            ClearFiles();
                            app.setDownloadzip(true);
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    BPRDownloading.zipdownload(getApplicationContext(), url, ZipUtil.zip_file,
                                            filename, new BPRDownloading.DownloadProgressUpdater() {
                                                public void downloadProgressUpdate(int percent) {
                                                    if (percent == 100) {
                                                        app.setDownloadzip(false);
                                                        SharedPreferences preferences = getSharedPreferences("task", Context.MODE_PRIVATE);
                                                        preferences.edit().putString("url", calendar.getProgramURL())
                                                                .commit();
                                                        System.out.println("下载zip文件：" + calendar.getName() + "成功");
                                                        ZipUtil.unZipFiles(filename);
                                                    }
                                                }


                                            });

                                    DownRes();
                                    app.setWeek(false);
                                }
                            }).start();
                        }


                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void DownRes() {
        try {
            String main = ZipUtil
                    .readMainFile(ZipUtil.gainNewestFileName());
            ProgramListVO programlistVO = App.gson.fromJson(main, ProgramListVO.class);

            List<ProgramVO> programVOs = programlistVO.getPrograms();

            if (programlistVO != null) {
                for (int i = 0; i < programVOs.size(); i++) {
                    if (programVOs.get(i).getBackground() != null) {
                        String url = programVOs.get(i).getBackground()
                                .getPath();
                        final String name = url.substring(url.lastIndexOf('/') + 1);
                        final int cur = i;
                        BPRDownloading.resdownload(getApplicationContext(), url,
                                ZipUtil.res_file, name, new BPRDownloading.DownloadProgressUpdater() {
                                    public void downloadProgressUpdate(int percent) {
                                        System.out.println("正在下载第" + (cur + 1) + "个" + name + "背景图片" + percent + "%");
                                    }
                                });
                    }

                    List<ProgramContentVO> contentVO = programVOs.get(i)
                            .getContents();

                    for (int j = 0; j < contentVO.size(); j++) {
                        List<MaterialVO> materialVOs = contentVO.get(j)
                                .getMaterials();
                        int ty = 0;
                        ty = contentVO.get(j).getClassify();

                        //                   if (ty != MaterialType.VIDEO) {
                        for (int k = 0; k < materialVOs.size(); k++) {
                            String url = materialVOs.get(k).getPath();
                            final String name = url.substring(url.lastIndexOf('/') + 1);
                            final int cur = k;
                            BPRDownloading.resdownload(getApplicationContext(), url,
                                    ZipUtil.res_file, name, new BPRDownloading.DownloadProgressUpdater() {
                                        public void downloadProgressUpdate(int percent) {
                                            System.out.println("正在下载第" + (cur + 1) + "个" + name + "资源" + percent + "%");
                                        }
                                    });
                            //                 }
                        }

                    }
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void ClearFiles() {
        try {
            File zip = new File(ZipUtil.zip_file);
            File main = new File(ZipUtil.main_file);
            File res = new File(ZipUtil.res_file);

            for (File zipfiles : zip.listFiles()) {
                zipfiles.delete();
                System.out.println("删除" + zipfiles.getName());
            }
            for (File programfiles : main.listFiles()) {
                if (programfiles.isDirectory()) {
                    DirectoryDelete(programfiles);
                }
            }
            for (File resfile : res.listFiles()) {
                resfile.delete();
                System.out.println("删除" + resfile.getName());

            }


        } catch (Exception e) {
            // TODO: handle exception
        }
    }

    private void DirectoryDelete(File file) {
        // TODO Auto-generated method stub
        for (File files : file.listFiles()) {
            files.delete();
            System.out.println("删除" + files.getName());
        }
        file.delete();
    }

    private List<Marquee> marquees;
    public int currentmsg;
    private MarqueeToast toast;
    private TextSurfaceView Text;

    public void showMessage() {
        try {
            if (marquees != null && !marquees.isEmpty()) {
                if (marquees.size() <= currentmsg)
                    currentmsg = 0;
                if (toast != null)
                    toast.hid();
                toast = new MarqueeToast(getApplicationContext());
                Text = new TextSurfaceView(getApplicationContext(), this);
                Text.setOrientation(1);
//                toast.setHeight(40);
                toast.setHeight(70);
                if (marquees.get(currentmsg).getContent().equals("")
                        && marquees.get(currentmsg).getContent() == null) {
                    Text.setContent("");
                } else {
                    Text.setContent(marquees.get(currentmsg).getContent());
                }
                toast.setView(Text);
                toast.setGravity(Gravity.TOP | Gravity.LEFT, 1920, 0, 0);
                toast.show();
                currentmsg++;
            }
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }

    }

    @Override
    public void onDestroy() {
        System.out.println("---MyService_onDestroy()---");
        super.onDestroy();
        unregisterReceiver(receiver);
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void start() {

//        System.out.println("---MyService_start()---");
    }

    @Override
    public void stop() {
        // TODO Auto-generated method stub

        try {
//            System.out.println("---MyService_stop()---");
            Text.setLoop(false);
            Looper.prepare();
            handler.post(this);
            Looper.loop();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        // TODO Auto-generated method stub
//        System.out.println("---MyService_run()---");
        showMessage();
    }

}
