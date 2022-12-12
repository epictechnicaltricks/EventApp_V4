package com.infyson.invitationcard.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.infyson.invitationcard.R;
import com.infyson.invitationcard.classes.AdmobBabberClass;
import com.infyson.invitationcard.classes.Constants;
import com.infyson.invitationcard.classes.CustomButton;
import com.infyson.invitationcard.classes.CustomEditText;
import com.infyson.invitationcard.classes.CustomTextView;

import java.util.Objects;

public class SelectTitleActivity extends AppCompatActivity {

    CustomButton btnNext;
    ImageView btnLeftTitle, btnRightTitle, btnRightMeg, btnLeftMsg;
    CustomEditText edtTitle, edtMessage;

    int nCounter = 0;
    String[] mTitle ;
    String[] mMessage ;

    CustomTextView ivSelectTitle,tvtitlePreview,tvMessageTitle,tvMessagePreview;
    String sSelecttheme;
    RelativeLayout bannerAdContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_title);
        this.getSupportActionBar().setDisplayShowCustomEnabled(true);
        this.getSupportActionBar().setDisplayShowTitleEnabled(false);
        LayoutInflater inflator = LayoutInflater.from(this);
        View v = inflator.inflate(R.layout.title_preview, null);
        ((CustomTextView)v.findViewById(R.id.tv_actionbar_title)).setCustomText(R.string.activity_select_card, Constants.FOURTH_FONT);
        this.getSupportActionBar().setCustomView(v);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);



        final Intent intent= getIntent();
        init();

        AdmobBabberClass.BannerAdShow(getApplicationContext(),bannerAdContainer);

        sSelecttheme=  intent.getExtras().getString("sThemetype");
        if (sSelecttheme.equalsIgnoreCase("General")){
            mTitle  = this.getResources().getStringArray(R.array.TitleGeneral);
            mMessage  = this.getResources().getStringArray(R.array.ContentGeneral);
        }else if(sSelecttheme.equalsIgnoreCase("Birthday")){
            mTitle  = this.getResources().getStringArray(R.array.TitleBirthday);
            mMessage  = this.getResources().getStringArray(R.array.ContentBirthday);
        }else if(sSelecttheme.equalsIgnoreCase("KidBirthday")){
            mTitle  = this.getResources().getStringArray(R.array.TitleKidsBirthday);
            mMessage  = this.getResources().getStringArray(R.array.ContentKidsBirthday);
        }else if(sSelecttheme.equalsIgnoreCase("Anniversary")){
            mTitle  = this.getResources().getStringArray(R.array.TitleAnniversary);
            mMessage  = this.getResources().getStringArray(R.array.ContentAnniversary);
        }else if(sSelecttheme.equalsIgnoreCase("Wedding")){
            mTitle  = this.getResources().getStringArray(R.array.TitleMarriage);
            mMessage  = this.getResources().getStringArray(R.array.ContentMarriage);
        }

        edtTitle.setSimpleCustomText(mTitle[0],Constants.THIRD_FONT);
        edtMessage.setSimpleCustomText(mMessage[0],Constants.THIRD_FONT);
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SelectTitleActivity.this, EnterDetailsActivity.class)
                        .putExtra("sTitle",edtTitle.getText().toString().trim())
                        .putExtra("sMessage",edtMessage.getText().toString().trim())
                        .putExtra("CardUrl",intent.getStringExtra("CardUrl"))

                );
                overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);

            }
        });

        btnLeftTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nCounter = nCounter - 1;
                if (nCounter == -1) {
                    nCounter = mTitle.length-1;
                }
                edtTitle.setSimpleCustomText(mTitle[nCounter],Constants.THIRD_FONT);
            }
        });
        btnRightTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nCounter = nCounter + 1;
                if (nCounter > mTitle.length-1) {
                    nCounter = 0;
                }
                edtTitle.setSimpleCustomText(mTitle[nCounter],Constants.THIRD_FONT);
            }
        });
        btnRightMeg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nCounter = nCounter + 1;
                if (nCounter > mTitle.length-1) {
                    nCounter = 0;
                }
                edtMessage.setSimpleCustomText(mMessage[nCounter],Constants.THIRD_FONT);
            }
        });
        btnLeftMsg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nCounter = nCounter - 1;
                if (nCounter == -1) {
                    nCounter = mTitle.length-1;
                }
                edtMessage.setSimpleCustomText(mMessage[nCounter],Constants.THIRD_FONT);
            }
        });
    }

    private void init() {
        bannerAdContainer = findViewById(R.id.bannerAdContainer);
        ivSelectTitle= findViewById(R.id.tv_set_title);
        ivSelectTitle.setCustomText(R.string.select_title, Constants.FIRST_FONT);
        tvtitlePreview= findViewById(R.id.tv_set_title_preview);
        tvtitlePreview.setCustomText(R.string.title_preview,Constants.SECOND_FONT);
        tvMessageTitle= findViewById(R.id.tv_msg_title);
        tvMessageTitle.setCustomText(R.string.select_message,Constants.FIRST_FONT);
        tvMessagePreview= findViewById(R.id.tv_set_msg_preview);
        tvMessagePreview.setCustomText(R.string.message_preview,Constants.SECOND_FONT);

        edtTitle = findViewById(R.id.edt_title);
        edtMessage = findViewById(R.id.edt_meg);

        btnLeftTitle = findViewById(R.id.btn_left_title);
        btnRightTitle = findViewById(R.id.btn_right_title);
        btnRightMeg = findViewById(R.id.btn_msg_right);
        btnLeftMsg = findViewById(R.id.btn_mrg_left);
        btnNext = findViewById(R.id.btn_next);
        btnNext.setCustomText(R.string.next,Constants.FIRST_FONT);

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
