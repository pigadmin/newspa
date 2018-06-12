package com.spa.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by zhu on 2017/9/22.
 */

public class LogoBg implements Serializable {
    private Logo logo;

    private List<Backs> backs;

    public void setLogo(Logo logo) {
        this.logo = logo;
    }

    public Logo getLogo() {
        return this.logo;
    }

    public void setBacks(List<Backs> backs) {
        this.backs = backs;
    }

    public List<Backs> getBacks() {
        return this.backs;
    }

}
