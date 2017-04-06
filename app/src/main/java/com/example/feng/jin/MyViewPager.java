package com.example.feng.jin;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.CornerPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by feng on 16/6/11.
 */
public class MyViewPager extends LinearLayout {

    private Paint mPaint;

    private Path mPath;

    private int mTriangleWidth;

    private int mTriangleHeight;

    private static final float RADIO_TEIANGLE_WIDTH = 1/6F;

    private int mInitTranslationX;

    private int mTranslationX;

    private static final int COLOR_TEXT_NORMAL = 0x77FFFFFF;

    private static final int COLOR_TEXT_HEIGHTLIGHT = 0xFFFFFFFF;

    public MyViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);

        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(Color.parseColor("#ffffffff"));
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setPathEffect(new CornerPathEffect(3));
    }

    public MyViewPager(Context context) {

        this(context, null);
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {

        canvas.save();

        //  y  getHeight() + 2
        canvas.translate(mInitTranslationX + mTranslationX, getHeight() + 2);
        canvas.drawPath(mPath,mPaint);

        canvas.restore();


        super.dispatchDraw(canvas);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mTriangleWidth = (int) (w / 3 * RADIO_TEIANGLE_WIDTH);

        // -
        mInitTranslationX = w /3 /2 - mTriangleWidth / 2;

        initTriangle();
    }
    /*
    初始化三角形
     */
    private void initTriangle() {

        mTriangleHeight = mTriangleWidth/2;

        mPath = new Path();
        mPath.moveTo(0, 0);
        mPath.lineTo(mTriangleWidth, 0);
        //mTriangleHeight
        mPath.lineTo(mTriangleWidth / 2, -mTriangleHeight);
        mPath.close();
    }

    /*
    指示器跟随手指滚动
     */
    public void scroll(int position, float offset) {
        int tabWidth = getWidth() / 3;
        mTranslationX = (int) (tabWidth * (offset + position));

        invalidate();
    }
    /*
    高亮某个文本
     */
    public void highlightTextView(int position){
        resetTextViewColor();
        View view = getChildAt(position);
        if(view instanceof TextView){
            ((TextView)view).setTextColor(Color.parseColor("#66ff00"));
        }
    }
    private void resetTextViewColor(){

        for(int i = 0;i < getChildCount();i++){
            View view = getChildAt(i);
            if(view instanceof TextView){
                ((TextView)view).setTextColor(Color.parseColor("#cc33cc33"));
            }
        }
    }

    public void setItemClickEvent(final ViewPager mViewPager){
        int cCount = getChildCount();
        for(int i =0;i < cCount;i++){
            View view = getChildAt(i);
            final int j = i;
            view.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    mViewPager.setCurrentItem(j);
                }
            });
        }
    }
}
