package com.spa.bean;

import java.io.Serializable;

/**
 * Created by zhu on 2017/9/18.
 */

public class WelcomeAd implements Serializable {
    private int id;

    private String name;

    private int type;

    private String filePath;

    private String bgFile;

    private int inter;

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

    public void setBgFile(String bgFile) {
        this.bgFile = bgFile;
    }

    public String getBgFile() {
        return this.bgFile;
    }

    public void setInter(int inter) {
        this.inter = inter;
    }

    public int getInter() {
        return this.inter;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public int getPosition() {
        return this.position;
    }

}
