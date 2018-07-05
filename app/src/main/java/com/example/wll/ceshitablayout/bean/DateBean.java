package com.example.wll.ceshitablayout.bean;

/**
 * Created by newnet on 2018/3/16.
 */

public class DateBean  {
    String name;
    int id;
    long time;
    String tvtime;


    public DateBean(String name, int id, long time, String tvtime) {
        this.name = name;
        this.id = id;
        this.time = time;
        this.tvtime = tvtime;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getTvtime() {
        return tvtime;
    }

    public void setTvtime(String tvtime) {
        this.tvtime = tvtime;
    }
}
