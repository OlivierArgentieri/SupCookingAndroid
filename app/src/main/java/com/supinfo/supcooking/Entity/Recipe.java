package com.supinfo.supcooking.Entity;

import org.json.JSONObject;

import java.io.Serializable;

public class Recipe implements Serializable {

    private int id;
    private String name;
    private String type;
    private Integer cookingTime;
    private Integer preparationTime;
    private String ingredients;
    private String preparationSteps;
    private Float rate;
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

    public Integer getCookingTime() {
        return cookingTime;
    }

    public void setCookingTime(Integer cookingTime) {
        this.cookingTime = cookingTime;
    }

    public Integer getPreparationTime() {
        return preparationTime;
    }

    public void setPreparationTime(Integer preparationType) {
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

    public Float getRate() {
        return rate;
    }

    public void setRate(Float rate) {
        this.rate = rate;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public Recipe(int id, String name, String type, Integer cookingTime, Integer preparationTime, String ingredients, String preparationSteps, Float rate, String picture) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.cookingTime = cookingTime;
        this.preparationTime = preparationTime;
        this.ingredients = ingredients;
        this.preparationSteps = preparationSteps;
        this.rate = rate;
        this.picture = picture;
    }


    public Recipe(JSONObject json){
        try{
            this.id = Integer.parseInt(json.get("id").toString());
            this.name = json.get("name").toString();
            this.type = json.get("type").toString();
            this.cookingTime = Integer.parseInt(json.get("cookingTime").toString());
            this.preparationTime = Integer.parseInt(json.get("preparationtTime").toString());
            this.ingredients = json.get("ingredients").toString();
            this.preparationSteps = json.get("preparationSteps").toString();
            this.rate = Float.parseFloat(json.get("rate").toString());
            if (json.get("picture").toString().equalsIgnoreCase("null"))
            {
                this.picture = "https://media.discordapp.net/attachments/215765926392496128/452898581758738462/DSC_0172.jpg";
            }else {
                this.picture = json.get("picture").toString();
            }
        }
        catch (Exception ignore){}
    }
}
