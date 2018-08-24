package com.spa.ui.ad.bean;

import com.spa.bean.Lives;

import java.io.Serializable;

public class MsgLives implements Serializable {
    private int id;

    private int mid;

    private Lives live;

    public void setId(int id){
        this.id = id;
    }
    public int getId(){
        return this.id;
    }
    public void setMid(int mid){
        this.mid = mid;
    }
    public int getMid(){
        return this.mid;
    }
    public void setLive(Lives live){
        this.live = live;
    }
    public Lives getLive(){
        return this.live;
    }

}