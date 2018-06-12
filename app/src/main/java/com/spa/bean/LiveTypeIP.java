package com.spa.bean;

import java.io.Serializable;
import java.util.List;

public class LiveTypeIP implements Serializable {
    private List<LiveIP> data;

    private int type;

    public void setData(List<LiveIP> data) {
        this.data = data;
    }

    public List<LiveIP> getData() {
        return this.data;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getType() {
        return this.type;
    }

}
