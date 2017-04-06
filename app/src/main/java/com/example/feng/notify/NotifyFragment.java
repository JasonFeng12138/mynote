package com.example.feng.notify;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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
 * Created by feng on 16/6/28.
 */

public class NotifyFragment extends Fragment implements AdapterView.OnItemClickListener,AdapterView.OnItemLongClickListener{

    private ListView lv_showdate;

    private MyDataBase myDataBase;

    private SQLiteDatabase sdb;

    private SimpleAdapter lv_notify_adapter;

    private FragmentListener fragmentListener;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View notify = inflater.inflate(R.layout.layout_notify,container,false);

        initview(notify);

        return notify;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            fragmentListener = (FragmentListener)activity;
        } catch (Exception e) {
            e.printStackTrace();

        }
    }

    private void initview(View view) {
        lv_showdate = (ListView) view.findViewById(R.id.notify_lv_content);

        addDate();
        
        lv_showdate.setAdapter(lv_notify_adapter);
        lv_showdate.setOnItemClickListener(this);
        lv_showdate.setOnItemLongClickListener(this);

    }


    /*
    *  "create table Notify(" +
                "notifyId integer primary key autoincrement," +
                "notifyContent text," +
                "notifyTime varchar(20)," +
                "notifyType vachar(6)," +
                "notifyLing int";*/

    private void addDate() {
        myDataBase = new MyDataBase(getActivity());
        sdb = myDataBase.getReadableDatabase();

        Cursor cursor = sdb.query("Notify", new String[]{"notifyId", "notifyContent", "notifyTime","notifyType","notifyLing"}, null, null, null, null, "notifyId asc");
        List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();

        while(cursor.moveToNext()){
            Map<String,Object> map = new HashMap<String,Object>();
            String strContent = cursor.getString(cursor.getColumnIndex("notifyContent"));
            strContent = strContent.replaceAll("\\r|\\n","");
            if(strContent.length() > 15){
                map.put("notifyContent",strContent.substring(0,15) + "......");
            }else{
                map.put("notifyContent",strContent);
            }
            map.put("notifyTime",cursor.getString(cursor.getColumnIndex("notifyTime")));
            map.put("notifyId",cursor.getString(cursor.getColumnIndex("notifyId")));
            map.put("notifyType",cursor.getString(cursor.getColumnIndex("notifyType")));
            String Ling = cursor.getString(cursor.getColumnIndex("notifyLing"));
            if(Ling.equals("0")){
                Ling = "无响铃";
            }else{
                Ling = "响铃";
            }
            map.put("notifyLing", Ling);
            list.add(map);
        }
        cursor.close();
        sdb.close();

        lv_notify_adapter = new SimpleAdapter(this.getActivity(),list,R.layout.lv_notify,new String[]{"notifyContent","notifyTime","notifyType","notifyLing"},new int[]{R.id.notify_tv_title,R.id.notify_tv_date,R.id.notify_tv_type,R.id.notify_tv_ling});
    }
    
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Map<String,Object> map = (Map<String, Object>) parent.getItemAtPosition(position);
        Intent intent = new Intent(getActivity(),SecendActivity.class);
        intent.putExtra("flag",4);
        intent.putExtra("version",1);
        intent.putExtra("notifyId",map.get("notifyId").toString());
        startActivityForResult(intent, 1);
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        final Map<String,Object> map = (Map<String, Object>) parent.getItemAtPosition(position);
        AlertDialog.Builder adb = new AlertDialog.Builder(getActivity());
        adb.setTitle("对提醒进行操作");
        adb.setItems(new String[]{"删除", "取消"}, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        sdb = myDataBase.getReadableDatabase();
                        sdb.delete("Notify", "notifyId = ?", new String[]{map.get("notifyId").toString()});
                        Toast.makeText(getActivity(), "删除成功!", Toast.LENGTH_SHORT).show();
                        //回调接口实现更新
                        if(fragmentListener != null){
                            fragmentListener.onFragmentClickListener();
                        }
                        break;
                    case 1:
                        break;
                }

            }
        });
        adb.show();
        return true;
    }
}
