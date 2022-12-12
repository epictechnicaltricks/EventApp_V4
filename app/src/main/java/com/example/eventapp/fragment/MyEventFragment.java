package com.example.eventapp.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.eventapp.R;
import com.example.eventapp.activity.EventDetail;
import com.example.eventapp.activity.Login;
import com.example.eventapp.activity.MainActivity;
import com.example.eventapp.adapter.MyEventAdapter;
import com.example.eventapp.interFace.OnClick;
import com.example.eventapp.item.EventList;
import com.example.eventapp.response.EventRP;
import com.example.eventapp.rest.ApiClient;
import com.example.eventapp.rest.ApiInterface;
import com.example.eventapp.util.API;
import com.example.eventapp.util.EndlessRecyclerViewScrollListener;
import com.example.eventapp.util.Events;
import com.example.eventapp.util.GlobalBus;
import com.example.eventapp.util.Method;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textview.MaterialTextView;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.greenrobot.eventbus.Subscribe;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyEventFragment extends Fragment {

    private Method method;
    private OnClick onClick;
    private ProgressBar progressBar;
    private ConstraintLayout conNoData;
    private MaterialButton buttonLogin;
    private ImageView imageViewData;
    private MaterialTextView textViewData;
    private RecyclerView recyclerView;
    private MyEventAdapter myEventAdapter;
    private List<EventList> eventLists;
    private Boolean isOver = false;
    private int paginationIndex = 1;

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = LayoutInflater.from(getContext()).inflate(R.layout.my_event_fragment, container, false);

        if (MainActivity.toolbar != null) {
            MainActivity.toolbar.setTitle(getResources().getString(R.string.my_event));
        }

        GlobalBus.getBus().register(this);

        eventLists = new ArrayList<>();

        onClick = (position, type, title, id) -> startActivity(new Intent(getActivity(), EventDetail.class)
                .putExtra("id", id)
                .putExtra("type", type)
                .putExtra("position", position));
        method = new Method(getActivity(), onClick);
        method.forceRTLIfSupported();

        progressBar = view.findViewById(R.id.progressbar_myEvent_fragment);
        conNoData = view.findViewById(R.id.con_not_login);
        imageViewData = view.findViewById(R.id.imageView_not_login);
        buttonLogin = view.findViewById(R.id.button_not_login);
        textViewData = view.findViewById(R.id.textView_not_login);
        recyclerView = view.findViewById(R.id.recyclerView_myEvent_fragment);

        data(false, false);
        progressBar.setVisibility(View.GONE);

        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        recyclerView.addOnScrollListener(new EndlessRecyclerViewScrollListener(layoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                if (!isOver) {
                    new Handler().postDelayed(() -> {
                        paginationIndex++;
                        callData();
                    }, 1000);
                } else {
                    if (myEventAdapter != null) {
                        myEventAdapter.hideHeader();
                    }
                }
            }
        });

        buttonLogin.setOnClickListener(v -> {
            startActivity(new Intent(getActivity(), Login.class));
            getActivity().finishAffinity();
        });

        callData();

        return view;

    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private void data(boolean isShow, boolean isLogin) {
        if (isShow) {
            if (isLogin) {
                buttonLogin.setVisibility(View.VISIBLE);
                textViewData.setText(getResources().getString(R.string.you_have_not_login));
                imageViewData.setImageDrawable(getResources().getDrawable(R.drawable.no_login));
            } else {
                buttonLogin.setVisibility(View.GONE);
                textViewData.setText(getResources().getString(R.string.no_data_found));
                imageViewData.setImageDrawable(getResources().getDrawable(R.drawable.no_data));
            }
            conNoData.setVisibility(View.VISIBLE);
        } else {
            conNoData.setVisibility(View.GONE);
        }
    }

    private void callData() {
        if (method.isNetworkAvailable()) {
            if (method.isLogin()) {
                myEvent(method.userId());
            } else {
                data(true, true);
            }
        } else {
            method.alertBox(getResources().getString(R.string.internet_connection));
        }
    }

    @Subscribe
    public void getNotify(Events.EventDelete eventDelete) {
        if (myEventAdapter != null) {
            int position = eventDelete.getPosition();
            eventLists.remove(position);
            myEventAdapter.notifyDataSetChanged();
        }
    }

    @Subscribe
    public void getNotify(Events.EventUpdate eventUpdate) {
        if (myEventAdapter != null) {
            int position = eventUpdate.getPosition();
            eventLists.get(position).setEvent_title(eventUpdate.getEvent_title());
            eventLists.get(position).setEvent_banner_thumb(eventUpdate.getEvent_banner_thumb());
            eventLists.get(position).setEvent_date(eventUpdate.getEvent_date());
            eventLists.get(position).setEvent_address(eventUpdate.getEvent_address());
            myEventAdapter.notifyItemChanged(position);
        }
    }

    private void myEvent(String userId) {

        if (getActivity() != null) {

            if (myEventAdapter == null) {
                eventLists.clear();
                progressBar.setVisibility(View.VISIBLE);
            }

            JsonObject jsObj = (JsonObject) new Gson().toJsonTree(new API(getActivity()));
            jsObj.addProperty("user_id", userId);
            jsObj.addProperty("page", paginationIndex);
            jsObj.addProperty("method_name", "get_user_events");
            ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
            Call<EventRP> call = apiService.getMyEvent(API.toBase64(jsObj.toString()));
            call.enqueue(new Callback<EventRP>() {
                @Override
                public void onResponse(@NotNull Call<EventRP> call, @NotNull Response<EventRP> response) {

                    if (getActivity() != null) {

                        try {
                            EventRP eventRP = response.body();
                            assert eventRP != null;

                            if (eventRP.getStatus().equals("1")) {

                                if (eventRP.getEventLists().size() == 0) {
                                    if (myEventAdapter != null) {
                                        myEventAdapter.hideHeader();
                                        isOver = true;
                                    }
                                } else {
                                    eventLists.addAll(eventRP.getEventLists());
                                }

                                if (myEventAdapter == null) {
                                    if (eventLists.size() != 0) {
                                        myEventAdapter = new MyEventAdapter(getActivity(), eventLists, "my_event", onClick);
                                        recyclerView.setAdapter(myEventAdapter);
                                    } else {
                                        data(true, false);
                                    }
                                } else {
                                    myEventAdapter.notifyDataSetChanged();
                                }

                            } else if (eventRP.getStatus().equals("2")) {
                                method.suspend(eventRP.getMessage());
                            } else {
                                data(true, false);
                                method.alertBox(eventRP.getMessage());
                            }

                        } catch (Exception e) {
                            Log.d("exception_error", e.toString());
                            method.alertBox(getResources().getString(R.string.failed_try_again));
                        }

                    }

                    progressBar.setVisibility(View.GONE);

                }

                @Override
                public void onFailure(@NotNull Call<EventRP> call, @NotNull Throwable t) {
                    // Log error here since request failed
                    Log.e("onFailure_data", t.toString());
                    progressBar.setVisibility(View.GONE);
                    method.alertBox(getResources().getString(R.string.failed_try_again));
                }
            });
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        // Unregister the registered event.
        GlobalBus.getBus().unregister(this);
    }

}
