package com.example.eventapp.fragment;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.eventapp.R;
import com.example.eventapp.activity.MainActivity;
import com.example.eventapp.response.DataRP;
import com.example.eventapp.response.ProfileRP;
import com.example.eventapp.rest.ApiClient;
import com.example.eventapp.rest.ApiInterface;
import com.example.eventapp.util.API;
import com.example.eventapp.util.Events;
import com.example.eventapp.util.GlobalBus;
import com.example.eventapp.util.Method;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.android.material.textview.MaterialTextView;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.greenrobot.eventbus.Subscribe;
import org.jetbrains.annotations.NotNull;

import java.io.File;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditProfileFragment extends Fragment {

    private Method method;
    public MaterialToolbar toolbar;
    private ProgressBar progressBar;
    private ProgressDialog progressDialog;
    private String imageProfile;
    private InputMethodManager imm;
    private CircleImageView imageViewUser;
    private ConstraintLayout conMain, conNoData;
    private MaterialTextView textViewEmail;
    private ImageView imageViewEdit;
    private MaterialButton buttonSubmit;
    private boolean isProfile = false, isRemove = false;
    private TextInputEditText editTextName, editTextEmail, editTextPhoneNo, editTextCity, editTextAddress;

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = LayoutInflater.from(getContext()).inflate(R.layout.edit_profile_fragment, container, false);

        if (MainActivity.toolbar != null) {
            MainActivity.toolbar.setTitle(getResources().getString(R.string.edit_profile));
        }

        GlobalBus.getBus().register(this);

        method = new Method(getActivity());
        progressDialog = new ProgressDialog(getActivity());

        imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        conMain = view.findViewById(R.id.con_editPro);
        conNoData = view.findViewById(R.id.con_noDataFound);
        progressBar = view.findViewById(R.id.progressbar_editPro);
        imageViewUser = view.findViewById(R.id.imageView_user_editPro);
        imageViewEdit = view.findViewById(R.id.imageView_editPro);
        textViewEmail = view.findViewById(R.id.textView_email_editPro);
        editTextName = view.findViewById(R.id.editText_name_editPro);
        editTextEmail = view.findViewById(R.id.editText_email_editPro);
        editTextPhoneNo = view.findViewById(R.id.editText_phone_editPro);
        editTextCity = view.findViewById(R.id.editText_city_editPro);
        editTextAddress = view.findViewById(R.id.editText_address_editPro);
        TextInputLayout textInputEmail = view.findViewById(R.id.textInput_email_editPro);
        buttonSubmit = view.findViewById(R.id.button_editPro);

        if (method.isDarkMode()) {
            imageViewEdit.setImageDrawable(getResources().getDrawable(R.drawable.ic_add_profile));
        } else {
            imageViewEdit.setImageDrawable(getResources().getDrawable(R.drawable.ic_add_profile_white));
        }

        if (method.getLoginType().equals("google") || method.getLoginType().equals("facebook")) {
            editTextName.setFocusable(false);
            editTextName.setCursorVisible(false);
            textInputEmail.setVisibility(View.GONE);
        } else {
            textInputEmail.setVisibility(View.VISIBLE);
        }

        progressBar.setVisibility(View.GONE);
        conNoData.setVisibility(View.GONE);
        conMain.setVisibility(View.GONE);

        if (method.isNetworkAvailable()) {
            profile(method.userId());
        } else {
            method.alertBox(getResources().getString(R.string.internet_connection));
        }

        return view;

    }

    @Subscribe
    public void getData(Events.ProImage proImage) {
        isProfile = proImage.isProfile();
        isRemove = proImage.isRemove();
        if (proImage.isProfile()) {
            imageProfile = proImage.getImagePath();
            Uri uri = Uri.fromFile(new File(imageProfile));
            Glide.with(getActivity().getApplicationContext()).load(uri)
                    .placeholder(R.drawable.user_profile)
                    .into(imageViewUser);
        }
        if (proImage.isRemove()) {
            Glide.with(getActivity().getApplicationContext()).load(R.drawable.user_profile)
                    .placeholder(R.drawable.user_profile)
                    .into(imageViewUser);
        }
    }

    private boolean isValidMail(String email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private void save() {

        String name = editTextName.getText().toString();
        String email = editTextEmail.getText().toString();
        String phoneNo = editTextPhoneNo.getText().toString();
        String city = editTextCity.getText().toString();
        String address = editTextAddress.getText().toString();

        editTextName.setError(null);
        editTextEmail.setError(null);
        editTextPhoneNo.setError(null);
        editTextCity.setError(null);
        editTextAddress.setError(null);

        if (name.equals("") || name.isEmpty()) {
            editTextName.requestFocus();
            editTextName.setError(getResources().getString(R.string.please_enter_name));
        } else if ((method.getLoginType().equals("normal")) && (!isValidMail(email) || email.isEmpty())) {
            editTextEmail.requestFocus();
            editTextEmail.setError(getResources().getString(R.string.please_enter_email));
        } else if (phoneNo.equals("") || phoneNo.isEmpty()) {
            editTextPhoneNo.requestFocus();
            editTextPhoneNo.setError(getResources().getString(R.string.please_enter_phone));
        } else if (city.equals("") || city.isEmpty()) {
            editTextCity.requestFocus();
            editTextCity.setError(getResources().getString(R.string.please_enter_city));
        } else if (address.equals("") || address.isEmpty()) {
            editTextAddress.requestFocus();
            editTextAddress.setError(getResources().getString(R.string.please_enter_address));
        } else {
            if (method.isNetworkAvailable()) {

                editTextName.clearFocus();
                editTextEmail.clearFocus();
                editTextPhoneNo.clearFocus();
                editTextCity.clearFocus();
                editTextAddress.clearFocus();
                imm.hideSoftInputFromWindow(editTextName.getWindowToken(), 0);
                imm.hideSoftInputFromWindow(editTextEmail.getWindowToken(), 0);
                imm.hideSoftInputFromWindow(editTextPhoneNo.getWindowToken(), 0);
                imm.hideSoftInputFromWindow(editTextCity.getWindowToken(), 0);
                imm.hideSoftInputFromWindow(editTextAddress.getWindowToken(), 0);

                profileUpdate(method.userId(), name, phoneNo, city, address, imageProfile);
            } else {
                method.alertBox(getResources().getString(R.string.internet_connection));
            }
        }

    }

    private void profile(String userId) {

        if (getActivity() != null) {

            progressBar.setVisibility(View.VISIBLE);

            JsonObject jsObj = (JsonObject) new Gson().toJsonTree(new API(getActivity()));
            jsObj.addProperty("id", userId);
            jsObj.addProperty("method_name", "user_profile");
            ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
            Call<ProfileRP> call = apiService.getProfile(API.toBase64(jsObj.toString()));
            call.enqueue(new Callback<ProfileRP>() {
                @SuppressLint("SetTextI18n")
                @Override
                public void onResponse(@NotNull Call<ProfileRP> call, @NotNull Response<ProfileRP> response) {

                    if (getActivity() != null) {

                        try {

                            ProfileRP profileRP = response.body();

                            assert profileRP != null;
                            if (profileRP.getStatus().equals("1")) {

                                if (profileRP.getSuccess().equals("1")) {

                                    imageProfile = profileRP.getUser_image();

                                    textViewEmail.setText(profileRP.getEmail());

                                    Glide.with(getActivity().getApplicationContext()).load(imageProfile)
                                            .placeholder(R.drawable.user_profile)
                                            .into(imageViewUser);

                                    editTextName.setText(profileRP.getName());
                                    editTextEmail.setText(profileRP.getEmail());
                                    editTextPhoneNo.setText(profileRP.getPhone());
                                    editTextCity.setText(profileRP.getCity());
                                    editTextAddress.setText(profileRP.getAddress());

                                    conMain.setVisibility(View.VISIBLE);

                                    imageViewEdit.setOnClickListener(v -> {
                                        BottomSheetDialogFragment fragment = new ProImage();
                                        fragment.show(getActivity().getSupportFragmentManager(), "Bottom Sheet Dialog Fragment");
                                    });

                                    imageViewUser.setOnClickListener(V -> {
                                        BottomSheetDialogFragment fragment = new ProImage();
                                        fragment.show(getActivity().getSupportFragmentManager(), "Bottom Sheet Dialog Fragment");
                                    });

                                    buttonSubmit.setOnClickListener(view -> save());

                                } else {
                                    conNoData.setVisibility(View.VISIBLE);
                                    method.alertBox(profileRP.getMsg());
                                }

                            } else if (profileRP.getStatus().equals("2")) {
                                method.suspend(profileRP.getMessage());
                            } else {
                                conNoData.setVisibility(View.VISIBLE);
                                method.alertBox(profileRP.getMessage());
                            }

                        } catch (Exception e) {
                            Log.d("exception_error", e.toString());
                            method.alertBox(getResources().getString(R.string.failed_try_again));
                        }

                    }

                    progressBar.setVisibility(View.GONE);

                }

                @Override
                public void onFailure(@NotNull Call<ProfileRP> call, @NotNull Throwable t) {
                    // Log error here since request failed
                    Log.e("onFailure_data", t.toString());
                    conNoData.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.GONE);
                    method.alertBox(getResources().getString(R.string.failed_try_again));
                }
            });

        }
    }

    private void profileUpdate(String id, String sendName, String sendPhone, String sendCity, String sendAddress, String profile_image) {

        if (getActivity() != null) {

            progressDialog.show();
            progressDialog.setMessage(getResources().getString(R.string.loading));
            progressDialog.setCancelable(false);

            MultipartBody.Part body = null;

            JsonObject jsObj = (JsonObject) new Gson().toJsonTree(new API(getActivity()));
            jsObj.addProperty("user_id", id);
            jsObj.addProperty("name", sendName);
            jsObj.addProperty("phone", sendPhone);
            jsObj.addProperty("city", sendCity);
            jsObj.addProperty("address", sendAddress);
            jsObj.addProperty("is_remove", isRemove);
            jsObj.addProperty("method_name", "user_profile_update");
            if (isProfile) {
                RequestBody requestFile =
                        RequestBody.create(MediaType.parse("multipart/form-data"), new File(profile_image));
                // MultipartBody.Part is used to send also the actual file name
                body = MultipartBody.Part.createFormData("user_image", new File(profile_image).getName(), requestFile);
            }
            // add another part within the multipart request
            RequestBody requestBody_data = RequestBody.create(MediaType.parse("multipart/form-data"), API.toBase64(jsObj.toString()));
            ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
            Call<DataRP> call = apiService.editProfile(requestBody_data, body);
            call.enqueue(new Callback<DataRP>() {
                @Override
                public void onResponse(@NotNull Call<DataRP> call, @NotNull Response<DataRP> response) {

                    if (getActivity() != null) {
                        try {
                            DataRP dataRP = response.body();
                            assert dataRP != null;

                            if (dataRP.getStatus().equals("1")) {
                                if (dataRP.getSuccess().equals("1")) {
                                    Events.ProfileUpdate profileUpdate = new Events.ProfileUpdate("");
                                    GlobalBus.getBus().post(profileUpdate);
                                    getActivity().getSupportFragmentManager().popBackStack();
                                } else {
                                    method.alertBox(dataRP.getMsg());
                                }
                            } else if (dataRP.getStatus().equals("2")) {
                                method.suspend(dataRP.getMessage());
                            } else {
                                method.alertBox(dataRP.getMessage());
                            }

                        } catch (Exception e) {
                            Log.d("exception_error", e.toString());
                            method.alertBox(getResources().getString(R.string.failed_try_again));
                        }
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

    }

}
