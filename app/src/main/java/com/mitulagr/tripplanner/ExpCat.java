package com.mitulagr.tripplanner;

public class ExpCat {

    int id;
    int fid;
    String category;
    int imageId;
    float Amt=0.0f;
    float homAmt=0.0f;
    float DesAmt=0.0f;

    ExpCat(String category, int imageId){
        this.category = category;
        this.imageId = imageId;
    }

}