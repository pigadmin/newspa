package com.spa.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;

import com.spa.app.App;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;


public class SocketService extends Service {
    private final int jd = 1;
    private int jdvalue = 0;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case jd:
                    System.out.println(jdvalue + "%");
                    sendBroadcast(new Intent("jd").putExtra("jd", jdvalue));
                    break;
            }
        }
    };

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    AudioManager audioManager;

    @Override
    public void onCreate() {
        super.onCreate();
        mobile();
        audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
    }

    private String url = App.socketurl;
    //private String url = "http://192.168.2.6:8000/tv";
    Socket socket;

    private void mobile() {

        try {
            socket = IO.socket(url);
//            System.out.println(url + "@@@@@@@@@@@@@@@@@@@@@");
            socket.on("title", new Emitter.Listener() {

                public void call(Object... arg0) {
                    // TODO Auto-generated method stub
                    try {
                        String json = arg0[0].toString();
                        System.out.println(json + "@@@@@@@@@@@@@@@@@@@@@");
//                        WebLive live = Ini.gson.fromJson(json, WebLive.class);
//                        Bundle bundle = new Bundle();
//                        bundle.putSerializable("key", live);
//                        startActivity(new Intent(SocketService.this, WebPlayActivity.class)
//                                .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK).putExtra("type", 1).putExtras(bundle));
                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }

                }
            });
            socket.on("in_play", new Emitter.Listener() {

                public void call(Object... arg0) {
                    // TODO Auto-generated method stub
                    try {
//                        String id = arg0[1].toString();
//                        String json = arg0[0].toString();
//                        String name = "";
//                        String path = "";
//                        WebVideo video = Ini.gson.fromJson(json, WebVideo.class);
//                        if (!id.equals("0")) {
//                            System.out.println("电视剧");
//                            for (WebVideoDetails details : video.getDetails()) {
//                                if (details.getId() == Integer.parseInt(id)) {
//                                    name = details.getName();
//                                    path = details.getFilePath();
//                                }
//                            }
//                        } else {
//                            System.out.println("电影");
//                            name = video.getName();
//                            path = video.getDetails().get(0).getFilePath();
//                        }
//
//                        startActivity(new Intent(SocketService.this, WebPlayActivity.class)
//                                .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK).putExtra("type", 2)
//                                .putExtra("name", name).putExtra("path", path));
//
//                        System.out.println(json);

                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }

                }
            });
//            socket.on("ml", new Emitter.Listener() {
//
//                public void call(Object... arg0) {
//                    // TODO Auto-generated method stub
//                    try {
//                        System.out.println(arg0[0].toString());
//                        String key = arg0[0].toString();
//                        if (key.equals("gj")) {
//                            adb.InputEvent(KeyEvent.KEYCODE_POWER);
//                        } else if (key.equals("jy")) {
////                            adb.InputEvent(KeyEvent.KEYCODE_VOLUME_MUTE);
//                            audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 0, AudioManager.FLAG_SHOW_UI);
//                        } else if (key.equals("top")) {
//                            adb.InputEvent(KeyEvent.KEYCODE_DPAD_UP);
//                        } else if (key.equals("bottom")) {
//                            adb.InputEvent(KeyEvent.KEYCODE_DPAD_DOWN);
//                        } else if (key.equals("left")) {
//                            adb.InputEvent(KeyEvent.KEYCODE_DPAD_LEFT);
//                        } else if (key.equals("right")) {
//                            adb.InputEvent(KeyEvent.KEYCODE_DPAD_RIGHT);
//                        } else if (key.equals("ok")) {
//                            adb.InputEvent(KeyEvent.KEYCODE_ENTER);
//                        } else if (key.equals("bf")) {
//                            adb.InputEvent(KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE);
//                        } else if (key.equals("zt")) {
//                            adb.InputEvent(KeyEvent.KEYCODE_MEDIA_PAUSE);
//                        } else if (key.equals("yl+")) {
//                            audioManager.adjustVolume(AudioManager.ADJUST_RAISE, AudioManager.FLAG_SHOW_UI);
//                        } else if (key.equals("yl-")) {
//                            audioManager.adjustVolume(AudioManager.ADJUST_LOWER, AudioManager.FLAG_SHOW_UI);
//                        } else if (key.equals("home")) {
//                            startActivity(new Intent(SocketService.this, MainActicity.class)
//                                    .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
//
//                        } else if (key.equals("fh")) {
//                            adb.InputEvent(KeyEvent.KEYCODE_BACK);
//                        } else if (key.equals("zm")) {
//                        } else if (key.equals("yg")) {
//                        } else if (key.equals("xx")) {
//                        }
//
//                    } catch (Exception e) {
//                        // TODO Auto-generated catch block
//                        e.printStackTrace();
//                    }
//
//                }
//            });
//            socket.on("jd", new Emitter.Listener() {
//
//                public void call(Object... arg0) {
//                    // TODO Auto-generated method stub
//                    try {
//
//                        jdvalue = Integer.parseInt(arg0[0].toString());
//
//                        handler.removeMessages(jd);
//                        handler.sendEmptyMessageDelayed(jd, 1000);
//                    } catch (Exception e) {
//                        // TODO Auto-generated catch block
//                        e.printStackTrace();
//                    }
//
//                }
//            });
            socket.on(Socket.EVENT_CONNECT, new Emitter.Listener() {

                public void call(Object... arg0) {
                    // TODO Auto-generated method stub
                    try {
                        System.out.println("连接成功----");
                        socket.emit("register", App.mac);
                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }

                }
            });
            socket.on(Socket.EVENT_DISCONNECT, new Emitter.Listener() {

                public void call(Object... arg0) {
                    // TODO Auto-generated method stub
                    try {
                        System.out.println("断开连接----offline");
                    } catch (Exception e) {
                        // TODO: handle exception
                    }
                }

            });
            socket.on(Socket.EVENT_CONNECT_ERROR, new Emitter.Listener() {

                public void call(Object... arg0) {
                    // TODO Auto-generated method stub
                    try {
                        System.out.println("连接失败----online fail");
                    } catch (Exception e) {
                        // TODO: handle exception
                    }
                }

            });
        } catch (Exception e) {
            e.printStackTrace();
        }


        socket.connect();
    }

}
