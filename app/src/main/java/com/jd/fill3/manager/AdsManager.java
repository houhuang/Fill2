package com.jd.fill3.manager;

import android.content.Context;
import android.util.Log;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;

/**
 * Created by houhuang on 18/3/15.
 */
public class AdsManager {

    public static Context mContext;

    public static final String MOPUB_ID = "";

    public static final String ADS_VEDIO_ID = "";
    public static final String ADS_FULL_ID = "";

    public static final String TEST_DEVICE_ID = "9B91B1E26590A312CE79F38C409461E9t";

    public static InterstitialAd mInterstitialAd;
    public static RewardedVideoAd mRewardedVideoAd;
    public static AdRequest mAdRequest;

    public static boolean mEnableShowIntertital = false;

    //delegate
    public interface RewardAdsDelegate
    {
        void onRewardExpanded();
        void onRewardLoadFaild();
    }

    public static RewardAdsDelegate mDelegate;

    public static void setRewardDelegate(RewardAdsDelegate delegate)
    {
        mDelegate = delegate;
    }


    public static void initAds(Context context)
    {
        mContext = context;

        MobileAds.initialize(context, MOPUB_ID);

        mAdRequest = new AdRequest.Builder().addTestDevice(TEST_DEVICE_ID).build();

        initIntertitialAd();
        initRewardAds();

    }

    public static void initIntertitialAd()
    {
        mInterstitialAd = new InterstitialAd(mContext);
        mInterstitialAd.setAdUnitId(ADS_FULL_ID);
        mInterstitialAd.loadAd(mAdRequest);
        mInterstitialAd.setAdListener(new AdListener(){
            @Override
            public void onAdClosed() {
                super.onAdClosed();
                mInterstitialAd.loadAd(mAdRequest);
            }


            @Override
            public void onAdFailedToLoad(int i) {
                super.onAdFailedToLoad(i);
                mInterstitialAd.loadAd(mAdRequest);
            }
        });

    }

    public static void initRewardAds()
    {
        mRewardedVideoAd = MobileAds.getRewardedVideoAdInstance(mContext);
        mRewardedVideoAd.setRewardedVideoAdListener(new RewardedVideoAdListener() {
            @Override
            public void onRewardedVideoAdLoaded() {
            }

            @Override
            public void onRewardedVideoAdOpened() {


            }

            @Override
            public void onRewardedVideoStarted() {
            }

            @Override
            public void onRewardedVideoAdClosed() {
                mRewardedVideoAd.loadAd(ADS_VEDIO_ID,mAdRequest);

            }

            @Override
            public void onRewarded(RewardItem rewardItem) {
                if (mDelegate != null)
                    mDelegate.onRewardExpanded();
            }

            @Override
            public void onRewardedVideoAdLeftApplication() {

            }

            @Override
            public void onRewardedVideoAdFailedToLoad(int i) {
                mRewardedVideoAd.loadAd(ADS_VEDIO_ID,mAdRequest);
            }
        });
        mRewardedVideoAd.loadAd(ADS_VEDIO_ID, mAdRequest);
    }

    public static void showIntertitialAd()
    {
//        if (mInterstitialAd.isLoaded())
//            mInterstitialAd.show();
    }

    public static void showRewardAds()
    {
//        if (mRewardedVideoAd.isLoaded())
//        {
//            mRewardedVideoAd.show();
//        }else
//        {
//            if (mDelegate != null)
//                mDelegate.onRewardLoadFaild();
//        }
    }

    public static RewardedVideoAd getRewardAdsInstance()
    {
        return mRewardedVideoAd;
    }
}
