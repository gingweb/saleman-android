package com.akkaratanapat.altear.vrp_onboard.Activity;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.akkaratanapat.altear.vrp_onboard.R;

/**
 * Created by Ging on 18/11/2017.
 */

public class PlaceListFragment extends Fragment {

    final static String ARG_POSITION = "position";// ** dual
    final static String ARG_TITLE = "title";// ** dual
    int mCurrentPosition = -1;
    final static String TAG = "myappfragment";

    String[] details;
    int imgIDs[] = {
            R.drawable.pict1, R.drawable.pict2,R.drawable.pict3,R.drawable.pict4,
            R.drawable.pict1, R.drawable.pict2,R.drawable.pict3,R.drawable.pict4
    };

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d("Fragment R", "onCreateView");

        if (savedInstanceState != null) {
            mCurrentPosition = savedInstanceState.getInt(ARG_POSITION);
            Log.d(TAG, "cur pos "+ mCurrentPosition);
        }

        return inflater.inflate(R.layout.fragment_place_detail, container, false);

    }



    @Override
    public void onStart(){
        super.onStart();
        Log.d("Fragment R ", "onStart");
        details = new String[] { "ab item 1","item 2 ja" , "android.com", "cca"};



        Bundle args = getArguments();
        if (args != null){
            showDetail(args.getString(ARG_TITLE), args.getInt(ARG_POSITION));
        } else if (mCurrentPosition != -1) {
            showDetail("title", mCurrentPosition);
        }

        if (mCurrentPosition == -1){
            ImageView img = (ImageView)getActivity().findViewById(R.id.imageView1Right);
            img.setImageResource(imgIDs[0]);
        }
    }


    public void showDetail(String title, int position) {
        TextView tv = (TextView) getView().findViewById(R.id.txtDetail1);
        TextView tv2 = (TextView) getView().findViewById(R.id.txtDetail2);
        tv.setText(title);
        tv2.setText(details[position]);

        ImageView img = (ImageView)getActivity().findViewById(R.id.imageView1Right);
        img.setImageResource(imgIDs[position]);
        mCurrentPosition = position;
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(ARG_POSITION, mCurrentPosition);
        Log.d(TAG, "Right - onSaveInstanceState" + mCurrentPosition);
    }

}