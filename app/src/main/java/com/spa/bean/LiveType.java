package com.spa.bean;

import java.io.Serializable;
import java.util.List;

public class LiveType<T> implements Serializable {
    private List<T> data;

    private int type;

    public void setData(List<T> data) {
        this.data = data;
    }

    public List<T> getData() {
        return this.data;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getType() {
        return this.type;
    }

}
