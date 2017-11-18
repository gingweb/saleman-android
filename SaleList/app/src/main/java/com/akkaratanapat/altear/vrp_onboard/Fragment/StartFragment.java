package com.akkaratanapat.altear.vrp_onboard.Fragment;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.akkaratanapat.altear.vrp_onboard.Activity.HomeActivity;
import com.akkaratanapat.altear.vrp_onboard.R;
import com.cengalabs.flatui.views.FlatButton;

/**
 * A simple {@link Fragment} subclass.
 */
public class StartFragment extends Fragment {

    HomeActivity activity;
    FlatButton button_start;

    public static StartFragment newInstance(){
        return new StartFragment();
    }

    public StartFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_start, container, false);
        setComponent(rootView);
        return rootView;
    }

    private void setComponent(View view){
        button_start = (FlatButton)view.findViewById(R.id.button_start);
        button_start.getAttributes().setTheme(R.array.custom_gold_dark, getResources());
        button_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.changeFragment("qr");
            }
        });
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity = (HomeActivity) getActivity();
    }
}
