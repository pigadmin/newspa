package com.spa.ui.live;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.BitmapDrawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.VideoView;

import com.spa.R;
import com.spa.bean.LiveIP;
import com.spa.ui.adapter.LiveListAdapter;
import com.spa.bean.LiveTypeIP;
import com.spa.tools.FULL;
import com.spa.ui.BaseActivity;
import com.spa.ui.diy.Toas;

import java.util.ArrayList;
import java.util.List;


public class LiveActivity extends BaseActivity implements MediaPlayer.OnErrorListener, MediaPlayer.OnPreparedListener, MediaPlayer.OnCompletionListener {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live);
        find();
        init();
        Log.e("time", System.currentTimeMillis() + "");

    }


    RelativeLayout live_tips;
    TextView live_no_s, live_no_name;
    TextView live_no_b;
    VideoView live_player;

    private void find() {
        live_tips = findViewById(R.id.live_tips);
        live_no_s = findViewById(R.id.live_no_s);
        live_no_name = findViewById(R.id.live_no_name);
        live_no_b = findViewById(R.id.live_no_b);
        live_player = findViewById(R.id.live_player);
        FULL.star(live_player);
        live_player.setOnPreparedListener(this);
        live_player.setOnCompletionListener(this);
        live_player.setOnErrorListener(this);
    }

    private SharedPreferences live_share;
    private AudioManager audioManager;
    private int maxVolume;
    private int currentVolume;
    private List<LiveIP> livelist = new ArrayList<>();
    private int historyno = 0;

    private void init() {

        try {
            LiveTypeIP ip = (LiveTypeIP) getIntent().getSerializableExtra("key");

            if (!ip.getData().isEmpty()) {
                livelist = ip.getData();
            }
            live_share = getSharedPreferences("live", Context.MODE_PRIVATE);
            audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
            maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
            currentVolume = audioManager
                    .getStreamVolume(AudioManager.STREAM_MUSIC);

            historyno = live_share.getInt("no", 0);

            play();
            showinfo();

        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }

    }


    private String testurl = "http://192.168.31.250:8105/wisdom_hotel/upload/1.ts";

    private void play() {
        // TODO Auto-generated method stub
        try {
            if (live_player.isPlaying()) {
                live_player.stopPlayback();
            }
            System.out.println(livelist.get(historyno).getAddress());
            if (live_player != null && !livelist.isEmpty()) {
                live_player.setVideoPath(livelist.get(historyno).getAddress());
//                live_player.setVideoPath(testurl);
            }
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
    }


    private ListView live_list;
    private TextView live_count;
    private PopupWindow popupWindow;
    private View view;

    //
    private void show() {
        // TODO Auto-generated method stub
        try {
            view = getLayoutInflater().inflate(R.layout.pop_live, null);
            popupWindow = new PopupWindow(view, WindowManager.LayoutParams.MATCH_PARENT,
                    WindowManager.LayoutParams.MATCH_PARENT);
            popupWindow.setFocusable(true);
            popupWindow.setOutsideTouchable(true);
            popupWindow.setBackgroundDrawable(new BitmapDrawable());
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    popupWindow.dismiss();
                }
            });
            live_list = view.findViewById(R.id.live_list);
            live_list.setAdapter(new LiveListAdapter(this, livelist));
            live_list.setSelectionFromTop(historyno, 220);
            popupWindow.showAtLocation(view, Gravity.BOTTOM, 0, 0);
            live_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> av, View v,
                                        int position, long id) {
                    historyno = position;
                    play();
                    showinfo();
                }
            });
            handler.removeMessages(HideLiveList);
            handler.sendEmptyMessageDelayed(HideLiveList, 10 * 1000);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //
    final int HideLiveList = 0;
    final int HideLiveInfo = 1;
    final int SwitchNo = 2;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case HideLiveList:
                    try {
                        if (popupWindow != null) {
                            if (popupWindow.isShowing()) {
                                popupWindow.dismiss();
                                popupWindow = null;
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case HideLiveInfo:
                    live_tips.setVisibility(View.GONE);
                    live_no_b.setText("");
                    break;
                case SwitchNo:
                    try {
                        int tmp = Integer.parseInt(cutno) - 1;
                        cutno = "";
                        if (tmp >= 0 && tmp < livelist.size()) {
                            historyno = tmp;
                            play();
                            showinfo();
                        } else {
                            Toas toas = new Toas();
                            toas.setMsg(getString(R.string.live_none));
                            toas.show(LiveActivity.this);
                            toas = null;
                        }

                    } catch (Exception e) {
                        // TODO: handle exception
                        cutno = "";
                    }
                    break;
            }
        }
    };
    String cutno = "";


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub


        if (keyCode >= KeyEvent.KEYCODE_0 && keyCode <= KeyEvent.KEYCODE_9) {
            cutno += keyCode - 7;
            live_no_b.setText(cutno);

            handler.removeMessages(SwitchNo);
            handler.sendEmptyMessageDelayed(SwitchNo, 2 * 1000);

        } else if (keyCode == KeyEvent.KEYCODE_DPAD_CENTER
                || keyCode == KeyEvent.KEYCODE_ENTER) {
            show();
        }
        switch (keyCode) {
            case KeyEvent.KEYCODE_DPAD_LEFT:
                downvol();
                break;
            case KeyEvent.KEYCODE_DPAD_RIGHT:
                upvol();
                break;
            case KeyEvent.KEYCODE_DPAD_DOWN:
                upchanle();
                break;
            case KeyEvent.KEYCODE_DPAD_UP:
                downchanle();
                break;
            case KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE:
                pause();
                break;
            case KeyEvent.KEYCODE_BACK:
                finish();
                break;
            default:
                break;
        }
        handler.removeMessages(HideLiveList);
        handler.sendEmptyMessageDelayed(HideLiveList, 10 * 1000);
        return super.onKeyDown(keyCode, event);
    }


    @Override
    public void onCompletion(MediaPlayer mediaPlayer) {
        mediaPlayer.setLooping(true);
    }

    @Override
    public boolean onError(MediaPlayer mediaPlayer, int i, int i1) {
        Toas toas = new Toas();
        toas.setMsg(getString(R.string.play_error));
        toas.show(LiveActivity.this);
        toas = null;
        return true;
    }

    @Override
    public void onPrepared(MediaPlayer mediaPlayer) {
        mediaPlayer.start();
    }

    private void savechanle() {
        // TODO Auto-generated method stub
        try {
            live_share.edit().putInt("no", historyno).commit();
            if (live_player.isPlaying()) {
                live_player.stopPlayback();
            }
        } catch (Exception e) {
            // TODO: handle exception
        }

    }

    @Override
    protected void onStop() {
        // TODO Auto-generated method stub
        savechanle();
        super.onStop();
    }

    private void showinfo() {
        handler.post(new Runnable() {
            @Override
            public void run() {
                live_tips.setVisibility(View.VISIBLE);
                live_no_s.setText(historyno + 1 + "");
                live_no_name.setText(livelist.get(historyno).getName());

                live_no_b.setText(historyno + 1 + "");
                handler.removeMessages(HideLiveInfo);
                handler.sendEmptyMessageDelayed(HideLiveInfo, 5 * 1000);
            }
        });

    }

    private void upchanle() {
        // TODO Auto-generated method stub
        try {
            if (historyno < livelist.size() - 1) {
                historyno += 1;
            } else {
                historyno = 0;
            }
            play();
            showinfo();

        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
    }

    private void downchanle() {
        // TODO Auto-generated method stub
        try {
            if (historyno > 0) {
                historyno -= 1;
            } else {
                historyno = livelist.size() - 1;
            }
            play();
            showinfo();
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
    }

    private void upvol() {
        // TODO Auto-generated method stub
        audioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC,
                audioManager.ADJUST_RAISE, AudioManager.FLAG_PLAY_SOUND
                        | AudioManager.FLAG_SHOW_UI);
    }

    private void downvol() {
        // TODO Auto-generated method stub
        audioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC,
                audioManager.ADJUST_LOWER, AudioManager.FLAG_PLAY_SOUND
                        | AudioManager.FLAG_SHOW_UI);
    }

    private void pause() {
        // TODO Auto-generated method stub
        try {
            if (live_player.isPlaying()) {
                live_player.pause();
            } else {
                live_player.start();
            }
        } catch (Exception e) {
            // TODO: handle exception

        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            try {

                if (popupWindow != null) {

                    System.out.println(popupWindow.isShowing());

                    if (popupWindow.isShowing()) {
                        handler.removeMessages(HideLiveList);
                        handler.sendEmptyMessageDelayed(HideLiveList, 10 * 1000);
                    } else {
                        show();
                    }

                } else {
                    show();
                }


            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return super.onTouchEvent(event);
    }
}
