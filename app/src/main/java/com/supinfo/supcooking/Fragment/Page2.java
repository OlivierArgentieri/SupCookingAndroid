package com.supinfo.supcooking.Fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.supinfo.supcooking.Entity.User;
import com.supinfo.supcooking.R;

public class Page2 extends Fragment{
    protected View view;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.view = inflater.inflate(R.layout.activity_page2, container, false);
        // construire la vue

        User u = (User) getActivity().getIntent().getSerializableExtra("currentUser");

        Log.d("StringExtra", u.getUsername());


        return this.view;
    }
}
