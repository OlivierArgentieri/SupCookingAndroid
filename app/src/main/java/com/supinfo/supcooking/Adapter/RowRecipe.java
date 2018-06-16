package com.supinfo.supcooking.Adapter;


import android.graphics.Bitmap;

public class RowRecipe {

    protected Bitmap img;
    protected String name;
    protected String type;
    protected Float rate;


    public RowRecipe(Bitmap img, String name, String type, Float rate){
        this.img = img;
        this.name = name;
        this.type = type;
        this.rate = rate;
    }
}
