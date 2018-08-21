package com.spa.adaters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.spa.R;
import com.spa.bean.TeachType;
import com.spa.bean.TechTypes;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MyExAdater extends BaseExpandableListAdapter {

    public List<TeachType> userBeans;
    public List<List> itemList;
    public Context context;
    public int userLayout;
    public int itemLayout;

    public MyExAdater(List<TeachType> userBeans, List<List> itemList, Context context, int userLayout, int itemLayout) {
        this.userBeans = userBeans;
        this.itemList = itemList;
        this.context = context;
        this.userLayout = userLayout;
        this.itemLayout = itemLayout;
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        List<TechTypes> itemBeans = itemList.get(groupPosition);
        return itemBeans.get(childPosition);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    /**
     * 读取子级数据条数
     *
     * @param groupPosition
     * @return
     */
    @Override
    public int getChildrenCount(int groupPosition) {
        List<TechTypes> itemBeans = itemList.get(groupPosition);
        return itemBeans.size();
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        View view;
        if (convertView == null) {
            view = LayoutInflater.from(context).inflate(itemLayout, parent, false);
        } else {
            view = convertView;
        }
        TechTypes itemBean = (TechTypes) getChild(groupPosition, childPosition);

        ImageView imageView = view.findViewById(R.id.icon);
        TextView textView = view.findViewById(R.id.intro_name);

        Picasso.with(context).load(itemBean.getIcon()).into(imageView);
        textView.setText(itemBean.getName());
        return view;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return userBeans.get(groupPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    /**
     * 读取父级数据条数
     *
     * @return
     */
    @Override
    public int getGroupCount() {
        return userBeans.size();
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        View view;
        if (convertView == null) {
            view = LayoutInflater.from(context).inflate(userLayout, parent, false);
        } else {
            view = convertView;
        }
        TeachType userBean = (TeachType) getGroup(groupPosition);

        ImageView imageView = view.findViewById(R.id.icon);
        TextView textView = view.findViewById(R.id.intro_name);

        Picasso.with(context).load(userBean.getIcon()).into(imageView);
        textView.setText(userBean.getName());
        return view;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    @Override
    public boolean areAllItemsEnabled() {
        return false;
    }
}

