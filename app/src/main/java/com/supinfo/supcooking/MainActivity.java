package com.supinfo.supcooking;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.supinfo.supcooking.Entity.User;
import com.supinfo.supcooking.Persist.SQLiteHelper;
import com.supinfo.supcooking.Util.Util;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;

import org.apache.http.client.HttpClient;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import static com.supinfo.supcooking.Util.Util.getStreamFromString;
import static com.supinfo.supcooking.Util.Util.isNetworkAvailable;
import static com.supinfo.supcooking.Util.Util.messageAlert;

public class MainActivity extends AppCompatActivity {

    protected SQLiteHelper db;
    protected EditText ETUsername;
    protected EditText ETPassword;
    protected ProgressBar PBLogin;

    // Pour avoir le context partout dans l`appli
    private static Context context;

    public static Context getAppContext() {
        return MainActivity.context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.db = new SQLiteHelper(this);
        this.ETUsername = findViewById(R.id.ETLoginUsername);
        this.ETPassword = findViewById(R.id.ETLoginPassword);
        this.PBLogin = findViewById(R.id.PBLogin);
        this.PBLogin.setVisibility(View.INVISIBLE);
        context = getApplicationContext();

    }

    public void onClickLogin(View v) {
        if (ETUsername.getText().toString().trim().isEmpty() || ETPassword.getText().toString().trim().isEmpty()) {
            messageAlert("Erreur", "Tout les champs sont obligatoire, \tmerci de rééssayer.", this);
            db.getAllRecipe();
        } else {
            if (isNetworkAvailable(this)) {
                // user de l'api (admin/admin)
                try {
                    this.PBLogin.setVisibility(View.VISIBLE);
                    List<NameValuePair> nameValuePairs = new ArrayList<>(3);
                    nameValuePairs.add(new BasicNameValuePair("action", "login"));
                    nameValuePairs.add(new BasicNameValuePair("login", ETUsername.getText().toString()));
                    nameValuePairs.add(new BasicNameValuePair("password", ETPassword.getText().toString()));

                    // Création de la task
                    requestContentTask task = new requestContentTask(this, nameValuePairs);
                    task.execute("http://supinfo.steve-colinet.fr/supcooking/");

                } catch (Exception e) {
                    Log.d("Error", e.getMessage());
                }
            } else {
                // user en local
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("Aucune connection internet trouvé, \rl'application va essayé de vous identifier en local.").setTitle("Information");
                final User u = db.getUserLogin(ETUsername.getText().toString(), ETPassword.getText().toString());
                builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        // Todo insérer test local cnx user
                        if (u != null) {
                            Intent intent = new Intent(MainActivity.super.getBaseContext(), RecipesActivity.class);
                            intent.putExtra("currentUser", u);
                            startActivity(intent);
                        }
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();
                if (u == null) {
                    messageAlert("Erreur", "Utilisateur introuvable, \rVeuillez rééssayer.", this);
                }
            }
        }
    }

    public void onClickRegister(View v) {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }


    // Class pour traitement des données
    @SuppressLint("StaticFieldLeak")
    public class requestContentTask extends AsyncTask<String, Void, String> {

        List<NameValuePair> nameValuePairs;
        private Activity activity;

        public requestContentTask(Activity activity, List<NameValuePair> nameValuePairs) {
            this.nameValuePairs = nameValuePairs;
            this.activity = activity;
        }

        @Override
        protected String doInBackground(String... url) {
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
                    result = getStreamFromString(instream);
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
                    // Construction de mon objet User à partir des données Json
                    User u = new User(json);
                    db.InsertOrUpdateUser(u);
                    Intent intent = new Intent(activity.getBaseContext(), RecipesActivity.class);

                    intent.putExtra("currentUser", db.getUser(u));

                    db.getAllUser();
                    activity.startActivity(intent);
                } else {
                    messageAlert("Erreur", "Utilisateur introuvable, \rVeuillez rééssayer.", activity);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            } // try / catch obligatoire pour objet JSONObject

            activity.findViewById(R.id.PBLogin).setVisibility(View.GONE);
        }
    }
}