package com.spa.ui.game;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.spa.R;
import com.spa.app.App;
import com.spa.app.Req;
import com.spa.event.DataMessage;
import com.spa.ui.BaseActivity;
import com.spa.ui.adapter.GameAdapter;
import com.spa.ui.adapter.MainMenuAdapter;
import com.spa.bean.AJson;
import com.spa.bean.Game;

import java.util.ArrayList;
import java.util.List;

public class GameActivity extends BaseActivity implements AdapterView.OnItemClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        find();
        init();

    }

    private void init() {
        Req.get(Req.game);
    }

    private GridView game;

    private void find() {
        game = findViewById(R.id.game);
        game.setOnItemClickListener(this);
    }

    private List<Game> games;
    private GameAdapter adapter;

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
                            adapter = new GameAdapter(GameActivity.this, games);
                            game.setAdapter(adapter);
                        }
                    });
                }

            }
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
        }
    }

    Handler handler = new Handler() {
    };


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (App.isInstall(GameActivity.this, games.get(position).getPackage_name())) {

        } else {

        }
    }
}
