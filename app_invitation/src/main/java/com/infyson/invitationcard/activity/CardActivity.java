package com.infyson.invitationcard.activity;

import static android.content.ContentValues.TAG;

import android.app.Activity;
import android.content.Intent;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ahmedadeltito.photoeditorsdk.BrushDrawingView;
import com.ahmedadeltito.photoeditorsdk.OnPhotoEditorSDKListener;
import com.ahmedadeltito.photoeditorsdk.PhotoEditorSDK;
import com.ahmedadeltito.photoeditorsdk.ViewType;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.infyson.invitationcard.R;
import com.infyson.invitationcard.adapter.AdapterFont;
import com.infyson.invitationcard.adapter.ImageAdapter;
import com.infyson.invitationcard.classes.AdmobInterstitialClass;
import com.infyson.invitationcard.classes.CircleImageView;
import com.infyson.invitationcard.classes.ColorPickerAdapter;
import com.infyson.invitationcard.classes.Constants;
import com.infyson.invitationcard.classes.HorizontalListView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class CardActivity extends Activity implements View.OnTouchListener, OnPhotoEditorSDKListener {

    TextView tv_dear_text, tv_dear_msg, tv_from, tv_data, tv_time, tv_venue, tv_title;
    ScaleGestureDetector scaleGestureDetector;
    float dX;
    float dY;
    private RecyclerView drawingViewColorPickerRecyclerView;
    private ArrayList<Integer> colorPickerColors;
    CircleImageView ivProfileImage;
    ImageView iv_color_picker, iv_font, ivDecrease, ivIncrease, ivStikers, ivDone, ivSelectorColor;
    HorizontalListView llFontPlatte;
    private String aFontList[];
    private float size = 15;
    private float TitleFontSize = 35;
    Intent intent;
    ImageView ivCardImag;
    String[] imgPath;
    private ImageView imageViewbyCode;

    private PhotoEditorSDK photoEditorSDK;

    RelativeLayout mail_layout, delete_rl, rl_color, rl_capture_image;
    RecyclerView imageRecyclerView;
    private ArrayList<Bitmap> stickerBitmaps;
    private AssetManager assetManager;
    File file;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card);
        scaleGestureDetector = new ScaleGestureDetector(this, new simpleOnScaleGestureListener());
        colorPickerColors = new ArrayList<>();
        colorPickerColors.add(getResources().getColor(R.color.black));
        colorPickerColors.add(getResources().getColor(R.color.blue_color_picker));
        colorPickerColors.add(getResources().getColor(R.color.brown_color_picker));
        colorPickerColors.add(getResources().getColor(R.color.green_color_picker));
        colorPickerColors.add(getResources().getColor(R.color.orange_color_picker));
        colorPickerColors.add(getResources().getColor(R.color.red_color_picker));
        colorPickerColors.add(getResources().getColor(R.color.red_orange_color_picker));
        colorPickerColors.add(getResources().getColor(R.color.sky_blue_color_picker));
        colorPickerColors.add(getResources().getColor(R.color.violet_color_picker));
        colorPickerColors.add(getResources().getColor(R.color.white));
        colorPickerColors.add(getResources().getColor(R.color.yellow_color_picker));
        colorPickerColors.add(getResources().getColor(R.color.yellow_green_color_picker));
        intent = getIntent();
        assetManager = getAssets();
        stickerBitmaps = new ArrayList<>();

        try {
            String[] imgPath = assetManager.list("img");
            for (int i = 0; i < imgPath.length; i++) {
                InputStream is = assetManager.open("img/" + imgPath[i]);
                Log.d(TAG, imgPath[i]);
                Bitmap bitmap = BitmapFactory.decodeStream(is);
                stickerBitmaps.add(bitmap);

            }
        } catch (IOException e) {
            Log.e(TAG, e.getMessage());
        }
        init();
        AdmobInterstitialClass.InterstitialAdShow(CardActivity.this);
        imageRecyclerView = (RecyclerView) findViewById(R.id.fragment_main_photo_edit_image_rv);
        imageRecyclerView.setVisibility(View.GONE);
        //imageRecyclerView.setLayoutManager(CardActivity.this,LinearLayoutManager.HORIZONTAL, false);

        LinearLayoutManager layoutManager
                = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        ImageAdapter adapter = new ImageAdapter(CardActivity.this, stickerBitmaps);
        adapter.setOnImageClickListener(new ImageAdapter.OnImageClickListener() {
            @Override
            public void onImageClickListener(Bitmap image) {
                imageRecyclerView.setVisibility(View.GONE);
                photoEditorSDK.addImage(image);
            }
        });
        imageRecyclerView.setLayoutManager(layoutManager);
        imageRecyclerView.setAdapter(adapter);
        BrushDrawingView brushDrawingView = (BrushDrawingView) findViewById(R.id.drawing_view);

        photoEditorSDK = new PhotoEditorSDK.PhotoEditorSDKBuilder(CardActivity.this)
                .parentView(rl_capture_image) // add parent image view
                .childView(ivCardImag) // add the desired image view
                .deleteView(delete_rl) // add the deleted view that will appear during the movement of the views
                .brushDrawingView(brushDrawingView) // add the brush drawing view that is responsible for drawing on the image view
                .buildPhotoEditorSDK(); // build photo editor sdk
        photoEditorSDK.setOnPhotoEditorSDKListener(this);

        Typeface type = Typeface.createFromAsset(getAssets(), "fonts/title_text.ttf");
        Typeface Titletype = Typeface.createFromAsset(getAssets(), "fonts/bodyFont.ttf");


        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 1;
        Bitmap bitmap = BitmapFactory.decodeFile(intent.getExtras().getString("ivProfile"), options);

        Glide.with(this).load(intent.getStringExtra("CardUrl"))
                .thumbnail(0.5f)

                .into(ivCardImag);


        if (bitmap == null) {
            ivProfileImage.getLayoutParams().width = 20;
            ivProfileImage.getLayoutParams().height = 20;
            ivProfileImage.setVisibility(View.INVISIBLE);
        } else {
            ivProfileImage.setImageBitmap(bitmap);
        }

        // txtyour.setTypeface(type);

        tv_title.setTypeface(Titletype);
        tv_dear_text.setTypeface(type);
        tv_dear_msg.setTypeface(type);
        tv_from.setTypeface(type);
        tv_data.setTypeface(type);
        tv_time.setTypeface(type);
        tv_venue.setTypeface(type);


        tv_title.setTextSize(TitleFontSize);
        tv_dear_text.setTextSize(size);
        tv_dear_msg.setTextSize(size);
        tv_from.setTextSize(size);
        tv_data.setTextSize(size);
        tv_time.setTextSize(size);
        tv_venue.setTextSize(size);


        iv_color_picker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                llFontPlatte.setVisibility(View.GONE);
                imageRecyclerView.setVisibility(View.GONE);
                if (drawingViewColorPickerRecyclerView.getVisibility() == View.GONE) {
                    drawingViewColorPickerRecyclerView.setVisibility(View.VISIBLE);
                } else {
                    drawingViewColorPickerRecyclerView.setVisibility(View.GONE);
                }
            }
        });
        iv_font.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawingViewColorPickerRecyclerView.setVisibility(View.GONE);
                imageRecyclerView.setVisibility(View.GONE);
                if (llFontPlatte.getVisibility() == View.GONE) {
                    llFontPlatte.setVisibility(View.VISIBLE);
                } else {
                    llFontPlatte.setVisibility(View.GONE);
                }
            }
        });
        ivDecrease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawingViewColorPickerRecyclerView.setVisibility(View.GONE);
                imageRecyclerView.setVisibility(View.GONE);
                llFontPlatte.setVisibility(View.GONE);
                size = size - 1;
                TitleFontSize = TitleFontSize - 1;
                tv_title.setTextSize(TitleFontSize);
                tv_dear_text.setTextSize(size);
                tv_dear_msg.setTextSize(size);
                tv_from.setTextSize(size);
                tv_data.setTextSize(size);
                tv_time.setTextSize(size);
                tv_venue.setTextSize(size);

            }
        });
        ivIncrease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawingViewColorPickerRecyclerView.setVisibility(View.GONE);
                imageRecyclerView.setVisibility(View.GONE);
                llFontPlatte.setVisibility(View.GONE);
                size = size + 1;
                TitleFontSize = TitleFontSize + 1;
                tv_dear_text.setTextSize(size);
                tv_title.setTextSize(TitleFontSize);
                tv_dear_msg.setTextSize(size);
                tv_from.setTextSize(size);
                tv_data.setTextSize(size);
                tv_time.setTextSize(size);
                tv_venue.setTextSize(size);

            }
        });

        ivStikers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawingViewColorPickerRecyclerView.setVisibility(View.GONE);
                llFontPlatte.setVisibility(View.GONE);
                if (imageRecyclerView.getVisibility() == View.GONE) {
                    imageRecyclerView.setVisibility(View.VISIBLE);
                } else {
                    imageRecyclerView.setVisibility(View.GONE);
                }
            }
        });

        Object objFont = getAssets();
        try {
            aFontList = ((AssetManager) (objFont)).list("fonts");


            //   ivProfileImage.setImageBitmap(imgPath[0]);


        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            String[] imgPath = assetManager.list("img");
            for (int i = 0; i < imgPath.length; i++) {
                InputStream is = assetManager.open("img/" + imgPath[i]);
                Log.d(TAG, imgPath[i]);
                Bitmap bitmap2 = BitmapFactory.decodeStream(is);

                //ivProfileImage.setImageBitmap(bitmap2);


            }
        } catch (IOException e) {
            Log.e(TAG, e.getMessage());
        }


        objFont = new AdapterFont(this, aFontList);
        llFontPlatte.setAdapter(((android.widget.ListAdapter) (objFont)));
        llFontPlatte.setOnItemClickListener(new android.widget.AdapterView.OnItemClickListener() {
            final CardActivity this$0;

            public void onItemClick(AdapterView adapterview, View view, int i, long l) {
                Typeface type = Typeface.createFromAsset(getAssets(), "fonts/" + aFontList[i]);
                tv_title.setTypeface(type);
                tv_dear_text.setTypeface(type);
                tv_dear_msg.setTypeface(type);
                tv_from.setTypeface(type);
                tv_data.setTypeface(type);
                tv_time.setTypeface(type);
                tv_venue.setTypeface(type);
            }

            {
                this$0 = CardActivity.this;
            }
        });

        ivDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finalCard();



            }


        });


    }
    protected String selectedOutputPath;

    private void finalCard() {

        rl_capture_image.setDrawingCacheEnabled(true);
        Bitmap BitmapPrepareCard = rl_capture_image.getDrawingCache();


        File mediaStorageDir = new File(
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), Constants.MEDIA_FOLDER);
        // Create a storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            mediaStorageDir.mkdirs();
        }
        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File mediaFile;
        selectedOutputPath = mediaStorageDir.getPath() + File.separator + "IMG_" + timeStamp + ".jpg";

        mediaFile = new File(selectedOutputPath);


        FileOutputStream fileOutputStream;
        try {
            fileOutputStream = new FileOutputStream(mediaFile);
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            BitmapPrepareCard.compress(Bitmap.CompressFormat.JPEG, 100, stream);
            byte[] byteArray = stream.toByteArray();
            fileOutputStream.write(byteArray);
            fileOutputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(mediaFile)));

        startActivity(new Intent(CardActivity.this, PreviewCardActivity.class).putExtra("myMalaImage", mediaFile));
        overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);

        rl_capture_image.setDrawingCacheEnabled(false);

    }


    public void TakeSS(){

               /* String str = Environment.getExternalStorageDirectory() + "/BhojpuriStatus";
                File file = new File(str);
                if (!file.exists()) {
                    if (file.mkdirs()) {
                        isFolder = true;
                    } else {
                        isFolder = false;
                        str = String.valueOf(context.getExternalFilesDir("/files/Videos/"));
                        File file2 = new File(str);
                        if (!file2.exists() && !file2.mkdirs()) {
                            str = String.valueOf(context.getExternalFilesDir("BhojpuriStatus"));
                            File file3 = new File(str);
                            if (!file3.exists()) {
                                file3.mkdirs();
                            }
                        }
                    }
                }*/



        rl_capture_image.setDrawingCacheEnabled(true);
        Bitmap BitmapPrepareCard = rl_capture_image.getDrawingCache();




        File externalStoragePublicDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File file = new File(externalStoragePublicDirectory.getAbsolutePath() + "/" + Constants.MEDIA_FOLDER);
        if (!file.exists())
            file.mkdirs();
        String str = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date()) + ".jpeg";
        File file2 = new File(file, str);
        file2.renameTo(file2);
        String str2 = "file://" + externalStoragePublicDirectory.getAbsolutePath() + "/" + Constants.MEDIA_FOLDER + "/" + str;
        String str3 = externalStoragePublicDirectory.getAbsolutePath() + "/" + Constants.MEDIA_FOLDER + "/" + str;



        FileOutputStream fileOutputStream;
        try {
            fileOutputStream = new FileOutputStream(str2);
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            BitmapPrepareCard.compress(Bitmap.CompressFormat.JPEG, 100, stream);
            byte[] byteArray = stream.toByteArray();
            fileOutputStream.write(byteArray);
            fileOutputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(file)));

        startActivity(new Intent(CardActivity.this, PreviewCardActivity.class).putExtra("myMalaImage", file));
        overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);

        rl_capture_image.setDrawingCacheEnabled(false);
    }



    private void init() {
        delete_rl = (RelativeLayout) findViewById(R.id.delete_rl);
        rl_capture_image = (RelativeLayout) findViewById(R.id.rl_capture_image);

        ivDone = (ImageView) findViewById(R.id.iv_done);
        ivCardImag = (ImageView) findViewById(R.id.iv_main_img);
        mail_layout = (RelativeLayout) findViewById(R.id.mail_layout);
        rl_color = (RelativeLayout) findViewById(R.id.rl_color);
        llFontPlatte = (HorizontalListView) findViewById(R.id.prepare_card_lv_font_platte);
        ivDecrease = (ImageView) findViewById(R.id.iv_decrease);
        ivStikers = (ImageView) findViewById(R.id.iv_stikers);
        ivIncrease = (ImageView) findViewById(R.id.iv_increase);
        iv_color_picker = (ImageView) findViewById(R.id.iv_color_picker);
        iv_font = (ImageView) findViewById(R.id.iv_font);

        drawingViewColorPickerRecyclerView = (RecyclerView) findViewById(R.id.drawing_view_color_picker_recycler_view);

        tv_venue = (TextView) findViewById(R.id.tv_venue);
        if (intent.getStringExtra("sVanue").equalsIgnoreCase("")||intent.getStringExtra("sVanue").equalsIgnoreCase(null)){
            tv_venue.setVisibility(View.INVISIBLE);
        }else {
            tv_venue.setVisibility(View.VISIBLE);
            tv_venue.setText("Venue :\n" + intent.getStringExtra("sVanue"));
        }
        tv_dear_text = (TextView) findViewById(R.id.tv_dear_text);
        if (intent.getStringExtra("sTo").equalsIgnoreCase("")||intent.getStringExtra("sTo").equalsIgnoreCase(null)){
            tv_dear_text.setVisibility(View.INVISIBLE);
        }else {
            tv_dear_text.setVisibility(View.VISIBLE);
            tv_dear_text.setText("Dear " + intent.getStringExtra("sTo") + ",");
        }

        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_title.setText(intent.getStringExtra("sTitle"));
        tv_time = (TextView) findViewById(R.id.tv_time);
        if (intent.getStringExtra("sTime").equalsIgnoreCase("")||intent.getStringExtra("sTime").equalsIgnoreCase(null)){
            tv_time.setVisibility(View.INVISIBLE);
        }else {
            tv_time.setVisibility(View.VISIBLE);
            tv_time.setText("Time:" + intent.getStringExtra("sTime"));

        }
        tv_dear_msg = (TextView) findViewById(R.id.tv_dear_msg);
        tv_dear_msg.setText(intent.getStringExtra("sMessage"));
        tv_from = (TextView) findViewById(R.id.tv_from);
        if (intent.getStringExtra("sFrom").equalsIgnoreCase("")||intent.getStringExtra("sFrom").equalsIgnoreCase(null)){
            tv_from.setVisibility(View.INVISIBLE);
        }else {
            tv_from.setVisibility(View.VISIBLE);
            tv_from.setText("From :\n" + intent.getStringExtra("sFrom"));

        }
        tv_data = (TextView) findViewById(R.id.tv_data);
        if (intent.getStringExtra("sData").equalsIgnoreCase("")||intent.getStringExtra("sData").equalsIgnoreCase(null)){
            tv_data.setVisibility(View.INVISIBLE);
        }else {
            tv_data.setVisibility(View.VISIBLE);
            tv_data.setText("Date:" + intent.getStringExtra("sData"));
        }
        ivProfileImage = (CircleImageView) findViewById(R.id.iv_add_img);

        tv_dear_text.setOnTouchListener(this);
        tv_title.setOnTouchListener(this);
        tv_dear_msg.setOnTouchListener(this);
        tv_from.setOnTouchListener(this);
        tv_data.setOnTouchListener(this);
        tv_time.setOnTouchListener(this);
        tv_venue.setOnTouchListener(this);
        ivProfileImage.setOnTouchListener(this);

        CardActivity.this.tv_title.setTextSize(0, CardActivity.this.tv_title.getTextSize() - 0.5f);
        CardActivity.this.tv_dear_text.setTextSize(0, CardActivity.this.tv_dear_text.getTextSize() - 0.5f);
        CardActivity.this.tv_dear_msg.setTextSize(0, CardActivity.this.tv_dear_msg.getTextSize() - 0.5f);
        CardActivity.this.tv_from.setTextSize(0, CardActivity.this.tv_from.getTextSize() - 0.5f);
        CardActivity.this.tv_data.setTextSize(0, CardActivity.this.tv_data.getTextSize() - 0.5f);
        CardActivity.this.tv_time.setTextSize(0, CardActivity.this.tv_time.getTextSize() - 0.5f);
        CardActivity.this.tv_venue.setTextSize(0, CardActivity.this.tv_venue.getTextSize() - 0.5f);

        LinearLayoutManager layoutManager = new LinearLayoutManager(CardActivity.this, LinearLayoutManager.HORIZONTAL, false);
        drawingViewColorPickerRecyclerView.setLayoutManager(layoutManager);
        drawingViewColorPickerRecyclerView.setHasFixedSize(true);

        ColorPickerAdapter colorPickerAdapter = new ColorPickerAdapter(CardActivity.this, colorPickerColors);
        colorPickerAdapter.setOnColorPickerClickListener(new ColorPickerAdapter.OnColorPickerClickListener() {
            @Override
            public void onColorPickerClickListener(int colorCode) {
                tv_title.setTextColor(colorCode);
                tv_dear_text.setTextColor(colorCode);
                tv_dear_msg.setTextColor(colorCode);
                tv_from.setTextColor(colorCode);
                tv_data.setTextColor(colorCode);
                tv_time.setTextColor(colorCode);
                tv_venue.setTextColor(colorCode);
            }
        });
        drawingViewColorPickerRecyclerView.setAdapter(colorPickerAdapter);
    }


    @Override
    public void onEditTextChangeListener(String text, int colorCode) {

    }

    @Override
    public void onAddViewListener(ViewType viewType, int numberOfAddedViews) {

    }

    @Override
    public void onRemoveViewListener(int numberOfAddedViews) {

    }

    @Override
    public void onStartViewChangeListener(ViewType viewType) {

    }

    @Override
    public void onStopViewChangeListener(ViewType viewType) {

    }

    public Bitmap decodeSampledBitmapFromResource(Resources res, int resId, int reqWidth, int reqHeight) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resId, options);
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(res, resId, options);
    }

    public int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;
        if (height > reqHeight || width > reqWidth) {
            final int halfHeight = height / 2;
            final int halfWidth = width / 2;
            while ((halfHeight / inSampleSize) >= reqHeight
                    && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2;
            }
        }
        return inSampleSize;
    }

    public class simpleOnScaleGestureListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        public boolean onScale(ScaleGestureDetector detector) {
            float product = CardActivity.this.tv_dear_text.getTextSize() * detector.getScaleFactor();
            CardActivity.this.tv_dear_text.setTextSize(0, product);
            //CardActivity.this.tv_title.setTextSize(0, product);
            CardActivity.this.tv_dear_msg.setTextSize(0, product);
            CardActivity.this.tv_from.setTextSize(0, product);
            CardActivity.this.tv_data.setTextSize(0, product);
            CardActivity.this.tv_time.setTextSize(0, product);
            CardActivity.this.tv_venue.setTextSize(0, product);
            return true;
        }
    }

    public boolean onTouchEvent(MotionEvent event) {
        this.scaleGestureDetector.onTouchEvent(event);
        return true;
    }

    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case 0:
                this.dX = v.getX() - event.getRawX();
                this.dY = v.getY() - event.getRawY();
                break;
            case 2:
                v.animate().x(event.getRawX() + this.dX).y(event.getRawY() + this.dY).setDuration(0).start();
                break;
            default:
                return false;
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
    }
}
