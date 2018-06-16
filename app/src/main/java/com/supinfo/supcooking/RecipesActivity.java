package com.supinfo.supcooking;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;

import com.supinfo.supcooking.Adapter.PagerAdapter;
import com.supinfo.supcooking.Entity.User;
import com.supinfo.supcooking.Util.Util;


public class RecipesActivity extends FragmentActivity{
    ViewPager viewPager;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipes);

        viewPager = findViewById(R.id.viewPager);
        PagerAdapter adapter = new PagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(1); // Afficher le deuxiéme fragment au demarrage de l'appli

    }

    public void onClickCreateRecipe(View view) {
        Intent intent = new Intent(this, AddRecipeActivity.class);
        User u = (User) getIntent().getSerializableExtra("currentUser");
        intent.putExtra("currentUser", u);
        startActivity(intent);
    }


}
