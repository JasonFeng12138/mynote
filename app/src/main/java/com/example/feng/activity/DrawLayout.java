package com.example.feng.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.feng.mynote.R;

/**
 * Created by feng on 16/7/1.
 */
public class DrawLayout extends Fragment implements View.OnClickListener{

    private TextView tv_signin;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.layout_darw,container,false);
        initView(v);
        return v;
    }

    private void initView(View v) {
        tv_signin = (TextView) v.findViewById(R.id.draw_sign_in);
        tv_signin.setOnClickListener(this);

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.draw_sign_in:
                Intent intent = new Intent(getActivity(),SecendActivity.class);
                intent.putExtra("flag",5);
                startActivityForResult(intent,1);
                break;
        }

    }
}
