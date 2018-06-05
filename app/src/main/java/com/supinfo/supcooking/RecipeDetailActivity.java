package com.supinfo.supcooking;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.supinfo.supcooking.Entity.Recipe;
import com.supinfo.supcooking.Util.Util;


public class RecipeDetailActivity extends AppCompatActivity {

    protected ImageView IMGRecipe;
    protected RatingBar RBRecipe;
    protected TextView NameRecipe;
    protected TextView TypeRecipe;
    protected TextView CookingTime;
    protected TextView PreparationTime;
    protected TextView Ingredients;
    protected TextView PreparationsSteps;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_recipe_detail);
        this.IMGRecipe = findViewById(R.id.IMGVRecipeDetail);
        this.RBRecipe = findViewById(R.id.RBDetailRecipe);
        this.NameRecipe = findViewById(R.id.TVRecipeDetailName);
        this.TypeRecipe = findViewById(R.id.TVRecipeDetailType);
        this.CookingTime = findViewById(R.id.TVRecipeDetailCookingTime);
        this.PreparationTime = findViewById(R.id.TVRecipeDetailPreparationTime);
        this.Ingredients = findViewById(R.id.TVRecipeDetailIngredients);
        this.PreparationsSteps = findViewById(R.id.TVRecipeDetailPreparationSteps);



        Recipe r = (Recipe) this.getIntent().getSerializableExtra("recipe");
        Log.d("attr", r.getPicture());

        this.IMGRecipe.setImageBitmap(Util.getBitmapFromURL(r.getPicture()));
        this.RBRecipe.setRating(r.getRate());
        this.NameRecipe.setText(r.getName());
        this.TypeRecipe.setText(r.getType());
        this.CookingTime.setText(String.valueOf(r.getCookingTime()));
        this.PreparationTime.setText(String.valueOf(r.getPreparationTime()));
        this.Ingredients.setText(r.getIngredients());
        this.PreparationsSteps.setText(r.getPreparationSteps());

    }
}
