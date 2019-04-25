package com.apps.newstudio.tasklist.ui.fragments;

import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.apps.newstudio.tasklist.R;
import com.apps.newstudio.tasklist.ui.views.CustomCircleDiagram;


public class StatisticFragment extends Fragment {


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_statistic, container, false);
        ((CustomCircleDiagram) view.findViewById(R.id.fragment_circle_diagram)).setValues(50, 25, 25);
        return view;
    }


}
