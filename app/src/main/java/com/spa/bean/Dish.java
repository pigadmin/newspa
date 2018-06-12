package com.spa.bean;

import java.io.Serializable;

public class Dish implements Serializable {
    private int id;

    private Style style;

    private String name;

    private double price;

    private String icon;

    private String discription;

    private String supply_time;

    private int saleNum;

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return this.id;
    }

    public void setStyle(Style style) {
        this.style = style;
    }

    public Style getStyle() {
        return this.style;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getPrice() {
        return this.price;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getIcon() {
        return this.icon;
    }

    public void setDiscription(String discription) {
        this.discription = discription;
    }

    public String getDiscription() {
        return this.discription;
    }

    public void setSupply_time(String supply_time) {
        this.supply_time = supply_time;
    }

    public String getSupply_time() {
        return this.supply_time;
    }

    public void setSaleNum(int saleNum) {
        this.saleNum = saleNum;
    }

    public int getSaleNum() {
        return this.saleNum;
    }

}
