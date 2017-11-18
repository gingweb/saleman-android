package com.akkaratanapat.altear.vrp_onboard.Activity;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.akkaratanapat.altear.vrp_onboard.R;

/**
 * Created by Ging on 18/11/2017.
 */

public class MainActivity extends AppCompatActivity implements PlaceDetailFragment.OnMyListSelectedListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (findViewById(R.id.fragment_container) !=null ) {
            if (savedInstanceState != null) {
                return;
            }


            PlaceDetailFragment actLeft = new PlaceDetailFragment();
            actLeft.setArguments(getIntent().getExtras());

            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction ft = fragmentManager.beginTransaction();

            ft.add(R.id.fragment_container, actLeft);
            ft.commit();
        }
    }


    @Override
    public void onMyListSelected(String title, int position) {

        PlaceListFragment fragRight = (PlaceListFragment)
                getSupportFragmentManager().findFragmentById(R.id.view);


        if (fragRight != null) {
            fragRight.showDetail(title,position);
        } else {
            PlaceListFragment newFragment = new PlaceListFragment();
            Bundle args = new Bundle();
            args.putString(PlaceListFragment.ARG_TITLE, title);
            args.putInt(PlaceListFragment.ARG_POSITION, position);

            newFragment.setArguments(args);

            FragmentTransaction trans = getSupportFragmentManager()
                    .beginTransaction();
            trans.replace(R.id.fragment_container, newFragment);

            trans.addToBackStack(null);
            trans.commit();

        }

    }

}
