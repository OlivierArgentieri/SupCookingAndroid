package com.supinfo.supcooking;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.JsonReader;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.supinfo.supcooking.Persist.SQLiteHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.Console;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


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
        db.getAll();
        this.ETUsername = findViewById(R.id.ETLoginUsername);
        this.ETPassword = findViewById(R.id.ETLoginPassword);
        this.PBLogin = findViewById(R.id.PBLogin);
        this.PBLogin.setVisibility(View.INVISIBLE);
    }

    public void onClickLogin(View v){
        this.PBLogin.setVisibility(View.VISIBLE);
        if(ETUsername.getText().toString().trim().isEmpty() || ETPassword.getText().toString().trim().isEmpty()){
            messageAlert("Tout les champs sont obligatoire, \tmerci de rééssayer.");
        }
        else{
            if(isNetworkAvailable()){
                // flag pour test user ok
                final boolean[] ok = {false};
                // user de l'api (admin/admin)
                AsyncTask.execute(new Runnable() {
                    @Override
                    public void run() {
                        // Create connection
                        try {
                            URL supintox = new URL("http://supinfo.steve-colinet.fr/supcooking/?action=login&login="+ETUsername.getText().toString()+"&password="+ETPassword.getText().toString());
                            HttpURLConnection cnx =(HttpURLConnection) supintox.openConnection();
                            cnx.setRequestMethod("POST");

                            JSONObject mainObject = new JSONObject(convertStreamToString(cnx.getInputStream()));

                            Log.d("JSon",mainObject.getString("success"));
                            if (mainObject.getString("success").equalsIgnoreCase("true")) {
                                ok[0] = true;
                                // Success
                                Intent intent = new Intent(MainActivity.super.getBaseContext(), RecipesActivity.class);
                                startActivity(intent);
                            }

                            // Error handling code goes here
                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
                if (!ok[0]){
                    messageAlert("Utilisateur introuvable, \rVeuillez rééssayer.");
                }
                   
            }
            else{
                // user en local
                Intent intent = new Intent(MainActivity.super.getBaseContext(), RecipesActivity.class);
                startActivity(intent);
            }

        }
        PBLogin.setVisibility(View.INVISIBLE);
    }

    public void onClickRegister(View v){
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager cm = (ConnectivityManager)this.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }

    public void messageAlert(String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(message)
                .setTitle("Erreur");

        builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });
        AlertDialog dialog =  builder.create();
        dialog.show();
    }

    @NonNull
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
}