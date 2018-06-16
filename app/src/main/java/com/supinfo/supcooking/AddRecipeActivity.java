package com.supinfo.supcooking;

import android.Manifest;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.SurfaceView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.supinfo.supcooking.Entity.Recipe;
import com.supinfo.supcooking.Entity.User;
import com.supinfo.supcooking.Persist.SQLiteHelper;
import com.supinfo.supcooking.Util.Util;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AddRecipeActivity extends AppCompatActivity {

    /**
     * permission code
     */
    private static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 1;


    // Zone d'affichage temporaire pour l'image de la recette
    protected ImageView IMGVNewRecipeTemp;

    protected EditText ETNewRecipeTitle;
    protected EditText ETNewRecipeType;
    protected EditText ETNewRecipeCookingTime;
    protected EditText ETNewRecipePreparationTime;
    protected EditText ETNewRecipeIngredients;
    protected EditText ETNewRecipePreparationSteps;


    // todo uri temporaire pour photo

    // Variable Bitmap temporaire pour le stockage de la photo
    private Bitmap bitmap;

    String pictureImagePath = "";

    public AddRecipeActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_recipe);

        this.IMGVNewRecipeTemp = findViewById(R.id.IMGVNewRecipeTemp);
        // bloqué la fenêtre d'ajout en portrait : TODO à changer !
       this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        this.ETNewRecipeTitle = findViewById(R.id.ETNewRecipeTitleName);
        this.ETNewRecipeType = findViewById(R.id.ETNewRecipeType);
        this.ETNewRecipeCookingTime = findViewById(R.id.ETNewRecipeCookingTime);
        this.ETNewRecipePreparationTime = findViewById(R.id.ETNewRecipePreparationTime);
        this.ETNewRecipeIngredients = findViewById(R.id.ETNewRecipeIngredients);
        this.ETNewRecipePreparationSteps = findViewById(R.id.ETNewRecipePreparationSteps);

        if (this.bitmap != null) {
            this.IMGVNewRecipeTemp.setImageBitmap(this.bitmap);
        }
    }


    public void onClickNewRecipeTakePicture(View view) {
        // Manifest

            StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
            StrictMode.setVmPolicy(builder.build());
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            String imageFileName = timeStamp + ".jpg";
            File storageDir = Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_PICTURES);
            pictureImagePath = storageDir.getAbsolutePath() + "/" + imageFileName;
            File file = new File(pictureImagePath);
            Uri outputFileUri = Uri.fromFile(file);
            Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
            startActivityForResult(cameraIntent, 1);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //MenuShootImage is user defined menu option to shoot image
        if (requestCode == 1) {
            File imgFile = new  File(pictureImagePath);
            if(imgFile.exists()){
                this.bitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                ImageView myImage = IMGVNewRecipeTemp;
                myImage.setImageBitmap(this.bitmap);
            }
        }
    }



    public void onClickNewRecipeAddRecipe(final View view) {
        if (!(this.ETNewRecipeTitle.getText().toString().trim().isEmpty() || this.ETNewRecipeType.getText().toString().trim().isEmpty() || this.ETNewRecipeCookingTime.getText().toString().trim().isEmpty() || this.ETNewRecipePreparationTime.getText().toString().trim().isEmpty() || this.ETNewRecipeIngredients.getText().toString().trim().isEmpty() || this.ETNewRecipePreparationSteps.getText().toString().trim().isEmpty())) {
            final User u = (User) getIntent().getSerializableExtra("currentUser");

            if (this.bitmap == null) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("Voulez vraiment créer une recette sans photo ? \r Une recette avec une photo aura plus de chance d'être visité.").setTitle("Attention");
                builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Todo créé une recette sans photo
                        SQLiteHelper db = new SQLiteHelper(view.getContext());
                      //  db.insertOrUpdateRecipe(new Recipe(db.getLastIdRecipe() + 1, ETNewRecipeTitle.getText().toString(), ETNewRecipeType.getText().toString(), Integer.parseInt(ETNewRecipeCookingTime.getText().toString()), Integer.parseInt(ETNewRecipePreparationTime.getText().toString()), ETNewRecipeIngredients.getText().toString(), ETNewRecipePreparationSteps.getText().toString(), 0, (BitmapFactory.decodeResource(getResources(), R.mipmap.ic_no_image))), u);
                    }
                });
                builder.setNegativeButton("Annuler", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            } else {
                // Todo créé une recette avec photo
                SQLiteHelper db = new SQLiteHelper(view.getContext());
              db.insertOrUpdateRecipe(new Recipe(db.getLastIdRecipe() + 1, ETNewRecipeTitle.getText().toString(), ETNewRecipeType.getText().toString(), Integer.parseInt(ETNewRecipeCookingTime.getText().toString()), Integer.parseInt(ETNewRecipePreparationTime.getText().toString()), ETNewRecipeIngredients.getText().toString(), ETNewRecipePreparationSteps.getText().toString(), 0, Util.getPathFromPicture(bitmap, db.getLastIdRecipe() + 1)), u);
            }
        } else {
            Util.messageAlert("Error", "Seul la photo n'est pas obligatoire pour la création d'une recette. \r Merci de rééssayer", this);
        }
    }

}
