package com.infyson.invitationcard.classes;

import android.content.Context;
import android.widget.RelativeLayout;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.infyson.invitationcard.R;

public class AdmobBabberClass {


    public static void BannerAdShow(Context mContext, RelativeLayout bannerAdContainer) {
        AdView mAdView = new AdView(mContext);
        mAdView.setAdSize(new com.google.android.gms.ads.AdSize(320, 50));
        mAdView.setAdUnitId(mContext.getResources().getString(R.string.admob_banner_id));
        AdRequest adRequest = new AdRequest.Builder()
                // Check the LogCat to get your test device ID
                .build();
        bannerAdContainer.addView(mAdView);
        mAdView.loadAd(adRequest);

    }

}
