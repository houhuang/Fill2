package com.jd.fill2.bean;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;


/**
 * Created by Administrator on 2018/3/11 0011.
 */

public class LeftAndTopView extends View {
    public enum ItemType{
        Vertical,
        Level,
        LeftAndTop,
        LeftAndBottom,
        RightAndTop,
        RightAndBottom
    }

    public enum ItemDir{
        TOP,
        BOTTOM,
        LEFT,
        RIGHT,
        NONE
    }

    private Paint mPaint;
    private ItemType mItemType;
    private int mColor;

    private boolean DrawTop = false;
    private boolean DrawBottom = false;
    private boolean DrawLeft = false;
    private boolean DrawRight = false;

    private static final double ITEM_WIDTH = 0.75;

    private ItemDir mItemDIr = ItemDir.NONE;

    private Context mContext;

    public LeftAndTopView(Context context, ItemType type, int color) {
        super(context);
        mContext = context;
        initPainType(type, color);
    }

    public LeftAndTopView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
//        mContext = context;
//        initPainType(ItemType.Level, getResources().getColor(R.color.colorAccent));
    }

    public void initPainType(ItemType type, int color)
    {
        mItemType = type;
        mColor = color;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);


        mPaint = new Paint();
        mPaint.setColor(ContextCompat.getColor(mContext ,mColor));
        mPaint.setStyle(Paint.Style.FILL);


//        draw1(canvas);
        draw2(canvas);

    }

    public void setItemType(ItemType type)
    {
        mItemType = type;
    }

    public void setDrawTop(boolean bool)
    {
        DrawTop = bool;
        if (mItemDIr == ItemDir.NONE)
        {
            mItemDIr = ItemDir.TOP;
        }
    }

    public void setDrawBottom(boolean bool)
    {
        DrawBottom = bool;
        if (mItemDIr == ItemDir.NONE)
        {
            mItemDIr = ItemDir.BOTTOM;
        }
    }

    public void setDrawLeft(boolean bool)
    {
        DrawLeft = bool;
        if (mItemDIr == ItemDir.NONE)
        {
            mItemDIr = ItemDir.LEFT;
        }
    }

    public void setDrawRight(boolean bool)
    {
        DrawRight = bool;
        if (mItemDIr == ItemDir.NONE)
        {
            mItemDIr = ItemDir.RIGHT;
        }
    }

    public void clearNotFirst()
    {
        DrawTop = false;
        DrawBottom = false;
        DrawLeft = false;
        DrawRight = false;

        if (mItemDIr == ItemDir.TOP)
        {
            DrawTop = true;
        }else if (mItemDIr == ItemDir.BOTTOM)
        {
            DrawBottom = true;
        }else if (mItemDIr == ItemDir.LEFT)
        {
            DrawLeft = true;
        }else if (mItemDIr == ItemDir.RIGHT)
        {
            DrawRight = true;
        }
//        mItemDIr = ItemDir.NONE;

        postInvalidate();
    }

    public void clearDrawDir()
    {
        DrawTop = false;
        DrawBottom = false;
        DrawLeft = false;
        DrawRight = false;

        mItemDIr = ItemDir.NONE;
    }

    private void draw2(Canvas canvas)
    {
        if (DrawTop)
        {
            canvas.drawRect(new Rect((int) (getMeasuredHeight() * (1 - ITEM_WIDTH)),
                    0,
                    (int) (getMeasuredWidth() * ITEM_WIDTH),
                    (int) (getMeasuredHeight() * 0.5)), mPaint);
            canvas.drawCircle(getMeasuredWidth()/2, getMeasuredHeight()/2, getMeasuredWidth()/4, mPaint);

        }

        if (DrawBottom)
        {
            canvas.drawRect(new Rect((int) (getMeasuredHeight() * (1 - ITEM_WIDTH)),
                    (int) (getMeasuredHeight() * 0.5),
                    (int) (getMeasuredWidth() * ITEM_WIDTH),
                    (int) (getMeasuredHeight() * 1.0)), mPaint);
            canvas.drawCircle(getMeasuredWidth()/2, getMeasuredHeight()/2, getMeasuredWidth()/4, mPaint);

        }

        if (DrawLeft)
        {
            canvas.drawRect(new Rect(0,
                    (int) (getMeasuredHeight() * (1 - ITEM_WIDTH)),
                    (int) (getMeasuredWidth() * 0.5),
                    (int) (getMeasuredHeight() * ITEM_WIDTH)), mPaint);
            canvas.drawCircle(getMeasuredWidth()/2, getMeasuredHeight()/2, getMeasuredWidth()/4, mPaint);

        }

        if (DrawRight)
        {
            canvas.drawRect(new Rect((int) (getMeasuredHeight() * 0.5),
                    (int) (getMeasuredHeight() * (1 - ITEM_WIDTH)),
                    (int) (getMeasuredWidth() * 1.0),
                    (int) (getMeasuredHeight() * ITEM_WIDTH)), mPaint);
            canvas.drawCircle(getMeasuredWidth()/2, getMeasuredHeight()/2, getMeasuredWidth()/4, mPaint);

        }


        mPaint.setColor(Color.WHITE);
        if (DrawTop)
        {
            canvas.drawLine((int)(getMeasuredHeight() * 0.5), 0,
                    (int) (getMeasuredWidth() * 0.5),
                    (int) (getMeasuredWidth() * 0.5), mPaint);
        }

        if (DrawBottom)
        {
            canvas.drawLine((int)(getMeasuredHeight() * 0.5), (int)(getMeasuredHeight() * 0.5),
                    (int) (getMeasuredWidth() * 0.5),
                    (int) (getMeasuredWidth() * 1.0), mPaint);
        }

        if (DrawLeft)
        {
            canvas.drawLine(0, (int)(getMeasuredHeight() * 0.5),
                    (int) (getMeasuredWidth() * 0.5),
                    (int) (getMeasuredWidth() * 0.5), mPaint);
        }

        if (DrawRight)
        {
            canvas.drawLine((int)(getMeasuredHeight() * 0.5), (int)(getMeasuredHeight() * 0.5),
                    (int) (getMeasuredWidth() * 1.0),
                    (int) (getMeasuredWidth() * 0.5), mPaint);
        }


    }

    private void draw1(Canvas canvas)
    {
        switch (mItemType) {
            case Level:
                canvas.drawRect(new Rect(0,
                        (int) (getMeasuredHeight() * (1 - ITEM_WIDTH)),
                        (int) (getMeasuredWidth() * 1.0),
                        (int) (getMeasuredHeight() * ITEM_WIDTH)), mPaint);

                mPaint.setColor(Color.WHITE);
                canvas.drawLine(0, (int) (getMeasuredHeight() * 0.5),
                        (int) (getMeasuredWidth() * 1.0),
                        (int) (getMeasuredWidth() * 0.5), mPaint);
                break;

            case Vertical:
                canvas.drawRect(new Rect((int) (getMeasuredWidth() * (1 - ITEM_WIDTH)),
                        0,
                        (int) (getMeasuredWidth() * ITEM_WIDTH),
                        (int) (getMeasuredHeight() * 1.0)), mPaint);

                mPaint.setColor(Color.WHITE);
                canvas.drawLine((int) (getMeasuredWidth() * 0.5), 0,
                        (int) (getMeasuredWidth() * 0.5),
                        (int) (getMeasuredWidth() * 1.0), mPaint);
                break;

            case LeftAndTop:
                canvas.drawRect(new Rect(0,
                        (int) (getMeasuredHeight() * (1 - ITEM_WIDTH)),
                        (int) (getMeasuredWidth() * ITEM_WIDTH),
                        (int) (getMeasuredHeight() * ITEM_WIDTH)), mPaint);

                canvas.drawRect(new Rect((int) (getMeasuredWidth() * (1 - ITEM_WIDTH)), 0,
                        (int) (getMeasuredWidth() * ITEM_WIDTH),
                        (int) (getMeasuredHeight() * (1 - ITEM_WIDTH))), mPaint);


                mPaint.setColor(Color.WHITE);
                canvas.drawLine((int) (getMeasuredHeight() * 0.5), 0,
                        (int) (getMeasuredWidth() * 0.5),
                        (int) (getMeasuredWidth() * 0.5), mPaint);


                canvas.drawLine(0, (int) (getMeasuredHeight() * 0.5),
                        (int) (getMeasuredWidth() * 0.5),
                        (int) (getMeasuredWidth() * 0.5), mPaint);
                break;

            case LeftAndBottom:
                canvas.drawRect(new Rect(0,
                        (int) (getMeasuredHeight() * (1 - ITEM_WIDTH)),
                        (int) (getMeasuredWidth() * ITEM_WIDTH),
                        (int) (getMeasuredHeight() * ITEM_WIDTH)), mPaint);

                canvas.drawRect(new Rect((int) (getMeasuredWidth() * (1 - ITEM_WIDTH)), (int) (getMeasuredHeight() * 1.0),
                        (int) (getMeasuredWidth() * ITEM_WIDTH),
                        (int) (getMeasuredHeight() * ITEM_WIDTH)), mPaint);


                mPaint.setColor(Color.WHITE);
                canvas.drawLine((int) (getMeasuredHeight() * 0.5), (int) (getMeasuredHeight() * 1.0),
                        (int) (getMeasuredWidth() * 0.5),
                        (int) (getMeasuredWidth() * 0.5), mPaint);


                canvas.drawLine(0, (int) (getMeasuredHeight() * 0.5),
                        (int) (getMeasuredWidth() * 0.5),
                        (int) (getMeasuredWidth() * 0.5), mPaint);
                break;

            case RightAndTop:
                canvas.drawRect(new Rect((int) (getMeasuredHeight() * (1 - ITEM_WIDTH)),
                        (int) (getMeasuredHeight() * (1 - ITEM_WIDTH)),
                        (int) (getMeasuredWidth() * 1.0),
                        (int) (getMeasuredHeight() * ITEM_WIDTH)), mPaint);

                canvas.drawRect(new Rect((int) (getMeasuredWidth() * (1 - ITEM_WIDTH)), 0,
                        (int) (getMeasuredWidth() * ITEM_WIDTH),
                        (int) (getMeasuredHeight() * (1 - ITEM_WIDTH))), mPaint);


                mPaint.setColor(Color.WHITE);
                canvas.drawLine((int) (getMeasuredHeight() * 0.5), (int) (getMeasuredHeight() * 0.5),
                        (int) (getMeasuredWidth() * 1.0),
                        (int) (getMeasuredWidth() * 0.5), mPaint);


                canvas.drawLine((int) (getMeasuredHeight() * 0.5), 0,
                        (int) (getMeasuredWidth() * 0.5),
                        (int) (getMeasuredWidth() * 0.5), mPaint);
                break;

            case RightAndBottom:
                canvas.drawRect(new Rect((int) (getMeasuredHeight() * (1 - ITEM_WIDTH)),
                        (int) (getMeasuredHeight() * (1 - ITEM_WIDTH)),
                        (int) (getMeasuredWidth() * 1.0),
                        (int) (getMeasuredHeight() * ITEM_WIDTH)), mPaint);

                canvas.drawRect(new Rect((int) (getMeasuredWidth() * (1 - ITEM_WIDTH)), (int) (getMeasuredWidth() * 1.0),
                        (int) (getMeasuredWidth() * ITEM_WIDTH),
                        (int) (getMeasuredHeight() * ITEM_WIDTH)), mPaint);


                mPaint.setColor(Color.WHITE);
                canvas.drawLine((int) (getMeasuredHeight() * 0.5), (int) (getMeasuredHeight() * 0.5),
                        (int) (getMeasuredWidth() * 1.0),
                        (int) (getMeasuredWidth() * 0.5), mPaint);


                canvas.drawLine((int) (getMeasuredHeight() * 0.5), (int) (getMeasuredHeight() * 0.5),
                        (int) (getMeasuredWidth() * 0.5),
                        (int) (getMeasuredWidth() * 1.0), mPaint);
                break;

            default:
                break;
        }
    }
}
