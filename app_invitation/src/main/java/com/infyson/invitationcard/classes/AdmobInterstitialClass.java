package com.infyson.invitationcard.classes;

import android.app.Activity;

import androidx.annotation.NonNull;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.infyson.invitationcard.R;

public class AdmobInterstitialClass {

    public static void InterstitialAdShow(Activity mContext) {


        AdRequest adRequest = new AdRequest.Builder().build();

        InterstitialAd.load(mContext, mContext.getResources().getString(R.string.admob_interstitial_id),
                adRequest,
                new InterstitialAdLoadCallback() {
                    @Override
                    public void onAdLoaded(@NonNull com.google.android.gms.ads.interstitial.InterstitialAd mInterstitialAdObj) {
                        mInterstitialAdObj.show(mContext);
                    }
                });
    }


}
