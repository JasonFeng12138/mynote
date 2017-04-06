package com.example.feng.memo;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.feng.data.MyDataBase;
import com.example.feng.mynote.R;

import java.util.Calendar;

/**
 * Created by feng on 16/6/20.
 */
public class NewMemoFragment extends Fragment {
    private EditText editText;

    private TextView tvTime;

    private SQLiteDatabase sdb;

    private MyDataBase myDataBase;

    private String content;

    private String time;

    private int version;

    private String memoId;

    private int new_old = 0;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View newmemo = inflater.inflate(R.layout.new_memo, container, false);

        memoId = getActivity().getIntent().getExtras().getString("memoId");
        version = getActivity().getIntent().getExtras().getInt("version");
        if(version == 0){
            initView(newmemo);
        }else{
            showView(newmemo,memoId);
        }

        return newmemo;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private void showView(View v,String memoId) {
        new_old = 1;

        editText = (EditText) v.findViewById(R.id.edit_memo);
        tvTime = (TextView) v.findViewById(R.id.tv_time);
        tvTime.setVisibility(View.VISIBLE);

        myDataBase = new MyDataBase(this.getActivity());
        sdb = myDataBase.getReadableDatabase();
        Cursor cursor = sdb.query("Memo", new String[]{ "memoContent", "memoTime"},"memoId = ?",new String[]{memoId},null,null,null);
        while(cursor.moveToNext()){
            tvTime.setText(cursor.getString(cursor.getColumnIndex("memoTime")));
            editText.setText(cursor.getString(cursor.getColumnIndex("memoContent")));
        }

        editText.setFocusable(true);
        editText.setFocusableInTouchMode(true);
        editText.setOnKeyListener(backListener);
        sdb.close();
    }

    private void initView(View v) {
        editText = (EditText) v.findViewById(R.id.edit_memo);
        editText.setFocusable(true);
        editText.setFocusableInTouchMode(true);
        editText.setOnKeyListener(backListener);

    }
    private  View.OnKeyListener backListener = new View.OnKeyListener(){

        @Override
        public boolean onKey(View v, int keyCode, KeyEvent event) {
            if(event.getAction() == event.ACTION_DOWN){
                if(keyCode == event.KEYCODE_BACK){
                    if(!editText.getText().toString().equals("")){
                        if(new_old == 0){
                            saveMemo();
                        }else{
                            updataMemo();
                        }
                    }
                    getActivity().setResult(1);
                    getActivity().finish();
                    return false;

                }
            }
            return false;
        }
    };

    private void updataMemo(){
        String strmonth;
        String strday;
        String strhours;
        String strmin;

        myDataBase = new MyDataBase(this.getActivity());
        sdb = myDataBase.getReadableDatabase();
        ContentValues cv = new ContentValues();

        content = editText.getText().toString().trim();

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

        cv.put("memoContent", content);
        cv.put("memoTime", time);
        cv.put("memoVersion",1);//新增版本号为1,需备份

        sdb.update("Memo",cv,"memoId = ?",new String[]{memoId});
        sdb.close();

    }
    private void saveMemo() {
        String strmonth;
        String strday;
        String strhours;
        String strmin;

        myDataBase = new MyDataBase(this.getActivity());
        sdb = myDataBase.getReadableDatabase();
        ContentValues cv = new ContentValues();

        content = editText.getText().toString().trim();

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

        cv.put("memoContent", content);
        cv.put("memoTime", time);
        cv.put("memoVersion",1);

        sdb.insert("Memo", null, cv);
        sdb.close();
        Toast.makeText(getActivity(), "保存成功", Toast.LENGTH_SHORT).show();

    }
}
