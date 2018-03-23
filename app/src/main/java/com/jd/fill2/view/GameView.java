package com.jd.fill2.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathDashPathEffect;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.support.v4.content.ContextCompat;
import android.support.v4.util.Pair;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.GridLayout;
import android.widget.LinearLayout;
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
    private GameItem mCurrentItem;
    private boolean isClickStartTarget = false;

    private GameItemInfo mItemInfo;

    private Context mContext;

    private boolean mIsHint = false;

    private int mTotalTag = 0;

    private Paint mLinePaint;
    private Path mLinePath;

    private List<Point> mLinePathVer = new ArrayList<Point>();

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
        void OnFaild();
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
        initLinePaintAndPath();
    }

    public GameView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        mContext = context;
        initGameMatrix();
        initLinePaintAndPath();
    }

    private void initLinePaintAndPath()
    {
        Path path = new Path();
        path.addCircle(0, 0, 3, Path.Direction.CW);

        mLinePaint = new Paint();
        mLinePaint.setAntiAlias(true);
        mLinePaint.setColor(Color.RED);
        mLinePaint.setStyle(Paint.Style.STROKE);
        mLinePaint.setStrokeWidth(5);
        mLinePaint.setPathEffect(new PathDashPathEffect(path, 15, 0, PathDashPathEffect.Style.ROTATE));

        mLinePath = new Path();
    }

    public void nextGame()
    {

        mLinePathVer.clear();
        postInvalidate();

        mIsHint = false;
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
        mTotalTag = 0;

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


    private void initHintsData()
    {
        if (mLinePathVer.size() != 0)
            return;

        int row = 0, col = 0;
        boolean jumpOutLoop = false;

        if (mItemInfo.getHint()[0] == '0')
        {
            for (int i = 0; i < mGameHLines; ++i)
            {
                for (int j = 0; j < mGameVLines; ++j)
                {
                    GameItem item = mGameMatrix[i][j];
                    if (item.getItemTag() == 2 || item.getItemTag() == 3)
                    {
                        Point point = new Point();
                        point.set(item.getLeft() + item.getWidth()/2, item.getTop() + item.getHeight()/2);
                        mLinePathVer.add(point);

                        row = item.getRow();
                        col = item.getCol();

                        jumpOutLoop = true;
                        break;
                    }
                }

                if (jumpOutLoop)
                    break;
            }
        }else
        {
            for (int i = mGameHLines - 1; i >= 0; --i)
            {
                for (int j = mGameVLines - 1; j >= 0; --j)
                {
                    GameItem item = mGameMatrix[i][j];
                    if (item.getItemTag() == 2 || item.getItemTag() == 3)
                    {
                        Point point = new Point();
                        point.set(item.getLeft() + item.getWidth()/2, item.getTop() + item.getHeight()/2);
                        mLinePathVer.add(point);

                        row = item.getRow();
                        col = item.getCol();

                        jumpOutLoop = true;
                        break;
                    }
                }

                if (jumpOutLoop)
                    break;
            }
        }

        for (int i = 1; i < mItemInfo.getHint().length; ++i)
        {
            GameItem item;
            Point point;

            if (mItemInfo.getHint()[i] == 'a')
            {
                col -= 1;
                item = mGameMatrix[row][col];
                point = new Point();

            }else if (mItemInfo.getHint()[i] == 's')
            {
                row += 1;
                item = mGameMatrix[row][col];
                point = new Point();

            }else if (mItemInfo.getHint()[i] == 'd')
            {
                col += 1;
                item = mGameMatrix[row][col];
                point = new Point();

            }else
            {
                row -= 1;
                item = mGameMatrix[row][col];
                point = new Point();

            }

            point.set(item.getLeft() + item.getWidth()/2, item.getTop() + item.getHeight()/2);
            mLinePathVer.add(point);
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

                    Point point = new Point();
                    point.set(mCurrentItem.getLeft() + mCurrentItem.getWidth()/2, mCurrentItem.getTop() + mCurrentItem.getHeight()/2);
                    mLinePathVer.add(point);

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

                    if (listener != null)
                    {
                        if (isCompleted() == 1)
                        {
                            listener.OnCompleted();
                        }else if (isCompleted() == 2)
                        {
                            listener.OnFaild();
                        }
                    }

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
        if (mIsHint)
        {
            Toast.makeText(mContext, "It's been hinted!", Toast.LENGTH_SHORT).show();
        }else
        {
            mIsHint = true;
            initHintsData();
            postInvalidate();
        }

    }

    // 0-未结束    1-完成    2-失败
    private int isCompleted()
    {
        boolean isFaild = false;
        int count = 0;
        for (int i = 0; i < mGameHLines; ++i)
        {
            for (int j = 0; j < mGameVLines; ++j)
            {
                if (mGameMatrix[i][j].getItemTag() != 9)
                {
                    count += mGameMatrix[i][j].getItemTag();

                    if (mGameMatrix[i][j].getItemTag() == 2 || mGameMatrix[i][j].getItemTag() == 3)
                        isFaild = true;
                }
            }
        }

        if (count == 0 || count == mTotalTag)
        {
            return 1;
        }else
        {
            if (isFaild)
                return 0;
            else
                return 2;
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


    private boolean isExistAlReady(GameItem item)
    {
        for (int i = 0; i < mAlreadyClickItem.size(); ++i)
        {
            if (item == mAlreadyClickItem.get(i))
                return true;
        }

        return false;
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        mLinePath.reset();

        if (mLinePathVer.size() >= 2)
        {
            mLinePath.moveTo(mLinePathVer.get(0).x, mLinePathVer.get(0).y);

            for (int i = 1; i < mLinePathVer.size(); ++i)
            {
                mLinePath.lineTo(mLinePathVer.get(i).x, mLinePathVer.get(i).y);
            }
        }

        canvas.drawPath(mLinePath, mLinePaint);

    }


}
