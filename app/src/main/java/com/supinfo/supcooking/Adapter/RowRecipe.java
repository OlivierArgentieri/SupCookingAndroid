package com.supinfo.supcooking.Adapter;



public class RowRecipe {

    protected String imgUrl;
    protected String name;
    protected String type;
    protected Float rate;


    public RowRecipe(String imgUrl, String name, String type, Float rate){
        this.imgUrl = imgUrl;
        this.name = name;
        this.type = type;
        this.rate = rate;
    }


}
