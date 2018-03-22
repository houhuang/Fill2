package com.jd.fill2.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.GridLayout;
import android.widget.Toast;

import com.jd.fill2.R;
import com.jd.fill2.bean.GameItem;
import com.jd.fill2.bean.GameItemInfo;
import com.jd.fill2.config.Config;
import com.jd.fill2.manager.DataManager;
import com.jd.fill2.util.FileUtil;
import com.jd.fill2.util.ScreenUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Administrator on 2018/3/11 0011.
 */

public class GameView extends GridLayout implements View.OnTouchListener {
    private GameItem[][] mGameMatrix;


    private int mGameVLines;
    private int mGameHLines;

    private int mStartX, mStartY;

    private List<GameItem> mAlreadyClickItem = new ArrayList<GameItem>();
    private GameItem mFirstItem;
    private GameItem mCurrentItem;
    private boolean isClickStartTarget = false;

    private int mNeedClickItem = 0;

    private GameItem clickItem;

    private GameItemInfo mItemInfo;

    private Context mContext;

    private List<Bitmap> mRabishs = new ArrayList<Bitmap>();

    private int mHintIndex = 1;

    private int mTotalTag = 0;

    private int[] mPathColor = {
            R.color.color_paht1,
            R.color.color_paht2,
            R.color.color_paht3,
            R.color.color_paht4,
            R.color.color_paht5,
            R.color.color_paht6,
            R.color.color_paht7,
            R.color.color_paht8,
            R.color.color_paht9,
            R.color.color_paht10};

    public OnGameCompletedListener listener;

    public interface OnGameCompletedListener
    {
        void OnCompleted();
        void OnHintSucc();
    }

    public void setGameViewListener(OnGameCompletedListener listener)
    {
        this.listener = listener;
    }

    public GameView(Context context)
    {
        super(context);
        mContext = context;
        initGameMatrix();
    }

    public GameView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        mContext = context;
        initGameMatrix();
    }

    public void nextGame()
    {

        mHintIndex = 1;
        mNeedClickItem = 0;
        initGameMatrix();

    }

    public void initGameMatrix()
    {
        Random random = new Random();
        int rIdx = random.nextInt(2);

        mAlreadyClickItem.clear();

        mItemInfo = DataManager.getInstance().getmGameInfo().get(Config.mChooseLevel);

        //初始化矩阵
        removeAllViews();


        mGameHLines = mItemInfo.getRow();
        mGameVLines = mItemInfo.getCol();
        mGameMatrix = new GameItem[mGameHLines][mGameVLines];

        setColumnCount(mGameVLines);
        setRowCount(mGameHLines);
        setOnTouchListener(this);

        //初始化View参数
        DisplayMetrics metrics = new DisplayMetrics();
        WindowManager wm = (WindowManager)getContext().getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        display.getMetrics(metrics);

        int sizeWidth = (int)(metrics.widthPixels - 50 * ScreenUtil.getScreenDensity(mContext)) / mGameVLines;
        int sizeHeight = (int)(metrics.heightPixels - 300 * ScreenUtil.getScreenDensity(mContext)) / mGameHLines;

        if (ScreenUtil.isTablet(mContext))
        {
            initGameView(sizeWidth < sizeHeight ? sizeWidth : sizeHeight);
        }else
        {
            int size = mGameVLines;
            if (mGameVLines < 5)
            {
                size = 5;
            }
            initGameView((int)(metrics.widthPixels - 50 * ScreenUtil.getScreenDensity(mContext)) / size);
        }

    }

    private void initGameView(int cardSize)
    {
        boolean isTrue = false;

        Random random = new Random();
        int cIndex = random.nextInt(10);


        removeAllViews();
        GameItem card;
        for (int i = 0; i < mGameHLines; ++i)
        {
            for (int j = 0; j < mGameVLines; ++j)
            {

                if (mGameVLines % 2 == 0)
                {
                    if (j % 2 == 0 && i % 2 != 0 || j % 2 != 0 && i % 2 == 0)
                    {
                        isTrue = true;
                    }else
                    {
                        isTrue = false;
                    }
                }else
                {
                    if ((i * mGameVLines + j) % 2 == 0)
                    {
                        isTrue = true;
                    }else
                    {
                        isTrue = false;
                    }
                }

                card = new GameItem(getContext(), isTrue, mPathColor[cIndex]);
                addView(card, cardSize, cardSize);

                // 初始化GameMatrix全部为0 空格List为所有
                mGameMatrix[i][j] = card;
                mGameMatrix[i][j].setRow(i);
                mGameMatrix[i][j].setCol(j);

                int index = i * mGameVLines + j;
                if (mItemInfo.getState()[index] == 0)
                {
                    ++mTotalTag;
                    mGameMatrix[i][j].setItemTag(0);
                }else if (mItemInfo.getState()[index] == 1)
                {
                    ++mTotalTag;
                    mGameMatrix[i][j].setItemTag(1);
                }else if (mItemInfo.getState()[index] == 2)
                {
                    ++mTotalTag;
                    mGameMatrix[i][j].setItemTag(2);
                }else if (mItemInfo.getState()[index] == 3)
                {
                    ++mTotalTag;
                    mGameMatrix[i][j].setItemTag(3);
                }else if (mItemInfo.getState()[index] == 9)
                {
                    mGameMatrix[i][j].setItemTag(9);

                }


            }
        }

    }


    @Override
    public boolean onTouch(View v, MotionEvent event) {

        switch (event.getAction())
        {
            case MotionEvent.ACTION_DOWN:
            {
                mStartX = (int)event.getX();
                mStartY = (int)event.getY();

                GameItem clickItem = getClickStartTarget(mStartX, mStartY);
                if (clickItem != null && (clickItem.getItemTag() == 2 || clickItem.getItemTag() == 3))
                {
                    isClickStartTarget = true;
                    mCurrentItem = clickItem;
                    mCurrentItem.setIsclicked(true);
                    mAlreadyClickItem.add(mCurrentItem);
                    mCurrentItem.getContentView().exactChooseAnimation();
                }else
                {
                    isClickStartTarget = false;
                }

            }
            break;

            case MotionEvent.ACTION_MOVE:
            {
                GameItem item = getClickStartTarget((int)event.getX(), (int) event.getY());
                if (item != null && item != mCurrentItem && isClickStartTarget && item.getItemTag() != 9)
                {
                    if (!isExistAlReady(item))
                    {
                        if (isAround(item, mAlreadyClickItem.get(mAlreadyClickItem.size() - 1)))
                        {
                            item.setIsclicked(true);
                            mAlreadyClickItem.add(item);
                            mCurrentItem.getContentView().exactChooseAnimation();
                            mCurrentItem = item;
                        }

                    }else
                    {
                        int idx = mAlreadyClickItem.size() - 2;
                        if (idx >= 0)
                        {
                            if (mAlreadyClickItem.get(idx) == item)
                            {
                                int index = idx + 1;
                                mAlreadyClickItem.get(index).setIsclicked(false);
                                mAlreadyClickItem.remove(index);
                                mCurrentItem = mAlreadyClickItem.get(index - 1);
                            }

                        }

                    }

                }
            }
            break;

            case MotionEvent.ACTION_UP:
            {
                if (isClickStartTarget)
                {
                    if (mAlreadyClickItem.size() == 1)
                    {
                        mAlreadyClickItem.get(0).setIsclicked(false);
                    }else
                    {
                        for (int i = 0; i < mAlreadyClickItem.size(); ++i )
                        {
                            if (i == 0)
                            {
                                mAlreadyClickItem.get(i).getContentView().reverse(false);
                            }else
                            {
                                int tag = mAlreadyClickItem.get(i).getContentView().getItemTag();
                                if (tag == 2 || tag == 3)
                                {
                                    mAlreadyClickItem.get(i).getContentView().reverse(true);
                                }else
                                {
                                    mAlreadyClickItem.get(i).getContentView().reverse(false);
                                }
                            }

                        }
                    }

                    if (isCompleted())
                        Toast.makeText(mContext, "Completed!!!", Toast.LENGTH_SHORT).show();

                }
                mAlreadyClickItem.clear();
                isClickStartTarget = false;


            }
            break;
        }

        return true;
    }

    public void hint()
    {
        if (Config.mHintNum == 0)
            return;

        if (mHintIndex > 3 )
        {
            Toast.makeText(mContext, "Three hints a maximum of each level.", Toast.LENGTH_SHORT).show();
//            return;
        }

        int count = mHintIndex * 5;

        if (mItemInfo.getHint().length < 15)
        {
            count = mItemInfo.getHint().length;
            mHintIndex = 10;
        }else if (mHintIndex > 3)
        {
            count = 15;
        }


        for (int i = 0; i < mAlreadyClickItem.size(); ++i)
        {
            mAlreadyClickItem.get(i).clearPathFromDir();
        }
        mAlreadyClickItem.clear();
        mCurrentItem = mFirstItem;
        mCurrentItem.clearPathFromDir();

        mAlreadyClickItem.add(mCurrentItem);
        for (int i = 0; i < count; ++i)
        {
            int row =  mCurrentItem.getRow();
            int col = mCurrentItem.getCol();

            char dir = mItemInfo.getHint()[i];
            if (dir == 'w')
            {
                mCurrentItem.setDrawTop(true);
                mCurrentItem.showPathFromDir();

                GameItem item = mGameMatrix[row - 1][col];
                item.setDrawBottom(true);
                mAlreadyClickItem.add(item);

                mCurrentItem = item;
            }else if (dir == 's')
            {
                mCurrentItem.setDrawBottom(true);
                mCurrentItem.showPathFromDir();

                GameItem item = mGameMatrix[row + 1][col];
                item.setDrawTop(true);
                mAlreadyClickItem.add(item);

                mCurrentItem = item;

            }else if (dir == 'a')
            {
                mCurrentItem.setDrawLeft(true);
                mCurrentItem.showPathFromDir();

                GameItem item = mGameMatrix[row][col - 1];
                item.setDrawRight(true);
                mAlreadyClickItem.add(item);

                mCurrentItem = item;

            }else if (dir == 'd')
            {
                mCurrentItem.setDrawRight(true);
                mCurrentItem.showPathFromDir();

                GameItem item = mGameMatrix[row][col + 1];
                item.setDrawLeft(true);
                mAlreadyClickItem.add(item);

                mCurrentItem = item;
            }
        }

        mCurrentItem.showPathFromDir();

        if (mHintIndex <= 3 || mHintIndex == 10)
        {
            mHintIndex ++;
            --Config.mHintNum;
            Config.saveConfigInfo();

            if (listener != null)
                listener.OnHintSucc();
        }

    }

    private boolean isCompleted()
    {
        int count = 0;
        for (int i = 0; i < mGameHLines; ++i)
        {
            for (int j = 0; j < mGameVLines; ++j)
            {
                if (mGameMatrix[i][j].getItemTag() != 9)
                {
                    count += mGameMatrix[i][j].getItemTag();
                }
            }
        }

        if (count == 0 || count == mTotalTag)
        {
            return true;
        }else
        {
            return false;
        }
    }

    private boolean isAround(GameItem preItem, GameItem nexItem)
    {
        if (preItem.getRow() == nexItem.getRow() &&
                Math.abs(preItem.getCol() - nexItem.getCol()) == 1)
        {
            return true;
        }

        if (preItem.getCol() == nexItem.getCol() &&
                Math.abs(preItem.getRow() - nexItem.getRow()) == 1)
        {
            return true;
        }

        return false;

    }

    private int isMoveDirFromNext(GameItem preItem, GameItem nexItem)
    {
        //0不能移动     1-Right      2-Left     3-Top   4-Bottom
        if ((preItem.getRow() == nexItem.getRow() && Math.abs(preItem.getCol() - nexItem.getCol()) == 1))
        {
            if (preItem.getCol() - nexItem.getCol() > 0)
            {
                return 1;
            }else
            {
                return 2;
            }
        }

        if ((preItem.getCol() == nexItem.getCol() && Math.abs(preItem.getRow() - nexItem.getRow()) == 1))
        {
            if (preItem.getRow() - nexItem.getRow() > 0)
            {
                return 4;
            }else
            {
                return 3;
            }
        }

        return 0;
    }

    private int isMoveDirFromPre(GameItem preItem, GameItem nexItem)
    {
        //0不能移动     1-Right      2-Left     3-Top   4-Bottom
        if ((preItem.getRow() == nexItem.getRow() && Math.abs(preItem.getCol() - nexItem.getCol()) == 1))
        {
            if (preItem.getCol() - nexItem.getCol() > 0)
            {
                return 2;
            }else
            {
                return 1;
            }
        }

        if ((preItem.getCol() == nexItem.getCol() && Math.abs(preItem.getRow() - nexItem.getRow()) == 1))
        {
            if (preItem.getRow() - nexItem.getRow() > 0)
            {
                return 3;
            }else
            {
                return 4;
            }
        }

        return 0;
    }

    private GameItem getClickStartTarget(int posX, int posY)
    {
        for (int i = 0; i < mGameHLines; ++i)
        {
            for (int j = 0; j < mGameVLines; ++j)
            {
                GameItem item = mGameMatrix[i][j];

                if (posX >= item.getLeft() && posX <= item.getRight()
                        && posY >= item.getTop() && posY <= item.getBottom())
                {
                    return item;
                }
            }
        }


        return null;
    }

    private GameItem isClickAlreadyExitTarget(int posX, int posY)
    {


        for (GameItem item : mAlreadyClickItem)
        {
            if (posX >= item.getLeft() && posX <= item.getRight()
                    && posY >= item.getTop() && posY <= item.getBottom())
            {
                return item;
            }
        }

        return null;
    }

    private boolean isExistAlReady(GameItem item)
    {
        for (int i = 0; i < mAlreadyClickItem.size(); ++i)
        {
            if (item == mAlreadyClickItem.get(i))
                return true;
        }

        return false;
    }

//    private GameItem getClickItem(int pox, int poy)
//    {
//        for (int i = 0; i < mGameHLines; ++i)
//        {
//            for (int j = 0; j < mGameVLines; ++j)
//            {
//                if (mGameMatrix[i][j].getItemTag() != 0)
//                {
//                    if (isClickStartTarget(pox, poy, mGameMatrix[i][j]))
//                    {
//                        return mGameMatrix[i][j];
//                    }
//                }
//            }
//        }
//        return null;
//    }

}
