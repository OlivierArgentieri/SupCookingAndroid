package com.supinfo.supcooking.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.supinfo.supcooking.Adapter.RowRecipe;
import com.supinfo.supcooking.Adapter.RowRecipeAdapter;
import com.supinfo.supcooking.Entity.Recipe;
import com.supinfo.supcooking.Entity.User;
import com.supinfo.supcooking.Persist.SQLiteHelper;
import com.supinfo.supcooking.R;
import com.supinfo.supcooking.RecipeDetailActivity;

import java.util.ArrayList;
import java.util.List;

public class Page3 extends Fragment{

        protected View view;

        //titre de la page
        protected TextView TVLayoutTitle;

        // list recette
        protected ListView listRecipe;

        //ligne recette
        protected ImageView imgRecette;

        // adapter pour chaque item de la listView
        protected static RowRecipeAdapter adapter;

        // Rond de chargerment
        protected ProgressBar PBGetAllRecipe;

        public static void setAdapter(RowRecipeAdapter adapter) {
            Page3.adapter = adapter;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            this.view = inflater.inflate(R.layout.activity_page3, container, false);

            // titre de la page
            this.TVLayoutTitle = this.view.findViewById(R.id.TVTitleListRecipe);
            this.TVLayoutTitle.setText("Mes recettes :");

            // List recette
            this.listRecipe = this.view.findViewById(R.id.LVListRecipe);

            // Ligne de cahque recette
            imgRecette = this.view.findViewById(R.id.IMGVRowRecipe);

            // Rond de chargement
            this.PBGetAllRecipe = this.view.findViewById(R.id.PBGetAllRecipe);
            this.PBGetAllRecipe.setVisibility(View.VISIBLE);

            // construire la vue
            User u = (User) getActivity().getIntent().getSerializableExtra("currentUser");

                // todo lire les données de la bdd du telehpone
                SQLiteHelper db = new SQLiteHelper(getContext());
                ArrayList<RowRecipe> rowRecipes = new ArrayList<RowRecipe>();
                ArrayList<Recipe> recipes = db.getRecipeByUser(u);

                for (Recipe r : recipes) {
                    rowRecipes.add(new RowRecipe(r.getPicture(), r.getName(), r.getType(), r.getRate()));
                }

                setAdapter(new RowRecipeAdapter(getContext(), rowRecipes));
                listRecipe.setAdapter(adapter);

                final List<Recipe> recipesl = recipes;
                listRecipe.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        // todo lancer la description de la recettes
                        // Log.d("index", recipes.get(0).getPicture());
                        Intent intent = new Intent(view.getContext(), RecipeDetailActivity.class);
                        intent.putExtra("recipe", recipesl.get(i));
                        startActivity(intent);
                    }
                });
                this.PBGetAllRecipe.setVisibility(View.GONE);


            return this.view;
        }

}
