package com.spa.ui.live;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.BitmapDrawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.VideoView;

import com.spa.R;
import com.spa.bean.LiveIP;
import com.spa.bean.LiveTypeIP;
import com.spa.tools.FULL;
import com.spa.ui.BaseActivity;
import com.spa.ui.adapter.LiveListAdapter;
import com.spa.ui.diy.Toas;
import com.spa.views.BtmDialog;

import java.util.ArrayList;
import java.util.List;


public class LiveActivity extends BaseActivity implements MediaPlayer.OnErrorListener, MediaPlayer.OnPreparedListener, MediaPlayer.OnCompletionListener {

    private static final String TAG = "LiveActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live);
        find();
        init();
    }

    TextView live_no_b;
    VideoView live_player;

    private void find() {
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
            e.printStackTrace();
        }
    }

    private void play() {
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
            e.printStackTrace();
        }
    }


    private ListView live_list;
    private TextView live_count;
    private PopupWindow popupWindow;
    private View view;

    //
    private void show() {
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
                        cutno = "";
                    }
                    break;
            }
        }
    };
    String cutno = "";


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
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
        showDialogStyle0();
        return true;
    }

    @Override
    public void onPrepared(MediaPlayer mediaPlayer) {
        mediaPlayer.start();
    }

    private void savechanle() {
        try {
            live_share.edit().putInt("no", historyno).commit();
            if (live_player.isPlaying()) {
                live_player.stopPlayback();
            }
        } catch (Exception e) {
        }
    }

    @Override
    protected void onStop() {
        savechanle();
        super.onStop();
    }

    private void showinfo() {
        handler.post(new Runnable() {
            @Override
            public void run() {
                live_no_b.setText(historyno + 1 + "");
                handler.removeMessages(HideLiveInfo);
                handler.sendEmptyMessageDelayed(HideLiveInfo, 5 * 1000);
            }
        });
    }

    private void upchanle() {
        try {
            if (historyno < livelist.size() - 1) {
                historyno += 1;
            } else {
                historyno = 0;
            }
            play();
            showinfo();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void downchanle() {
        try {
            if (historyno > 0) {
                historyno -= 1;
            } else {
                historyno = livelist.size() - 1;
            }
            play();
            showinfo();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void upvol() {
        audioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC,
                audioManager.ADJUST_RAISE, AudioManager.FLAG_PLAY_SOUND
                        | AudioManager.FLAG_SHOW_UI);
    }

    private void downvol() {
        audioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC,
                audioManager.ADJUST_LOWER, AudioManager.FLAG_PLAY_SOUND
                        | AudioManager.FLAG_SHOW_UI);
    }

    private void pause() {
        try {
            if (live_player.isPlaying()) {
                live_player.pause();
            } else {
                live_player.start();
            }
        } catch (Exception e) {
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

    private void showDialogStyle0() {
        final BtmDialog dialog = new BtmDialog(LiveActivity.this, R.layout.custom_alertdiaog);
        TextView cancel = dialog.findViewById(R.id.cancel);
        TextView confirm = dialog.findViewById(R.id.confirm);
        TextView mTitle = dialog.findViewById(R.id.title);
        TextView mEssage = dialog.findViewById(R.id.message);
        mTitle.setText("温馨提示");
        mEssage.setText("播放异常");
        cancel.setVisibility(View.GONE);
        confirm.setBackgroundResource(R.drawable.selector_back3_shape);
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                dialog.dismiss();
            }
        });
        dialog.setCancelable(false);
        dialog.show();
    }
}
