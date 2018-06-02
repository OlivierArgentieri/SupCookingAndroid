package com.supinfo.supcooking;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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


public class MainActivity extends AppCompatActivity {

    protected SQLiteHelper db;
    protected EditText ETUsername;
    protected EditText ETPassword;
    protected ProgressBar PBLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        db = new SQLiteHelper(this);
        db.getAllUser();
        this.ETUsername = findViewById(R.id.ETLoginUsername);
        this.ETPassword = findViewById(R.id.ETLoginPassword);
        this.PBLogin = findViewById(R.id.PBLogin);
        this.PBLogin.setVisibility(View.INVISIBLE);
    }

    public void onClickLogin(View v) {
        if (ETUsername.getText().toString().trim().isEmpty() || ETPassword.getText().toString().trim().isEmpty()) {
            messageAlert("Tout les champs sont obligatoire, \tmerci de rééssayer.");
        } else {
            if (isNetworkAvailable()) {
                // user de l'api (admin/admin)
                try {
                    this.PBLogin.setVisibility(View.VISIBLE);
                    requestContentTask task = new requestContentTask(ETUsername.getText().toString(), ETPassword.getText().toString(), this);
                    task.execute("http://supinfo.steve-colinet.fr/supcooking/");

                    //URL supintox = new URL("http://supinfo.steve-colinet.fr/supcooking/?action=login&login=" + ETUsername.getText().toString() + "&password=" + ETPassword.getText().toString());
                    //HttpURLConnection cnx = (HttpURLConnection) supintox.openConnection();


                }catch (Exception e){
                    Log.d("Error", e.getMessage());
                }


            } else {
                // user en local
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("Aucune connection internet trouvé, \rl'application va essayé de vous identifier en local.")
                        .setTitle("Information");

                builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        Intent intent = new Intent(MainActivity.super.getBaseContext(), RecipesActivity.class);
                        startActivity(intent);
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();

            }

        }
    }

    public void onClickRegister(View v) {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager cm = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }

    public void messageAlert(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(message)
                .setTitle("Erreur");

        builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }


    @SuppressLint("StaticFieldLeak")
    public class requestContentTask extends AsyncTask<String, Void, String> {
        protected String login;
        protected String password;
        protected Activity activity;

        public requestContentTask(String login, String password, Activity activity) {
            this.login = login;
            this.password = password;
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
                List<NameValuePair> nameValuePairs = new ArrayList<>(3);
                nameValuePairs.add(new BasicNameValuePair("action", "login"));
                nameValuePairs.add(new BasicNameValuePair("login", login));
                nameValuePairs.add(new BasicNameValuePair("password", password));
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

        private String convertStreamToString(InputStream is) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            StringBuilder sb = new StringBuilder();

            String line;
            try {
                while ((line = reader.readLine()) != null) {
                    sb.append(line).append('\n');
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return sb.toString();
        }

        @Override
        protected void onPostExecute(String result) {
            try {
                JSONObject json = new JSONObject(result);
                if (!result.contains("#745") && json.getString("success").equalsIgnoreCase("true")) {
                    // Construction de mon objet User à partir des données Json
                    User u = new User(json);
                    Intent intent = new Intent(activity.getBaseContext(), RecipesActivity.class);
                    intent.putExtra("currentUser", u);
                    activity.startActivity(intent);

                    // Construction de mon objet Recipe à partir des données Json

                }
                else {
                    messageAlert("Utilisateur introuvable, \rVeuillez rééssayer.");
                }
            } catch (JSONException e) {
                e.printStackTrace();
            } // try / catch obligatoire pour objet JSONObject

            activity.findViewById(R.id.PBLogin).setVisibility(View.GONE);
        }
    }
}