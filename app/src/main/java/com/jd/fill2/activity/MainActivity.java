package com.jd.fill2.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.jd.fill2.R;
import com.jd.fill2.config.Config;
import com.jd.fill2.fragment.AchievementFragment;
import com.jd.fill2.manager.AdsManager;
import com.jd.fill2.manager.DataManager;
import com.jd.fill2.util.FileUtil;
import com.jd.fill2.util.GeneralUtil;
import com.jd.fill2.util.OkHttpUtil;
import com.jd.fill2.util.StatusBarUtil;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button mBtnPlay;
    private Button mBtnSounds;
    private Button mBtnShare;
    private Button mBtnAchi;

    private ImageView mSoundImage;
    private Context mContext;

    private FragmentTransaction mTransition;
    private AchievementFragment achievementFragment;

    private FrameLayout mAchiBg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mContext = this;
        DataManager.getInstance().initData(this);

        StatusBarUtil.setWindowStatusBarColor(this, R.color.transpatent);
        StatusBarUtil.StatusBarLightMode(this);

        Config.initMusic(getApplicationContext());
        if (Config.mSoundsIsOpen)
            Config.playMusic();

        bindView();

        downloadJsonFile();
        Crashlytics.getInstance();

        AdsManager.initAds(getApplicationContext());
    }

    private void bindView()
    {
        mBtnPlay = (Button)findViewById(R.id.btn_play);
        mBtnShare = (Button)findViewById(R.id.btn_share);
        mBtnSounds = (Button)findViewById(R.id.btn_sound);
        mBtnAchi = (Button)findViewById(R.id.btn_achi);
        mBtnPlay.setOnClickListener(this);
        mBtnShare.setOnClickListener(this);
        mBtnSounds.setOnClickListener(this);
        mBtnAchi.setOnClickListener(this);


        mSoundImage = (ImageView)findViewById(R.id.image_sounds);
        updateSoundState();

        FragmentManager fragmentManager = getSupportFragmentManager();
        mTransition = fragmentManager.beginTransaction();
        achievementFragment = new AchievementFragment();
        mTransition.add(R.id.achievement_fragment_parent, achievementFragment);
        mTransition.commit();

        mAchiBg = (FrameLayout)findViewById(R.id.achievement_fragment_parent);
        mAchiBg.setVisibility(View.INVISIBLE);

        achievementFragment.setOnAchieveListener(new AchievementFragment.OnAchieveClickListener() {
            @Override
            public void OnHomeButton() {
                mAchiBg.setVisibility(View.INVISIBLE);
            }
        });
    }

    private void downloadJsonFile()
    {
        OkHttpUtil.sendOkHttpRequest("https://firebasestorage.googleapis.com/v0/b/fill-5faf3.appspot.com/o/pub.json?alt=media", new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String content = response.body().string();
                FileUtil.saveFileTo_datadata(mContext, "pub.json", content);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.btn_play:
                Config.mRateUsCount++;
                Config.saveConfigInfo();

                Intent intent = new Intent(this, MapActivity.class);
                startActivity(intent);
                break;

            case R.id.btn_share:
                GeneralUtil.shareText(this);
                break;

            case R.id.btn_sound:
                Config.mSoundsIsOpen = !Config.mSoundsIsOpen;
                updateSoundState();
                if (Config.mSoundsIsOpen)
                    Config.playMusic();
                else
                    Config.pauseMusic();
                break;

            case R.id.btn_achi:
                achievementFragment.caculateIQNum();
                mAchiBg.setVisibility(View.VISIBLE);
                break;

            default:
                break;
        }
    }

    public void updateSoundState()
    {
        if (Config.mSoundsIsOpen)
        {
            mSoundImage.setImageBitmap(FileUtil.getBitmapFromDrawable(this, R.drawable.sounds));
        }else
        {
            mSoundImage.setImageBitmap(FileUtil.getBitmapFromDrawable(this, R.drawable.sounds_c));
        }

        Config.saveConfigInfo();
    }

    private boolean isQuit = false;

    @Override
    public void onBackPressed() {

        if (!isQuit)
        {
            Toast.makeText(MainActivity.this, "Please click BACK again to exit game!", Toast.LENGTH_SHORT).show();
            isQuit = true;
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(2000);
                    }catch (InterruptedException e)
                    {
                        e.printStackTrace();
                    }finally {
                        isQuit = false;
                    }
                }
            }).start();
        }else
        {
            System.exit(0);
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Config.stopMusic();
        AdsManager.getRewardAdsInstance().destroy(getApplicationContext());
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (AdsManager.mEnableShowIntertital)
            AdsManager.showIntertitialAd();
        AdsManager.mEnableShowIntertital = false;
    }
}
