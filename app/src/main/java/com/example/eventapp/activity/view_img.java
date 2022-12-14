package com.example.eventapp.activity;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.example.eventapp.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;

public class view_img extends Activity {

    private RelativeLayout relative;
    private WebView webview1;
    private LinearLayout progress;
    private ProgressBar progressbar1;


    FloatingActionButton download_fab;


    File file;


    @Override
    protected void onCreate(Bundle _savedInstanceState) {
        super.onCreate(_savedInstanceState);
        setContentView(R.layout.view_img);
        initialize(_savedInstanceState);
        initializeLogic();
    }

    private void initialize(Bundle _savedInstanceState) {
        relative = findViewById(R.id.relative);
        webview1 = findViewById(R.id.webview1);
        webview1.getSettings().setJavaScriptEnabled(true);
        webview1.getSettings().setSupportZoom(true);
        progress = findViewById(R.id.progress);
        progressbar1 = findViewById(R.id.progressbar1);


        download_fab = findViewById(R.id.fab_download);

        download_fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

/*
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED &&
                            ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 123);
                        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 123);
                        showMessage("Need Permission to access storage for Downloading Image");
                    } else {*/
                        showMessage("Downloading..");
                        download(getIntent().getStringExtra("img"),"/storage/emulated/0/Download/",getIntent().getStringExtra("img_id")+".png");

/*
                    }
                }*/


            }
        });



        webview1.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView _param1, String _param2, Bitmap _param3) {
                final String _url = _param2;
                progress.setVisibility(View.VISIBLE);
                super.onPageStarted(_param1, _param2, _param3);
            }

            @Override
            public void onPageFinished(WebView _param1, String _param2) {
                final String _url = _param2;
                progress.setVisibility(View.GONE);
                super.onPageFinished(_param1, _param2);
            }
        });
    }

    private void initializeLogic() {
        _transparent_satus();
        _ZoomWebView(webview1, true);

        // this is for desktop view
        // code by Shubhamjeet

        webview1.getSettings().setLoadWithOverviewMode(true); webview1.getSettings().setUseWideViewPort(true); final WebSettings webSettings = webview1.getSettings(); final String newUserAgent; newUserAgent = "Mozilla/5.0 (Android) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/41.0.2228.0 Safari/537.36"; webSettings.setUserAgentString(newUserAgent);
        webview1.loadUrl(getIntent().getStringExtra("img"));
      //  startActivity(cc);
    }

    public void _transparent_satus() {

        Window w = this.getWindow();w.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        getWindow().getDecorView().setSystemUiVisibility( View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN); if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) { getWindow().setStatusBarColor(Color.TRANSPARENT); }

    }


    public void _ZoomWebView(final WebView _web, final boolean _ballon) {
        _web.getSettings().setBuiltInZoomControls(_ballon);
        _web.getSettings().setDisplayZoomControls(false);
    }

private void download(String _url, String _path, String _name){


    AndroidNetworking.download(_url, _path, _name).build().startDownload(new com.androidnetworking.interfaces.DownloadListener() {
        @Override
        public void onDownloadComplete() {

            Toast.makeText(view_img.this, "Download Completed", Toast.LENGTH_SHORT).show();

        }

        @Override
        public void onError(ANError anError) {
            Log.d("download_",anError.toString());
            Toast.makeText(view_img.this, "Failed to download", Toast.LENGTH_SHORT).show();

        }
    });
}

    @Deprecated
    public void showMessage(String _s) {
        Toast.makeText(getApplicationContext(), _s, Toast.LENGTH_LONG).show();
    }


}
