package com.jd.fill2.bean;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.jd.fill2.R;
import com.jd.fill2.util.FileUtil;

import java.util.Random;

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
    private ImageView contentImage;

    private int mColor;

    private final int[] stone = {R.drawable.ston1, R.drawable.ston2,
            R.drawable.ston3, R.drawable.ston4,
            R.drawable.ston5, R.drawable.ston6,
            R.drawable.ston7, R.drawable.ston8,
            R.drawable.ston9};

    private final int[] rabbit = {R.drawable.rabbit1, R.drawable.rabbit2, R.drawable.rabbit3};

    private Bitmap mRadishWhite;
    private Bitmap mRadishRed;

    public GameItem(Context context, boolean isWhite, int color, Bitmap red, Bitmap white)
    {
        super(context);
        this.isWhite = isWhite;
        mContext = context;
        mColor = color;

        mRadishRed = red;
        mRadishWhite = white;

        initCardItem();
    }

    private void initCardItem()
    {

        View view = new View(mContext);

        if (isWhite)
        {
            view.setBackgroundColor(ContextCompat.getColor(mContext, R.color.color_bg_white));
        }else
        {
            view.setBackgroundColor(ContextCompat.getColor(mContext, R.color.color_bg_black));
        }


        LayoutParams vp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        view.setLayoutParams(vp);
        addView(view);

        mPathView = new LeftAndTopView(mContext , LeftAndTopView.ItemType.RightAndBottom, mColor);
        LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        mPathView.setLayoutParams(lp);
        addView(mPathView);
        mPathView.setVisibility(INVISIBLE);


        contentImage = new ImageView(mContext);
//        LayoutParams layout = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
//        layout.width = getWidth();
//        layout.height = getHeight();
//        contentImage.setLayoutParams(layout);

        addView(contentImage);
    }

    public void setRadishColor(boolean isRed)
    {
        if (mItemTag == 2)
            return;

        if (isRed)
        {
            contentImage.setImageBitmap(mRadishRed);
        }else
        {
            contentImage.setImageBitmap(mRadishWhite);
        }
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
        return mItemTag;
    }

    public void setItemTag(int tag, Bitmap bitmap)
    {
        mItemTag = tag;

        if (mItemTag == 0)
        {
            Random random = new Random();
            int id = random.nextInt(9);

            contentImage.setImageBitmap(FileUtil.getBitmapFromDrawable(mContext, stone[id]));
            float scaleX = 0.7F;
            contentImage.setScaleX(scaleX);
            contentImage.setScaleY(scaleX);
        }else if (mItemTag == 1)
        {
            contentImage.setImageBitmap(bitmap);
            float scaleX = 0.5F;
            contentImage.setScaleX(scaleX);
            contentImage.setScaleY(scaleX);
        }else if (mItemTag == 2)
        {
            Random random = new Random();
            int id = random.nextInt(3);

            contentImage.setImageBitmap(FileUtil.getBitmapFromDrawable(mContext, rabbit[1]));
            float scaleX = 1.0F;
            contentImage.setScaleX(scaleX);
            contentImage.setScaleY(scaleX);
        }

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
