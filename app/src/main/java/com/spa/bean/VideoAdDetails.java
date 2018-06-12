package com.spa.bean;

import java.io.Serializable;

public class VideoAdDetails implements Serializable {
    private int id;

    private String name;

    private int type;

    private String filePath;

    private int ad;

    private int position;

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

    public void setType(int type) {
        this.type = type;
    }

    public int getType() {
        return this.type;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getFilePath() {
        return this.filePath;
    }

    public void setAd(int ad) {
        this.ad = ad;
    }

    public int getAd() {
        return this.ad;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public int getPosition() {
        return this.position;
    }
}
