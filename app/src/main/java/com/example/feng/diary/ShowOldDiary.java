package com.example.feng.diary;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.example.feng.data.MyDataBase;
import com.example.feng.mynote.R;

/**
 * Created by feng on 16/6/27.
 */
public class ShowOldDiary extends Fragment {

    private MyDataBase myDataBase;

    private SQLiteDatabase sdb;

    private TextView tv_time,tv_weather,tv_mood;

    private EditText ed_show;

    private String diaryId;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View showdiary = inflater.inflate(R.layout.show_diary,container,false);

        diaryId = getActivity().getIntent().getExtras().getString("diaryId");

        initview(showdiary);

        return showdiary;
    }

    private void initview(View v) {
        tv_time = (TextView) v.findViewById(R.id.showdiary_tv_time);
        tv_mood = (TextView) v.findViewById(R.id.showdiary_tv_mood);
        tv_weather = (TextView) v.findViewById(R.id.showdiary_tv_weather);
        ed_show = (EditText) v.findViewById(R.id.edit_showdiary);
        //设置不可编辑
        ed_show.setFocusable(false);
        ed_show.setEnabled(false);
        showDate();
    }

    private void showDate() {
        myDataBase = new MyDataBase(getActivity());
        sdb = myDataBase.getReadableDatabase();
        /* "create table Diary(" +
            "diaryId integer primary key autoincrement," +
            "diaryContent text," +
            "diaryTime varchar(20)," +
            "diaryWeather varchar(6)," +
            "diaryMood varchar(6))"; */
        Cursor cursor = sdb.query("Diary", new String[]{ "diaryContent", "diaryTime","diaryWeather","diaryMood"},"diaryId = ?",new String[]{diaryId},null,null,null);
        while(cursor.moveToNext()){
            tv_time.setText(cursor.getString(cursor.getColumnIndex("diaryTime")));
            tv_weather.setText(cursor.getString(cursor.getColumnIndex("diaryWeather")));
            tv_mood.setText(cursor.getString(cursor.getColumnIndex("diaryMood")));
            ed_show.setText(cursor.getString(cursor.getColumnIndex("diaryContent")));
        }
        cursor.close();
        sdb.close();
    }
}
