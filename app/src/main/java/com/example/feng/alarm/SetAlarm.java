package com.example.feng.alarm;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.example.feng.mynote.R;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by feng on 16/6/29.
 */
public class SetAlarm extends Fragment implements AdapterView.OnItemClickListener,AdapterView.OnItemLongClickListener {

    private ListView lv_music;

    private SimpleAdapter lv_music_sa;

    private static final String MUSIC_PATCH = new String("/system/media/audio/alarms");

    private MediaPlayer mediaPlayer;

    private Uri uri ;

    private Uri defulturi = Uri.parse("/system/media/audio/alarms/Carbon.ogg");

    private Musicuri muri;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View selectmmusic = inflater.inflate(R.layout.layout_music,container,false);

        initView(selectmmusic);
        muri = new Musicuri();
        return selectmmusic;
    }

    private void initView(View view) {
        lv_music = (ListView) view.findViewById(R.id.lv_music);
        musicList();
        lv_music.setOnItemClickListener(this);
        lv_music.setOnItemLongClickListener(this);

    }

    private void musicList() {
        File music = new File(MUSIC_PATCH);
        List<Map<String,String>> list = new ArrayList<Map<String,String>>();
        if(music.listFiles(new MusicFilter()).length > 0){
            for(File file : music.listFiles(new MusicFilter())){
                Map<String,String> map = new HashMap<String,String>();
                map.put("musicname",file.getName());
                list.add(map);

            }
            lv_music_sa = new SimpleAdapter(getActivity(),list,R.layout.lv_music,new String[]{"musicname"},new int[]{R.id.musicname});
            lv_music.setAdapter(lv_music_sa);
        }

    }
    //单击选中
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Map<String,String> map = (Map<String, String>) parent.getItemAtPosition(position);
        String name = map.get("musicname");
        uri = Uri.parse(MUSIC_PATCH + "/" + name);
        muri.setUri(uri);
        //   Log.i("bofang",":"+ uri + MUSIC_PATCH + name);
        try{
            mediaPlayer = MediaPlayer.create(getActivity(),uri);
            mediaPlayer.setVolume(300,300);
            mediaPlayer.setLooping(false);
        }catch (Exception e){
            Toast.makeText(getActivity(),"音乐文件播放异常",Toast.LENGTH_SHORT);
        }
        mediaPlayer.start();

        CountDownTimer timer = new CountDownTimer(1000,1000) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                mediaPlayer.stop();
            }
        };
        timer.start();

    }

    //长按播放
    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        Map<String,String> map = (Map<String, String>) parent.getItemAtPosition(position);
        final String name = map.get("musicname");
        uri = Uri.parse(MUSIC_PATCH + "/" + name);
        AlertDialog.Builder adb = new AlertDialog.Builder(getActivity());
        adb.setTitle("提示消息");
        adb.setMessage("确定将" + name + "设为闹铃提示音?");
        adb.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                muri.setUri(uri);
                Toast.makeText(getActivity(), "设置成功", Toast.LENGTH_SHORT).show();
                getActivity().finish();
                Log.i("murl",""+muri.getUri());
            }
        });
        adb.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                muri.setUri(defulturi);
                Toast.makeText(getActivity(), "设置默认歌曲", Toast.LENGTH_SHORT).show();
                getActivity().finish();
            }
        });
        adb.show();
        return true;
    }
}
class MusicFilter implements FilenameFilter{


    @Override
    public boolean accept(File dir, String filename) {
        return (filename.endsWith(".ogg"));
    }
}
