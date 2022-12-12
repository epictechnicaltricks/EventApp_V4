// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: braces fieldsfirst space lnc

package com.infyson.invitationcard.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.infyson.invitationcard.R;


public class AdapterFont extends BaseAdapter {

    Activity context;
    private LayoutInflater inflater;
    private String items[];

    public AdapterFont(Activity activity, String as[]) {
        context = activity;
        items = as;
        inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public int getCount() {
        return items.length;
    }

    public Object getItem(int i) {
        return null;
    }

    public long getItemId(int i) {
        return 0L;
    }

    public View getView(int i, View view, ViewGroup viewgroup) {
        Object obj = view;
        view = ((View) (obj));
        if (obj == null) {
            view = inflater.inflate(R.layout.row_font_platte, null);
        }
        obj = (TextView) view.findViewById(R.id.itemfont);
        TextView tvFont = (TextView) view.findViewById(R.id.itemfont);
//        try
//        {
        ((TextView) (obj)).setTypeface(Typeface.createFromAsset(context.getAssets(), (new StringBuilder("fonts/")).append(items[i]).toString()));
        ((TextView) (obj)).setTag(items[i]);
        i = viewgroup.getHeight();
//            viewgroup = new RelativeLayout.LayoutParams(viewgroup.getWidth() / 5, i);

//            ((TextView) (obj)).setLayoutParams(viewgroup);
//            imageview.setLayoutParams(viewgroup);
//        }
        // Misplaced declaration of an exception variable
//        catch (ViewGroup viewGroup)
//        {
//            return view;
//        }
        DisplayMetrics displaymetrics = new DisplayMetrics();
        context.getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        final ViewGroup.LayoutParams params = tvFont.getLayoutParams();

        if (params != null) {
            params.width = (displaymetrics.widthPixels) / 5;
        }

        return view;
    }
}
