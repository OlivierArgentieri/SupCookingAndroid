package com.supinfo.supcooking.Adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import com.supinfo.supcooking.Fragment.Page1;
import com.supinfo.supcooking.Fragment.Page2;
import com.supinfo.supcooking.Fragment.Page3;


public class PagerAdapter extends FragmentPagerAdapter{

    public PagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public Fragment getItem(int arg0) {
        switch (arg0)
        {
            case 0:
                return new Page1();
            case 1:
                return new Page2();
            case 2:
                return new Page1();
          default:
             return null;
        }
    }

}
