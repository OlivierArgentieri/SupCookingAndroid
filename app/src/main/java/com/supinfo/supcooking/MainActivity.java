package com.supinfo.supcooking;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

import com.supinfo.supcooking.Persist.SQLiteHelper;

public class MainActivity extends AppCompatActivity {

    SQLiteHelper db;
    EditText emailView;
    EditText passwordView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        db = new SQLiteHelper(this);
        db.getAll();
        this.emailView = findViewById(R.id.ETEmail);
        this.passwordView = findViewById(R.id.ETPassword);
    }

    public void onClickLogin(View v){
       Intent intent = new Intent(this, RecipesActivity.class);
       startActivity(intent);
    }

    public void onClickRegister(View v){
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }
}
