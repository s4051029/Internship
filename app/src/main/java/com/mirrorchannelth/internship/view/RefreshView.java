package com.mirrorchannelth.internship.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.aspsine.irecyclerview.RefreshTrigger;
import com.mirrorchannelth.internship.R;

import java.util.Calendar;
import java.util.Date;

public class RefreshView extends LinearLayout implements RefreshTrigger {

    private Context context;
    private LayoutInflater inflater;
    private LinearLayout view ;
    public RefreshView(Context context) {
        super(context);
        this.context = context;
        initLayout();
    }

    public RefreshView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        initLayout();
    }

    public RefreshView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        initLayout();
    }
    @TargetApi(21)
    public RefreshView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.context = context;
        initLayout();
    }

    private void initLayout() {
        inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = (LinearLayout) inflater.inflate(R.layout.layout_refresh, this);

    }


    @Override
    public void onStart(boolean b, int i, int i1) {

    }

    @Override
    public void onMove(boolean b, boolean b1, int i) {

    }

    @Override
    public void onRefresh() {

    }

    @Override
    public void onRelease() {

    }

    @Override
    public void onComplete() {
    }

    @Override
    public void onReset() {

    }
}
