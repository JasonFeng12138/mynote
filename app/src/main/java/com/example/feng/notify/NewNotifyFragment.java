package com.example.feng.notify;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.feng.activity.ThreadActivity;
import com.example.feng.alarm.AlarmBroad;
import com.example.feng.data.MyDataBase;
import com.example.feng.mynote.R;

import java.util.Calendar;

/**
 * Created by feng on 16/6/28.
 */
public class NewNotifyFragment extends Fragment implements View.OnClickListener,CompoundButton.OnCheckedChangeListener{

    private EditText ed_time,ed_content;

    private CheckBox cb_ling;

    private Spinner sp_type;

    private Calendar calendar;

    private String date,time;

    private int ischecked = 0;

    private MyDataBase myDateBase;

    private SQLiteDatabase sdb;

    private int version;

    private String notifyId;

    private int changeflag = 0;

    private AlarmManager alm;

    private PendingIntent pendingIntent;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View newNotify = inflater.inflate(R.layout.new_notify, container, false);

        version = getActivity().getIntent().getExtras().getInt("version");
        notifyId = getActivity().getIntent().getExtras().getString("notifyId");

        initView(newNotify);

        if(version == 1){
            showData();
        }

        return newNotify;
    }

    private void showData() {
        changeflag = 1;
        ed_time.setEnabled(false);
        ed_time.setFocusable(false);
        myDateBase = new MyDataBase(this.getActivity());
        sdb =myDateBase.getReadableDatabase();
        Cursor cursor = sdb.query("Notify", new String[]{ "notifyContent", "NotifyTime","notifyType","notifyLing"},"notifyId = ?",new String[]{notifyId},null,null,null);
        while(cursor.moveToNext()){
            ed_time.setText(cursor.getString(cursor.getColumnIndex("notifyTime")));
            ed_content.setText(cursor.getString(cursor.getColumnIndex("notifyContent")));
            sp_type.setTag(cursor.getString(cursor.getColumnIndex("notifyType")));
            String Ling = cursor.getString(cursor.getColumnIndex("notifyLing"));
            if(Ling.equals("0")){
                cb_ling.setChecked(false);
            }else{
                cb_ling.setChecked(true);
            }
        }
        cursor.close();
        sdb.close();


    }

    private void initView(View view) {

        ed_time = (EditText) view.findViewById(R.id.notify_et_time);
        ed_content = (EditText) view.findViewById(R.id.notify_et_content);
        cb_ling = (CheckBox) view.findViewById(R.id.notify_cb_ling);
        sp_type = (Spinner) view.findViewById(R.id.notify_sp_type);

        ed_time.setFocusable(false);
        //ed_time.setEnabled(false);
        ed_time.setOnClickListener(this);
        cb_ling.setOnCheckedChangeListener(this);

        ed_content.setFocusable(true);
        ed_content.setFocusableInTouchMode(true);
        ed_content.setOnKeyListener(backListener);

    }
    private  View.OnKeyListener backListener = new View.OnKeyListener(){

        @Override
        public boolean onKey(View v, int keyCode, KeyEvent event) {
            if(event.getAction() == event.ACTION_DOWN){
                if(keyCode == event.KEYCODE_BACK){
                    if(!ed_content.getText().toString().equals("")){
                        if(changeflag == 0){
                            saveDate();
                        }else{
                            upDate(notifyId);
                        }

                    }
                    if(ischecked == 1){
                        setAlarm();
                    }else{

                    }
                    //change date
                    getActivity().setResult(1);
                    getActivity().finish();

                    return false;

                }
            }
            return false;
        }
    };

    private void setAlarm() {
        String[] t = ed_time.getText().toString().trim().split(" ");
        String[] t1 = t[0].split("\\-");
        String[] t2 = t[1].split(":");
        Calendar c = Calendar.getInstance();
        Log.e("t1[0]",""+t1[0]+t1[1]+t1[2]+t2[0]+t2[1]);
        c.set(Integer.parseInt(t1[0]), Integer.parseInt(t1[1]) - 1, Integer.parseInt(t1[2]), Integer.parseInt(t2[0]), Integer.parseInt(t2[1]));
        Intent intent = new Intent(getActivity(), AlarmBroad.class);
        String strContent = ed_content.getText().toString().trim();
//        strContent = strContent.replaceAll("\\r|\\n","");
//        if(strContent.length() > 15){
//            strContent = strContent.substring(0,15) + "......";
//        }
        intent.putExtra("notifyContent",strContent);
        intent.putExtra("Ling",1);
        pendingIntent = PendingIntent.getBroadcast(getActivity(),0,intent,PendingIntent.FLAG_UPDATE_CURRENT);
        //获得闹钟服务
        alm = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
        //设置闹钟
        alm.set(AlarmManager.RTC_WAKEUP,c.getTimeInMillis(),pendingIntent);

    }

    /*  "create table Notify(" +
            "notifyId integer primary key autoincrement," +
            "notifyContent text," +
            "notifyTime varchar(20)," +
            "notifyType vachar(6)," +
            "notifyLing int";*/
    private void saveDate(){
        myDateBase = new MyDataBase(getActivity());
        sdb = myDateBase.getReadableDatabase();

        ContentValues cv = new ContentValues();
        cv.put("notifyContent",ed_content.getText().toString().trim());
        cv.put("notifyTime",ed_time.getText().toString().trim());
        cv.put("notifyType",sp_type.getSelectedItem().toString().trim());
        cv.put("notifyLing",ischecked);
        sdb.insert("Notify", null, cv);
        sdb.close();
        Toast.makeText(getActivity(), "保存成功", Toast.LENGTH_SHORT).show();
    }
    private void upDate(String notifyId){
        myDateBase = new MyDataBase(getActivity());
        sdb = myDateBase.getReadableDatabase();

        ContentValues cv = new ContentValues();
        cv.put("notifyContent",ed_content.getText().toString().trim());
        cv.put("notifyTime",ed_time.getText().toString().trim());
        //cv.put("notifyType",sp_type.getSelectedItem().toString().trim());
        cv.put("notifyLing",ischecked);
        sdb.update("Notify", cv, "notifyId = ?", new String[]{notifyId});
        sdb.close();
        //Toast.makeText(getActivity(), "修改成功", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View v) {
        final int year,month,day,hours,minute;
        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        hours = calendar.get(Calendar.HOUR_OF_DAY);
        minute = calendar.get(Calendar.MINUTE);

        DatePickerDialog dpd = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                String month = " ";
                String day = "";
                if(monthOfYear < 9){
                    month = "0" + (monthOfYear + 1);

                }else{
                    month = monthOfYear + 1 + "";
                }
                if(dayOfMonth < 10){
                    day = "0" + dayOfMonth;
                }else{
                    day = dayOfMonth + "";
                }
                date = year + "-" + month + "-" + day;
                ed_time.setText(date + " " + time);
            }
        },year,month,day);
        dpd.setTitle("设置日期");
        dpd.show();

        TimePickerDialog tpd = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                String hou = " ";
                String min = "";
                if(hourOfDay < 10){
                    hou = "0" + hourOfDay;

                }else{
                    hou = hourOfDay +  "";
                }
                if(minute < 10){
                    min = "0" + minute;
                }else{
                    min = minute + "";
                }
                time = hou + ":" + min;
                ed_time.setText( date + " " + time);
            }
        },hours,minute,true);
        tpd.setTitle("设置时间");
        tpd.show();
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (isChecked){
            ischecked = 1;
            Log.i("ischecked",":" + ischecked);
            if(version != 1){
                Intent intent = new Intent(getActivity(), ThreadActivity.class);
                intent.putExtra("flag", 1);//加载设置音乐fragment
                startActivity(intent);
            }

        }else{
            ischecked = 0;
        }
    }
}
