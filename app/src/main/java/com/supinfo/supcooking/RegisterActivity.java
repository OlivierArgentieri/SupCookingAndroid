package com.supinfo.supcooking;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

import com.supinfo.supcooking.Entity.User;
import com.supinfo.supcooking.Persist.SQLiteHelper;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.supinfo.supcooking.Util.Util.isEmailValid;
import static com.supinfo.supcooking.Util.Util.messageAlert;


public class RegisterActivity extends AppCompatActivity {
    SQLiteHelper db;
    EditText ETemail;
    EditText ETpassword;
    EditText ETusername;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        db = new SQLiteHelper(this);
        db.getAllUser();
        this.ETemail = findViewById(R.id.ETRegisterEmail);
        this.ETpassword = findViewById(R.id.ETRegisterPassword);
        this.ETusername = findViewById(R.id.ETRegisterUsername);
    }

    public void onClickRegister(View v){

        if(ETemail.getText().toString().trim().isEmpty() || ETpassword.getText().toString().trim().isEmpty() || ETusername.getText().toString().trim().isEmpty()){
            messageAlert("Erreur","Tout les champs sont obligatoire, \tmerci de rééssayer.", this);
        }

        else if(!isEmailValid(ETemail.getText().toString().trim())){
            messageAlert("Erreur","Votre addresses email ne respecte pas la bonne syntax, \tmerci de rééssayer.", this );
        }

        else if(db.getUser(ETemail.getText().toString().trim(),ETusername.getText().toString().trim()) != null){
           messageAlert("Erreur","Utilisateur déja éxistant ! \nEssayez de vous connecter.", this);
        }

        else{
            db.insertUser(ETemail.getText().toString().trim(), ETpassword.getText().toString().trim(), ETusername.getText().toString().trim());
            messageAlert("Information","Utilisateur bien enregistré ! \nEssayez de vous connecter.", this);
        }
    }

    public void onClickLogin(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }


}
