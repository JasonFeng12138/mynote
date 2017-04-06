package com.example.feng.mynote;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by feng on 16/6/11.
 */
public class VpFragment extends Fragment {
    private int id;
    public static final String BUNDLE_ID = "id";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Bundle bundle = getArguments();
        if(bundle != null){
            id = bundle.getInt(BUNDLE_ID);

        }
        View view = inflater.inflate(id,container,false);
        return view;
    }

    public static VpFragment newInstance(int id){
        Bundle bundle = new Bundle();
        bundle.putInt(BUNDLE_ID, id);
        VpFragment fragment = new VpFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

}
