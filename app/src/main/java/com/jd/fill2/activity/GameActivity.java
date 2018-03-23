package com.jd.fill2.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.jd.fill2.R;
import com.jd.fill2.config.Config;
import com.jd.fill2.fragment.WinFragment;
import com.jd.fill2.manager.AdsManager;
import com.jd.fill2.manager.DataManager;
import com.jd.fill2.util.GeneralUtil;
import com.jd.fill2.util.StatusBarUtil;
import com.jd.fill2.view.GameView;

public class GameActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView mHint;
    private ImageView mHome;
    private ImageView mVedio;
    private GameView mGameView;

    private Button mBackButton;

    private FragmentTransaction mTransaction;
    private WinFragment mWinFragment;
    private FrameLayout mFragmentParent;

    private TextView mLevelText;
    private TextView mHintText;

    private int mAdsCount = 0;

    private AdView mAdView;
    private AdRequest mAdRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        StatusBarUtil.setWindowStatusBarColor(this, R.color.status_bar_map_color);
        StatusBarUtil.StatusBarLightMode(this);

//        mAdView = (AdView) findViewById(R.id.banner_View);
//        mAdRequest = new AdRequest.Builder().addTestDevice(AdsManager.TEST_DEVICE_ID).build();
//        mAdView.loadAd(mAdRequest);
//        mAdView.setAdListener(new AdListener(){
//
//            @Override
//            public void onAdFailedToLoad(int i) {
//                super.onAdFailedToLoad(i);
//                mAdView.loadAd(mAdRequest);
//            }
//        });

        bindView();
    }

    private void bindView()
    {
        mGameView = (GameView)findViewById(R.id.gameView);
        mGameView.setBackgroundColor(ContextCompat.getColor(this, R.color.transpatent));
        mGameView.setGameViewListener(new GameView.OnGameCompletedListener() {
            @Override
            public void OnCompleted() {

                if (Config.mChooseLevel == Config.mCurrentLevel)
                {
                    Config.mCurrentLevel ++;
                    Config.saveConfigInfo();
                }

                showWinFragment(true);
            }

            @Override
            public void OnHintSucc() {
                mHintText.setText("" + Config.mHintNum);
            }

            @Override
            public void OnFaild() {
                showWinFragment(false);
            }
        });

        mBackButton = (Button)findViewById(R.id.game_back);
        mBackButton.setOnClickListener(this);

        mHint = (ImageView) findViewById(R.id.game_button_hint);
        mHome = (ImageView)findViewById(R.id.game_button_home);
        mVedio = (ImageView)findViewById(R.id.game_button_vedio);
        mHint.setOnClickListener(this);
        mHome.setOnClickListener(this);
        mVedio.setOnClickListener(this);

        mHintText = (TextView)findViewById(R.id.game_hint_num);
        mLevelText = (TextView)findViewById(R.id.game_level_text);

        StringBuilder builder = new StringBuilder();
        builder.append("Level - ").append(Config.mChooseLevel + 1);
        mLevelText.setText(builder.toString());

        mHintText.setText("" + Config.mHintNum);

        FragmentManager fragmentManager = getSupportFragmentManager();
        mTransaction = fragmentManager.beginTransaction();

        mWinFragment = new WinFragment();
        mTransaction.add(R.id.win_fragment_parent, mWinFragment);
        mTransaction.commit();

        mFragmentParent = (FrameLayout)findViewById(R.id.win_fragment_parent);
        mFragmentParent.setForegroundGravity(10);
        hideWinFragment();

        mWinFragment.setOnWinFragmentListent(new WinFragment.OnWinFragmentListener() {
            @Override
            public void OnPlay() {
                if (Config.mChooseLevel < DataManager.getInstance().getmGameInfo().size() - 1)
                {
                    Config.mChooseLevel ++;
                    nextLevel();
                }else
                {
                    finish();
                }

            }

            @Override
            public void OnHome() {
                if (Config.mCurrentLevel > 5)
                    AdsManager.mEnableShowIntertital = true;
                Intent intent = new Intent(GameActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }

            @Override
            public void OnShare() {
                GeneralUtil.shareText(getApplicationContext());
            }

            @Override
            public void OnFaild() {
                nextLevel();
            }
        });

        updateTourist();

    }

    private void nextLevel()
    {
        mHintText.setText("" + Config.mHintNum);

        StringBuilder builder = new StringBuilder();
        builder.append("Level - ").append(Config.mChooseLevel + 1);
        mLevelText.setText(builder.toString());

        mGameView.nextGame();
        hideWinFragment();

        if (Config.mCurrentLevel > 10)
        {

            if (Config.mChooseLevel < 50)
            {
                mAdsCount += 2;
            }else if (Config.mChooseLevel < 250)
            {
                mAdsCount += 4;
            }else if (Config.mChooseLevel < 700)
            {
                mAdsCount += 5;
            }else
            {
                mAdsCount += 10;
            }

            if (mAdsCount >= 10)
            {
                mAdsCount = 0;
                AdsManager.showIntertitialAd();
            }
        }

        updateTourist();
    }

    private void hideWinFragment()
    {
//        mAdView.setVisibility(View.VISIBLE);
        mFragmentParent.setVisibility(View.GONE);
    }

    private void showWinFragment(boolean isWin)
    {
//        mAdView.setVisibility(View.INVISIBLE);
        mWinFragment.updateContent(isWin);
        mFragmentParent.setVisibility(View.VISIBLE);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.game_back:
                if (Config.mCurrentLevel > 10)
                    AdsManager.mEnableShowIntertital = true;
                finish();
                break;
            case R.id.game_button_hint:
                mGameView.hint();
                break;

            case R.id.game_button_home:
                if (Config.mCurrentLevel > 10)
                    AdsManager.mEnableShowIntertital = true;
                Intent intent = new Intent(this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                break;

            case R.id.game_button_vedio:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Do you watch the video ads?")
                        .setMessage("You can get more hint when you watch the video ads.Would you like to watch?")
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                AdsManager.showRewardAds();
                                AdsManager.setRewardDelegate(new AdsManager.RewardAdsDelegate() {
                                    @Override
                                    public void onRewardExpanded() {
                                        Config.mHintNum += 2;
                                        Config.saveConfigInfo();
                                        mHintText.setText("" + Config.mHintNum);
                                    }

                                    @Override
                                    public void onRewardLoadFaild() {
                                        Toast.makeText(GameActivity.this, "Video ad loads failed! You can try later!", Toast.LENGTH_LONG).show();
                                    }
                                });
                            }
                        }).
                        setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        }).show();
                break;
            default:
                break;
        }
    }

    private void updateTourist()
    {
//        ImageView tourist = (ImageView)findViewById(R.id.game_tourist);
//        if (Config.mChooseLevel == 0)
//        {
//            tourist.setVisibility(View.VISIBLE);
//        }else
//        {
//            tourist.setVisibility(View.INVISIBLE);
//        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (Config.mCurrentLevel > 10)
            AdsManager.mEnableShowIntertital = true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
