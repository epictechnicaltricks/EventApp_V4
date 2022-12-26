package com.example.eventapp.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.print.PrintAttributes;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.example.eventapp.R;
import com.example.eventapp.adapter.GalleryAdapter;
import com.example.eventapp.fragment.BookEventFragment;
import com.example.eventapp.fragment.ChangePasswordFragment;
import com.example.eventapp.fragment.EditProfileFragment;
import com.example.eventapp.fragment.MyEventFragment;
import com.example.eventapp.fragment.ReportFragment;
import com.example.eventapp.interFace.OnClick;
import com.example.eventapp.response.DataRP;
import com.example.eventapp.response.EventDetailRP;
import com.example.eventapp.response.ProfileRP;
import com.example.eventapp.response.TicketDownloadRP;
import com.example.eventapp.response.TicketViewRP;
import com.example.eventapp.response.UserTicketListRP;
import com.example.eventapp.rest.ApiClient;
import com.example.eventapp.rest.ApiInterface;
import com.example.eventapp.util.API;
import com.example.eventapp.util.Constant;
import com.example.eventapp.util.Events;
import com.example.eventapp.util.FileUtil;
import com.example.eventapp.util.GlobalBus;
import com.example.eventapp.util.Method;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.rd.PageIndicatorView;
import com.uttampanchasara.pdfgenerator.CreatePdf;

import org.greenrobot.eventbus.Subscribe;
import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.Locale;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

import io.github.inflationx.viewpump.ViewPumpContextWrapper;
import io.github.lizhangqu.coreprogress.ProgressHelper;
import io.github.lizhangqu.coreprogress.ProgressUIListener;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.ResponseBody;
import okio.BufferedSink;
import okio.BufferedSource;
import okio.Okio;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EventDetail extends AppCompatActivity {




    private Method method;
    private OnClick onClick;
    private MaterialToolbar toolbar;
    private ProgressBar progressBar;
    private Animation myAnim;
    private Menu menu;
    private WebView webView;
    private int position;
    public String id, type;
    private ViewPager viewPager;
    private boolean isMenu = false;
    private EventDetailRP eventDetailRP;
    private ProgressDialog progressDialog;
    private PageIndicatorView pageIndicatorView;
    private CoordinatorLayout coordinatorLayout;
    private ConstraintLayout conNoData;
    private View view;
    private int REQUEST_CODE_PERMISSION_PDF = 101, REQUEST_CODE_PERMISSION_USERLIST = 102;
    private MaterialButton button, buttonUserList, buttonViewTicket, buttonDownloadTicket;
    private ImageView imageView, imageViewLogo, imageViewReport, imageViewFav, imageViewMap, imageViewPhone, imageViewEmail, imageViewWeb;
    private MaterialTextView textViewTitle, textViewAddress, textViewEventDateTime, textViewRegisterEventDateTime, textViewTitleRemaining, textViewRemaining,
            textViewPhone, textViewEmail, textViewWeb, textViewPerson, textViewPrice,   event_photo_text, reviews_text;


    private MaterialButton submit_msg, add_photo;

    TextInputEditText message;

    ArrayList<HashMap<String, Object>> firebase_msg_list = new ArrayList<>();
    ArrayList<HashMap<String, Object>> firebase_image_list = new ArrayList<>();

    ////////////////////////////////////////
    /** FIREBASE **/

    private int REQUEST_GALLERY_IMAGE_PICKER = 110;
    public final int REQ_CD_FP = 101;

    private FirebaseDatabase _firebase = FirebaseDatabase.getInstance();
    private FirebaseStorage _firebase_storage = FirebaseStorage.getInstance();

    private DatabaseReference user = _firebase.getReference("user");
    private ChildEventListener _user_child_listener;


    private DatabaseReference fb_images = _firebase.getReference("images");
    private ChildEventListener _fb_images_child_listener;
    



    private Calendar c = Calendar.getInstance();

    RecyclerView recyclerview1,recyclerview2;





    //////////////// pick the files from gallary

    private Intent fp = new Intent(Intent.ACTION_GET_CONTENT);
    private String path = "";
    private final ArrayList<HashMap<String, Object>> galleryLists = new ArrayList<>();

    RecyclerView recyclerview3;

    ////////////////////////////////////////



    //upload firebase storege

    private StorageReference img_db = _firebase_storage.getReference("img_db");
    private OnCompleteListener<Uri> _img_db_upload_success_listener;
    private OnSuccessListener<FileDownloadTask.TaskSnapshot> _img_db_download_success_listener;
    private OnSuccessListener _img_db_delete_success_listener;
    private OnProgressListener<UploadTask.TaskSnapshot> _img_db_upload_progress_listener;
    private OnProgressListener<FileDownloadTask.TaskSnapshot> _img_db_download_progress_listener;
    private OnFailureListener _img_db_failure_listener;


    private TimerTask upload_scheduler;
    private final Timer _timer = new Timer();
    private ArrayList<String> firebase_imgurl_list = new ArrayList<>();

    boolean next_upload = true;

    ProgressDialog progressDoalog;
    ///private final ProgressDialog progressDialog_upload = new ProgressDialog(EventDetail.this);
    int upload_quality = 15; // WEBP FORMAT
    String user_name, user_id_ , profile_img_url_;



    int galleryList_position=0;
    String _download_url_="";



    ///////////////////
    /** FIREBASE **/







    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_detail);

        FirebaseApp.initializeApp(this);

        progressDoalog = new ProgressDialog(EventDetail.this);

        recyclerview3 = findViewById(R.id.selected_img_recycler);
        recyclerview1 = findViewById(R.id.recyclerview1_msg);
        recyclerview2 = findViewById(R.id.recyclerview1_photos);


        event_photo_text  = findViewById(R.id.event_photo_text);

        reviews_text = findViewById(R.id.reviews_text);




        _user_child_listener = new ChildEventListener() {
    @Override
    public void onChildAdded(@NonNull DataSnapshot _param1, @Nullable String s) {

        GenericTypeIndicator<HashMap<String, Object>> _ind = new GenericTypeIndicator<HashMap<String, Object>>() {};
        final String _childKey = _param1.getKey();
        final HashMap<String, Object> _childValue = _param1.getValue(_ind);


        message.setText("");
       /* if(_childKey.equals(eventDetailRP.getId())){

        }*/

        user.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot _dataSnapshot) {
                firebase_msg_list = new ArrayList<>();
                try {
                    GenericTypeIndicator<HashMap<String, Object>> _ind = new GenericTypeIndicator<HashMap<String, Object>>() {};
                    for (DataSnapshot _data : _dataSnapshot.getChildren()) {
                        HashMap<String, Object> _map = _data.getValue(_ind);
                        firebase_msg_list.add(_map);
                    }
                }
                catch (Exception _e) {
                    _e.printStackTrace();
                }







                Collections.reverse(firebase_msg_list);
                recyclerview1.setAdapter(new Recyclerview1Adapter(firebase_msg_list));
                recyclerview1.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                recyclerview1.setLayoutManager(new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.HORIZONTAL, false));


                if(firebase_msg_list.size()>0){ reviews_text.setText("Reviews");}

            }
            @Override
            public void onCancelled(DatabaseError _databaseError) {

                Toast.makeText(EventDetail.this, _databaseError.toString(), Toast.LENGTH_SHORT).show();
            }
        });



       // Toast.makeText(EventDetail.this, "Added", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {



    }

    @Override
    public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

    }

    @Override
    public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

    }

    @Override
    public void onCancelled(@NonNull DatabaseError databaseError) {

        Toast.makeText(EventDetail.this, databaseError.toString(), Toast.LENGTH_SHORT).show();

    }
};
         user.addChildEventListener(_user_child_listener);



        fp.setType("image/*");
        fp.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);



    _fb_images_child_listener = new ChildEventListener() {
        @Override
        public void onChildAdded(@NonNull DataSnapshot _param1, @Nullable String s) {
            GenericTypeIndicator<HashMap<String, Object>> _ind = new GenericTypeIndicator<HashMap<String, Object>>() {};
            final String _childKey = _param1.getKey();
            final HashMap<String, Object> _childValue = _param1.getValue(_ind);
/*
            if(_childKey.equals(eventDetailRP.getId())){

            }*/

            fb_images.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot _dataSnapshot) {
                    firebase_image_list = new ArrayList<>();
                    try {
                        GenericTypeIndicator<HashMap<String, Object>> _ind = new GenericTypeIndicator<HashMap<String, Object>>() {};
                        for (DataSnapshot _data : _dataSnapshot.getChildren()) {
                            HashMap<String, Object> _map = _data.getValue(_ind);
                            firebase_image_list.add(_map);
                        }
                    }
                    catch (Exception _e) {
                        _e.printStackTrace();
                    }



                    Collections.reverse(firebase_image_list);
                    recyclerview2.setAdapter(new Recyclerview2Adapter(firebase_image_list));
                    recyclerview2.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                    recyclerview2.setLayoutManager(new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.HORIZONTAL, false));

                    //Toast.makeText(EventDetail.this, firebase_image_list.size()+"", Toast.LENGTH_SHORT).show();
                    if(firebase_image_list.size()>0){ event_photo_text.setText("Event Photos"); }
                }
                @Override
                public void onCancelled(DatabaseError _databaseError) {

                    Toast.makeText(EventDetail.this, _databaseError.toString(), Toast.LENGTH_SHORT).show();
                }
            });



        }

        @Override
        public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

        }

        @Override
        public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

        }

        @Override
        public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    };
    fb_images.addChildEventListener(_fb_images_child_listener);
    
      
        //////////////////


        submit_msg = findViewById(R.id.submit);
        add_photo = findViewById(R.id.upload);
        message =findViewById(R.id.message_);


        add_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(fp, REQ_CD_FP);
            }
        });






        submit_msg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!message.getText().toString().trim().equals(""))
                {
                    HashMap<String, Object> messages;
                    messages = new HashMap<>();
                    c = Calendar.getInstance();
                    messages.put("date",new SimpleDateFormat("dd MMM yyyy").format(c.getTime()));
                    messages.put("user_id", user_id_);
                    messages.put("name",user_name);
                    messages.put("event_id",eventDetailRP.getId());

                    // messages.put("user_img",profile_img_url_);
                    messages.put("msg", message.getText().toString());
                    user.push().updateChildren(messages);
                    //user.child(user_id_).updateChildren(messages);
                    messages.clear();
                }



                if(galleryLists.size()>0){


                    progressDoalog.setMax(100);
                    progressDoalog.setMessage("Uploading..");
                    progressDoalog.setCancelable(false);
                    progressDoalog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                    progressDoalog.show();


                 //  progressDialog_upload.setMessage("Uploading images..");
                 //  progressDialog_upload.setMax(100);
                 //  progressDialog_upload.setCancelable(false);
                 //  progressDialog_upload.show();
                    galleryList_position = 0;


                  for (int x=0; x<galleryLists.size(); x++)
                  {
                      Log.d("paths", Objects.requireNonNull(galleryLists.get(x).get("path_url")).toString());
                  }

                    upload_scheduler = new TimerTask() {
                        @Override
                        public void run() {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {


                                    if(next_upload) {

                                        if (galleryList_position < galleryLists.size()) {

                                            try {
                                                if(galleryLists.size()==1){
                                                    progressDoalog.setMessage("Uploading "+(galleryList_position+1)+"/"+galleryLists.size() +" image");
                                                }else {
                                                    progressDoalog.setMessage("Uploading "+(galleryList_position+1)+"/"+galleryLists.size() +" images");
                                                }

                                                c = Calendar.getInstance();

                                                uploadToFirebase(Objects.requireNonNull(galleryLists.get(galleryList_position).get("path_url")).toString(),String.valueOf(c.getTimeInMillis()));


                                                 galleryList_position++;
                                                next_upload = false;


                                            }catch (Exception e)
                                            {
                                                Log.d("upload_error",e.toString());
                                            }


                                        } else {

                                            upload_scheduler.cancel(); // this will always on top
                                            next_upload = true;
                                            Toast.makeText(EventDetail.this, "Uploaded successfully.", Toast.LENGTH_SHORT).show();
                                            progressDoalog.dismiss();
                                           // finish();
                                           // startActivity(new Intent(getApplicationContext(),EventDetail.class));



                                        }
                                    }
                                }
                            });
                        }
                    };
                    _timer.scheduleAtFixedRate(upload_scheduler, 0, 100);



                }


              /*  HashMap<String, Object> a;
                a = new HashMap<>();
                c = Calendar.getInstance();
                a.put("date",new SimpleDateFormat("dd MMM yyyy").format(c.getTime()));
                a.put("id", method.userId());
                a.put("name","Demo user");
                a.put("user_img",method.userImage.trim());
                a.put("msg", message.getText().toString());
                a.put("img_url", "https://wallpaperaccess.com/full/8053661.jpg");
                user.push().updateChildren(a);
                //user.child("1").updateChildren(a);
                a.clear();*/



            }
        });




        ///////////


        onClick = (position, type, title, id) -> {
            switch (type) {
                case "event_gallery":
                    if (eventDetailRP != null) {
                        Constant.galleryLists.clear();
                        Constant.galleryLists.addAll(eventDetailRP.getGalleryLists());
                        startActivity(new Intent(EventDetail.this, GalleryView.class)
                                .putExtra("position", position));
                    }
                    break;
                case "event_image":
                    startActivity(new Intent(EventDetail.this, EventImageView.class)
                            .putExtra("url", eventDetailRP.getEvent_banner()));
                    break;
                default:
                    startActivity(new Intent(EventDetail.this, EventImageView.class)
                            .putExtra("url", eventDetailRP.getEvent_logo()));
                    break;
            }

        };
        method = new Method(EventDetail.this, onClick);
        method.forceRTLIfSupported();

        GlobalBus.getBus().register(this);

        progressDialog = new ProgressDialog(EventDetail.this);
        myAnim = AnimationUtils.loadAnimation(EventDetail.this, R.anim.bounce);

        Intent intent = getIntent();
        id = intent.getStringExtra("id");
        type = intent.getStringExtra("type");
        position = intent.getIntExtra("position", 0);

        int columnWidth = method.getScreenWidth();

        toolbar = findViewById(R.id.toolbar_event_detail);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        final CollapsingToolbarLayout collapsingToolbarLayout = findViewById(R.id.collapsing_toolbar);
        collapsingToolbarLayout.setTitleEnabled(false);

        coordinatorLayout = findViewById(R.id.main_content);
        coordinatorLayout.setVisibility(View.GONE);

        AppBarLayout appBarLayout = findViewById(R.id.appbar);
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = true;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0) {
                    toolbar.setTitle(getResources().getString(R.string.event_detail));
                    isShow = true;
                } else if (isShow) {
                    toolbar.setTitle("");
                    isShow = false;
                }
            }
        });

        progressBar = findViewById(R.id.progressbar_ed);

        imageView = findViewById(R.id.imageView_ed);
        view = findViewById(R.id.view_ed);
        imageViewLogo = findViewById(R.id.imageView_logo_ed);
        imageViewReport = findViewById(R.id.imageView_report_ed);
        imageViewFav = findViewById(R.id.imageView_fav_ed);
        imageViewMap = findViewById(R.id.imageView_map_ed);
        imageViewPhone = findViewById(R.id.imageView_phone_ed);
        imageViewEmail = findViewById(R.id.imageView_email_ed);
        imageViewWeb = findViewById(R.id.imageView_web_ed);
        conNoData = findViewById(R.id.con_noDataFound);

        textViewTitle = findViewById(R.id.textView_title_ed);
        textViewPrice = findViewById(R.id.textView_price_ed);
        textViewPerson = findViewById(R.id.textView_person_ed);
        textViewAddress = findViewById(R.id.textView_address_ed);
        textViewEventDateTime = findViewById(R.id.textView_eventDateTime_ed);
        textViewRegisterEventDateTime = findViewById(R.id.textView_registerEvent_dateTime_ed);
        textViewTitleRemaining = findViewById(R.id.textView_titleRemaining_ed);
        textViewRemaining = findViewById(R.id.textView_remaining_ed);
        textViewPhone = findViewById(R.id.textView_phone_ed);
        textViewEmail = findViewById(R.id.textView_email_ed);
        textViewWeb = findViewById(R.id.textView_web_ed);
        webView = findViewById(R.id.webView_ed);

        buttonDownloadTicket = findViewById(R.id.button_downloadTicket_ed);
        buttonViewTicket = findViewById(R.id.button_viewTicket_ed);
        buttonUserList = findViewById(R.id.button_userList_ed);
        button = findViewById(R.id.button_event_booking_ed);

        viewPager = findViewById(R.id.viewpager_ed);
        pageIndicatorView = findViewById(R.id.pageIndicatorView);

        LinearLayout linearLayout = findViewById(R.id.linearLayout_ed);
        method.bannerAd(linearLayout);

        conNoData.setVisibility(View.GONE);
        button.setVisibility(View.GONE);

        if (type.equals("book_event")) {
            buttonDownloadTicket.setVisibility(View.VISIBLE);
            buttonViewTicket.setVisibility(View.VISIBLE);
        } else {
            buttonDownloadTicket.setVisibility(View.GONE);
            buttonViewTicket.setVisibility(View.GONE);
        }

        imageView.setLayoutParams(new ConstraintLayout.LayoutParams(columnWidth, (int) (columnWidth / 1.5)));
        viewPager.setLayoutParams(new ConstraintLayout.LayoutParams(columnWidth, (int) (columnWidth / 1.5)));
        view.setLayoutParams(new ConstraintLayout.LayoutParams(columnWidth, (int) (columnWidth / 1.5)));

        callData();

    }




    private void uploadToFirebase(String imagefilepath_, String file_name) throws IOException {

      //  img_db.child(file_name).putFile(Uri.fromFile(new File(imagefilepath_)));

        Bitmap bmp = MediaStore.Images.Media.getBitmap(getContentResolver(),Uri.fromFile(new File(imagefilepath_)));
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

            bmp.compress(Bitmap.CompressFormat.WEBP, upload_quality , baos);

        byte[] data = baos.toByteArray();
        //uploading the image
        UploadTask uploadTask2 = img_db.child(file_name).putBytes(data);

        uploadTask2.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                if (taskSnapshot.getMetadata() != null) {
                    if (taskSnapshot.getMetadata().getReference() != null) {
                        Task<Uri> result = taskSnapshot.getStorage().getDownloadUrl();
                        result.addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {

                                Log.d("download url", uri.toString());


                              //////////////////////////////////////////////
                                _download_url_ = uri.toString();
                                Log.d("paths download url", _download_url_);
                                HashMap<String, Object> a;
                                a = new HashMap<>();
                                a.put("img_url", _download_url_);
                                c = Calendar.getInstance();
                                a.put("img_id",c.getTimeInMillis());
                                a.put("user_id",user_id_);
                                a.put("event_id",eventDetailRP.getId());
                                //eventDetailRP.getId();
                                fb_images.push().updateChildren(a);
                                a.clear();

                                ////////////////////////////////////////


                                next_upload = true;
                                Log.d("paths success","onSuccess");

                                //createNewPost(imageUrl);
                            }
                        });
                    }}


                // comment add







            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("paths Failure","Uploading Failed !!");
            }
        });

     /*   img_db.child(file_name)
                .putFile(Uri.fromFile(new File(imagefilepath_)))

                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {



                img_db.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Log.d("paths success"," getDownloadUrl() onSuccess " +uri.toString());
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("paths success","getDownloadUrl() onFailure "+e.toString() );
                    }
                }).addOnCanceledListener(new OnCanceledListener() {
                    @Override
                    public void onCanceled() {
                        Log.d("paths success","getDownloadUrl() onCanceled ");
                    }
                });




            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {



            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {



            }
        });*/
    }


/*    private void uploadThisImage(Object imagefilepath_ , String filename_){


        Toast.makeText(EventDetail.this, imagefilepath_.toString(), Toast.LENGTH_SHORT).show();


        c = Calendar.getInstance();

        ///new SimpleDateFormat("hh:mm:ss,dd-MMM-yyyy").format(String.valueOf((long)(c.getTimeInMillis())))
        try {
            img_db.child(filename_)
                    .putFile(Uri.fromFile(new File(imagefilepath_.toString())))
                    .addOnFailureListener(_img_db_failure_listener)
                    .addOnProgressListener(_img_db_upload_progress_listener)
                    .continueWithTask((Continuation<UploadTask.TaskSnapshot, Task<Uri>>) task ->
                            img_db.child(imagefilepath_.toString()).getDownloadUrl()).addOnCompleteListener(_img_db_upload_success_listener);
        }catch (Exception e){

            Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show();
            Log.d("upload",e.toString());

        }


 *//*       img_db.child(new SimpleDateFormat("hh:mm:ss,dd-MMM-yyyy")
                        .format(String.valueOf((long)(c.getTimeInMillis()))))
                .putFile(Uri.fromFile(new File(imagefilepath)))
                .addOnFailureListener(_img_db_failure_listener)
                .addOnProgressListener(_img_db_upload_progress_listener)
                .continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(Task<UploadTask.TaskSnapshot> task) throws Exception {
                        return img_db.child(new SimpleDateFormat("hh:mm:ss,dd-MMM-yyyy").format(c.getTime())).getDownloadUrl();
                    }
                }).addOnCompleteListener(_img_db_upload_success_listener);
*//*

    }*/


    @Subscribe
    public void getNotify(Events.EventUpdateDetail eventUpdateDetail) {
        coordinatorLayout.setVisibility(View.GONE);
        conNoData.setVisibility(View.GONE);
        button.setVisibility(View.GONE);
        callData();
    }

    @Subscribe
    public void getLogin(Events.Login login) {
        coordinatorLayout.setVisibility(View.GONE);
        conNoData.setVisibility(View.GONE);
        button.setVisibility(View.GONE);
        callData();
    }

    @Override
    protected void onActivityResult(int _requestCode, int _resultCode, @Nullable Intent _data) {
        super.onActivityResult(_requestCode, _resultCode, _data);
        if (_requestCode == REQ_CD_FP) {
            if (_resultCode == Activity.RESULT_OK) {
                ArrayList<String> _filePath = new ArrayList<>();
                if (_data != null) {
                    if (_data.getClipData() != null) {
                        for (int _index = 0; _index < _data.getClipData().getItemCount(); _index++) {
                            ClipData.Item _item = _data.getClipData().getItemAt(_index);
                            _filePath.add(FileUtil.convertUriToFilePath(getApplicationContext(), _item.getUri()));
                        }
                    } else {
                        _filePath.add(FileUtil.convertUriToFilePath(getApplicationContext(), _data.getData()));
                    }
                }
                //c = Calendar.getInstance();
                //path = _filePath.get((int) (0));

                galleryLists.clear();
                for(int x=0; _filePath.size()>x; x++){


                    HashMap<String, Object> path_map = new HashMap<>();
                    path_map.put("path_url",_filePath.get(x));
                    galleryLists.add(path_map);
                }



               // Collections.reverse(galleryLists);
                recyclerview3.setAdapter(new Recyclerview3Adapter(galleryLists));
                recyclerview3.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                recyclerview3.setLayoutManager(new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.HORIZONTAL, false));


                     } else {

            }
        }


    }





    public class Recyclerview1Adapter extends RecyclerView.Adapter<Recyclerview1Adapter.ViewHolder> {
        ArrayList<HashMap<String, Object>> _data;
        public Recyclerview1Adapter(ArrayList<HashMap<String, Object>> _arr) {
            _data = _arr;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater _inflater = (LayoutInflater)getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View _v = _inflater.inflate(R.layout.custom_msg_event_details, null);
            RecyclerView.LayoutParams _lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            _v.setLayoutParams(_lp);
            return new ViewHolder(_v);
        }

        @Override
        public void onBindViewHolder(ViewHolder _holder, final int _position) {
            View _view = _holder.itemView;



            final TextView user_name = _view.findViewById(R.id.user_name);
            final TextView message = _view.findViewById(R.id.message);
            final TextView more_ = _view.findViewById(R.id.more_);

            final TextView date = _view.findViewById(R.id.date);



          // message.setTypeface(Typeface.createFromAsset(getAssets(),"fonts/montserrat_regular.ttf"), Typeface.NORMAL);
          // view.setTypeface(Typeface.createFromAsset(getAssets(),"fonts/montserrat_regular.ttf"), Typeface.NORMAL);
          // user_name.setTypeface(Typeface.createFromAsset(getAssets(),"fonts/montserrat_regular.ttf"), Typeface.BOLD);


            try{


                message.setText(Objects.requireNonNull(firebase_msg_list.get(_position).get("msg")).toString());
                user_name.setText(Objects.requireNonNull(firebase_msg_list.get(_position).get("name")).toString());


               date.setText(Objects.requireNonNull(firebase_msg_list.get(_position).get("date")).toString());


                if(Objects.requireNonNull(firebase_msg_list.get(_position).get("msg")).toString().length()>150)
                {
                    more_.setVisibility(View.VISIBLE);
                }else {

                    more_.setVisibility(View.INVISIBLE);
                }


            more_.setOnClickListener(v ->

                    method.alertBox(Objects.requireNonNull(firebase_msg_list.get(_position).get("msg")).toString()));



               /* api_map3 = new Gson().fromJson(Objects.requireNonNull(listmap2.get(_position).get("img_url")).toString(), new TypeToken<HashMap<String, Object>>(){}.getType());


                String img_url = Objects.requireNonNull(api_map3.get("img_url")).toString();

                Glide.with(getApplicationContext())
                        .load(Uri.parse(img_url))
                        .error(R.drawable.school2)
                        .placeholder(R.drawable.school2)
                        .thumbnail(0.01f)
                        .into(imageview2);*/


				/*ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
				ClipData clip = ClipData.newPlainText("Copied Text", img_url );
				clipboard.setPrimaryClip(clip);

				Log.d("img_obj", img_url);*/



            }catch (Exception e)
            {
                //showMessage("887 line "+e.toString());
            }



        }

        @Override
        public int getItemCount() {
            return _data.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder{
            public ViewHolder(View v){
                super(v);
            }
        }

    }

    public class Recyclerview2Adapter extends RecyclerView.Adapter<Recyclerview2Adapter.ViewHolder> {
        ArrayList<HashMap<String, Object>> _data;
        public Recyclerview2Adapter(ArrayList<HashMap<String, Object>> _arr) {
            _data = _arr;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater _inflater = (LayoutInflater)getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View _v = _inflater.inflate(R.layout.custom_image_event_details, null);
            RecyclerView.LayoutParams _lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            _v.setLayoutParams(_lp);
            return new ViewHolder(_v);
        }

        @Override
        public void onBindViewHolder(ViewHolder _holder, final int _position) {
            View _view = _holder.itemView;



        final ImageView img_ = _view.findViewById(R.id.img_events);

           try{


              //  message.setText(Objects.requireNonNull(firebase_msg_list.get(_position).get("msg")).toString());

              String img_url = Objects.requireNonNull(firebase_image_list.get(_position).get("img_url")).toString();


               Glide.with(getApplicationContext()).load(Uri.parse(img_url)).thumbnail(0.1f).into(img_);



               img_.setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View v) {
                       Intent i = new Intent();
                       i.putExtra("img",img_url);
                       i.putExtra("img_id",Objects.requireNonNull(firebase_image_list.get(_position).get("img_id")).toString());
                       i.setClass(getApplicationContext(), view_img.class);
                       startActivity(i);
                   }
               });


				/*ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
				ClipData clip = ClipData.newPlainText("Copied Text", img_url );
				clipboard.setPrimaryClip(clip);

				Log.d("img_obj", img_url);*/



            }catch (Exception e) {

                //showMessage("887 line "+e.toString());
            }



        }

        @Override
        public int getItemCount() {
            return _data.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder{
            public ViewHolder(View v){
                super(v);
            }
        }

    }


    public class Recyclerview3Adapter extends RecyclerView.Adapter<Recyclerview3Adapter.ViewHolder> {
        ArrayList<HashMap<String, Object>> _data;
        public Recyclerview3Adapter(ArrayList<HashMap<String, Object>> _arr) {
            _data = _arr;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater _inflater = (LayoutInflater)getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);



            View _v = _inflater.inflate(R.layout.selected_img_custom, parent, false);
            RecyclerView.LayoutParams _lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            _v.setLayoutParams(_lp);
            return new ViewHolder(_v);
        }

        @Override
        public void onBindViewHolder(ViewHolder _holder, final int _position) {
            View _view = _holder.itemView;



            final ImageView selected_img = _view.findViewById(R.id.selected_img);
            final ImageView delete = _view.findViewById(R.id.cancel_img);



            try{



                //  message.setText(Objects.requireNonNull(firebase_msg_list.get(_position).get("msg")).toString());

                String img_path_ = Objects.requireNonNull(Objects.requireNonNull(galleryLists.get(_position).get("path_url")).toString());

                selected_img.setImageBitmap(FileUtil.decodeSampleBitmapFromPath(img_path_,1024,1024));


                selected_img.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
/*

                        Intent intent = new Intent();
                        intent.setAction(android.content.Intent.ACTION_VIEW);
                        Uri uri = Uri.parse("file://" + img_path_);
                        intent.setDataAndType(uri,"image/*");
                        startActivity(intent);
*/


                    }
                });



                delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        galleryLists.remove(_position);

                        //Collections.reverse(galleryLists);
                        recyclerview3.setAdapter(new Recyclerview3Adapter(galleryLists));
                        recyclerview3.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                        recyclerview3.setLayoutManager(new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.HORIZONTAL, false));



                    }
                });


				/*ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
				ClipData clip = ClipData.newPlainText("Copied Text", img_url );
				clipboard.setPrimaryClip(clip);

				Log.d("img_obj", img_url);*/



            }catch (Exception e) {

                //showMessage("887 line "+e.toString());
            }



        }

        @Override
        public int getItemCount() {
            return _data.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder{
            public ViewHolder(View v){
                super(v);
            }
        }

    }



    private void callData() {
        new Handler().postDelayed(() -> {
            if (method.isNetworkAvailable()) {
                if (method.isLogin()) {

                    /// get profile
                    profile(method.userId());

                    eventDetail(method.userId(), id);
                } else {
                    eventDetail("0", id);
                }
            } else {
                method.alertBox(getResources().getString(R.string.internet_connection));
            }
        }, 500);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.event_detail, menu);
        this.menu = menu;
        if (!isMenu) {
            isMenu = true;
        }
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // action with ID action_refresh was selected
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }

        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    public void eventDetail(String userId, String id) {

        progressBar.setVisibility(View.VISIBLE);

        JsonObject jsObj = (JsonObject) new Gson().toJsonTree(new API(EventDetail.this));
        jsObj.addProperty("event_id", id);
        jsObj.addProperty("user_id", userId);
        jsObj.addProperty("method_name", "single_event");
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<EventDetailRP> call = apiService.getEventDetail(API.toBase64(jsObj.toString()));
        call.enqueue(new Callback<EventDetailRP>() {
            @SuppressLint({"SetJavaScriptEnabled", "UseCompatLoadingForDrawables"})
            @Override
            public void onResponse(@NotNull Call<EventDetailRP> call, @NotNull Response<EventDetailRP> response) {

                try {
                    eventDetailRP = response.body();
                    assert eventDetailRP != null;

                    if (eventDetailRP.getStatus().equals("1")) {

                        if (eventDetailRP.getSuccess().equals("1")) {

                            if (type.equals("book_event")) {
                                button.setVisibility(View.GONE);
                            } else {
                                if (eventDetailRP.isIs_booking()) {
                                    button.setVisibility(View.VISIBLE);
                                } else {
                                    button.setVisibility(View.GONE);
                                }
                            }

                            if (eventDetailRP.isIs_userList()) {
                                buttonUserList.setVisibility(View.VISIBLE);
                                textViewTitleRemaining.setVisibility(View.VISIBLE);
                                textViewRemaining.setVisibility(View.VISIBLE);
                                textViewRemaining.setText(eventDetailRP.getRemain_tickets());
                            } else {
                                buttonUserList.setVisibility(View.GONE);
                                textViewTitleRemaining.setVisibility(View.GONE);
                                textViewRemaining.setVisibility(View.GONE);
                            }

                            if (isMenu) {

                                MenuItem share = menu.findItem(R.id.action_share);
                                share.setOnMenuItemClickListener(item -> {

                                    Intent intent = new Intent(Intent.ACTION_SEND);
                                    intent.setType("text/plain");
                                    intent.putExtra(Intent.EXTRA_TEXT, eventDetailRP.getShare_link());
                                    startActivity(Intent.createChooser(intent, getResources().getString(R.string.choose_one)));

                                    return false;
                                });

                                MenuItem more = menu.findItem(R.id.action_more_option);
                                if (eventDetailRP.isIs_userList()) {
                                    more.setVisible(true);
                                    MenuItem menuEdit = more.getSubMenu().findItem(R.id.edit_event);
                                    MenuItem menuDelete = more.getSubMenu().findItem(R.id.delete_event);

                                    menuEdit.setOnMenuItemClickListener(item -> {
                                        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(EventDetail.this, R.style.DialogTitleTextStyle);
                                        builder.setMessage(getResources().getString(R.string.edit_event_message));
                                        builder.setCancelable(false);
                                        builder.setPositiveButton(getResources().getString(R.string.yes),
                                                (arg0, arg1) -> startActivity(new Intent(EventDetail.this, CreateEvent.class)
                                                        .putExtra("type", "edit_event")
                                                        .putExtra("id", eventDetailRP.getId())
                                                        .putExtra("position", position)));
                                        builder.setNegativeButton(getResources().getString(R.string.no), (dialog, which) -> {

                                        });

                                        AlertDialog alertDialog = builder.create();
                                        alertDialog.show();
                                        return false;
                                    });

                                    menuDelete.setOnMenuItemClickListener(item -> {
                                        deleteDialog(eventDetailRP.getId(), position);
                                        return false;
                                    });

                                } else {
                                    more.setVisible(false);
                                }
                            }

                            if (eventDetailRP.isIs_fav()) {
                                imageViewFav.setImageDrawable(getResources().getDrawable(R.drawable.ic_fav_hov));
                            } else {
                                imageViewFav.setImageDrawable(getResources().getDrawable(R.drawable.ic_fav));
                            }

                            Glide.with(EventDetail.this).load(eventDetailRP.getEvent_logo_thumb())
                                    .placeholder(R.drawable.placeholder_logo)
                                    .into(imageViewLogo);

                            imageViewLogo.setOnClickListener(v -> {
                                method.click(0, "event_logo", "", "");
                            });

                            textViewTitle.setText(eventDetailRP.getEvent_title());
                            textViewAddress.setText(eventDetailRP.getEvent_address());
                            textViewEventDateTime.setText(eventDetailRP.getEvent_date_time());
                            textViewRegisterEventDateTime.setText(eventDetailRP.getEvent_registration_date_time());
                            textViewPhone.setText(eventDetailRP.getEvent_phone());
                            textViewEmail.setText(eventDetailRP.getEvent_email());
                            textViewWeb.setText(eventDetailRP.getEvent_website());
                            textViewPrice.setText(eventDetailRP.getTicket_price());
                            textViewPerson.setText(eventDetailRP.getEvent_ticket());

                            webView.setBackgroundColor(Color.TRANSPARENT);
                            webView.setFocusableInTouchMode(false);
                            webView.setFocusable(false);
                            webView.getSettings().setDefaultTextEncodingName("UTF-8");
                            webView.getSettings().setJavaScriptEnabled(true);
                            String mimeType = "text/html";
                            String encoding = "utf-8";

                            String text = "<html dir=" + method.isWebViewTextRtl() + "><head>"
                                    + "<style type=\"text/css\">@font-face {font-family: MyFont;src: url(\"file:///android_asset/fonts/montserrat_regular.ttf\")}body{font-family: MyFont;color: " + method.webViewText() + "line-height:1.6}"
                                    + "a {color:" + method.webViewLink() + "text-decoration:none}"
                                    + "</style>"
                                    + "</head>"
                                    + "<body>"
                                    + eventDetailRP.getEvent_description()
                                    + "</body></html>";

                            webView.loadDataWithBaseURL(null, text, mimeType, encoding, null);

                            if (eventDetailRP.getGalleryLists().size() == 0) {
                                imageView.setVisibility(View.VISIBLE);
                                viewPager.setVisibility(View.GONE);
                                pageIndicatorView.setVisibility(View.GONE);
                                Glide.with(EventDetail.this)
                                        .load(eventDetailRP.getEvent_banner())
                                        .placeholder(R.drawable.placeholder_banner)
                                        .into(imageView);
                                imageView.setOnClickListener(v -> {
                                    method.click(0, "event_image", "", "");
                                });
                            } else {
                                imageView.setVisibility(View.GONE);
                                viewPager.setVisibility(View.VISIBLE);
                                pageIndicatorView.setVisibility(View.VISIBLE);
                                GalleryAdapter galleryDetailAdapter = new GalleryAdapter(EventDetail.this, eventDetailRP.getGalleryLists(), "event_gallery", onClick);
                                viewPager.setAdapter(galleryDetailAdapter);
                            }

                            coordinatorLayout.setVisibility(View.VISIBLE);

                            imageViewReport.setOnClickListener(v -> {
                                if (method.isLogin()) {
                                    BottomSheetDialogFragment bottomSheetDialogFragment = new ReportFragment();
                                    Bundle bundle = new Bundle();
                                    bundle.putString("event_id", id);
                                    bottomSheetDialogFragment.setArguments(bundle);
                                    bottomSheetDialogFragment.show(getSupportFragmentManager(), "Bottom Sheet Dialog Fragment");
                                } else {
                                    Method.loginBack = true;
                                    startActivity(new Intent(EventDetail.this, Login.class));
                                }
                            });

                            imageViewFav.setOnClickListener(v -> {
                                if (method.isLogin()) {
                                    method.addToFav(eventDetailRP.getId(), method.userId(), "detail", 0, (isFavourite, message) -> {
                                        if (isFavourite) {
                                            imageViewFav.setImageDrawable(getResources().getDrawable(R.drawable.ic_fav_hov));
                                        } else {
                                            imageViewFav.setImageDrawable(getResources().getDrawable(R.drawable.ic_fav));
                                        }
                                    });
                                } else {
                                    Method.loginBack = true;
                                    startActivity(new Intent(EventDetail.this, Login.class));
                                }
                            });

                            imageViewWeb.setOnClickListener(v -> {
                                imageViewWeb.startAnimation(myAnim);
                                try {
                                    String url = eventDetailRP.getEvent_website();
                                    if (!url.startsWith("http://") && !url.startsWith("https://")) {
                                        url = "http://" + url;
                                    }
                                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                                    startActivity(browserIntent);
                                } catch (Exception e) {
                                    method.alertBox(getResources().getString(R.string.wrong));
                                }
                            });

                            imageViewPhone.setOnClickListener(v -> {
                                imageViewPhone.startAnimation(myAnim);
                                try {
                                    Intent callIntent = new Intent(Intent.ACTION_DIAL);
                                    callIntent.setData(Uri.parse("tel:" + eventDetailRP.getEvent_phone()));
                                    startActivity(callIntent);
                                } catch (Exception e) {
                                    method.alertBox(getResources().getString(R.string.wrong));
                                }
                            });

                            imageViewEmail.setOnClickListener(v -> {
                                imageViewEmail.startAnimation(myAnim);
                                try {
                                    Intent emailIntent = new Intent(Intent.ACTION_VIEW);
                                    emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{eventDetailRP.getEvent_email()});
                                    emailIntent.setData(Uri.parse("mailto:"));
                                    startActivity(emailIntent);
                                } catch (ActivityNotFoundException ex) {
                                    method.alertBox(getResources().getString(R.string.wrong));
                                }

                            });

                            imageViewMap.setOnClickListener(v -> {
                                if (method.isAppInstalled()) {
                                    try {
                                        String latitude = eventDetailRP.getEvent_map_latitude();
                                        String longitude = eventDetailRP.getEvent_map_longitude();
                                        Intent intent = new Intent(Intent.ACTION_VIEW,
                                                Uri.parse("http://maps.google.com/maps?daddr=" + latitude + "," + longitude));
                                        intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");
                                        startActivity(intent);
                                    } catch (Exception e) {
                                        method.alertBox(getResources().getString(R.string.wrong));
                                    }
                                } else {
                                    method.alertBox(getResources().getString(R.string.map_not_install));
                                }
                            });

                            buttonUserList.setOnClickListener(view -> {
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                                    requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_CODE_PERMISSION_USERLIST);
                                } else {
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                        requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE_PERMISSION_USERLIST);
                                    } else {
                                        userList(eventDetailRP.getId());
                                    }
                                }
                            });

                            buttonViewTicket.setOnClickListener(view -> {
                                if (method.isNetworkAvailable()) {
                                    viewTicket(eventDetailRP.getBooking_id());
                                } else {
                                    method.alertBox(getResources().getString(R.string.internet_connection));
                                }
                            });

                            buttonDownloadTicket.setOnClickListener(view -> {
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                                    requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_CODE_PERMISSION_PDF);
                                } else {
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                        requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE_PERMISSION_PDF);
                                    } else {
                                        ticketPdf(eventDetailRP.getBooking_id());
                                    }
                                }
                            });

                            button.setOnClickListener(v -> {
                                if (method.isNetworkAvailable()) {
                                    if (method.isLogin()) {
                                        startActivity(new Intent(EventDetail.this, EventBooking.class)
                                                .putExtra("event_id", id));
                                    } else {
                                        Method.loginBack = true;
                                        startActivity(new Intent(EventDetail.this, Login.class));
                                    }
                                } else {
                                    method.alertBox(getResources().getString(R.string.internet_connection));
                                }
                            });

                        } else {
                            conNoData.setVisibility(View.VISIBLE);
                            method.alertBox(eventDetailRP.getMsg());
                        }

                    } else if (eventDetailRP.getStatus().equals("2")) {
                        method.suspend(eventDetailRP.getMessage());
                    } else {
                        conNoData.setVisibility(View.VISIBLE);
                        method.alertBox(eventDetailRP.getMessage());
                    }

                } catch (Exception e) {
                    Log.d("exception_error", e.toString());
                    method.alertBox(getResources().getString(R.string.failed_try_again));
                }

                progressBar.setVisibility(View.GONE);

            }

            @Override
            public void onFailure(@NotNull Call<EventDetailRP> call, @NotNull Throwable t) {
                // Log error here since request failed
                Log.e("onFailure_data", t.toString());
                conNoData.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);
                method.alertBox(getResources().getString(R.string.failed_try_again));
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NotNull String[] permissions, @NotNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_PERMISSION_PDF) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (eventDetailRP != null) {
                    ticketPdf(eventDetailRP.getBooking_id());
                } else {
                    method.alertBox(getResources().getString(R.string.wrong));
                }
            } else {
                method.alertBox(getResources().getString(R.string.storage_permission));
            }
        }
        if (requestCode == REQUEST_CODE_PERMISSION_USERLIST) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (eventDetailRP != null) {
                    userList(eventDetailRP.getId());
                } else {
                    method.alertBox(getResources().getString(R.string.wrong));
                }
            } else {
                method.alertBox(getResources().getString(R.string.storage_permission));
            }
        }
    }

    public void deleteDialog(String eventId, int position) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(EventDetail.this, R.style.DialogTitleTextStyle);
        alertDialogBuilder.setCancelable(false);
        alertDialogBuilder.setMessage(getResources().getString(R.string.delete_event_message));
        alertDialogBuilder.setPositiveButton(getResources().getString(R.string.yes),
                (arg0, arg1) -> {
                    if (method.isNetworkAvailable()) {
                        deleteEvent(eventId, position);
                    } else {
                        method.alertBox(getResources().getString(R.string.internet_connection));
                    }
                });
        alertDialogBuilder.setNegativeButton(getResources().getString(R.string.no),
                (dialogInterface, i) -> {

                });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();


    }

    public void deleteEvent(String id, int position) {

        progressDialog.show();
        progressDialog.setMessage(getResources().getString(R.string.loading));
        progressDialog.setCancelable(false);

        JsonObject jsObj = (JsonObject) new Gson().toJsonTree(new API(EventDetail.this));
        jsObj.addProperty("event_id", id);
        jsObj.addProperty("method_name", "delete_event");
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<DataRP> call = apiService.deleteEvent(API.toBase64(jsObj.toString()));
        call.enqueue(new Callback<DataRP>() {
            @Override
            public void onResponse(@NotNull Call<DataRP> call, @NotNull Response<DataRP> response) {

                try {

                    DataRP dataRP = response.body();

                    assert dataRP != null;
                    if (dataRP.getStatus().equals("1")) {
                        if (dataRP.getSuccess().equals("1")) {
                            Toast.makeText(EventDetail.this, dataRP.getMsg(), Toast.LENGTH_SHORT).show();
                            Events.EventDelete eventDelete = new Events.EventDelete("delete", position);
                            GlobalBus.getBus().post(eventDelete);
                            onBackPressed();
                        }
                    } else {
                        method.alertBox(dataRP.getMessage());
                    }

                } catch (Exception e) {
                    Log.d("exception_error", e.toString());
                    method.alertBox(getResources().getString(R.string.failed_try_again));
                }

                progressDialog.dismiss();

            }

            @Override
            public void onFailure(@NotNull Call<DataRP> call, @NotNull Throwable t) {
                // Log error here since request failed
                Log.e("onFailure_data", t.toString());
                progressDialog.dismiss();
                method.alertBox(getResources().getString(R.string.failed_try_again));
            }
        });

    }

    public void viewTicket(String ticketId) {

        progressDialog.show();
        progressDialog.setMessage(getResources().getString(R.string.loading));
        progressDialog.setCancelable(false);

        JsonObject jsObj = (JsonObject) new Gson().toJsonTree(new API(EventDetail.this));
        jsObj.addProperty("booking_id", ticketId);
        jsObj.addProperty("is_dark", method.isDarkMode());
        jsObj.addProperty("method_name", "view_ticket");
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<TicketViewRP> call = apiService.viewTicket(API.toBase64(jsObj.toString()));
        call.enqueue(new Callback<TicketViewRP>() {
            @Override
            public void onResponse(@NotNull Call<TicketViewRP> call, @NotNull Response<TicketViewRP> response) {

                try {

                    TicketViewRP ticketViewRP = response.body();

                    assert ticketViewRP != null;
                    if (ticketViewRP.getStatus().equals("1")) {
                        if (ticketViewRP.getSuccess().equals("1")) {
                            startActivity(new Intent(EventDetail.this, ViewTicket.class)
                                    .putExtra("url", ticketViewRP.getUrl()));
                        }
                    } else {
                        method.alertBox(ticketViewRP.getMessage());
                    }

                } catch (Exception e) {
                    Log.d("exception_error", e.toString());
                    method.alertBox(getResources().getString(R.string.failed_try_again));
                }

                progressDialog.dismiss();

            }

            @Override
            public void onFailure(@NotNull Call<TicketViewRP> call, @NotNull Throwable t) {
                // Log error here since request failed
                Log.e("onFailure_data", t.toString());
                progressDialog.dismiss();
                method.alertBox(getResources().getString(R.string.failed_try_again));
            }
        });

    }


    public void ticketPdf(String ticketId) {

        progressDialog.show();
        progressDialog.setMessage(getResources().getString(R.string.loading));
        progressDialog.setCancelable(false);

        JsonObject jsObj = (JsonObject) new Gson().toJsonTree(new API(EventDetail.this));
        jsObj.addProperty("booking_id", ticketId);
        jsObj.addProperty("method_name", "download_ticket");
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<TicketDownloadRP> call = apiService.downloadTicket(API.toBase64(jsObj.toString()));
        call.enqueue(new Callback<TicketDownloadRP>() {
            @Override
            public void onResponse(@NotNull Call<TicketDownloadRP> call, @NotNull Response<TicketDownloadRP> response) {

                try {

                    TicketDownloadRP ticketDownloadRP = response.body();

                    assert ticketDownloadRP != null;
                    if (ticketDownloadRP.getStatus().equals("1")) {
                        if (ticketDownloadRP.getSuccess().equals("1")) {

                            new CreatePdf(EventDetail.this)
                                    .setPdfName(ticketDownloadRP.getFile_name())
                                    .openPrintDialog(false)
                                    .setContentBaseUrl("file:///android_asset/image/")
                                    .setPageSize(PrintAttributes.MediaSize.ISO_A4)
                                    .setContent(ticketDownloadRP.getString_data())
                                    .setFilePath(Constant.appStorage + "/")
                                    .setCallbackListener(new CreatePdf.PdfCallbackListener() {
                                        @Override
                                        public void onFailure(@NotNull String s) {
                                            // handle error
                                            method.alertBox(getResources().getString(R.string.failed_try_again));
                                        }

                                        @Override
                                        public void onSuccess(@NotNull String s) {
                                            // do your stuff here
                                            showMedia(Constant.appStorage, ticketDownloadRP.getFile_name() + ".pdf");
                                            method.alertBox(getResources().getString(R.string.ticket_download));
                                        }
                                    })
                                    .create();
                        }
                    } else {
                        method.alertBox(ticketDownloadRP.getMessage());
                    }

                } catch (Exception e) {
                    Log.d("exception_error", e.toString());
                    method.alertBox(getResources().getString(R.string.failed_try_again));
                }

                progressDialog.dismiss();

            }

            @Override
            public void onFailure(@NotNull Call<TicketDownloadRP> call, @NotNull Throwable t) {
                // Log error here since request failed
                Log.e("onFailure_data", t.toString());
                progressDialog.dismiss();
                method.alertBox(getResources().getString(R.string.failed_try_again));
            }
        });

    }


    public void userList(String eventId) {

        progressDialog.show();
        progressDialog.setMessage(getResources().getString(R.string.loading));
        progressDialog.setCancelable(false);

        JsonObject jsObj = (JsonObject) new Gson().toJsonTree(new API(EventDetail.this));
        jsObj.addProperty("event_id", eventId);
        jsObj.addProperty("method_name", "event_user_list");
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<UserTicketListRP> call = apiService.getUserTicketList(API.toBase64(jsObj.toString()));
        call.enqueue(new Callback<UserTicketListRP>() {
            @Override
            public void onResponse(@NotNull Call<UserTicketListRP> call, @NotNull Response<UserTicketListRP> response) {

                try {

                    UserTicketListRP userTicketListRP = response.body();

                    assert userTicketListRP != null;
                    if (userTicketListRP.getStatus().equals("1")) {
                        if (userTicketListRP.getSuccess().equals("1")) {
                            downloadUserList(userTicketListRP.getUrl(), userTicketListRP.getFile_name());
                        }
                    } else {
                        method.alertBox(userTicketListRP.getMessage());
                    }

                } catch (Exception e) {
                    Log.d("exception_error", e.toString());
                    method.alertBox(getResources().getString(R.string.failed_try_again));
                }

                progressDialog.dismiss();

            }

            @Override
            public void onFailure(@NotNull Call<UserTicketListRP> call, @NotNull Throwable t) {
                // Log error here since request failed
                Log.e("onFailure_data", t.toString());
                progressDialog.dismiss();
                method.alertBox(getResources().getString(R.string.failed_try_again));
            }
        });

    }

    private void downloadUserList(String downloadUrl, String fileName) {

        final String CANCEL_TAG = "c_tag";

        progressDialog.setMessage(getResources().getString(R.string.downloading));
        progressDialog.setCancelable(false);
        progressDialog.show();

        OkHttpClient client = new OkHttpClient();
        Request.Builder builder = new Request.Builder()
                .url(downloadUrl)
                .addHeader("Accept-Encoding", "identity")
                .get()
                .tag(CANCEL_TAG);

        okhttp3.Call call = client.newCall(builder.build());

        call.enqueue(new okhttp3.Callback() {
            @Override
            public void onFailure(@NonNull okhttp3.Call call, @NonNull IOException e) {
                Log.e("TAG", "=============onFailure===============");
                e.printStackTrace();
                Log.d("error_downloading", e.toString());
                progressDialog.dismiss();
            }

            @Override
            public void onResponse(@NonNull okhttp3.Call call, @NonNull okhttp3.Response response) throws IOException {
                Log.e("TAG", "=============onResponse===============");
                Log.e("TAG", "request headers:" + response.request().headers());
                Log.e("TAG", "response headers:" + response.headers());
                assert response.body() != null;
                ResponseBody responseBody = ProgressHelper.withProgress(response.body(), new ProgressUIListener() {

                    //if you don't need this method, don't override this methd. It isn't an abstract method, just an empty method.
                    @Override
                    public void onUIProgressStart(long totalBytes) {
                        super.onUIProgressStart(totalBytes);
                        Log.e("TAG", "onUIProgressStart:" + totalBytes);
                        method.alertBox(getResources().getString(R.string.userList_download));
                    }

                    @Override
                    public void onUIProgressChanged(long numBytes, long totalBytes, float percent, float speed) {
                        Log.e("TAG", "=============start===============");
                        Log.e("TAG", "numBytes:" + numBytes);
                        Log.e("TAG", "totalBytes:" + totalBytes);
                        Log.e("TAG", "percent:" + percent);
                        Log.e("TAG", "speed:" + speed);
                        Log.e("TAG", "============= end ===============");
                    }

                    //if you don't need this method, don't override this methd. It isn't an abstract method, just an empty method.
                    @Override
                    public void onUIProgressFinish() {
                        super.onUIProgressFinish();
                        progressDialog.dismiss();
                        showMedia(Constant.appStorage, fileName);
                        Log.e("TAG", "onUIProgressFinish:");
                    }
                });


                try {

                    BufferedSource source = responseBody.source();
                    File outFile = new File(Constant.appStorage + "/" + fileName);
                    BufferedSink sink = Okio.buffer(Okio.sink(outFile));
                    source.readAll(sink);
                    sink.flush();
                    source.close();

                } catch (Exception e) {
                    progressDialog.dismiss();
                    Log.d("show_data", e.toString());
                }

            }
        });

    }

    public void showMedia(String filePath, String fileName) {
        try {
            MediaScannerConnection.scanFile(getApplicationContext(), new String[]{filePath + "/" + fileName},
                    null,
                    (path, uri) -> {

                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Unregister the registered event.
        GlobalBus.getBus().unregister(this);
    }


    public void profile(String userId) {

        progressBar.setVisibility(View.VISIBLE);

        JsonObject jsObj = (JsonObject) new Gson().toJsonTree(new API(EventDetail.this));
        jsObj.addProperty("id", userId);
        jsObj.addProperty("method_name", "user_profile");
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<ProfileRP> call = apiService.getProfile(API.toBase64(jsObj.toString()));
        call.enqueue(new Callback<ProfileRP>() {
            @SuppressLint("UseCompatLoadingForDrawables")
            @Override
            public void onResponse(@NotNull Call<ProfileRP> call, @NotNull Response<ProfileRP> response) {

                try {
                    ProfileRP profileRP = response.body();
                    assert profileRP != null;

                    if (profileRP.getStatus().equals("1")) {

                        method.editor.putString(method.userImage, profileRP.getUser_image());
                        method.editor.commit();
                        user_name = profileRP.getName();
                        user_id_ = profileRP.getUser_id();
                        profile_img_url_ = profileRP.getUser_image();


                  //      Toast.makeText(EventDetail.this, "inside profile ", Toast.LENGTH_SHORT).show();


                    } else if (profileRP.getStatus().equals("2")) {
                        method.suspend(profileRP.getMessage());
                    } else {

                        method.alertBox(profileRP.getMessage());
                    }

                } catch (Exception e) {
                    Log.d("exception_error", e.toString());
                    method.alertBox(getResources().getString(R.string.failed_try_again));
                }



            }

            @Override
            public void onFailure(@NotNull Call<ProfileRP> call, @NotNull Throwable t) {
                // Log error here since request failed
                Log.e("fail", t.toString());
                method.alertBox(getResources().getString(R.string.failed_try_again));
            }
        });
    }
}
