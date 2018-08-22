package com.spa.ui.game;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.spa.R;
import com.spa.app.App;
import com.spa.app.Req;
import com.spa.bean.AJson;
import com.spa.bean.Game;
import com.spa.event.DataMessage;
import com.spa.tools.WebInstaller;
import com.spa.ui.BaseFr;
import com.spa.ui.adapter.Game2dapter;
import com.spa.ui.adapter.GameAdapter;

import java.util.List;

import de.greenrobot.event.EventBus;

public class GameFr extends BaseFr implements Game2dapter.OnItemClickListener {

    private View view;
    private Activity activity;
    private App app;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        view = inflater.inflate(R.layout.activity_game, container, false);
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
    }


    private void init() {
        Req.get(Req.game);
    }

    private GridView game;
    RecyclerView mainrecyle;
    StaggeredGridLayoutManager layoutManager;

    private void find() {
//        game = view.findViewById(R.id.game);
//        game.setOnItemClickListener(this);
        mainrecyle = view.findViewById(R.id.mainrecyle);
        layoutManager = new StaggeredGridLayoutManager(2, LinearLayoutManager.HORIZONTAL);
        mainrecyle.setLayoutManager(layoutManager);


    }

    private List<Game> games;
    private GameAdapter adapter;
    Game2dapter adapter2;

    public void onEvent(DataMessage event) {
        try {
            if (event.getApi().equals(Req.game)) {
                AJson<List<Game>> data = App.gson.fromJson(
                        event.getData(), new TypeToken<AJson<List<Game>>>() {
                        }.getType());
                games = data.getData();
                if (!games.isEmpty()) {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
//                            adapter = new GameAdapter(activity, games);
//                            game.setAdapter(adapter);
                            adapter2 = new Game2dapter(activity, games);
                            adapter2.setOnItemClickListener(GameFr.this);
                            mainrecyle.setAdapter(adapter2);
                        }
                    });
                }

            }
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
        }
    }

    Handler handler = new Handler();


//    @Override
//    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//        if (App.isInstall(activity, games.get(position).getPackage_name())) {
//            App.startApk(activity, games.get(position).getPackage_name(), "");
//        } else {
//            new WebInstaller(activity, games.get(position).getPath())
//                    .downloadAndInstall(activity
//                            .getString(R.string.Downloading_game));
//        }
//
//    }

    @Override
    public void onItemClick(View view, int position) {
        if (App.isInstall(activity, games.get(position).getPackage_name())) {
            App.startApk(activity, games.get(position).getPackage_name(), "");
        } else {
            new WebInstaller(activity, games.get(position).getPath())
                    .downloadAndInstall(activity
                            .getString(R.string.Downloading_game));
        }
    }
}
