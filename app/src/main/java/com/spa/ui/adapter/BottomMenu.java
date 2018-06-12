package com.spa.ui.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.spa.R;

import org.w3c.dom.Text;

import de.greenrobot.event.EventBus;

public class BottomMenu extends RecyclerView.Adapter<BottomMenu.ViewHolder> implements View.OnClickListener {
    int[] icon = {R.drawable.bottom_menu1, R.drawable.bottom_menu2, R.drawable.bottom_menu3, R.drawable.bottom_menu4, R.drawable.bottom_menu5,
            R.drawable.bottom_menu6, R.drawable.bottom_menu7, R.drawable.bottom_menu8, R.drawable.bottom_menu9, R.drawable.bottom_menu10};
    int[] name = {R.string.bottom_menu1, R.string.bottom_menu2, R.string.bottom_menu3, R.string.bottom_menu4, R.string.bottom_menu5,
            R.string.bottom_menu6, R.string.bottom_menu7, R.string.bottom_menu8, R.string.bottom_menu9, R.string.bottom_menu10,};
    Context context;

    View view;

    public class ViewHolder extends RecyclerView.ViewHolder {
        //        ImageView menu_icon;
//        TextView menu_name;
        Button botton_menu;

        public ViewHolder(View v) {
            super(v);
            view = v;
            botton_menu = v.findViewById(R.id.botton_menu);
//            menu_icon = v.findViewById(R.id.menu_icon);
//            menu_name = v.findViewById(R.id.menu_name);
//
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    System.out.println("--------------");
                }
            });
            botton_menu.setOnClickListener(BottomMenu.this);
//            menu_icon.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//                @Override
//                public void onFocusChange(View v, boolean hasFocus) {
//                    if (hasFocus) {
////                        System.out.println(menu_name.getText().toString());
//                        menu_name.setTextColor(context.getResources().getColor(R.color.white));
//                    } else {
////                        System.out.println(menu_name.getText().toString());
//                        menu_name.setTextColor(context.getResources().getColor(R.color.menu));
//                    }
//
//                }
//            });


        }
    }

    public BottomMenu(Context context) {
        this.context = context;
    }

    private OnItemClickListener mOnItemClickListener = null;


    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    @Override
    public void onClick(View v) {
        try {
//            System.out.println(v.get);
        } catch (Exception e) {
            e.printStackTrace();
        }

//        if (mOnItemClickListener != null) {
//            mOnItemClickListener.onItemClick(v, (int) v.getTag());
//        }
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }


    @Override
    public BottomMenu.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_bottom_menu, parent, false);
        ViewHolder vh = new ViewHolder(v);

        return vh;
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        try {
//            holder.menu_icon.setImageResource(icon[position]);
//            holder.menu_name.setText(name[position]);
            holder.botton_menu.setText(name[position]);

            Drawable drawable = context.getResources().getDrawable(icon[position]);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());

            holder.botton_menu.setCompoundDrawables(null, drawable, null, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public int getItemCount() {
        return name.length;
//        return 0;
    }
}
