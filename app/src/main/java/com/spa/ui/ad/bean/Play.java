package com.spa.ui.ad.bean;

import java.io.Serializable;

public class Play implements Serializable {
    private int id;

    private String name;

    private String sname;

    private int status;

    private int stype;

    private int sid;

    private String surl;

    private int type;

    private int lastStatus;

    private String creatTime;


    private Sources source;

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

    public void setSname(String sname) {
        this.sname = sname;
    }

    public String getSname() {
        return this.sname;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getStatus() {
        return this.status;
    }

    public void setStype(int stype) {
        this.stype = stype;
    }

    public int getStype() {
        return this.stype;
    }

    public void setSid(int sid) {
        this.sid = sid;
    }

    public int getSid() {
        return this.sid;
    }

    public void setSurl(String surl) {
        this.surl = surl;
    }

    public String getSurl() {
        return this.surl;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getType() {
        return this.type;
    }

    public void setLastStatus(int lastStatus) {
        this.lastStatus = lastStatus;
    }

    public int getLastStatus() {
        return this.lastStatus;
    }

    public void setCreatTime(String creatTime) {
        this.creatTime = creatTime;
    }

    public String getCreatTime() {
        return this.creatTime;
    }


    public void setSource(Sources source) {
        this.source = source;
    }

    public Sources getSource() {
        return this.source;
    }

}
