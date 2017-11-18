package com.akkaratanapat.altear.vrp_onboard.Activity;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

/**
 * Created by Ging on 18/11/2017.
 */

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class PlaceDetailFragment extends ListFragment {
    private static String TAG = "fragRight";
    String [ ] titles;
    //String [] details;

    OnMyListSelectedListener mCallback;


    public interface OnMyListSelectedListener {
        public void onMyListSelected(String title, int position);
    }



    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        titles = new String[ ] { "item 1", "ทดสอบ", "ok3",   "Google.com", "Android.com"};

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),

                android.R.layout.simple_list_item_activated_1, titles);
        setListAdapter(adapter);

    }


    @Override
    public void onStart() {
        super.onStart();
        getListView().setChoiceMode(ListView.CHOICE_MODE_SINGLE);

    }

    @Override
    public void onAttach(Context context){
        super.onAttach(context);

        Log.d(TAG,getActivity().toString());
        try {
            mCallback = (OnMyListSelectedListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException( getActivity().toString() +
                    " must implement on..listener");
        }

    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        mCallback.onMyListSelected(titles[position], position);

    }


}
