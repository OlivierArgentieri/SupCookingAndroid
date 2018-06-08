package com.supinfo.supcooking.Adapter;



public class RowRecipe {

    protected byte[] img;
    protected String name;
    protected String type;
    protected Float rate;


    public RowRecipe(byte[] img, String name, String type, Float rate){
        this.img = img;
        this.name = name;
        this.type = type;
        this.rate = rate;
    }
}
