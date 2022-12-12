package com.infyson.invitationcard.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.infyson.invitationcard.R;
import com.infyson.invitationcard.classes.AdmobBabberClass;
import com.infyson.invitationcard.classes.AdmobInterstitialClass;
import com.infyson.invitationcard.classes.Constants;
import com.infyson.invitationcard.classes.CustomButton;
import com.infyson.invitationcard.classes.CustomTextView;

public class ParthThemeActivity extends AppCompatActivity {
    Intent intent;
    CustomButton btnGeneral,btnbirthday,btnKidbirthday,btnAniversary,btnWedding,btnMoreApps;
    RelativeLayout bannerAdContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parth_theme);
        this.getSupportActionBar().setDisplayShowCustomEnabled(true);
        this.getSupportActionBar().setDisplayShowTitleEnabled(false);
        LayoutInflater inflator = LayoutInflater.from(this);
        View v = inflator.inflate(R.layout.title_preview, null);
        ((CustomTextView)v.findViewById(R.id.tv_actionbar_title)).setCustomText(R.string.activity_party_theme, Constants.FOURTH_FONT);
        this.getSupportActionBar().setCustomView(v);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        intent=getIntent();
        init();
        AdmobBabberClass.BannerAdShow(getApplicationContext(),bannerAdContainer);
        AdmobInterstitialClass.InterstitialAdShow(ParthThemeActivity.this);
        btnGeneral.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NextActivity("General");
            }
        });btnbirthday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NextActivity("Birthday");
            }
        });btnKidbirthday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NextActivity("KidBirthday");
            }
        });btnAniversary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NextActivity("Anniversary");
            }
        });btnWedding.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NextActivity("Wedding");
            }
        });

        btnMoreApps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://google.com/")));
                } catch (android.content.ActivityNotFoundException anfe) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://google.com/")));
                }
            }
        });

    }

    private void init() {
        bannerAdContainer= findViewById(R.id.bannerAdContainer);
       btnMoreApps=(CustomButton)findViewById(R.id.iv_more_app) ;
        btnGeneral=(CustomButton)findViewById(R.id.iv_general);
      //  btnGeneral.setSimpleCustomText("General",Constants.FIRST_FONT);

        btnbirthday=(CustomButton)findViewById(R.id.iv_birthday);
       // btnbirthday.setSimpleCustomText("Birthday",Constants.FIRST_FONT);

        btnKidbirthday=(CustomButton)findViewById(R.id.iv_kids_birthday);
       // btnKidbirthday.setSimpleCustomText("Kid Birthday",Constants.FIRST_FONT);

        btnAniversary=(CustomButton)findViewById(R.id.iv_anniversary);
     //   btnAniversary.setSimpleCustomText("Anniversary",Constants.FIRST_FONT);

        btnWedding=(CustomButton)findViewById(R.id.iv_wedding);
       // btnWedding.setSimpleCustomText("Wedding",Constants.FIRST_FONT);

    }

    public void NextActivity(String sThemetype){
        startActivity(new Intent(ParthThemeActivity.this, SelectTitleActivity.class)
                .putExtra("CardUrl",intent.getExtras().getString("CardUrl"))
                .putExtra("sThemetype",sThemetype)
        );
        overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id = item.getItemId();
        if (id == android.R.id.home)
        {
            onBackPressed();
            return true;
        }
        else
        {
            return super.onOptionsItemSelected(item);
        }
    }
}
