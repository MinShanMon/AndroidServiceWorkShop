package com.example.mymusicservice;

public class Song {
    private String title;
    private String desc;
    private String fname;

    public Song(String title, String desc, String fname) {
        this.title = title;
        this.desc = desc;
        this.fname = fname;
    }

    public String getTitle() {
        return title;
    }

    public String getDesc() {
        return desc;
    }

    public String getFname() {
        return fname;
    }
}
