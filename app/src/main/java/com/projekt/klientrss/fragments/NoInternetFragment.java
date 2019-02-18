package com.projekt.klientrss.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.projekt.klientrss.MainActivity;
import com.projekt.klientrss.R;

public class NoInternetFragment extends Fragment {

    public Context mContext;
    public LinearLayout myRoot;
    public View root2;
    LinearLayout.LayoutParams layoutParams;

    public NoInternetFragment() {
        layoutParams = new LinearLayout.LayoutParams( LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT  );
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_no_internet, container, false);
        mContext = getContext();
        root2 = rootView;
        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        MainActivity.iwLove.setVisibility(View.INVISIBLE);
        myRoot = (LinearLayout) getView().findViewById(R.id.newestRoot);
    }
}