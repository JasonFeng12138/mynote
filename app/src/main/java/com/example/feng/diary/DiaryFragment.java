package com.example.feng.diary;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.example.feng.activity.SecendActivity;
import com.example.feng.data.MyDataBase;
import com.example.feng.jin.FragmentListener;
import com.example.feng.mynote.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by feng on 16/6/16.
 */
public class DiaryFragment extends Fragment implements AdapterView.OnItemLongClickListener,AdapterView.OnItemClickListener{

    private MyDataBase myDataBase ;

    private SimpleAdapter lv_diary_adapter;

    private ListView lv_diary;

    private FragmentListener listener;




    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View diary = inflater.inflate(R.layout.layout_diary,container,false);

        lv_diary = (ListView) diary.findViewById(R.id.diary_lv_content);

        showData();

        lv_diary.setAdapter(lv_diary_adapter);
        lv_diary.setOnItemLongClickListener(this);
        lv_diary.setOnItemClickListener(this);

        return diary;
    }
    /* "create table Diary(" +
            "diaryId integer primary key autoincrement," +
            "diaryContent text," +
            "diaryTime varchar(20)," +
            "diaryWeather varchar(6)," +
            "diaryMood varchar(6))"; */
    public void showData(){
        Log.i("message", "showdata");
        myDataBase = new MyDataBase(this.getActivity());
        SQLiteDatabase sdb = myDataBase.getReadableDatabase();
        Cursor selectCursor = sdb.query("Diary", new String[]{"diaryId", "diaryContent", "diaryTime","diaryWeather","diaryMood"}, null, null, null, null, "diaryId asc");

        List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();

        while(selectCursor.moveToNext()){
            Map<String,Object> map = new HashMap<String,Object>();

            String strContent = selectCursor.getString(selectCursor.getColumnIndex("diaryContent"));
            strContent = strContent.replaceAll("\\r|\\n","");
            if(strContent.length() > 15){
                map.put("diaryContent",strContent.substring(0,15) + "......");
            }else{
                map.put("diaryContent",strContent);
            }
            map.put("diaryTime",selectCursor.getString(selectCursor.getColumnIndex("diaryTime")));
            map.put("diaryId",selectCursor.getInt(selectCursor.getColumnIndex("diaryId")));
            map.put("diaryWeather",selectCursor.getString(selectCursor.getColumnIndex("diaryWeather")));
            map.put("diaryMood",selectCursor.getString(selectCursor.getColumnIndex("diaryMood")));
            list.add(map);
            //"diaryContent","diaryTime","diaryWeather","diaryMood"
        }
        selectCursor.close();
        sdb.close();

        lv_diary_adapter = new SimpleAdapter(this.getActivity(),list,R.layout.lv_diary,new String[]{"diaryContent","diaryTime","diaryWeather","diaryMood"},new int[]{R.id.diary_tv_title,R.id.diary_tv_date,R.id.diray_tv_weather,R.id.diary_tv_mood});

    }

    //删除日记操作
    /* "create table Diary(" +
            "diaryId integer primary key autoincrement," +
            "diaryContent text," +
            "diaryTime varchar(20)," +
            "diaryWeather varchar(6)," +
            "diaryMood varchar(6))"; */

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        final Map<String, Object> map = (Map<String, Object>) parent.getItemAtPosition(position);
        AlertDialog.Builder adb = new AlertDialog.Builder(getActivity());
        adb.setTitle("对日记进行操作");
        adb.setItems(new String[]{"删除", "取消"}, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        SQLiteDatabase sdb = myDataBase.getReadableDatabase();
                        sdb.delete("Diary", "diaryId = ?", new String[]{map.get("diaryId").toString()});
                        Toast.makeText(getActivity(), "删除成功!", Toast.LENGTH_SHORT).show();
                        Log.i("DiaryFragment", "delete");
//                        MainActivity m = new MainActivity();
//                        m.update();
                        if(listener != null){
                            listener.onFragmentClickListener();
                        }
                        Log.i("DiaryFragment", "mainupdate");
                        break;
                    case 1:
                        break;
                }

            }
        });
        adb.show();
        return true;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try{
            listener = (FragmentListener) activity;
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Map<String,Object> map = (Map<String, Object>) parent.getItemAtPosition(position);
        Intent intent = new Intent(getActivity(),SecendActivity.class);
        intent.putExtra("flag",3);  //3 展示日记
        intent.putExtra("diaryId",map.get("diaryId").toString());
        startActivity(intent);
    }
}
