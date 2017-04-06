package com.example.feng.alarm;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.feng.mynote.R;

/**
 * Created by feng on 16/6/30.
 */
public class Alarm extends Fragment implements View.OnClickListener {

    private EditText show;

    private MediaPlayer mediaPlayer;

    private Button cancle;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View alarm = inflater.inflate(R.layout.new_alarm,container,false);

        show = (EditText) alarm.findViewById(R.id.show_notify_content);
        cancle = (Button) alarm.findViewById(R.id.cancle);
        cancle.setOnClickListener(this);
        show.setText(getActivity().getIntent().getStringExtra("content"));
        try{
            mediaPlayer = MediaPlayer.create(getActivity(),Musicuri.getUri());
            mediaPlayer.setVolume(300,300);
            mediaPlayer.setLooping(true);
        }catch (Exception e){
            Toast.makeText(getActivity(),"音乐播放异常",Toast.LENGTH_SHORT).show();
        }
        mediaPlayer.start();
        AlertDialog.Builder adb = new AlertDialog.Builder(getActivity());
        adb.setTitle("提醒");
        adb.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mediaPlayer.stop();
                mediaPlayer.release();
            }
        });
        adb.show();
        return alarm;
    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        getActivity().finish();
    }
}
