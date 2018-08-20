package com.spa.bean;

import java.io.Serializable;

public class EvaluateBean implements Serializable {
    public int src;

    public String name;

    public EvaluateBean(int src, String name) {
        this.src = src;
        this.name = name;
    }

    public EvaluateBean(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "EvaluateBean{" +
                "src=" + src +
                ", name='" + name + '\'' +
                '}';
    }
}
