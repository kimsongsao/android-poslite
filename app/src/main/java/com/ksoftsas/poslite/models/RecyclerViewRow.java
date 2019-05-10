package com.ksoftsas.poslite.models;

public class RecyclerViewRow {
    private String code;
    private String name;
    private String phone_no;
    private int image;

    public RecyclerViewRow(String code, String name, int image) {
        this.code = code;
        this.name = name;
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNo() {
        return phone_no;
    }

    public void setPhoneNo(String phone_no) {
        this.phone_no = phone_no;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }
}
