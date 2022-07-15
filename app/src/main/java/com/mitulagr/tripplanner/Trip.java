package com.mitulagr.tripplanner;

import java.util.Random;

public class Trip {

    int srno;
    String place;
    int imageId;
    int bg;
    Float rate = 1.0f; //TODO: Store current usd to each currency rate for internet problem or request fail
    String depDate;
    String retDate;
    int nights = -1;
    Float exp = 0.0f;
    Float Hexp = 0.0f;
    Float Dexp = 0.0f;
    int isHom = 1;
    String HCur;
    String DCur;

    Trip(int srno, String place, int imageId){
        this.srno = srno;
        this.place = place;
        this.imageId = imageId;
        Random rand = new Random();
        this.bg = rand.nextInt(8);
    }

}
