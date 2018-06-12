package com.spa.ui.diy.wea;

import java.io.Serializable;

public class NewWea implements Serializable {
    private int id;

    private String info;

    public void setId(int id){
        this.id = id;
    }
    public int getId(){
        return this.id;
    }
    public void setInfo(String info){
        this.info = info;
    }
    public String getInfo(){
        return this.info;
    }

}
