package com.spa.bean;

import java.io.Serializable;

/**
 * Created by zhu on 2017/9/25.
 */

public class Subs implements Serializable {
    private int id;

    private String name;

    private String icon;

    private int parent;

    private int status;

    private int position;

    private String subs;

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

    public void setParent(int parent) {
        this.parent = parent;
    }

    public int getParent() {
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

    public void setSubs(String subs) {
        this.subs = subs;
    }

    public String getSubs() {
        return this.subs;
    }
}
