package com.spa.bean;

import java.io.Serializable;
import java.util.List;

public class VideoAd implements Serializable {
    private int id;

    private String name;

    private String inter;

    private List<VideoAdDetails> details;

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

    public void setInter(String inter) {
        this.inter = inter;
    }

    public String getInter() {
        return this.inter;
    }

    public void setDetails(List<VideoAdDetails> details) {
        this.details = details;
    }

    public List<VideoAdDetails> getDetails() {
        return this.details;
    }
}
