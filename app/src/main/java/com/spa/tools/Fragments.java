package com.spa.tools;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;

import com.spa.R;

public class Fragments {
    private static FragmentTransaction ft;
    private static int fragmentid = R.id.main;

    public static void To(FragmentManager fm, Fragment fragment) {
        // TODO Auto-generated method stub
        if (fragment != null) {
            ft = fm.beginTransaction();
            ft.addToBackStack(null);
            ft.replace(fragmentid, fragment).commit();
        }

    }

    public static void replace(FragmentManager fm, Fragment fragment) {
        // TODO Auto-generated method stub
        if (fragment != null) {
            ft = fm.beginTransaction();
            ft.addToBackStack(null);
            ft.replace(fragmentid, fragment).commit();
        }

    }


}
