package com.spa.bean;

import java.io.Serializable;

public class TechTypes implements Serializable {
    private int id;

    private String name;

    private int cid;

    private String icon;

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

    public void setCid(int cid) {
        this.cid = cid;
    }

    public int getCid() {
        return this.cid;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getIcon() {
        return this.icon;
    }

}
