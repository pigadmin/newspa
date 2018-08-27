package com.spa.ui.video;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.reflect.TypeToken;
import com.spa.R;
import com.spa.adaters.MusicTypeAdapter;
import com.spa.app.App;
import com.spa.app.Req;
import com.spa.bean.AJson;
import com.spa.bean.Video;
import com.spa.bean.VideoData;
import com.spa.bean.VideoType;
import com.spa.event.DataMessage;
import com.spa.tools.Logger;
import com.spa.ui.BaseFr;
import com.spa.ui.adapter.TeleplayGridAdapter;
import com.spa.ui.adapter.VideoAdapter;
import com.spa.ui.adapter.VideoTypeAdapter;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Random;

import de.greenrobot.event.EventBus;

/**
 * 点播资源Fragment
 */
public class VideoFr extends BaseFr implements AdapterView.OnItemClickListener {

    private final static String TAG = "VideoFr";

    private View view;
    private Activity activity;
    private App app;

    private ListView mListView;//音乐listview
    private MusicTypeAdapter typeAdapter;

    private LinearLayout layout;
    private ImageView mImageSrc;//音乐图片

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_video, container, false);
        activity = getActivity();
        app = (App) activity.getApplication();
        EventBus.getDefault().register(this);
        find();
        init();
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
        stopMusic();
        player.release();
        isState = false;
    }

    private void init() {
        Req.get(Req.videotype);
    }

    private ListView left_list;
    private GridView right_grid;
    private EditText video_keyword;
    private TextView music_size;

    private boolean firstPlay = true;

    private void find() {
        setMediaListene();
        mImageSrc = view.findViewById(R.id.music_src);
        layout = view.findViewById(R.id.llt);
        music_size = view.findViewById(R.id.music_size);


        mListView = view.findViewById(R.id.listview_lvw);
        left_list = view.findViewById(R.id.left_list);
        left_list.setOnItemClickListener(this);
        right_grid = view.findViewById(R.id.right_grid);
        right_grid.setOnItemClickListener(this);
        video_keyword = view.findViewById(R.id.video_keyword);
        video_keyword.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                handler.removeMessages(search);
                handler.sendEmptyMessageDelayed(search, 1000);
            }
        });


        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                musicposition = position;
                changemusicinfo();
                playerSong();

                setPostion(position);
            }
        });
        last = view.findViewById(R.id.last);
        last.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if (--musicposition > 0) {
                        playerSong();
                    } else {
                        musicposition = 0;
                        playerSong();
                    }

                    setPostion(musicposition);

                } catch (IllegalStateException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        playpause = view.findViewById(R.id.playpause);
        playpause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (firstPlay) {
                    firstPlay = false;
                    playpause.setImageResource(R.drawable.select_content_icon2);
                    playerSong();
                } else {
                    pauseMusic();
                }
            }
        });
        next = view.findViewById(R.id.next);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if (++musicposition < grid.getData().size()) {
                        playerSong();
                    } else {
                        musicposition = grid.getData().size() - 1;
                        playerSong();
                    }

                    setPostion(musicposition);

                } catch (IllegalStateException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        sj_play = view.findViewById(R.id.sj_play);
        sj_play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                playmodel = 1;
                Toast.makeText(activity, "切换至随机播放模式", Toast.LENGTH_LONG).show();
            }
        });
        sx_play = view.findViewById(R.id.sx_play);
        sx_play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                playmodel = 0;
                Toast.makeText(activity, "切换至顺序播放模式", Toast.LENGTH_LONG).show();
            }
        });
        xh_play = view.findViewById(R.id.xh_play);
        xh_play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                playmodel = 3;
                Toast.makeText(activity, "切换至循坏播放模式", Toast.LENGTH_LONG).show();
            }
        });
        audio = view.findViewById(R.id.audio);
        audio.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                try {
                    player.seekTo(seekBar.getProgress());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        startTime = view.findViewById(R.id.startTime);
        endTime = view.findViewById(R.id.endTime);
    }

    private void setPostion(int position) {
        typeAdapter.setCurrentItem(position);
        typeAdapter.notifyDataSetChanged();
    }


    private ImageView last, playpause, next;
    private ImageView sj_play, sx_play, xh_play;
    private SeekBar audio;
    private TextView startTime, endTime;

    private int musicposition = 0;

    private void changemusicinfo() {
        try {
            Picasso.with(activity).load(Uri.parse(grid.getData().get(musicposition).getIcon())).into(mImageSrc);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private final int search = 0;
    private String keywd;
    private final int UPDATESEEK = 1;
    private final int UPDATESEL = 2;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            try {
                switch (msg.what) {
                    case 0:
                        keywd = "&keywd=" + video_keyword.getText().toString();
                        Req.get(Req.video + keywd);

                        break;
                    case UPDATESEEK:
                        audio.setProgress(player.getCurrentPosition());
                        startTime.setText(getTimeStr(player.getCurrentPosition()));
                        handler.sendEmptyMessageDelayed(UPDATESEEK, 1000);
                        break;
                    case UPDATESEL:
//                        typeAdapter.sel(musicposition);
                        mListView.setSelection(musicposition);
                        break;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    // 获取歌曲时间
    private String getTimeStr(int time) {
        return new SimpleDateFormat("mm:ss").format(new Date(time));
    }

    private List<VideoType> list;
    private VideoTypeAdapter adapter;

    private Video grid;

    public void onEvent(DataMessage event) {
        try {
            if (event.getApi().equals(Req.videotype)) {
                AJson<List<VideoType>> data = App.gson.fromJson(
                        event.getData(), new TypeToken<AJson<List<VideoType>>>() {
                        }.getType());
                list = data.getData();
                if (!list.isEmpty()) {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            resetList();
                        }
                    });
                }

            } else if (event.getApi().equals(Req.video + type)) {
                AJson<Video> data = App.gson.fromJson(
                        event.getData(), new TypeToken<AJson<Video>>() {
                        }.getType());
                grid = data.getData();
//                if (!grid.getData().isEmpty()) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        resetUI();
                    }
                });
//                }

            } else if (event.getApi().equals(Req.video + keywd)) {
                AJson<Video> data = App.gson.fromJson(
                        event.getData(), new TypeToken<AJson<Video>>() {
                        }.getType());
                grid = data.getData();
//                if (!grid.getData().isEmpty()) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        resetUI();
                    }
                });
//                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String type;

    private void resetList() {
        adapter = new VideoTypeAdapter(activity, list, left_list);
        left_list.setAdapter(adapter);

        type = "&type=" + list.get(0).getId();
        Req.get(Req.video + type);
    }

    private VideoAdapter adapter2;

    private void resetUI() {
        if (isState) {
            musicposition = 0;
            typeAdapter = new MusicTypeAdapter(activity, R.layout.item_music_layout, grid.getData());
            mListView.setAdapter(typeAdapter);

            typeAdapter.notifyDataSetChanged();
            right_grid.setVisibility(View.GONE);
            mListView.setVisibility(View.VISIBLE);
            layout.setVisibility(View.VISIBLE);
            music_size.setText("播放列表(" + grid.getData().size() + ")");
            changemusicinfo();
        } else {
            adapter2 = new VideoAdapter(activity, grid.getData());
            right_grid.setAdapter(adapter2);
            adapter2.notifyDataSetChanged();
            mListView.setVisibility(View.GONE);
            right_grid.setVisibility(View.VISIBLE);
            layout.setVisibility(View.GONE);
        }
    }

    VideoData videoData;

    boolean isState = false;

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        try {
            final int p = position;
//        isState = list.get(p).getId() == 26 ? true : false;

            if (parent == left_list) {
                isState = list.get(p).getName().equals("音乐") ? true : false;
                type = "&type=" + list.get(p).getId();
                Req.get(Req.video + type);

                adapter.notifyDataSetChanged();

            } else if (parent == right_grid) {
                videoData = grid.getData().get(p);
                if (videoData.getDetails().size() > 1) {
                    teledialog();
                } else {
                    play(0);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static AlertDialog teleplay_dialog;
    GridView teleplay_grid;
    TeleplayGridAdapter teleplayadapter;

    public void teledialog() {
        teleplay_dialog = new AlertDialog.Builder(activity).create();
        if (teleplay_dialog.isShowing()) {
            teleplay_dialog.dismiss();
        } else {
            teleplay_dialog.show();
        }

        teleplay_dialog.setContentView(R.layout.dialog_teleplay);

        teleplay_grid = teleplay_dialog.findViewById(R.id.teleplay_grid);
        teleplayadapter = new TeleplayGridAdapter(getActivity(), videoData.getDetails());
        teleplay_grid.setAdapter(teleplayadapter);
        teleplay_grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                play(i);
            }
        });
    }

    private void play(int position) {
        try {
            String path = videoData.getDetails().get(position).getFilePath();

            Intent intent = new Intent();
            Bundle bundle = new Bundle();
            bundle.putSerializable("key", videoData.getDetails().get(position));
            intent.setClass(activity, PlayerActivity.class);
            startActivity(intent.putExtras(bundle));
        } catch (Exception e) {
//            AppTool.toast(context, getString(R.string.res_error), 0, Gravity.CENTER, 0, 0);
            e.printStackTrace();
        }
    }

    private MediaPlayer player;


    private void setMediaListene() {
        player = new MediaPlayer();
        player.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.start();
                try {
                    if (isState) {
                        System.out.println("@@@@@@当前" + musicposition);
                        handler.sendEmptyMessage(UPDATESEL);
                        audio.setMax(mp.getDuration());
                        endTime.setText(getTimeStr(mp.getDuration()));
                        handler.sendEmptyMessage(UPDATESEEK);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                try {
                    if (playmodel == 0) {
                        //顺序
                        if (++musicposition < grid.getData().size()) {
                            try {
                                playerSong();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else {
                            musicposition = 0;
                            playerSong();
                        }
                        Logger.d(TAG, "顺序" + musicposition);
                    } else if (playmodel == 1) {
                        //随机
                        musicposition = getRandom();
                        playerSong();
                        Logger.d(TAG, "随机" + musicposition);
                    } else {
                        //循坏
                        playerSong();
                        Logger.d(TAG, "循坏" + musicposition);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                setPostion(musicposition);
            }
        });

        player.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mediaPlayer, int i, int i1) {
                return true;
            }
        });
    }

    public int getRandom() {
        if (grid.getData().size() == 1) {
            return 0;
        } else {
            Random random = new Random();
            int s = random.nextInt(grid.getData().size() - 1) % (grid.getData().size() - 1 - 0 + 1) + 0;
            return s;
        }
    }

    int playmodel = 0;
    // 暂停播放

    private void pauseMusic() {
        if (player.isPlaying()) {
            player.pause();
            playpause.setImageResource(R.drawable.select_content_icon4);
        } else {
            player.start();
            playpause.setImageResource(R.drawable.select_content_icon2);
        }
    }

    // 播放哪一首歌
    private void playerSong() {
        try {
            player.stop();
            player.reset();
            System.out.println(grid.getData().get(musicposition).getDetails().get(0).getFilePath());
            player.setDataSource(activity,
                    Uri.parse(grid.getData().get(musicposition).getDetails().get(0).getFilePath()));
            player.prepareAsync();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }    // 停止播放

    private void stopMusic() {
        player.stop();
    }
}
