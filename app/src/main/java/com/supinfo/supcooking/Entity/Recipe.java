package com.supinfo.supcooking.Entity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.supinfo.supcooking.Util.Util;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URL;

import static com.supinfo.supcooking.Util.Util.getBitmapFromURL;
import static com.supinfo.supcooking.Util.Util.getPathFromPicture;

public class Recipe implements Serializable {

    private int id;
    private String name;
    private String type;
    private int cookingTime;
    private int preparationTime;
    private String ingredients;
    private String preparationSteps;
    private float rate;
    private String picture;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getCookingTime() {
        return cookingTime;
    }

    public void setCookingTime(int cookingTime) {
        this.cookingTime = cookingTime;
    }

    public int getPreparationTime() {
        return preparationTime;
    }

    public void setPreparationTime(int preparationType) {
        preparationTime = preparationType;
    }

    public String getIngredients() {
        return ingredients;
    }

    public void setIngredients(String ingredients) {
        this.ingredients = ingredients;
    }

    public String getPreparationSteps() {
        return preparationSteps;
    }

    public void setPreparationSteps(String preparationSteps) {
        this.preparationSteps = preparationSteps;
    }

    public float getRate() {
        return rate;
    }

    public void setRate(float rate) {
        this.rate = rate;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public Recipe(int id, String name, String type, int cookingTime, int preparationTime, String ingredients, String preparationSteps, float rate, String picture) {
        this.setId(id);
        this.setName(name);
        this.setType(type);
        this.setCookingTime(cookingTime);
        this.setPreparationTime(preparationTime);
        this.setIngredients(ingredients);
        this.setPreparationSteps(preparationSteps);
        this.setRate(rate);
        this.setPicture(picture);
    }


    public Recipe(JSONObject json){
        try{
            this.setId(Integer.parseInt(json.get("id").toString()));
            this.setName(json.get("name").toString());
            this.setType(json.get("type").toString());
            this.setCookingTime(Integer.parseInt(json.get("cookingTime").toString()));
            this.setPreparationTime(Integer.parseInt(json.get("preparationtTime").toString()));
            this.setIngredients(json.get("ingredients").toString());
            this.setPreparationSteps(json.get("preparationSteps").toString());
            this.setRate(Float.parseFloat(json.get("rate").toString()));
            if (json.get("picture").toString().equalsIgnoreCase("null"))
            {
                // TODO : mettre une vrai image par défaut
                this.setPicture(getPathFromPicture(getBitmapFromURL("https://media.discordapp.net/attachments/215765926392496128/452898581758738462/DSC_0172.jpg"), this.getId()));
                Log.d("SetPicture", "OK");
            }else {
                this.setPicture(getPathFromPicture(getBitmapFromURL(json.get("picture").toString()), this.getId()));
            }
        }
        catch (Exception e){
            Log.e("Receipe", "Recipe: ", e);
            e.printStackTrace();
        }
    }

}
