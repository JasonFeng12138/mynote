package com.example.feng.diary;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.feng.data.MyDataBase;
import com.example.feng.mynote.R;

import java.util.Calendar;

/**
 * Created by feng on 16/6/23.
 */
public class NewDiaryFragment extends Fragment {

    private Spinner sp_weather,sp_mood;

    private EditText ed_diary;

    private TextView tv_time;

    private SQLiteDatabase sdb;

    private MyDataBase myDataBase;

    private String time;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View newdiary = inflater.inflate(R.layout.new_diary,container,false);

        initView(newdiary);

        return newdiary;

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    private void initView(View newdiary) {
        tv_time = (TextView) newdiary.findViewById(R.id.diary_tv_time);
        sp_weather = (Spinner) newdiary.findViewById(R.id.diary_sp_weather);
        sp_mood = (Spinner) newdiary.findViewById(R.id.diary_sp_mood);
        ed_diary = (EditText) newdiary.findViewById(R.id.edit_diary);

        setTime();

        ed_diary.setFocusable(true);
        ed_diary.setFocusableInTouchMode(true);
        ed_diary.setOnKeyListener(backListener);

    }

    //返回键自动保存

    private  View.OnKeyListener backListener = new View.OnKeyListener(){

        @Override
        public boolean onKey(View v, int keyCode, KeyEvent event) {
            if(event.getAction() == event.ACTION_DOWN){
                if(keyCode == event.KEYCODE_BACK){
                    if(!ed_diary.getText().toString().equals("")){
                        saveData();
                    }
                    //change date
//                    Intent intent = new Intent();
//                    intent.putExtra("position",1);
                    getActivity().setResult(1);
                   // getActivity().setResult(Activity.RESULT_OK,intent);
                    getActivity().finish();

                    return false;

                }
            }
            return false;
        }
    };
    //数据保存
    private void saveData() {
        myDataBase = new MyDataBase(getActivity());
        sdb = myDataBase.getReadableDatabase();

        /* "create table Diary(" +
            "diaryId integer primary key autoincrement," +
            "diaryContent text," +
            "diaryTime varchar(20)," +
            "diaryWeather varchar(6)," +
            "diaryMood varchar(6))"; */

        ContentValues cv = new ContentValues();
        cv.put("diaryContent",ed_diary.getText().toString().trim());
        cv.put("diaryTime",tv_time.getText().toString().trim());
        cv.put("diaryWeather",sp_weather.getSelectedItem().toString().trim());
        cv.put("diaryMood",sp_mood.getSelectedItem().toString().trim());
        cv.put("diaryVersion",1);

        sdb.insert("Diary", null, cv);
        sdb.close();
        Toast.makeText(getActivity(), "保存成功", Toast.LENGTH_SHORT).show();


    }
    //时间格式
    private void setTime() {
        String strmonth;
        String strday;
        String strhours;
        String strmin;

        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        int hours = c.get(Calendar.HOUR_OF_DAY);
        int min = c.get(Calendar.MINUTE);
        if(month < 10){
            strmonth = "0" + month;
        }else{
            strmonth = month + "";
        }
        if(day < 10){
            strday = "0" + day;
        }else{
            strday = day +"";
        }
        if(hours < 10){
            strhours = "0" + hours;
        }else{
            strhours = hours + "";
        }
        if(min < 10){
            strmin = "0" + min;
        }else{
            strmin = min + "";
        }

        time = year + "-" + strmonth + "-" + strday + " " + strhours + ":" + strmin;

        tv_time.setText(time);
    }
}
