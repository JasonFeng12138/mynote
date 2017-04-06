package com.example.feng.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.example.feng.LoginFragment;
import com.example.feng.diary.NewDiaryFragment;
import com.example.feng.memo.NewMemoFragment;
import com.example.feng.notify.NewNotifyFragment;
import com.example.feng.mynote.R;
import com.example.feng.diary.ShowOldDiary;

/**
 * Created by feng on 16/6/21.
 */
public class SecendActivity extends FragmentActivity {

    private Fragment Fragment;

    private int flag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_secend);
        Intent intent = getIntent();
        flag = intent.getExtras().getInt("flag");

        initView();
    }

    private void initView() {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        switch(flag)
        {
            case 1:
                Fragment = new NewMemoFragment();
                break;
            case 2:
                Fragment = new NewDiaryFragment();
                break;
            case 3:
                Fragment = new ShowOldDiary();
                break;
            case 4:
                Fragment = new NewNotifyFragment();
                break;
            case 5:
                Fragment = new LoginFragment();
                break;
        }
        transaction.replace(R.id.defult_fragment,Fragment);
        transaction.commit();

    }

}
