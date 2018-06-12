package com.spa.bean;

import java.io.Serializable;

public class JishiData implements Serializable {
    private int id;

    private String name;

    private String numbering;

    private int sex;

    private String pic;

    private int status;

    private int onduty;

    private String services;

    private String origin;

    private String height;

    private String weight;

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
    public void setNumbering(String numbering){
        this.numbering = numbering;
    }
    public String getNumbering(){
        return this.numbering;
    }
    public void setSex(int sex){
        this.sex = sex;
    }
    public int getSex(){
        return this.sex;
    }
    public void setPic(String pic){
        this.pic = pic;
    }
    public String getPic(){
        return this.pic;
    }
    public void setStatus(int status){
        this.status = status;
    }
    public int getStatus(){
        return this.status;
    }
    public void setOnduty(int onduty){
        this.onduty = onduty;
    }
    public int getOnduty(){
        return this.onduty;
    }
    public void setServices(String services){
        this.services = services;
    }
    public String getServices(){
        return this.services;
    }
    public void setOrigin(String origin){
        this.origin = origin;
    }
    public String getOrigin(){
        return this.origin;
    }
    public void setHeight(String height){
        this.height = height;
    }
    public String getHeight(){
        return this.height;
    }
    public void setWeight(String weight){
        this.weight = weight;
    }
    public String getWeight(){
        return this.weight;
    }
}
