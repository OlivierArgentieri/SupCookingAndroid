package com.supinfo.supcooking.Fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.supinfo.supcooking.Adapter.RowRecipe;
import com.supinfo.supcooking.Adapter.RowRecipeAdapter;
import com.supinfo.supcooking.Entity.Recipe;
import com.supinfo.supcooking.Entity.User;
import com.supinfo.supcooking.Persist.SQLiteHelper;
import com.supinfo.supcooking.R;
import com.supinfo.supcooking.RecipeDetailActivity;
import com.supinfo.supcooking.RecipesActivity;
import com.supinfo.supcooking.Util.Util;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import static com.supinfo.supcooking.Util.Util.convertStreamToString;
import static com.supinfo.supcooking.Util.Util.isNetworkAvailable;
import static com.supinfo.supcooking.Util.Util.messageAlert;

public class Page2 extends Fragment {
    protected View view;

    // list recette
    protected ListView listRecipe;

    //ligne recette
    protected ImageView imgRecette;

    // adapter pour chaque item de la listView
    protected static RowRecipeAdapter adapter;

    // Rond de chargerment
    protected ProgressBar PBGetAllRecipe;

    public static void setAdapter(RowRecipeAdapter adapter) {
        Page2.adapter = adapter;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.view = inflater.inflate(R.layout.activity_page2, container, false);

        // List recette
        this.listRecipe = this.view.findViewById(R.id.LVListRecipe);

        // Ligne de cahque recette
        imgRecette = this.view.findViewById(R.id.IMGVRowRecipe);

        // Rond de chargement
        this.PBGetAllRecipe = this.view.findViewById(R.id.PBGetAllRecipe);
        this.PBGetAllRecipe.setVisibility(View.VISIBLE);

        // construire la vue
        User u = (User) getActivity().getIntent().getSerializableExtra("currentUser");
        if (isNetworkAvailable(this.getActivity())) {
            List<NameValuePair> nameValuePairs = new ArrayList<>(3);
            nameValuePairs.add(new BasicNameValuePair("action", "getCooking"));
            nameValuePairs.add(new BasicNameValuePair("login", u.getUsername()));
            nameValuePairs.add(new BasicNameValuePair("password", u.getPassword()));

            requestContentTask task = new requestContentTask(this.getActivity(), nameValuePairs);
            task.execute("http://supinfo.steve-colinet.fr/supcooking/");
        }
        else {

            // todo lire les données de la bdd du telehpone
            SQLiteHelper db = new SQLiteHelper(getContext());
            ArrayList<RowRecipe> rowRecipes = new ArrayList<RowRecipe>();
            ArrayList<Recipe> recipes = db.getAllRecipe();

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
        }

        return this.view;
    }


    // Class pour traitement des données
    public class requestContentTask extends AsyncTask<String, Void, List<Recipe>> {

        List<NameValuePair> nameValuePairs;
        protected Activity activity;

        public requestContentTask(Activity activity, List<NameValuePair> nameValuePairs) {
            this.nameValuePairs = nameValuePairs;
            this.activity = activity;
        }

        @Override
        protected List<Recipe> doInBackground(String... url) {

            HttpClient httpclient = new DefaultHttpClient();
            String result = null;
            HttpPost httppost = new HttpPost(url[0]);
            HttpResponse response = null;
            List<Recipe> recipes = new ArrayList<>();

            InputStream instream = null;
            try {
                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                response = httpclient.execute(httppost);

                HttpEntity entity = response.getEntity();
                if (entity != null) {
                    instream = entity.getContent();
                    result = convertStreamToString(instream);
                    Log.d("Json", result);

                    JSONObject json = new JSONObject(result);


                    for (int i = 0; i < json.getJSONArray("recipes").length(); i++) {
                        JSONObject temp = new JSONObject(json.getJSONArray("recipes").get(i).toString());
                        recipes.add(new Recipe(temp));
                    }
                }
            } catch (Exception e) {
                recipes = null; //code erreur perso pour test
            }
            return recipes;
        }


        @Override
        protected void onPostExecute(List<Recipe> recipes) {
            try {
               // JSONObject json = new JSONObject(result);
                if ( recipes != null) {
                    PBGetAllRecipe.setVisibility(View.VISIBLE);

                    // todo Persist en base pendant qu'on dispose d'une connection
                    ArrayList<RowRecipe> rowRecipes = new ArrayList<RowRecipe>();
                    SQLiteHelper db = new SQLiteHelper(getContext());
                    for (Recipe r : recipes) {
                        db.insertOrUpdateRecipe(r, (User) getActivity().getIntent().getSerializableExtra("currentUser"));

                    }

                    // récupérer toutes les recettes en base de données
                    for (Recipe r : db.getAllRecipe()){
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

                } else {
                    messageAlert("Erreur", "FDP, \rVeuillez rééssayer.", activity);
                }
                PBGetAllRecipe.setVisibility(View.GONE);
            } catch (Exception e) {
               Log.d("OUI", e.getMessage());
            } // try / catch obligatoire pour objet JSONObject

            // activity.findViewById(R.id.PBLogin).setVisibility(View.GONE);
        }
    }

}
