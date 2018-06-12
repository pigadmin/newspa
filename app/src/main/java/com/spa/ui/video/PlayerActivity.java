package com.spa.ui.video;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import com.spa.R;
import com.spa.app.App;
import com.spa.bean.VideoDetails;
import com.spa.tools.FULL;
import com.spa.ui.BaseActivity;
import com.spa.ui.diy.Toas;
import com.spa.ui.live.LiveActivity;

public class PlayerActivity extends BaseActivity implements MediaPlayer.OnPreparedListener,
        MediaPlayer.OnErrorListener, MediaPlayer.OnCompletionListener {
    App app;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_player);

        app = (App) getApplication();

        find();

        init();
    }

    VideoDetails videoDetails;

    private void init() {
        // TODO Auto-generated method stub
        try {
            videoDetails = (VideoDetails) getIntent().getExtras().getSerializable("key");
            video_title.setText(videoDetails.getName());

            vodtime = tvchanle.getInt("resvideo" + videoDetails.getId(), -1);

            if (vodtime / 1000 <= 0) {
                video_player.setVideoPath(videoDetails.getFilePath());

//                Req.get(Req.vrecord+"&vid="+videoDetails.getId());
            } else {
                crt(vodtime);
            }


        } catch (Exception e) {
            // TODO: handle exception
        }

    }


    VideoView video_player;
    TextView video_title;
    private SharedPreferences tvchanle;

    private void find() {
        // TODO Auto-generated method stub
        video_player = (VideoView) findViewById(R.id.video_player);
        FULL.star(video_player);

        MediaController controller = new MediaController(this);
        video_player.setMediaController(controller);


        video_title = (TextView) findViewById(R.id.video_title);

        video_player.setOnPreparedListener(this);
        video_player.setOnCompletionListener(this);
        video_player.setOnErrorListener(this);

        tvchanle = getSharedPreferences("resvideo", Context.MODE_PRIVATE);
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        // TODO Auto-generated method stub
        finish();
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        // TODO Auto-generated method stub
        Toas toas = new Toas();
        toas.setMsg(getString(R.string.play_error));
        toas.show(PlayerActivity.this);
        toas = null;
        return true;
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        // TODO Auto-generated method stub
        mp.start();
    }

    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        setdown();
        super.onPause();
    }

    private void setdown() {
        // TODO Auto-generated method stub
        System.out.println("getDuration:" + video_player.getDuration());

        System.out.println("getCurrentPosition:" + video_player.getCurrentPosition());

        if (video_player.getCurrentPosition() > 0
                && video_player.getCurrentPosition() < video_player.getDuration()) {
            tvchanle.edit()
                    .putInt("resvideo" + video_player.getId(),
                            video_player.getCurrentPosition()).commit();
        } else {
            tvchanle.edit().remove("resvideo" + video_player.getId()).commit();
        }
    }

    AlertDialog vod_time_dialog;
    int vodtime = 0;

    public void crt(final int vodtime) {
        // TODO Auto-generated method stub

        vod_time_dialog = new AlertDialog.Builder(this).create();
        // update_dialog.setCancelable(false);
        if (vod_time_dialog != null && vod_time_dialog.isShowing()) {
            vod_time_dialog.dismiss();
        } else {
            vod_time_dialog.show();
        }
        vod_time_dialog.setContentView(R.layout.vod_time_dialog);

        TextView update_content = (TextView) vod_time_dialog
                .findViewById(R.id.update_content);

        update_content.setText(getString(R.string.video_time).replace("x", getTimeStr(vodtime)));

        ImageButton update_ok = vod_time_dialog.findViewById(R.id.update_ok);

        update_ok.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                video_player.setVideoPath(videoDetails.getFilePath());
                video_player.seekTo(vodtime);
//                Req.get(Req.vrecord+"&vid="+videoDetails.getId());
                vod_time_dialog.dismiss();
            }
        });
        ImageButton update_cancle = vod_time_dialog
                .findViewById(R.id.update_cancle);
        update_cancle.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                vod_time_dialog.cancel();
                if (!video_player.isPlaying()) {
                    video_player.setVideoPath(videoDetails.getFilePath());
//                    Req.get(Req.vrecord+"&vid="+videoDetails.getId());
                }
            }
        });

        vod_time_dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {

            @Override
            public void onCancel(DialogInterface dialog) {
                // TODO Auto-generated method stub
                if (!video_player.isPlaying()) {
                    video_player.setVideoPath(videoDetails.getFilePath());
                }
            }
        });
    }

    private String getTimeStr(int d) {
        // TODO Auto-generated method stub

        int hour = 0;
        int minute = 0;
        int second = 0;

        second = d / 1000;

        if (second > 60) {
            minute = second / 60;
            second = second % 60;
        }
        if (minute > 60) {
            hour = minute / 60;
            minute = minute % 60;
        }
        return (getTwoLength(hour) + ":" + getTwoLength(minute) + ":" + getTwoLength(second));
    }

    private static String getTwoLength(final int data) {
        if (data < 10) {
            return "0" + data;
        } else {
            return "" + data;
        }
    }

}