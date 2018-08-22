package com.spa.bean;

import java.io.Serializable;

public class VideoType implements Serializable {
    private int id;

    private String name;

    private String icon;

    private String sources;

    public void setId(int id){
        this.id = id;
    }
    public int getId(){
        return this.id;
    }
    public void setName(String name){
        this.name = name;
    }
    public String getName(){
        return this.name;
    }
    public void setIcon(String icon){
        this.icon = icon;
    }
    public String getIcon(){
        return this.icon;
    }
    public void setSources(String sources){
        this.sources = sources;
    }
    public String getSources(){
        return this.sources;
    }

    @Override
    public String toString() {
        return "VideoType{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", icon='" + icon + '\'' +
                ", sources='" + sources + '\'' +
                '}';
    }
}
