package com.supinfo.supcooking;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

public class AddRecipeActivity extends AppCompatActivity {

    // Zone d'affichage temporaire pour l'image de la recette
    protected ImageView IMGVNewRecipeTemp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_recipe);
        this.IMGVNewRecipeTemp = findViewById(R.id.IMGVNewRecipeTemp);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }




    public void onClickNewRecipeTakePicture(View view) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, 0);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Bitmap bitmp = (Bitmap) data.getExtras().get("data");
        IMGVNewRecipeTemp.setImageBitmap(bitmp);
    }
}
