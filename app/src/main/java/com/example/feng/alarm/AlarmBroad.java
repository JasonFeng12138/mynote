package com.example.feng.alarm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;

import com.example.feng.activity.ThreadActivity;

/**
 * Created by feng on 16/6/30.
 */
public class AlarmBroad extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        String content = intent.getStringExtra("notifyContent");
        int Ling = intent.getExtras().getInt("Ling");
        Log.e("广播","已收到广播");

        if(Ling == 1){
            Intent intent1 = new Intent();
            intent1.setClass(context, ThreadActivity.class);
            intent1.putExtra("flag",2);
            intent1.putExtra("content", content);
            intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent1);
        }


    }
}
