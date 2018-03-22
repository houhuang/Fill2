package com.jd.fill2.bean;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.jd.fill2.R;
import com.jd.fill2.util.FileUtil;
import com.jd.fill2.view.ItemView;

import java.util.Random;

import cn.gavinliu.android.lib.shapedimageview.ShapedImageView;

/**
 * Created by Administrator on 2018/3/11 0011.
 */

public class GameItem extends FrameLayout {
    private boolean isWhite;

    private TextView mTvNum;

    private LayoutParams mParams;

    private Context mContext;

    private int mItemTag = 0;

    private int row = 0;
    private int col = 0;

    private LeftAndTopView mPathView;

    private int mColor;

    private ItemView mContentView;

    public GameItem(Context context, boolean isWhite, int color)
    {
        super(context);
        this.isWhite = isWhite;
        mContext = context;
        mColor = color;

        initCardItem();
    }

    private void initCardItem()
    {

        mContentView = new ItemView(mContext);

//        if (isWhite)
//        {
//            mContentView.setBackgroundColor(ContextCompat.getColor(mContext, R.color.color_bg_white));
//        }else
//        {
//            mContentView.setBackgroundColor(ContextCompat.getColor(mContext, R.color.color_bg_black));
//        }



        LayoutParams vp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        mContentView.setLayoutParams(vp);
        addView(mContentView);

        mPathView = new LeftAndTopView(mContext , LeftAndTopView.ItemType.RightAndBottom, mColor);
        LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        mPathView.setLayoutParams(lp);
        addView(mPathView);
        mPathView.setVisibility(INVISIBLE);

    }


    public void setDrawTop(boolean bool)
    {
        mPathView.setDrawTop(bool);
    }


    public void setDrawBottom(boolean bool)
    {
        mPathView.setDrawBottom(bool);
    }

    public void setDrawLeft(boolean bool)
    {
        mPathView.setDrawLeft(bool);
    }

    public void setDrawRight(boolean bool)
    {
        mPathView.setDrawRight(bool);
    }

    public void showPathFromDir()
    {
        mPathView.postInvalidate();
        mPathView.setVisibility(VISIBLE);
    }

    public void clearPathFromDir()
    {
        mPathView.clearDrawDir();
        mPathView.setVisibility(INVISIBLE);
    }

    public void clearPathNotFirst()
    {
        mPathView.clearNotFirst();
    }

    public void showPath(LeftAndTopView.ItemType itemType)
    {

        mPathView.setItemType(itemType);
        mPathView.postInvalidate();
        mPathView.setVisibility(VISIBLE);
    }

    public void clearPath()
    {
        mPathView.setVisibility(INVISIBLE);
    }


    public int getItemTag()
    {
        return mContentView.getItemTag();
    }

    public void setItemTag(int tag)
    {
        mItemTag = tag;

        mContentView.setItemTag(tag);
    }

    public void setIsclicked(boolean isclicked)
    {
        mContentView.setItemIsclicked(isclicked);
    }


    public ItemView getContentView()
    {
        return mContentView;
    }

    public int getRow()
    {
        return row;
    }

    public void setRow(int row)
    {
        this.row = row;
    }

    public int getCol()
    {
        return col;
    }

    public void setCol(int col)
    {
        this.col = col;
    }
}
