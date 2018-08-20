package com.spa.bean;

import java.io.Serializable;
import java.util.List;

public class TeachType implements Serializable {
    private int id;

    private String name;

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


}
