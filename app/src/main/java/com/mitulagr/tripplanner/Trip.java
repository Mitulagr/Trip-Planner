package com.mitulagr.tripplanner;

import java.util.Random;

public class Trip {

    int srno;
    String place;
    int imageId;
    int bg;
    Float rate = 0.0f; //TODO: Store current usd to each currency rate for internet problem or request fail
    String depDate;
    String retDate;
    int nights = 0;
    Float exp = 0.0f;
    Float Hexp = 0.0f;
    Float Dexp = 0.0f;
    int isHom = 1;
    String Hcur;
    String Dcur;

    Trip(int srno, String place, int imageId){
        this.srno = srno;
        this.place = place;
        this.imageId = imageId;
        Random rand = new Random();
        int r = rand.nextInt(8);
        if(r==0) this.bg = R.drawable.rounded;
        if(r==1) this.bg = R.drawable.rounded2;
        if(r==2) this.bg = R.drawable.rounded3;
        if(r==3) this.bg = R.drawable.rounded4;
        if(r==4) this.bg = R.drawable.rounded5;
        if(r==5) this.bg = R.drawable.rounded6;
        if(r==6) this.bg = R.drawable.rounded7;
        if(r==7) this.bg = R.drawable.rounded8;
    }

}
