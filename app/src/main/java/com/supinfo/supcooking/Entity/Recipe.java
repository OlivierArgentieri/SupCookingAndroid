package com.supinfo.supcooking.Entity;

import java.io.Serializable;

public class Recipe implements Serializable {

    private String name;
    private String type;
    private Integer cookingTime;
    private Integer preparationTime;
    private String ingredients;
    private String preparationSteps;
    private Integer rate;
    private String picture;


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

    public Integer getRate() {
        return rate;
    }

    public void setRate(Integer rate) {
        this.rate = rate;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public Recipe(String name, String type, Integer cookingTime, Integer preparationTime, String ingredients, String preparationSteps, Integer rate, String picture) {
        this.name = name;
        this.type = type;
        this.cookingTime = cookingTime;
        this.preparationTime = preparationTime;
        this.ingredients = ingredients;
        this.preparationSteps = preparationSteps;
        this.rate = rate;
        this.picture = picture;
    }



}
