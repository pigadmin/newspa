package com.spa.ui.diy.wea;

import java.io.Serializable;
import java.util.List;

public class WeaData implements Serializable {
    private String shidu;

    private double pm25;

    private double pm10;

    private String quality;

    private String wendu;

    private String ganmao;

    private Yesterday yesterday;

    private List<Forecast> forecast ;

    public void setShidu(String shidu){
        this.shidu = shidu;
    }
    public String getShidu(){
        return this.shidu;
    }
    public void setPm25(double pm25){
        this.pm25 = pm25;
    }
    public double getPm25(){
        return this.pm25;
    }
    public void setPm10(double pm10){
        this.pm10 = pm10;
    }
    public double getPm10(){
        return this.pm10;
    }
    public void setQuality(String quality){
        this.quality = quality;
    }
    public String getQuality(){
        return this.quality;
    }
    public void setWendu(String wendu){
        this.wendu = wendu;
    }
    public String getWendu(){
        return this.wendu;
    }
    public void setGanmao(String ganmao){
        this.ganmao = ganmao;
    }
    public String getGanmao(){
        return this.ganmao;
    }
    public void setYesterday(Yesterday yesterday){
        this.yesterday = yesterday;
    }
    public Yesterday getYesterday(){
        return this.yesterday;
    }
    public void setForecast(List<Forecast> forecast){
        this.forecast = forecast;
    }
    public List<Forecast> getForecast(){
        return this.forecast;
    }
}
