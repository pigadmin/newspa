package com.spa.bean;

import java.io.Serializable;
import java.util.List;

public class Video implements Serializable{
    private int pageNo;

    private int pageSize;

    private int totalPage;

    private int totalRows;

    private boolean hasPrePage;

    private boolean hasNextPage;

    private List<VideoData> data ;

    public void setPageNo(int pageNo){
        this.pageNo = pageNo;
    }
    public int getPageNo(){
        return this.pageNo;
    }
    public void setPageSize(int pageSize){
        this.pageSize = pageSize;
    }
    public int getPageSize(){
        return this.pageSize;
    }
    public void setTotalPage(int totalPage){
        this.totalPage = totalPage;
    }
    public int getTotalPage(){
        return this.totalPage;
    }
    public void setTotalRows(int totalRows){
        this.totalRows = totalRows;
    }
    public int getTotalRows(){
        return this.totalRows;
    }
    public void setHasPrePage(boolean hasPrePage){
        this.hasPrePage = hasPrePage;
    }
    public boolean getHasPrePage(){
        return this.hasPrePage;
    }
    public void setHasNextPage(boolean hasNextPage){
        this.hasNextPage = hasNextPage;
    }
    public boolean getHasNextPage(){
        return this.hasNextPage;
    }
    public void setData(List<VideoData> data){
        this.data = data;
    }
    public List<VideoData> getData(){
        return this.data;
    }

}
