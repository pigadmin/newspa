package com.spa.bean;


import java.io.Serializable;
import java.util.List;

/**
 * Created by zhu on 2017/9/25.
 */

public class Menu implements Serializable {

    private int id;

    private String name;

    private String icon;

    private String parent;

    private int status;

    private int position;

    private List<Subs> subs;

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return this.id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getIcon() {
        return this.icon;
    }

    public void setParent(String parent) {
        this.parent = parent;
    }

    public String getParent() {
        return this.parent;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getStatus() {
        return this.status;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public int getPosition() {
        return this.position;
    }

    public void setSubs(List<Subs> subs) {
        this.subs = subs;
    }

    public List<Subs> getSubs() {
        return this.subs;
    }



    public static final int TYPE_ONE = 1;
    public static final int TYPE_TWO = 2;

//    @Override
//    public int getItemType() {
//        return itemType;
//    }

    private int itemType;

    public Menu(int itemType) {
        this.itemType = itemType;
    }


}
