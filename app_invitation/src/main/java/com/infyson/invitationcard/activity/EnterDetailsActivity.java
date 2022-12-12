package com.infyson.invitationcard.activity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TimePicker;

import com.infyson.invitationcard.R;
import com.infyson.invitationcard.classes.AdmobBabberClass;
import com.infyson.invitationcard.classes.CircleImageView;
import com.infyson.invitationcard.classes.Constants;
import com.infyson.invitationcard.classes.CustomButton;
import com.infyson.invitationcard.classes.CustomEditText;
import com.infyson.invitationcard.classes.CustomTextView;
import com.infyson.invitationcard.classes.Utility;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;

public class EnterDetailsActivity extends MediaActivity {

    CustomEditText edtFrom, edtTo, edtVanue, edtTime, edtDate;
    private int mYear, mMonth, mDay, mHour, mMinute, mAMPM;
    CircleImageView ivProfil;
    private int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    private String userChoosenTask;
    CustomButton btnNext, btnBack, btnClear;
    String sTitle, sMessage;
    public Bitmap thumbnail;
    Intent intent;

    ImageView ivCamera, ivGallery;
    RelativeLayout bannerAdContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_details);

        this.getSupportActionBar().setDisplayShowCustomEnabled(true);
        this.getSupportActionBar().setDisplayShowTitleEnabled(false);
        LayoutInflater inflator = LayoutInflater.from(this);
        View v = inflator.inflate(R.layout.title_preview, null);
        ((CustomTextView) v.findViewById(R.id.tv_actionbar_title)).setCustomText(R.string.activity_enter_details, Constants.FOURTH_FONT);
        this.getSupportActionBar().setCustomView(v);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        intent = getIntent();
        sMessage = intent.getStringExtra("sMessage");
        sTitle = intent.getStringExtra("sTitle");
        init();
        AdmobBabberClass.BannerAdShow(getApplicationContext(),bannerAdContainer);
    }

    public boolean isValid() {
        if (edtFrom.getText().toString().length() == 0) {
            edtFrom.setError("From is required!");
            return false;
        }
        if (edtTo.getText().toString().length() == 0) {
            edtTo.setError("To is required!");
            return false;
        }
        if (edtVanue.getText().toString().length() == 0) {
            edtVanue.setError("Vanue is required!");
            return false;
        }
        if (edtTime.getText().toString().length() == 0) {
            edtTime.setError("Time is required!");
            return false;
        }
        if (edtDate.getText().toString().length() == 0) {
            edtDate.setError("Date is required!");
            return false;
        }

        return true;
    }

    private void init() {
        bannerAdContainer= findViewById(R.id.bannerAdContainer);
        ivCamera = (ImageView) findViewById(R.id.ic_camera);
        ivGallery = (ImageView) findViewById(R.id.ic_gellary);
        btnClear = (CustomButton) findViewById(R.id.btn_clear);
        btnClear.setCustomText(R.string.clear, Constants.FIRST_FONT);
        btnNext = (CustomButton) findViewById(R.id.btn_next);
        btnNext.setCustomText(R.string.next, Constants.FIRST_FONT);
        btnBack = (CustomButton) findViewById(R.id.btn_back);
        btnBack.setCustomText(R.string.back, Constants.FIRST_FONT);

        edtFrom = (CustomEditText) findViewById(R.id.edt_from);
        edtFrom.setCustomHint(R.string.from, Constants.THIRD_FONT);

        edtTo = (CustomEditText) findViewById(R.id.edt_to);
        edtTo.setCustomHint(R.string.to, Constants.THIRD_FONT);

        edtVanue = (CustomEditText) findViewById(R.id.edt_venue);
        edtVanue.setCustomHint(R.string.vanue, Constants.THIRD_FONT);

        edtTime = (CustomEditText) findViewById(R.id.edt_time);
        edtTime.setCustomHint(R.string.time, Constants.THIRD_FONT);

        edtDate = (CustomEditText) findViewById(R.id.edt_date);
        edtDate.setCustomHint(R.string.date, Constants.THIRD_FONT);

        ivProfil = (CircleImageView) findViewById(R.id.iv_profile);

        btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edtFrom.setText("");
                edtTo.setText("");
                edtVanue.setText("");
                edtTime.setText("");
                edtDate.setText("");
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
            }
        });

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(EnterDetailsActivity.this, CardActivity.class)
                        .putExtra("sMessage", sMessage)
                        .putExtra("sTitle", sTitle)
                        .putExtra("sFrom", edtFrom.getText().toString().trim())
                        .putExtra("sTo", edtTo.getText().toString().trim())
                        .putExtra("sVanue", edtVanue.getText().toString().trim())
                        .putExtra("sData", edtDate.getText().toString().trim())
                        .putExtra("sTime", edtTime.getText().toString().trim())
                        .putExtra("ivProfile", selectedImagePath)
                        .putExtra("CardUrl", intent.getStringExtra("CardUrl"))
                );
                overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);

            }
        });

        edtDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog = new DatePickerDialog(EnterDetailsActivity.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                edtDate.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });

        edtTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get Current Time
                final Calendar c = Calendar.getInstance();
                mHour = c.get(Calendar.HOUR_OF_DAY);
                mMinute = c.get(Calendar.MINUTE);
                // Launch Time Picker Dialog
                TimePickerDialog timePickerDialog = new TimePickerDialog(EnterDetailsActivity.this,
                        new TimePickerDialog.OnTimeSetListener() {

                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay,
                                                  int minute) {
                                mHour = hourOfDay;
                                mMinute = minute;
                                String AM_PM;
                                if (hourOfDay < 12) {
                                    AM_PM = "AM";

                                } else {
                                    AM_PM = "PM";
                                    mHour = mHour - 12;
                                }

                                edtTime.setText(hourOfDay + ":" + minute + ":" + AM_PM);
                            }
                        }, mHour, mMinute, false);
                timePickerDialog.show();
            }
        });

        ivProfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //selectImage();
            }
        });

        ivCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startCameraActivity();
            }
        });
        ivGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case Utility.MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (userChoosenTask.equals("Take Photo"))
                        cameraIntent();
                    else if (userChoosenTask.equals("Choose from Library"))
                        galleryIntent();
                } else {
                    //code for deny
                }
                break;
        }
    }


    private void galleryIntent() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);//
        startActivityForResult(Intent.createChooser(intent, "Select File"), SELECT_FILE);
    }

    private void cameraIntent() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_CAMERA);
    }

   /* @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_FILE)
                onSelectFromGalleryResult(data);
            else if (requestCode == REQUEST_CAMERA)
                onCaptureImageResult(data);
        }
    }*/

    @Override
    protected void onPhotoTaken() {

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 1;
        Bitmap bitmap = BitmapFactory.decodeFile(selectedImagePath, options);

        ivProfil.setImageBitmap(bitmap);
    }

    private void onCaptureImageResult(Intent data) {
        thumbnail = (Bitmap) data.getExtras().get("data");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        thumbnail.compress(Bitmap.CompressFormat.PNG, 100, bytes);

        File destination = new File(Environment.getExternalStorageDirectory(),
                System.currentTimeMillis() + ".jpg");

        FileOutputStream fo;
        try {
            destination.createNewFile();
            fo = new FileOutputStream(destination);
            fo.write(bytes.toByteArray());
            fo.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        ivProfil.setImageBitmap(thumbnail);
    }

    @SuppressWarnings("deprecation")
    private void onSelectFromGalleryResult(Intent data) {

        thumbnail = null;
        if (data != null) {
            try {
                thumbnail = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), data.getData());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

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
