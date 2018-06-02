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


public class RegisterActivity extends AppCompatActivity {

    SQLiteHelper db;
    EditText ETemail;
    EditText ETpassword;
    EditText ETpseudo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        db = new SQLiteHelper(this);
        db.getAllUser();
        this.ETemail = findViewById(R.id.ETRegisterEmail);
        this.ETpassword = findViewById(R.id.ETRegisterPassword);
        this.ETpseudo = findViewById(R.id.ETRegisterUsername);
    }

    public void onClickRegister(View v){
        User u = new User(ETemail.getText().toString().trim(), ETpassword.getText().toString().trim(), ETpseudo.getText().toString().trim());

        if(ETemail.getText().toString().trim().isEmpty() || ETpassword.getText().toString().trim().isEmpty() || ETpseudo.getText().toString().trim().isEmpty()){
            messageAlert("Tout les champs sont obligatoire, \tmerci de rééssayer.");
        }

        else if(!isEmailValid(ETemail.getText().toString().trim())){
            messageAlert("Votre addresses email ne respecte pas la bonne syntax, \tmerci de rééssayer.");
        }

        else if(db.getUser(u) != null){
           messageAlert("Utilisateur déja éxistant ! \nEssayez de vous connecter.");
        }

        else{
            db.insertUser(u.getPassword(), u.getEmail(), u.getUsername());
        }

    }
    public boolean isEmailValid(String email)
    {
        String regExpn =
                "^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@"
                        +"((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                        +"[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
                        +"([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                        +"[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|"
                        +"([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$";

        CharSequence inputStr = email;

        Pattern pattern = Pattern.compile(regExpn,Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);

        if(matcher.matches())
            return true;
        else
            return false;
    }
    public void onClickLogin(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
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
}
