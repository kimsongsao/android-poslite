package com.ksoftsas.poslite.models;

public class DataSpinner {
    private String value;
    private String description;
    public DataSpinner(String value,String description) {
        this.value = value;
        this.description = description;
    }
    public String getValue(){
        return value;
    }
    public String getDescription(){
        return description;
    }
    public String toString(){
        return description;
    }
}
