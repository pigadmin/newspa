package com.spa.ui.bottom;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.spa.R;
import com.spa.app.App;
import com.spa.ui.BaseFr;

public class OrderFr extends BaseFr {

    private View view;
    private Activity activity;
    private App app;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_order, container, false);
        activity = getActivity();
        app = (App) activity.getApplication();
        return view;
    }
}
