package com.spa.bean;

import java.io.Serializable;
import java.util.List;

public class LiveTypeApk implements Serializable {
    private List<LiveApk> data;

    private int type;

    public void setData(List<LiveApk> data) {
        this.data = data;
    }

    public List<LiveApk> getData() {
        return this.data;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getType() {
        return this.type;
    }

}
