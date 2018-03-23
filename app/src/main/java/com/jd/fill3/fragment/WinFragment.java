package com.jd.fill3.fragment;


import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.jd.fill3.R;
import com.jd.fill3.config.Config;
import com.jd.fill3.manager.DataManager;
import com.jd.fill3.util.FileUtil;

/**
 * Created by houhuang on 18/3/14.
 */
public class WinFragment extends Fragment implements View.OnClickListener {

    private Button mHomeButton;
    private Button mShareButton;

    private Button mPlayButton;
    private ImageView mBtnPlayIcon;
    private ImageView mStarIcon;

    private TextView mLevelText;
    private ImageView mHintView;

    private TextView mTitleText;

    private boolean mIsVinContent = true;


    private Bitmap mStarBitmap;
    private Bitmap mStarBitmap1;
    private Bitmap mPlayBitmap;
    private Bitmap mPlayBitmap2;

    public OnWinFragmentListener listener;

    public interface OnWinFragmentListener
    {
        void OnFaild();
        void OnPlay();
        void OnHome();
        void OnShare();

    }

    public void setOnWinFragmentListent(OnWinFragmentListener listent)
    {
        this.listener = listent;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        return super.onCreateView(inflater, container, savedInstanceState);

        View view = inflater.inflate(R.layout.fragment_win, container, false);

        //屏蔽事件
        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });
        bindView(view);
        return view;
    }

    private void bindView(View view)
    {
        mStarBitmap = FileUtil.getBitmapFromDrawable(getContext(), R.drawable.star);
        mStarBitmap1 = FileUtil.getBitmapFromDrawable(getContext(), R.drawable.star_b);
        mPlayBitmap = FileUtil.getBitmapFromDrawable(getContext(), R.drawable.play);
        mPlayBitmap2 = FileUtil.getBitmapFromDrawable(getContext(), R.drawable.reset);

        mLevelText = (TextView)view.findViewById(R.id.fragment_level_text);
        mHintView = (ImageView)view.findViewById(R.id.fragment_hint);
        mTitleText = (TextView)view.findViewById(R.id.win_title);

        mBtnPlayIcon = (ImageView)view.findViewById(R.id.btn_play_icon);
        mStarIcon = (ImageView)view.findViewById(R.id.star_icon);

        mHomeButton = (Button)view.findViewById(R.id.btn_fragment_home);
        mPlayButton = (Button)view.findViewById(R.id.btn_fragment_play);
        mShareButton = (Button)view.findViewById(R.id.btn_fragment_share);
        mHomeButton.setOnClickListener(this);
        mPlayButton.setOnClickListener(this);
        mShareButton.setOnClickListener(this);

        updateContent(true);
    }

    public void updateContent(boolean isWin)
    {
        mIsVinContent = isWin;

        if (isWin)
        {
            mTitleText.setText("CLEAR");
            mTitleText.setTextColor(ContextCompat.getColor(getContext(), R.color.map_item_content_pass));
            mBtnPlayIcon.setImageBitmap(mPlayBitmap);
            mStarIcon.setImageBitmap(mStarBitmap);
        }else
        {
            mTitleText.setText("FAILED");
            mTitleText.setTextColor(ContextCompat.getColor(getContext(), R.color.map_item_content_nopass));
            mBtnPlayIcon.setImageBitmap(mPlayBitmap2);
            mStarIcon.setImageBitmap(mStarBitmap1);
        }

        StringBuilder builder = new StringBuilder();
        builder.append("Level - ").append(Config.mChooseLevel + 1);

        if ( Config.mChooseLevel >= DataManager.getInstance().getmGameInfo().size() - 1 && isWin)
        {
            builder.append("- Max");
        }

        mLevelText.setText(builder.toString());


        if (isWin && (Config.mChooseLevel == Config.mCurrentLevel) &&
                ((Config.mCurrentLevel + 1) % 5 == 0))
        {
            mHintView.setVisibility(View.VISIBLE);
            Config.mHintNum ++;
            Config.saveConfigInfo();

        }else
        {
            mHintView.setVisibility(View.GONE);
        }


    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.btn_fragment_home:
                if (listener != null)
                    listener.OnHome();
                break;
            case R.id.btn_fragment_share:
                if (listener != null)
                    listener.OnShare();
                break;
            case R.id.btn_fragment_play:
                if (listener != null)
                {
                    if (mIsVinContent)
                        listener.OnPlay();
                    else
                        listener.OnFaild();
                }
                break;
            default:
                break;
        }
    }

}
