package com.spa.ui.adapter;

import android.content.Context;
import android.graphics.Picture;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.spa.R;
import com.spa.app.Req;
import com.spa.event.BitmapMessage;
import com.spa.event.DataMessage;
import com.spa.bean.Menu;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.List;

/**
 * Created by zhu on 2017/9/26.
 */

public class MainMenuAdapter extends RecyclerView.Adapter<MainMenuAdapter.ViewHolder> implements View.OnClickListener {
    private List<Menu> menus;
    Context context;

    public class ViewHolder extends RecyclerView.ViewHolder {
        Button main_menu;

        public ViewHolder(View v) {
            super(v);
            main_menu = v.findViewById(R.id.main_menu);
            v.setOnClickListener(MainMenuAdapter.this);
        }
    }

    public MainMenuAdapter(Context context, List<Menu> menus) {
        this.context = context;
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
    public MainMenuAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_main, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    ViewHolder holder;

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        try {
            this.holder = holder;
            holder.main_menu.setText(menus.get(position).getName());

            if (position % 3 == 0) {
                holder.main_menu.setBackgroundResource(R.drawable.main_menu_bg1);
//                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(450, 220);
//
//                lp.setMargins(0, 10, 10, 0);
//                holder.main_menu.setLayoutParams(lp);
            } else {
                holder.main_menu.setBackgroundResource(R.drawable.main_menu_bg2);
//                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(220, 220);
//                lp.setMargins(0, 10, 10, 0);
//                holder.main_menu.setLayoutParams(lp);
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    Handler handler = new Handler();


    @Override
    public int getItemCount() {
        return menus.size();
    }
}
