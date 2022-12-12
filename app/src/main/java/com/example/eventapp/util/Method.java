package com.example.eventapp.util;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.Point;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.provider.Settings;
import android.text.Html;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

import com.example.eventapp.activity.MainActivity;
import com.example.eventapp.R;
import com.example.eventapp.interFace.FavouriteIF;
import com.example.eventapp.interFace.OnClick;
import com.example.eventapp.response.FavouriteRP;
import com.example.eventapp.rest.ApiClient;
import com.example.eventapp.rest.ApiInterface;
import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.CacheFlag;
import com.facebook.ads.InterstitialAdListener;
import com.facebook.login.LoginManager;
import com.google.ads.mediation.admob.AdMobAdapter;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.jetbrains.annotations.NotNull;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Method {

    private Activity activity;
    private OnClick onClick;
    public static boolean loginBack = false, personalizationAd = false;

    public SharedPreferences pref;
    public SharedPreferences.Editor editor;
    private final String myPreference = "EventApp";
    public String pref_login = "pref_login";
    private String firstTime = "firstTime";
    public String profileId = "profileId";
    public String userImage = "userImage";
    public String userEmail = "userEmail";
    public String loginType = "loginType";
    public String show_login = "show_login";
    public String notification = "notification";
    public String themSetting = "them";


    public Method(Activity activity) {
        this.activity = activity;
        pref = activity.getSharedPreferences(myPreference, 0); // 0 - for private mode
        editor = pref.edit();
    }

    public Method(Activity activity, OnClick onClick) {
        this.activity = activity;
        this.onClick = onClick;
        pref = activity.getSharedPreferences(myPreference, 0); // 0 - for private mode
        editor = pref.edit();
    }

    public void login() {
        if (!pref.getBoolean(firstTime, false)) {
            editor.putBoolean(pref_login, false);
            editor.putBoolean(firstTime, true);
            editor.commit();
        }
    }

    //user login or not
    public boolean isLogin() {
        return pref.getBoolean(pref_login, false);
    }

    //get login type
    public String getLoginType() {
        return pref.getString(loginType, null);
    }

    //get user id
    public String userId() {
        return pref.getString(profileId, null);
    }

    //get device id
    @SuppressLint("HardwareIds")
    public String getDevice() {
        String deviceId;
        try {
            deviceId = Settings.Secure.getString(activity.getContentResolver(), Settings.Secure.ANDROID_ID);
        } catch (Exception e) {
            deviceId = "Not Found";
        }
        return deviceId;
    }

    //rtl
    public void forceRTLIfSupported() {
        if (activity.getResources().getString(R.string.isRTL).equals("true")) {
            activity.getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        }
    }

    //rtl or not
    public boolean isRtl() {
        return activity.getResources().getString(R.string.isRTL).equals("true");
    }

    //them mode
    public String themMode() {
        return pref.getString(themSetting, "system");
    }

    //google map application installation or not check
    public boolean isAppInstalled() {
        String packageName = "com.google.android.apps.maps";
        Intent mIntent = activity.getPackageManager().getLaunchIntentForPackage(packageName);
        return mIntent != null;
    }

    public void changeStatusBarColor() {
        Window window = activity.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(Color.TRANSPARENT);
    }

    //network check
    public boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public int getScreenWidth() {
        int columnWidth;
        WindowManager wm = (WindowManager) activity
                .getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();

        final Point point = new Point();

        point.x = display.getWidth();
        point.y = display.getHeight();

        columnWidth = point.x;
        return columnWidth;
    }

    //add to favourite
    public void addToFav(String id, String userId, String type, int position, FavouriteIF favouriteIF) {

        ProgressDialog progressDialog = new ProgressDialog(activity);

        progressDialog.show();
        progressDialog.setMessage(activity.getResources().getString(R.string.loading));
        progressDialog.setCancelable(false);

        JsonObject jsObj = (JsonObject) new Gson().toJsonTree(new API(activity));
        jsObj.addProperty("event_id", id);
        jsObj.addProperty("user_id", userId);
        jsObj.addProperty("method_name", "add_favourite");
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<FavouriteRP> call = apiService.getFavouriteEvent(API.toBase64(jsObj.toString()));
        call.enqueue(new Callback<FavouriteRP>() {
            @Override
            public void onResponse(@NotNull Call<FavouriteRP> call, @NotNull Response<FavouriteRP> response) {

                try {
                    FavouriteRP favouriteRP = response.body();
                    assert favouriteRP != null;

                    if (favouriteRP.getStatus().equals("1")) {
                        if (favouriteRP.getSuccess().equals("1")) {
                            favouriteIF.isFavourite(favouriteRP.isIs_favourite(), favouriteRP.getMsg());
                            Events.Favourite favourite = new Events.Favourite(id, type, favouriteRP.isIs_favourite(), position);
                            GlobalBus.getBus().post(favourite);
                        }
                        Toast.makeText(activity, favouriteRP.getMsg(), Toast.LENGTH_SHORT).show();
                    } else if (favouriteRP.getStatus().equals("2")) {
                        suspend(favouriteRP.getMessage());
                    } else {
                        alertBox(favouriteRP.getMessage());
                    }

                } catch (Exception e) {
                    Log.d("exception_error", e.toString());
                    alertBox(activity.getResources().getString(R.string.failed_try_again));
                }

                progressDialog.dismiss();

            }

            @Override
            public void onFailure(@NotNull Call<FavouriteRP> call, @NotNull Throwable t) {
                // Log error here since request failed
                Log.e("onFailure_data", t.toString());
                progressDialog.dismiss();
                alertBox(activity.getResources().getString(R.string.failed_try_again));
            }
        });

    }

    //---------------Interstitial Ad---------------//

    public void click(final int position, final String type, final String title, final String id) {

        ProgressDialog progressDialog = new ProgressDialog(activity);

        progressDialog.show();
        progressDialog.setMessage(activity.getResources().getString(R.string.loading));
        progressDialog.setCancelable(false);

        if (Constant.appRP != null) {

            if (Constant.appRP.isInterstitial_ad()) {

                Constant.AD_COUNT = Constant.AD_COUNT + 1;
                if (Constant.AD_COUNT == Constant.AD_COUNT_SHOW) {
                    Constant.AD_COUNT = 0;

                    if (Constant.appRP.getInterstitial_ad_type().equals("admob")) {

                        AdRequest adRequest;
                        if (personalizationAd) {
                            adRequest = new AdRequest.Builder()
                                    .build();
                        } else {
                            Bundle extras = new Bundle();
                            extras.putString("npa", "1");
                            adRequest = new AdRequest.Builder()
                                    .addNetworkExtrasBundle(AdMobAdapter.class, extras)
                                    .build();
                        }
                        InterstitialAd.load(activity, Constant.appRP.getInterstitial_ad_id(), adRequest, new InterstitialAdLoadCallback() {
                            @Override
                            public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                                // The mInterstitialAd reference will be null until
                                // an ad is loaded.
                                Log.i("admob_error", "onAdLoaded");

                                progressDialog.dismiss();
                                interstitialAd.show(activity);

                                interstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                                    @Override
                                    public void onAdDismissedFullScreenContent() {
                                        // Called when fullscreen content is dismissed.
                                        Log.d("admob_error", "The ad was dismissed.");
                                        onClick.click(position, type, title, id);
                                    }

                                    @Override
                                    public void onAdFailedToShowFullScreenContent(com.google.android.gms.ads.AdError adError) {
                                        // Called when fullscreen content failed to show.
                                        Log.d("admob_error", "The ad failed to show.");
                                        onClick.click(position, type, title, id);
                                    }

                                    @Override
                                    public void onAdShowedFullScreenContent() {
                                        // Called when fullscreen content is shown.
                                        // Make sure to set your reference to null so you don't
                                        // show it a second time.
                                        Log.d("admob_error", "The ad was shown.");
                                    }
                                });

                            }

                            @Override
                            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                                // Handle the error
                                progressDialog.dismiss();
                                onClick.click(position, type, title, id);
                                Log.i("admob_error", loadAdError.getMessage());
                            }
                        });

                    } else {

                        final com.facebook.ads.InterstitialAd interstitialAd = new com.facebook.ads.InterstitialAd(activity, Constant.appRP.getInterstitial_ad_id());
                        InterstitialAdListener interstitialAdListener = new InterstitialAdListener() {
                            @Override
                            public void onInterstitialDisplayed(Ad ad) {
                                // Interstitial ad displayed callback
                                Log.e("fb_ad", "Interstitial ad displayed.");
                            }

                            @Override
                            public void onInterstitialDismissed(Ad ad) {
                                // Interstitial dismissed callback
                                progressDialog.dismiss();
                                onClick.click(position, type, title, id);
                                Log.e("fb_ad", "Interstitial ad dismissed.");
                            }

                            @Override
                            public void onError(Ad ad, AdError adError) {
                                // Ad error callback
                                progressDialog.dismiss();
                                onClick.click(position, type, title, id);
                                Log.e("fb_ad", "Interstitial ad failed to load: " + adError.getErrorMessage());
                            }

                            @Override
                            public void onAdLoaded(Ad ad) {
                                // Interstitial ad is loaded and ready to be displayed
                                Log.d("fb_ad", "Interstitial ad is loaded and ready to be displayed!");
                                progressDialog.dismiss();
                                // Show the ad
                                interstitialAd.show();
                            }

                            @Override
                            public void onAdClicked(Ad ad) {
                                // Ad clicked callback
                                Log.d("fb_ad", "Interstitial ad clicked!");
                            }

                            @Override
                            public void onLoggingImpression(Ad ad) {
                                // Ad impression logged callback
                                Log.d("fb_ad", "Interstitial ad impression logged!");
                            }
                        };

                        // For auto play video ads, it's recommended to load the ad
                        // at least 30 seconds before it is shown
                        com.facebook.ads.InterstitialAd.InterstitialLoadAdConfig loadAdConfig = interstitialAd.buildLoadAdConfig().
                                withAdListener(interstitialAdListener).withCacheFlags(CacheFlag.ALL).build();
                        interstitialAd.loadAd(loadAdConfig);

                    }

                } else {
                    progressDialog.dismiss();
                    onClick.click(position, type, title, id);
                }

            } else {
                progressDialog.dismiss();
                onClick.click(position, type, title, id);
            }
        } else {
            progressDialog.dismiss();
            onClick.click(position, type, title, id);
        }

    }

    //---------------Interstitial Ad---------------//

    //---------------Banner Ad---------------//

    public void bannerAd(LinearLayout linearLayout) {

        if (Constant.appRP != null) {
            if (Constant.appRP.isBanner_ad()) {
                if (Constant.appRP.getBanner_ad_type().equals("admob")) {
                    if (personalizationAd) {
                        showPersonalizedAds(linearLayout);
                    } else {
                        showNonPersonalizedAds(linearLayout);
                    }
                } else {
                    fbBannerAd(linearLayout);
                }
            } else {
                linearLayout.setVisibility(View.GONE);
            }
        } else {
            linearLayout.setVisibility(View.GONE);
        }

    }

    public void showPersonalizedAds(LinearLayout linearLayout) {

        AdView adView = new AdView(activity);
        AdRequest adRequest = new AdRequest.Builder()
                .build();
        adView.setAdUnitId(Constant.appRP.getBanner_ad_id());
        adView.setAdSize(AdSize.BANNER);
        linearLayout.addView(adView);
        adView.loadAd(adRequest);

    }

    public void showNonPersonalizedAds(LinearLayout linearLayout) {

        Bundle extras = new Bundle();
        extras.putString("npa", "1");
        AdView adView = new AdView(activity);
        AdRequest adRequest = new AdRequest.Builder()
                .addNetworkExtrasBundle(AdMobAdapter.class, extras)
                .build();
        adView.setAdUnitId(Constant.appRP.getBanner_ad_id());
        adView.setAdSize(AdSize.BANNER);
        linearLayout.addView(adView);
        adView.loadAd(adRequest);

    }

    public void fbBannerAd(LinearLayout linearLayout) {
        com.facebook.ads.AdView adView = new com.facebook.ads.AdView(activity, Constant.appRP.getBanner_ad_id(), com.facebook.ads.AdSize.BANNER_HEIGHT_50);
        // Find the Ad Container
        // Add the ad view to your activity layout
        linearLayout.addView(adView);
        // Request an ad
        adView.loadAd();
    }

    //---------------Banner Ad---------------//


    //--------------Event Format---------//

    //year
    public String monthYear(int monthOfYear) {
        String monthYear;
        if (monthOfYear + 1 < 10) {
            monthYear = "0" + (monthOfYear + 1);
        } else {
            monthYear = String.valueOf(monthOfYear + 1);
        }
        return monthYear;
    }

    //month
    public String dayMonth(int dayOfMonth) {
        String dayMonth;
        if (dayOfMonth < 10) {
            dayMonth = "0" + dayOfMonth;
        } else {
            dayMonth = String.valueOf(dayOfMonth);
        }
        return dayMonth;
    }

    //date
    public String userViewDate(int dayOfMonth, int monthOfYear, int year, int dayOfMonthEnd, int monthOfYearEnd, int yearEnd) {
        return dayMonth(dayOfMonth) + "/" + monthYear(monthOfYear) + "/" + year
                + " " + activity.getResources().getString(R.string.to)
                + " " + dayMonth(dayOfMonthEnd) + "/" + monthYear(monthOfYearEnd) + "/" + yearEnd;
    }

    //time
    public String timeFormat(String hourString, String minuteString) {

        String hour = null;
        boolean PM = false;
        int hours_int = Integer.parseInt(hourString);

        if (hours_int > 11) {
            PM = true;
            if (hours_int > 12) {
                hour = String.valueOf(hours_int - 12);
            } else {
                hour = "12";
            }
        } else {
            if (hourString.equals("00")) {
                hour = "12";
            } else {
                hour = hourString;
            }
        }

        if (PM) {
            return hour + ":" + minuteString + " PM";
        } else {
            return hour + ":" + minuteString + " AM";
        }

    }

    //checking date start date is not greater than end date
    public boolean checkDatesBefore(String startDate, String endDate) {
        boolean isDate = false;
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat dfDate = new SimpleDateFormat("MM/dd/yyyy");
        try {
            if (dfDate.parse(startDate).before(dfDate.parse(endDate))) {
                isDate = true;// If start date is before end date
            } else if (dfDate.parse(startDate).equals(dfDate.parse(endDate))) {
                isDate = true;// If two dates are equal
            } else {
                isDate = false; // If start date is after the end date
            }
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return isDate;
    }

    //--------------Event Format---------//

    //account suspend
    public void suspend(String message) {

        if (isLogin()) {

            String typeLogin = getLoginType();
            assert typeLogin != null;
            if (typeLogin.equals("google")) {

                // Configure sign-in to request the user's ID, email address, and basic
                // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
                GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                        .requestEmail()
                        .build();

                // Build a GoogleSignInClient with the options specified by gso.
                GoogleSignInClient mGoogleSignInClient = GoogleSignIn.getClient(activity, gso);
                mGoogleSignInClient.signOut()
                        .addOnCompleteListener(activity, task -> {

                        });
            } else if (typeLogin.equals("facebook")) {
                LoginManager.getInstance().logOut();
            }

            editor.putBoolean(pref_login, false);
            editor.commit();
            Events.Login loginNotify = new Events.Login("");
            GlobalBus.getBus().post(loginNotify);
        }

        try {
            if (activity != null) {
                if (!activity.isFinishing()) {
                    MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(activity, R.style.DialogTitleTextStyle);
                    builder.setMessage(Html.fromHtml(message));
                    builder.setCancelable(false);
                    builder.setPositiveButton(activity.getResources().getString(R.string.ok),
                            (arg0, arg1) -> {
                                activity.startActivity(new Intent(activity, MainActivity.class));
                                activity.finishAffinity();
                            });

                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                }
            }
        } catch (Exception e) {
            Log.d("error_message", e.toString());
        }

    }


    //alert message box
    public void alertBox(String message) {

        try {
            if (activity != null) {
                if (!activity.isFinishing()) {
                    MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(activity, R.style.DialogTitleTextStyle);
                    builder.setMessage(Html.fromHtml(message));
                    builder.setCancelable(false);
                    builder.setPositiveButton(activity.getResources().getString(R.string.ok),
                            (arg0, arg1) -> {

                            });

                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                }
            }
        } catch (Exception e) {
            Log.d("error_message", e.toString());
        }

    }

    public String webViewText() {
        String color;
        if (isDarkMode()) {
            color = Constant.webViewTextDark;
        } else {
            color = Constant.webViewText;
        }
        return color;
    }

    public String webViewLink() {
        String color;
        if (isDarkMode()) {
            color = Constant.webViewLinkDark;
        } else {
            color = Constant.webViewLink;
        }
        return color;
    }

    //webview rtl formate
    public String isWebViewTextRtl() {
        String isRtl;
        if (isRtl()) {
            isRtl = "rtl";
        } else {
            isRtl = "ltr";
        }
        return isRtl;
    }

    //check dark mode or not
    public boolean isDarkMode() {
        int currentNightMode = activity.getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        switch (currentNightMode) {
            case Configuration.UI_MODE_NIGHT_NO:
                // Night mode is not active, we're using the light theme
                return false;
            case Configuration.UI_MODE_NIGHT_YES:
                // Night mode is active, we're using dark theme
                return true;
            default:
                return false;
        }
    }

}
