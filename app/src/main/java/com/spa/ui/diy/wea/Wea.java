package com.spa.ui.diy.wea;

import java.io.Serializable;

public class Wea implements Serializable {
    private String date;

    private String message;

    private int status;

    private String city;

    private int count;

    private WeaData data;

    public void setDate(String date) {
        this.date = date;
    }

    public String getDate() {
        return this.date;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return this.message;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getStatus() {
        return this.status;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCity() {
        return this.city;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getCount() {
        return this.count;
    }

    public void setData(WeaData data) {
        this.data = data;
    }

    public WeaData getData() {
        return this.data;
    }

}
