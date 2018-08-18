package com.spa.tools;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;

import com.spa.R;
import com.spa.app.App;

public class Fragments {
    private static FragmentTransaction ft;
    private static int fragmentid = R.id.main;

    public static void To(FragmentManager fm, Fragment fragment) {
        // TODO Auto-generated method stub
        if (fragment != null) {
            ft = fm.beginTransaction();
            ft.replace(fragmentid, fragment);
            ft.addToBackStack(null).commit();
        }

    }

    public static void Replace(FragmentManager fm, Fragment fragment) {
        // TODO Auto-generated method stub
        if (fragment != null) {
            ft = fm.beginTransaction();
            ft.replace(fragmentid, fragment).commit();
        }

    }
    //    public static void replace(FragmentManager fm, Fragment fragment) {
//        // TODO Auto-generated method stub
//        if (fragment != null) {
//            ft = fm.beginTransaction();
//            ft.addToBackStack(null);
//            ft.replace(fragmentid, fragment).commit();
//        }
//
//    }

    public static void hideFragment(App app, Fragment fragment, FragmentTransaction fragmentTransaction) {
        //如果此fragment不为空的话就隐藏起来
        if (fragment != null) {
            fragmentTransaction.hide(fragment).commit();
        }
    }

}
