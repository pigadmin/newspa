package com.spa.ui.bottom;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.spa.R;
import com.spa.ui.BaseFr;

public class OrderFr extends BaseFr {

    private View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_order, container, false);
        return view;
    }
}
