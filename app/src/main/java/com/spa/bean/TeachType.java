package com.spa.bean;

import java.io.Serializable;
import java.util.List;

public class TeachType implements Serializable {
    private int id;

    private String name;

    private String icon;

    private List<TechTypes> techTypes;

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

    public void setTechTypes(List<TechTypes> techTypes) {
        this.techTypes = techTypes;
    }

    public List<TechTypes> getTechTypes() {
        return this.techTypes;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    @Override
    public String toString() {
        return "TeachType{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", icon='" + icon + '\'' +
                ", techTypes=" + techTypes +
                '}';
    }
}
