package com.example.feng.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;

import com.example.feng.alarm.Alarm;
import com.example.feng.alarm.SetAlarm;
import com.example.feng.mynote.R;

/**
 * Created by feng on 16/6/28.
 */
public class ThreadActivity extends FragmentActivity {

    private Fragment defaultFragment;

    private int flag;

    private FragmentManager manager;

    private FragmentTransaction transaction;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thread);
        Intent intent = getIntent();
        flag = intent.getExtras().getInt("flag");

        inflateFragment();


    }

    private void inflateFragment() {
        manager = getSupportFragmentManager();
        transaction = manager.beginTransaction();
        switch(flag)
        {
            case 1:    //设置闹铃
                defaultFragment = new SetAlarm();
                //Log.i("fragment","jiazai");
                //transaction.replace(R.id.defult_thread_fragment,defaultFragment);
                //Log.i("fragment", "jiazai2");
                break;
            case 2:    //闹铃响
                defaultFragment = new Alarm();
                //transaction.replace(R.id.defult_thread_fragment,defaultFragment);
                break;
        }
        transaction.replace(R.id.defult_thread_fragment,defaultFragment);
        transaction.commit();
        Log.i("fragment", "tijiao");
    }
}
