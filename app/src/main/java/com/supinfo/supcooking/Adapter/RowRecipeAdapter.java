package com.supinfo.supcooking.Adapter;

import android.content.Context;

import android.net.Uri;
import android.os.StrictMode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.supinfo.supcooking.R;
import com.supinfo.supcooking.Util.Util;

import java.util.ArrayList;

public class RowRecipeAdapter extends ArrayAdapter<RowRecipe> {

    public RowRecipeAdapter(Context context, ArrayList<RowRecipe> rowRecipes){
        super(context, 0, rowRecipes);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.row_recipe,parent, false);
        }

        RecipeViewHolder viewHolder = (RecipeViewHolder) convertView.getTag();

        if (viewHolder == null){
            viewHolder = new RecipeViewHolder();
            viewHolder.imgRecipe = convertView.findViewById(R.id.IMGVRowRecipe);
            viewHolder.name = convertView.findViewById(R.id.TVRowRecipeName);
            viewHolder.type = convertView.findViewById(R.id.TVRowRecipeType);
            viewHolder.rate = convertView.findViewById(R.id.RBRowRecipe);

            convertView.setTag(viewHolder);

            // getItem(position) va récupérer l'item [position] de la List
            RowRecipe rowRecipe = getItem(position);
            Log.d("position", String.valueOf((rowRecipe.name)));

            // remplir la vue
            viewHolder.name.setText(rowRecipe.name);
            viewHolder.type.setText(rowRecipe.type);
            viewHolder.rate.setRating(rowRecipe.rate);

         //   if (!rowRecipe.imgUrl.equalsIgnoreCase("null")){
                viewHolder.imgRecipe.setImageBitmap(Util.getBitmapFromURL(rowRecipe.imgUrl));
           // }
           // else{
             //   viewHolder.imgRecipe.setImageBitmap(Util.getBitmapFromURL("https://media.discordapp.net/attachments/215765926392496128/452898581758738462/DSC_0172.jpg"));
           // }
        }


        return  convertView;
    }

    private class RecipeViewHolder{
        protected ImageView imgRecipe;
        protected TextView name;
        protected TextView type;
        protected RatingBar rate;
    }
}
