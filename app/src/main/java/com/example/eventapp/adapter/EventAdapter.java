package com.example.eventapp.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.eventapp.R;
import com.example.eventapp.activity.Login;
import com.example.eventapp.interFace.FavouriteIF;
import com.example.eventapp.interFace.OnClick;
import com.example.eventapp.item.EventList;
import com.example.eventapp.util.Method;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.textview.MaterialTextView;

import java.util.List;

public class EventAdapter extends RecyclerView.Adapter {

    private Activity activity;
    private Method method;
    private String type;
    private int columnWidth;
    private List<EventList> eventLists;
    private final int VIEW_TYPE_LOADING = 0;
    private final int VIEW_TYPE_ITEM = 1;

    public EventAdapter(Activity activity, List<EventList> eventLists, String type, OnClick onClick) {
        this.activity = activity;
        this.type = type;
        this.eventLists = eventLists;
        method = new Method(activity, onClick);
        columnWidth = method.getScreenWidth();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if (viewType == VIEW_TYPE_ITEM) {
            View view = LayoutInflater.from(activity).inflate(R.layout.event_adapter, parent, false);
            return new ViewHolder(view);
        } else if (viewType == VIEW_TYPE_LOADING) {
            View v = LayoutInflater.from(activity).inflate(R.layout.layout_loading_item, parent, false);
            return new ProgressViewHolder(v);
        }
        return null;

    }

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {

        if (holder.getItemViewType() == VIEW_TYPE_ITEM) {

            final ViewHolder viewHolder = (ViewHolder) holder;

            if (eventLists.get(position).isIs_fav()) {
                viewHolder.imageViewFav.setImageDrawable(activity.getResources().getDrawable(R.drawable.ic_fav_hov));
            } else {
                viewHolder.imageViewFav.setImageDrawable(activity.getResources().getDrawable(R.drawable.ic_fav));
            }

            viewHolder.imageView.setLayoutParams(new ConstraintLayout.LayoutParams(columnWidth, columnWidth / 2));
            viewHolder.view.setLayoutParams(new ConstraintLayout.LayoutParams(columnWidth, columnWidth / 2));

            Glide.with(activity).load(eventLists.get(position).getEvent_banner_thumb())
                    .placeholder(R.drawable.placeholder_banner).into(viewHolder.imageView);

            viewHolder.textViewTitle.setText(eventLists.get(position).getEvent_title());
            String date = eventLists.get(position).getEvent_date();
            String[] separated = date.split(",");
            viewHolder.textViewDay.setText(separated[0]);
            viewHolder.textViewMonth.setText(separated[1]);
            viewHolder.textViewAdd.setText(eventLists.get(position).getEvent_address());

            viewHolder.materialCardView.setOnClickListener(v -> method.click(position, type, eventLists.get(position).getEvent_title(), eventLists.get(position).getId()));

            viewHolder.imageViewFav.setOnClickListener(view -> {
                if (method.isLogin()) {
                    FavouriteIF favouriteIF = (isFavourite, message) -> {
                        if (isFavourite) {
                            viewHolder.imageViewFav.setImageDrawable(activity.getResources().getDrawable(R.drawable.ic_fav_hov));
                        } else {
                            viewHolder.imageViewFav.setImageDrawable(activity.getResources().getDrawable(R.drawable.ic_fav));
                        }
                    };
                    method.addToFav(eventLists.get(position).getId(), method.userId(), type, position, favouriteIF);
                } else {
                    Method.loginBack = true;
                    activity.startActivity(new Intent(activity, Login.class));
                }
            });

        }

    }

    @Override
    public int getItemCount() {
        return eventLists.size() + 1;
    }

    public void hideHeader() {
        ProgressViewHolder.progressBar.setVisibility(View.GONE);
    }

    private boolean isHeader(int position) {
        return position == eventLists.size();
    }

    @Override
    public int getItemViewType(int position) {
        return isHeader(position) ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private View view;
        private MaterialCardView materialCardView;
        private ImageView imageView, imageViewFav;
        private MaterialTextView textViewTitle, textViewDay, textViewMonth, textViewAdd;

        public ViewHolder(View itemView) {
            super(itemView);

            view = itemView.findViewById(R.id.view_subCat_adapter);
            materialCardView = itemView.findViewById(R.id.cardView_event_adapter);
            imageView = itemView.findViewById(R.id.imageView_event_adapter);
            imageViewFav = itemView.findViewById(R.id.imageView_fav_event_adapter);
            textViewTitle = itemView.findViewById(R.id.textView_title_event_adapter);
            textViewDay = itemView.findViewById(R.id.textView_day_event_adapter);
            textViewMonth = itemView.findViewById(R.id.textView_month_event_adapter);
            textViewAdd = itemView.findViewById(R.id.textView_add_event_adapter);

        }
    }

    public static class ProgressViewHolder extends RecyclerView.ViewHolder {
        public static ProgressBar progressBar;

        public ProgressViewHolder(View v) {
            super(v);
            progressBar = v.findViewById(R.id.progressBar_loading);
        }
    }

}
