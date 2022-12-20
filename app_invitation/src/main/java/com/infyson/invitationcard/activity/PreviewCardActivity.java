package com.infyson.invitationcard.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.core.content.FileProvider;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.infyson.invitationcard.R;
import com.infyson.invitationcard.classes.AdmobInterstitialClass;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class PreviewCardActivity extends Activity {

    ImageView ivPreview, ic_wp_share, iv_fb, iv_insta, iv_share, iv_like;
    File picture;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview_card);

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        

        init();
        Intent intent = getIntent();
        picture = (File) intent.getExtras().get("myMalaImage");
        AdmobInterstitialClass.InterstitialAdShow(PreviewCardActivity.this);
        Glide.with(this).load(picture)
                .thumbnail(0.5f)

                .into(ivPreview);

        ic_wp_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent whatsappIntent = new Intent(android.content.Intent.ACTION_SEND);
                whatsappIntent.setType("image/*");
                whatsappIntent.setPackage("com.whatsapp");
                //whatsappIntent.putExtra(Intent.EXTRA_TEXT, "https://goo.gl/Wfrfrs");

                whatsappIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse("file://" + picture)); //add image path
                try {
                    startActivity(whatsappIntent);
                } catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(getApplicationContext(), "Whatsapp have not been installed.", Toast.LENGTH_SHORT).show();
                }
            }
        });
        iv_fb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try
                {
                    List<Intent> targetedShareIntents = new ArrayList<Intent>();
                    Intent share = new Intent(android.content.Intent.ACTION_SEND);
                    share.setType("image/jpeg");
                    List<ResolveInfo> resInfo = getPackageManager().queryIntentActivities(share, 0);
                    if (!resInfo.isEmpty()){
                        for (ResolveInfo info : resInfo) {
                            Intent targetedShare = new Intent(android.content.Intent.ACTION_SEND);
                            targetedShare.setType("image/jpeg"); // put here your mime type
                            Log.e("infoinfoinfo",""+info);
                            if (info.activityInfo.packageName.toLowerCase().contains("com.facebook.katana") || info.activityInfo.name.toLowerCase().contains("com.facebook.katana")) {
                                targetedShare.putExtra(Intent.EXTRA_SUBJECT, "Sample Photo");
                                targetedShare.putExtra(Intent.EXTRA_TEXT,"This photo is created by App Name");
                                targetedShare.putExtra(Intent.EXTRA_STREAM,  Uri.fromFile(picture) );
                                targetedShare.setPackage(info.activityInfo.packageName);
                                targetedShareIntents.add(targetedShare);
                            }
                        }
                        Intent chooserIntent = Intent.createChooser(targetedShareIntents.remove(0), "Select app to share");
                        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, targetedShareIntents.toArray(new Parcelable[]{}));
                        startActivity(chooserIntent);
                    }
                }
                catch(Exception e){
                    Log.v("VM","Exception while sending image on" + "Facebook" + " "+  e.getMessage());
                }


            }
        });
        iv_insta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent shareIntent = new Intent(android.content.Intent.ACTION_SEND);
                shareIntent.setType("image/*");
                shareIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(picture) );
                shareIntent.putExtra(Intent.EXTRA_TITLE, "YOUR TEXT HERE");
                shareIntent.setPackage("com.instagram.android");
                try {
                    startActivity(shareIntent);

                } catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(getApplicationContext(), "Instagram have not been installed.", Toast.LENGTH_SHORT).show();
                }





            }
        });
        iv_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent share = new Intent(Intent.ACTION_SEND);
                share.setType("image/jpeg");
                share.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(picture));
                // share.putExtra(Intent.EXTRA_TEXT, "https://goo.gl/Wfrfrs");

             /*   Uri photoURI = FileProvider.getUriForFile(getApplicationContext(), getApplicationContext()
                        .getApplicationContext().getPackageName() + ".provider", picture);*/
               // share.putExtra(Intent.EXTRA_STREAM, photoURI);

                startActivity(Intent.createChooser(share, "Share Image"));



            }
        });
        iv_like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String appPackageName = getPackageName(); // package name of the app
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                } catch (android.content.ActivityNotFoundException anfe) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                }
            }
        });

    }

    private void init() {
        ivPreview = (ImageView) findViewById(R.id.iv_main_img);
        ic_wp_share = (ImageView) findViewById(R.id.ic_wp_share);
        iv_fb = (ImageView) findViewById(R.id.iv_fb);
        iv_insta = (ImageView) findViewById(R.id.iv_insta);
        iv_share = (ImageView) findViewById(R.id.iv_share);
        iv_like = (ImageView) findViewById(R.id.iv_like);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
    }
}
