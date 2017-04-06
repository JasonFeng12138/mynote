package com.example.feng.memo;

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
public class MemoFragment extends Fragment implements AdapterView.OnItemLongClickListener,AdapterView.OnItemClickListener{

    private ListView lv_memo;

    private SimpleAdapter lv_memo_adapter;

    private MyDataBase myDataBase;

    private FragmentListener listener;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View memo = inflater.inflate(R.layout.layout_memo, container, false);



        lv_memo = (ListView) memo.findViewById(R.id.memo_lv_content);

        showData();

        lv_memo.setOnItemClickListener(this);
        lv_memo.setOnItemLongClickListener(this);
        lv_memo.setAdapter(lv_memo_adapter);

        Log.i("memofragment", "createview");

        return memo;
    }

    //展示数据
    public void showData(){
        Log.i("memofragment","showdata");
        myDataBase = new MyDataBase(this.getActivity());
        SQLiteDatabase sdb = myDataBase.getReadableDatabase();
        Cursor selectCursor = sdb.query("Memo", new String[]{"memoId", "memoContent", "memoTime"}, null, null, null, null, "memoId asc");

        List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();

        while(selectCursor.moveToNext()){
            Map<String,Object> map = new HashMap<String,Object>();

            String strContent = selectCursor.getString(selectCursor.getColumnIndex("memoContent"));
            strContent = strContent.replaceAll("\\r|\\n","");
            if(strContent.length() > 15){
                map.put("memoContent",strContent.substring(0,15) + "......");
            }else{
                map.put("memoContent",strContent);
            }
            map.put("memoTime",selectCursor.getString(selectCursor.getColumnIndex("memoTime")));
            map.put("memoId",selectCursor.getInt(selectCursor.getColumnIndex("memoId")));
            list.add(map);
        }
        selectCursor.close();
        sdb.close();

        lv_memo_adapter = new SimpleAdapter(this.getActivity(),list,R.layout.lv_memo,new String[]{"memoContent","memoTime"},new int[]{R.id.memo_tv_title,R.id.memo_tv_date});
        //lv_memo_adapter.notifyDataSetChanged();

    }
    //编辑备忘录

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Map<String,Object> map = (Map<String, Object>) parent.getItemAtPosition(position);
        Intent intent = new Intent(getActivity(),SecendActivity.class);
        intent.putExtra("flag",1);//展示备忘
        intent.putExtra("version",1);
        intent.putExtra("memoId",map.get("memoId").toString());
        startActivityForResult(intent,1);

    }

    //删除备忘

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        final Map<String,Object> map = (Map<String, Object>) parent.getItemAtPosition(position);
        AlertDialog.Builder adb = new AlertDialog.Builder(getActivity());
        adb.setTitle("对备忘录进行操作");
        adb.setItems(new String[]{"删除", "取消"}, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        SQLiteDatabase sdb = myDataBase.getReadableDatabase();
                        sdb.delete("Memo", "memoId = ?", new String[]{map.get("memoId").toString()});
                        Toast.makeText(getActivity(),"删除成功!",Toast.LENGTH_SHORT).show();
                        Log.i("MemoFragment","delete");
 //                       getActivity().setResult(Activity.RESULT_OK);
//                        MainActivity m = new MainActivity();
//                        m.update();
//                        MainActivity m = new MainActivity();
//                        m.update();
//                        getActivity().notify();
                       //回调接口实现更新
                        if(listener != null){
                            listener.onFragmentClickListener();
                        }
                        Log.i("MemoFragment", "mainupdate");
                        break;
                    case 1:
                        break;
                }

            }
        });
        adb.show();
        return true;
    }

    //新建备忘

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            listener = (FragmentListener)activity;
        } catch (Exception e) {
            e.printStackTrace();

        }
    }
}
