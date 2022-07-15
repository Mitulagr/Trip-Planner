package com.mitulagr.tripplanner;

public class Travel {

    int img;
    String no;

    String from_time;
    String from;
    String from_date;

    String to_time;
    String to;
    String to_date;

    public Travel(int img, String no, String from, String from_date, String from_time, String to, String to_date, String to_time) {
        this.img = img;
        this.no = no;
        this.from_time = from_time;
        this.from = from;
        this.from_date = from_date;
        this.to_time = to_time;
        this.to = to;
        this.to_date = to_date;
    }

}
