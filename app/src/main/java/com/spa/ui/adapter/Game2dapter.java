package com.spa.ui.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.spa.R;
import com.spa.bean.Game;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by zhu on 2017/9/26.
 */

public class Game2dapter extends RecyclerView.Adapter<Game2dapter.ViewHolder> implements View.OnClickListener {
    List<Game> menus;
    Context context;

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView game_bg;
        TextView game_name;
        FrameLayout menu_linearlayout;

        public ViewHolder(View v) {
            super(v);
            menu_linearlayout = v.findViewById(R.id.menu_linearlayout);
            game_bg = v.findViewById(R.id.game_bg);
            game_name = v.findViewById(R.id.game_name);
            v.setOnClickListener(Game2dapter.this);
        }
    }

    public Game2dapter(Activity activity, List<Game> menus) {
        this.context = activity;
        this.menus = menus;
    }

    private OnItemClickListener mOnItemClickListener = null;


    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    @Override
    public void onClick(View v) {
        if (mOnItemClickListener != null) {
            mOnItemClickListener.onItemClick(v, (int) v.getTag());
        }
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    @Override
    public Game2dapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_game, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        try {
            holder.game_name.setText(menus.get(position).getName());
            holder.itemView.setTag(position);
            Picasso.with(context).load(menus.get(position).getIcon()).into(holder.game_bg);
            if (position == 0 || position == 1) {
                StaggeredGridLayoutManager.LayoutParams lp = new StaggeredGridLayoutManager.LayoutParams(387,  RecyclerView.LayoutParams.MATCH_PARENT);
                lp.setMargins(5, 5, 5, 5);
                lp.setFullSpan(true);
                holder.menu_linearlayout.setLayoutParams(lp);
            } else if (position == 2) {
                StaggeredGridLayoutManager.LayoutParams lp = new StaggeredGridLayoutManager.LayoutParams(600,  RecyclerView.LayoutParams.MATCH_PARENT);
                lp.setMargins(5, 5, 5, 5);
                holder.menu_linearlayout.setLayoutParams(lp);
            }else if (position == 3|| position == 4) {
                StaggeredGridLayoutManager.LayoutParams lp = new StaggeredGridLayoutManager.LayoutParams(300,  RecyclerView.LayoutParams.MATCH_PARENT);
                lp.setMargins(5, 5, 5, 5);
                holder.menu_linearlayout.setLayoutParams(lp);
            } else {
                StaggeredGridLayoutManager.LayoutParams lp = new StaggeredGridLayoutManager.LayoutParams(300, RecyclerView.LayoutParams.MATCH_PARENT);
                lp.setMargins(5, 5, 5, 5);
                holder.menu_linearlayout.setLayoutParams(lp);
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public int getItemCount() {
        return menus.size();
    }
}
