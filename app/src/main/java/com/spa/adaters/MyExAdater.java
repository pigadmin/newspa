package com.spa.adaters;

import android.app.Activity;
import android.content.Context;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;

import com.spa.R;
import com.spa.bean.TeachType;
import com.spa.bean.TechTypes;
import com.spa.tools.Logger;
import com.spa.views.AutoScrollTextView;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MyExAdater extends BaseExpandableListAdapter {

    private final static String TAG = "MyExAdater";

    public List<TeachType> userBeans;
    public List<List> itemList;
    public Context context;
    public int userLayout;
    public int itemLayout;
    public ExpandableListView left_list;
    public SparseBooleanArray selected;
    public int old = -1;
    public int parentPosition = -1;
    public boolean isState;
    public Activity activity;

    public AutoScrollTextView intro_name_Group;

    public MyExAdater(List<TeachType> userBeans, List<List> itemList, Context context, int userLayout, int itemLayout, ExpandableListView left_list, Activity activity) {
        this.userBeans = userBeans;
        this.itemList = itemList;
        this.context = context;
        this.userLayout = userLayout;
        this.itemLayout = itemLayout;
        this.left_list = left_list;
        this.activity = activity;
        selected = new SparseBooleanArray();
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
        ViewHolderChild holder = null;
        if (convertView == null) {
            holder = new ViewHolderChild();
            convertView = LayoutInflater.from(context).inflate(itemLayout, parent, false);
            holder.intro_name_Child = convertView
                    .findViewById(R.id.intro_name);
            holder.icon_Child = convertView
                    .findViewById(R.id.icon);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolderChild) convertView.getTag();
        }

        TechTypes itemBean = (TechTypes) getChild(groupPosition, childPosition);
        Picasso.with(context).load(itemBean.getIcon()).into(holder.icon_Child);
        holder.intro_name_Child.setText(itemBean.getName());

        if (isState) {
            selected.clear();
            isState = false;
        }

        //重点代码
        if (selected.get(childPosition) && this.parentPosition == groupPosition) {
            convertView.setBackgroundResource(R.mipmap.youce_k_1);
        } else {
            convertView.setBackgroundResource(R.color.transparent);
        }

        return convertView;
    }

    /**
     * 刷新子级状态
     *
     * @param groupPosition
     * @param selected
     */
    public void setSelectedItem(int groupPosition, int selected) {
        this.parentPosition = groupPosition;
        if (old != -1) {
            this.selected.put(old, false);
        }
        this.selected.put(selected, true);
        old = selected;
    }

    /**
     * 点击父级Item,更新子级状态
     *
     * @param isState
     */
    public void setSelecte(boolean isState) {
        this.isState = isState;
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
        ViewHolderGroup holder = null;
        if (convertView == null) {
            holder = new ViewHolderGroup();
            convertView = LayoutInflater.from(context).inflate(userLayout, parent, false);
            holder.intro_name_Group = convertView
                    .findViewById(R.id.intro_name);
            holder.icon_Group = convertView
                    .findViewById(R.id.icon);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolderGroup) convertView.getTag();
        }
        TeachType userBean = (TeachType) getGroup(groupPosition);
        Picasso.with(context).load(userBean.getIcon()).into(holder.icon_Group);

        holder.intro_name_Group.setText(userBean.getName());
        holder.intro_name_Group.init(activity.getWindowManager(), 300);
        if (holder.intro_name_Group.getText().toString().trim().length() > 4) {
            holder.intro_name_Group.startScroll();
        } else {
            holder.intro_name_Group.stopScroll();
        }

        intro_name_Group = holder.intro_name_Group;

//        Logger.d(TAG, "." + holder.intro_name_Group.getText().toString());
//        Logger.d(TAG, ".." + holder.intro_name_Group.isStarting);

        if (isExpanded) {
            // 条目展开，设置向下的箭头
            convertView.setBackgroundResource(R.mipmap.polygon_4_79);
        } else {
            // 条目未展开，设置向上的箭头
            convertView.setBackgroundResource(R.mipmap.polygon_3_29);
        }

        return convertView;
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

    public class ViewHolderGroup {
        public AutoScrollTextView intro_name_Group;
        private ImageView icon_Group;
    }

    public class ViewHolderChild {
        private TextView intro_name_Child;
        private ImageView icon_Child;
    }
}