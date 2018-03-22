package com.jd.fill2.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;

import com.jd.fill2.R;

import java.util.Random;

/**
 * Created by houhuang on 18/3/21.
 */
public class ItemView extends View {

    private Context mContext;
    private int mTag = 9;
    private float mRadios;

    private static int mStartItemIndex = 0;

    private static Bitmap mBlackBitmap;
    private static Bitmap mWhiteBitmap;

    private static Bitmap mBlackBitmap2;
    private static Bitmap mWhiteBitmap2;

    private static Bitmap mBlackBitmap3;
    private static Bitmap mWhiteBitmap3;

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
        if (mBlackBitmap == null)
        {
            mBlackBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.black);
            mWhiteBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.white);

            mBlackBitmap2 = BitmapFactory.decodeResource(getResources(), R.drawable.black1);
            mWhiteBitmap2 = BitmapFactory.decodeResource(getResources(), R.drawable.black1);

            mBlackBitmap3 = BitmapFactory.decodeResource(getResources(), R.drawable.black2);
            mWhiteBitmap3 = BitmapFactory.decodeResource(getResources(), R.drawable.black2);
        }

        Random random = new Random();
        mStartItemIndex = random.nextInt(2);

    }

    public void setItemTag(int tag)
    {
        mTag = tag;

        postInvalidate();
    }

    public int getItemTag()
    {
        return mTag;
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

    public void reverse(boolean isNoReverse)
    {
        if (mTag == 0 || mTag == 2)
        {
            if (mTag == 2 && isNoReverse)
                mTag = 2;
            else
                mTag = 1;
        }else if (mTag == 1 || mTag == 3)
        {
            if (mTag == 3 && isNoReverse)
                mTag = 3;
            else
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
            switch (mTag)
            {
                case 0:
                    // 指定图片绘制区域(左上角的四分之一)
                    Rect src = new Rect(0,0,mBlackBitmap.getWidth(),mBlackBitmap.getHeight());
                    // 指定图片在屏幕上显示的区域
                    Rect dst = new Rect(0, 0, getMeasuredWidth(), getMeasuredWidth());
                    canvas.drawBitmap(mBlackBitmap,src,dst,null);
                    break;

                case 1:
                    Rect src1 = new Rect(0,0,mWhiteBitmap.getWidth(),mWhiteBitmap.getHeight());
                    Rect dst1 = new Rect(0, 0, getMeasuredWidth(), getMeasuredWidth());
                    canvas.drawBitmap(mWhiteBitmap,src1,dst1,null);
                    break;

                case 2:
                    Bitmap bitmap;
                    if (mStartItemIndex == 0)
                    {
                        bitmap = mBlackBitmap2;
                    }else
                    {
                        bitmap = mBlackBitmap3;
                    }

                    Rect src2 = new Rect(0,0,bitmap.getWidth(),bitmap.getHeight());
                    Rect dst2 = new Rect(0, 0, getMeasuredWidth(), getMeasuredWidth());
                    canvas.drawBitmap(bitmap,src2,dst2,null);
                    break;

                case 3:
                    Bitmap bitmap2;
                    if (mStartItemIndex == 0)
                    {
                        bitmap2 = mWhiteBitmap2;
                    }else
                    {
                        bitmap2 = mWhiteBitmap3;
                    }

                    Rect src3 = new Rect(0,0,bitmap2.getWidth(),bitmap2.getHeight());
                    Rect dst3 = new Rect(0, 0, getMeasuredWidth(), getMeasuredWidth());
                    canvas.drawBitmap(bitmap2,src3,dst3,null);
                    break;
                default:
                    break;

            }
        }


    }
}
