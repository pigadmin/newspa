package com.spa.bean;

import java.io.Serializable;
import java.util.List;

public class VideoData implements Serializable {

    private int id;

    private int typeId;

    private String name;

    private String act;

    private String des;

    private String icon;

    private long createDate;

    private String vv;

    private VideoAd ad;

    private int fileType;

    private int types;

    private List<VideoDetails> details;

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return this.id;
    }

    public void setTypeId(int typeId) {
        this.typeId = typeId;
    }

    public int getTypeId() {
        return this.typeId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public void setAct(String act) {
        this.act = act;
    }

    public String getAct() {
        return this.act;
    }

    public void setDes(String des) {
        this.des = des;
    }

    public String getDes() {
        return this.des;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getIcon() {
        return this.icon;
    }

    public void setCreateDate(long createDate) {
        this.createDate = createDate;
    }

    public long getCreateDate() {
        return this.createDate;
    }

    public void setVv(String vv) {
        this.vv = vv;
    }

    public String getVv() {
        return this.vv;
    }

    public void setAd(VideoAd ad) {
        this.ad = ad;
    }

    public VideoAd getAd() {
        return this.ad;
    }

    public void setFileType(int fileType) {
        this.fileType = fileType;
    }

    public int getFileType() {
        return this.fileType;
    }

    public void setTypes(int types) {
        this.types = types;
    }

    public int getTypes() {
        return this.types;
    }

    public void setDetails(List<VideoDetails> details) {
        this.details = details;
    }

    public List<VideoDetails> getDetails() {
        return this.details;
    }

    public VideoData(String name, String act) {
        this.name = name;
        this.act = act;
    }
}
