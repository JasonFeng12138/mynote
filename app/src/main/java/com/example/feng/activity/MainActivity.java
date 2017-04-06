package com.example.feng.activity;

import android.content.Intent;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.example.feng.jin.FragmentListener;
import com.example.feng.jin.MyViewPager;
import com.example.feng.diary.DiaryFragment;
import com.example.feng.memo.MemoFragment;
import com.example.feng.notify.NotifyFragment;
import com.example.feng.mynote.R;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends FragmentActivity implements FragmentListener,View.OnClickListener{

    private ViewPager viewPager;

    private MyViewPager mIndicator;

    private TextView add,opendraw;

    private List<Fragment> mContent = new ArrayList<Fragment>();

    public FragmentPagerAdapter mAdapter;

    private int DefulaPostion;

    private DrawerLayout drawerLayout;

    private TextView login;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
       //需要设置这个 flag 才能调用 setStatusBarColor 来设置状态栏颜色
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        //设置状态栏颜色
        getWindow().setStatusBarColor(Color.parseColor("#996633"));
        setContentView(R.layout.activity_main);
        initView();
        initDatas();



        viewPager.setAdapter(mAdapter);
        viewPager.setCurrentItem(DefulaPostion);

        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                //tabWidth * positionOffset + postion * tabwidth
                mIndicator.scroll(position, positionOffset);
                DefulaPostion = position;

            }

            @Override
            public void onPageSelected(int position) {

                mIndicator.highlightTextView(position);
                DefulaPostion = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        mIndicator.highlightTextView(0);
        mIndicator.setItemClickEvent(viewPager);
        add.setOnClickListener(this);
        opendraw.setOnClickListener(this);
    }

    private void initDatas() {
        MemoFragment memoFragment = new MemoFragment();
        DiaryFragment diaryFragment = new DiaryFragment();
        NotifyFragment notifyFragment = new NotifyFragment();
        mContent.add(memoFragment);
        mContent.add(diaryFragment);
        mContent.add(notifyFragment);

        mAdapter = new MyPagerAdapter(getSupportFragmentManager());

    }

    @Override
    public void onFragmentClickListener() {
        mAdapter.notifyDataSetChanged();
    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        switch(v.getId()){
            case R.id.add:
                switch(DefulaPostion) {
                    case 0:
                        intent.setClass(this, SecendActivity.class);
                        intent.putExtra("flag", 1);
                        intent.putExtra("version", 0);
                        startActivityForResult(intent, 1);
                        break;
                    case 1:
                        intent.setClass(this, SecendActivity.class);
                        intent.putExtra("flag", 2);
                        intent.putExtra("version", 0);
                        startActivityForResult(intent, 1);
                        break;
                    case 2:
                        intent.setClass(this, SecendActivity.class);
                        intent.putExtra("flag", 4);
                        intent.putExtra("version", 0);
                        startActivityForResult(intent, 1);
                        break;
                }
                break;
            case R.id.open_draw:
                drawerLayout.openDrawer(GravityCompat.START);
                break;
        }

    }

    public class MyPagerAdapter extends FragmentPagerAdapter{

        public MyPagerAdapter(android.support.v4.app.FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getItemPosition(Object object) {
            Log.i("getItemPosition","go on");
            return POSITION_NONE;
        }

        @Override
        public Fragment getItem(int position) {
            return mContent.get(position);
        }

        @Override
        public int getCount() {
            return mContent.size();
        }

//        @Override
//        public Object instantiateItem(View container, int position) {
//            Log.i("instantiateItem:","instantiateItem");
//            tagList.add(makeFragmentName(container.getId(),(int)getItemId(position)));
//            return super.instantiateItem(container, position);
//        }
    }



    private void initView() {
        viewPager = (ViewPager) findViewById(R.id.id_viewpager);
        mIndicator = (MyViewPager) findViewById(R.id.my_indicator);
        add = (TextView) findViewById(R.id.add);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        opendraw = (TextView) findViewById(R.id.open_draw);
        login = (TextView) findViewById(R.id.draw_sign_in);
        // drawerLayout.openDrawer(GravityCompat.START);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//        DefulaPostion = data.getExtras().getInt("position");
        Log.i("result", "result");
        if(resultCode == 1){
            mAdapter.notifyDataSetChanged();
        }else if(resultCode == 2){
           // data = getIntent();
            login.setText(data.getExtras().getString("data"));
        }

    }

}
