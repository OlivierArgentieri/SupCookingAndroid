package com.supinfo.supcooking.Fragment;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.supinfo.supcooking.Entity.User;
import com.supinfo.supcooking.R;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

public class Page2 extends Fragment{
    protected View view;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.view = inflater.inflate(R.layout.activity_page2, container, false);
        // construire la vue

        User u = (User) getActivity().getIntent().getSerializableExtra("currentUser");

        List<NameValuePair> nameValuePairs = new ArrayList<>(3);
        nameValuePairs.add(new BasicNameValuePair("action", "getRecipe"));
        //nameValuePairs.add(new BasicNameValuePair("login", ETUsername.getText().toString()));
        //nameValuePairs.add(new BasicNameValuePair("password",  ETPassword.getText().toString()));

       // AsyncTask task = new requestContentTask(this, nameValuePairs);
       // task.execute("http://supinfo.steve-colinet.fr/supcooking/");
        Log.d("StringExtra", u.getUsername());

        return this.view;
    }
}
