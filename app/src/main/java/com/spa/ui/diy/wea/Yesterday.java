package com.spa.ui.diy.wea;

import java.io.Serializable;

public class Yesterday implements Serializable {
    private String date;

    private String sunrise;

    private String high;

    private String low;

    private String sunset;

    private double aqi;

    private String fx;

    private String fl;

    private String type;

    private String notice;

    public void setDate(String date){
        this.date = date;
    }
    public String getDate(){
        return this.date;
    }
    public void setSunrise(String sunrise){
        this.sunrise = sunrise;
    }
    public String getSunrise(){
        return this.sunrise;
    }
    public void setHigh(String high){
        this.high = high;
    }
    public String getHigh(){
        return this.high;
    }
    public void setLow(String low){
        this.low = low;
    }
    public String getLow(){
        return this.low;
    }
    public void setSunset(String sunset){
        this.sunset = sunset;
    }
    public String getSunset(){
        return this.sunset;
    }
    public void setAqi(double aqi){
        this.aqi = aqi;
    }
    public double getAqi(){
        return this.aqi;
    }
    public void setFx(String fx){
        this.fx = fx;
    }
    public String getFx(){
        return this.fx;
    }
    public void setFl(String fl){
        this.fl = fl;
    }
    public String getFl(){
        return this.fl;
    }
    public void setType(String type){
        this.type = type;
    }
    public String getType(){
        return this.type;
    }
    public void setNotice(String notice){
        this.notice = notice;
    }
    public String getNotice(){
        return this.notice;
    }

}
