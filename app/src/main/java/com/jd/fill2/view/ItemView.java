package com.jd.fill2.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;

import com.jd.fill2.R;

/**
 * Created by houhuang on 18/3/21.
 */
public class ItemView extends View {

    private Context mContext;
    private Paint mPaint;
    private Paint mPaint2;
    private int mTag = 9;
    private float mRadios;


    public ItemView(Context context) {
        super(context);
        mContext = context;
        initContent();

    }

    public ItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        initContent();
    }

    private void initContent()
    {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.FILL);

        mPaint2 = new Paint();
        mPaint2.setAntiAlias(true);
        mPaint2.setStyle(Paint.Style.STROKE);

    }

    public void setItemTag(int tag)
    {
        mTag = tag;

        if (tag == 0 || tag == 2)
        {
            mPaint.setColor(ContextCompat.getColor(mContext, R.color.color_black));
            mPaint2.setColor(ContextCompat.getColor(mContext, R.color.color_black));

        }else if (tag == 1 || tag == 3)
        {
            mPaint.setColor(ContextCompat.getColor(mContext, R.color.color_white));
            mPaint2.setColor(ContextCompat.getColor(mContext, R.color.color_white));
        }else if (tag == 9)
        {
            mPaint.setColor(ContextCompat.getColor(mContext, R.color.transpatent));
        }

        postInvalidate();
    }


    public void setItemIsclicked(boolean isClick)
    {
        if (isClick)
        {
            mRadios = (float) (getMeasuredWidth() * 0.45);
        }else
        {
            mRadios = (float) (getMeasuredWidth() * 0.4);
        }

        postInvalidate();
    }

    public void reverse()
    {
        if (mTag == 0 || mTag == 2)
        {
            mTag = 1;
        }else if (mTag == 1 || mTag == 3)
        {
            mTag = 0;
        }

        setItemIsclicked(false);
        setItemTag(mTag);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, widthMeasureSpec);

        mRadios = (float) (getMeasuredWidth() * 0.4);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (mTag != 9)
        {
            if (mTag == 2 || mTag == 3)
            {
                float radios = (float) (getMeasuredWidth() * 0.3);
                mPaint2.setStrokeWidth((float) (getMeasuredWidth()*0.2));
                canvas.drawCircle(getMeasuredWidth()/2, getMeasuredHeight()/2, radios, mPaint2);
            }else
            {
                canvas.drawCircle(getMeasuredWidth()/2, getMeasuredHeight()/2, mRadios, mPaint);
            }
        }


    }
}
