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
import android.widget.ImageView;
import android.widget.ListView;

import com.supinfo.supcooking.Adapter.RowRecipe;
import com.supinfo.supcooking.Adapter.RowRecipeAdapter;
import com.supinfo.supcooking.Entity.Recipe;
import com.supinfo.supcooking.Entity.User;
import com.supinfo.supcooking.R;
import com.supinfo.supcooking.RecipesActivity;

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
import static com.supinfo.supcooking.Util.Util.messageAlert;

public class Page2 extends Fragment{
    protected View view;

    // list recette
    protected ListView listRecipe;

    //ligne recette
    protected ImageView imgRecette;

    // adapter pour chaque item de la listView
    protected static RowRecipeAdapter adapter;

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

        // construire la vue

        User u = (User) getActivity().getIntent().getSerializableExtra("currentUser");

        List<NameValuePair> nameValuePairs = new ArrayList<>(3);
        nameValuePairs.add(new BasicNameValuePair("action", "getCooking"));
        nameValuePairs.add(new BasicNameValuePair("login", u.getUsername()));
        nameValuePairs.add(new BasicNameValuePair("password", u.getPassword()));

        requestContentTask task = new requestContentTask(this.getActivity(), nameValuePairs);
        task.execute("http://supinfo.steve-colinet.fr/supcooking/");

        return this.view;
    }



    // Class pour traitement des données

    public class requestContentTask extends AsyncTask<String, Void, String> {

        List<NameValuePair> nameValuePairs;
        protected Activity activity;

        public requestContentTask(Activity activity, List<NameValuePair> nameValuePairs) {

            Log.d("doinbacktamer", "OUI");
            this.nameValuePairs = nameValuePairs;

            Log.d("doinbacktamer", "OUI");
            this.activity = activity;

            Log.d("doinbacktamer", "OUI");
        }

        @Override
        protected String doInBackground(String... url) {
            Log.d("doinbacktamer", "OUI");
            HttpClient httpclient = new DefaultHttpClient();
            String result = null;
            HttpPost httppost = new HttpPost(url[0]);
            HttpResponse response = null;

            InputStream instream = null;
            try {
                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                response = httpclient.execute(httppost);

                HttpEntity entity = response.getEntity();
                if (entity != null) {
                    instream = entity.getContent();
                    result = convertStreamToString(instream);
                    Log.d("Json", result);
                }
            } catch (Exception e) {
                result = e.getMessage() + " #745"; //code erreur perso pour test
            }
            return result;
        }


        @Override
        protected void onPostExecute(String result) {
            try {
                JSONObject json = new JSONObject(result);
                if (!result.contains("#745") && json.getString("success").equalsIgnoreCase("true")) {
                    // Construction de mon objet Recipe à partir des données Json
                    List<Recipe> recipes = new ArrayList<>();

                   for (int i =0 ; i<json.getJSONArray("recipes").length(); i++){
                      JSONObject temp = new JSONObject(json.getJSONArray("recipes").get(i).toString());
                      recipes.add(new Recipe(temp));
                   }
                   // todo faire la liste des recettes
                    ArrayList<RowRecipe> rowRecipes = new ArrayList<RowRecipe>();

                    for (Recipe r : recipes){
                        rowRecipes.add(new RowRecipe(r.getPicture(), r.getName(), r.getType(), r.getRate()));
                    }
                    Log.d("SIZE DE CETTE DE L", String.valueOf(recipes.size()));
                    setAdapter(new RowRecipeAdapter(getContext(), rowRecipes));
                    listRecipe.setAdapter(adapter);

                   //Intent intent = new Intent(activity.getBaseContext(), RecipesActivity.class);
                 //   intent.putExtra("currentUser", u);

                  //  activity.startActivity(intent);
                }
                else {
                    messageAlert("Erreur","Utilisateur introuvable, \rVeuillez rééssayer.", activity);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            } // try / catch obligatoire pour objet JSONObject

           // activity.findViewById(R.id.PBLogin).setVisibility(View.GONE);
        }
    }

}
