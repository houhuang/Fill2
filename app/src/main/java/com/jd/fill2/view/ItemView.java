package com.jd.fill2.view;

import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.AnimationSet;

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

    private float mValue = 1;

    private static Bitmap mBlackBitmap;
    private static Bitmap mWhiteBitmap;

    private static Bitmap mBlackBitmap2;
    private static Bitmap mWhiteBitmap2;

    private static Bitmap mBlackBitmap3;
    private static Bitmap mWhiteBitmap3;

    private static Bitmap mStokeBitmap;

    private boolean mSelected = false;

    private Paint mWhiteDotPaint;
    private Paint mBlackDotPaint;

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
            mWhiteBitmap2 = BitmapFactory.decodeResource(getResources(), R.drawable.white1);

            mBlackBitmap3 = BitmapFactory.decodeResource(getResources(), R.drawable.black2);
            mWhiteBitmap3 = BitmapFactory.decodeResource(getResources(), R.drawable.white2);

            mStokeBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.stoke);
        }

        Random random = new Random();
        mStartItemIndex = random.nextInt(2);

        mWhiteDotPaint = new Paint();
        mWhiteDotPaint.setAntiAlias(true);
        mWhiteDotPaint.setColor(Color.parseColor("#FFFFFF"));
        mWhiteDotPaint.setStyle(Paint.Style.FILL);

        mBlackDotPaint = new Paint();
        mBlackDotPaint.setAntiAlias(true);
        mBlackDotPaint.setColor(Color.parseColor("#000000"));
        mBlackDotPaint.setStyle(Paint.Style.FILL);


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

        mSelected = isClick;

        postInvalidate();
    }

    public void reverse(boolean isNoReverse)
    {
        if (mTag == 0 || mTag == 2)
        {
            if (mTag == 2 && isNoReverse)
                mTag = 3;
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

        showAnimation();
    }

    private void showAnimation()
    {
        ValueAnimator animator = ValueAnimator.ofFloat(0, 1);
        animator.setDuration(1000);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mValue = (float)animation.getAnimatedValue();
                Log.d("UUUU", String.valueOf(mValue));

                setAlpha(mValue);
                postInvalidate();
            }
        });

        animator.start();

    }

    public void exactChooseAnimation()
    {
        final ValueAnimator ani1 = ValueAnimator.ofFloat(1, 1.1f);
        ani1.setDuration(100);
        ani1.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (float)animation.getAnimatedValue();
                setScaleX(value);
                setScaleY(value);
            }
        });

        final ValueAnimator ani2 = ValueAnimator.ofFloat(1.1f, 1);
        ani2.setDuration(100);
        ani2.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (float)animation.getAnimatedValue();
                setScaleX(value);
                setScaleY(value);
            }
        });

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.play(ani1).before(ani2);
        animatorSet.start();

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, widthMeasureSpec);

        mRadios = (float) (getMeasuredWidth() * 0.4);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int width = (int)(1.0 * getMeasuredWidth());

        if (mTag != 9)
        {
            switch (mTag)
            {
                case 0:
                    // 指定图片绘制区域(左上角的四分之一)
                    Rect src = new Rect(0, 0, mBlackBitmap.getWidth(), mBlackBitmap.getWidth());
                    // 指定图片在屏幕上显示的区域
                    Rect dst = new Rect(0, 0, width, width);
                    canvas.drawBitmap(mBlackBitmap,src,dst,null);
                    break;

                case 1:
                    Rect src1 = new Rect(0, 0, mWhiteBitmap.getWidth(), mWhiteBitmap.getWidth());
                    Rect dst1 = new Rect(0, 0, width, width);
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

                    Rect src2 = new Rect(0, 0, bitmap.getWidth(), bitmap.getWidth());
                    Rect dst2 = new Rect(0, 0, width, width);
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

                    Rect src3 = new Rect(0, 0, bitmap2.getWidth(), bitmap2.getWidth());
                    Rect dst3 = new Rect(0, 0, width, width);
                    canvas.drawBitmap(bitmap2,src3,dst3,null);
                    break;
                default:
                    break;

            }

            if (mSelected)
            {
                if (mTag == 0 || mTag == 2)
                {
                    canvas.drawCircle(getMeasuredWidth()/2, getMeasuredHeight()/2, getMeasuredWidth()/3, mWhiteDotPaint);

                }else
                {
                    canvas.drawCircle(getMeasuredWidth()/2, getMeasuredHeight()/2, getMeasuredWidth()/3, mBlackDotPaint);
                }

                Rect src3 = new Rect(0, 0, mStokeBitmap.getWidth(), mStokeBitmap.getWidth());
                Rect dst3 = new Rect((int)(width * 0.1), (int)(width * 0.1), (int)(width * 0.9), (int)(width * 0.9));
                canvas.drawBitmap(mStokeBitmap,src3,dst3,null);
            }
        }

    }
}
