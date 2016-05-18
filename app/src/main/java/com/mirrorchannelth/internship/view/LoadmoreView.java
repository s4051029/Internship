package com.mirrorchannelth.internship.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.aspsine.irecyclerview.RefreshTrigger;
import com.mirrorchannelth.internship.R;

public class LoadmoreView extends LinearLayout implements RefreshTrigger {

    private Context context;
    private LayoutInflater inflater;
    private LinearLayout view ;
    private ProgressBar progressBar;
    public LoadmoreView(Context context) {
        super(context);
        this.context = context;
        initLayout();
    }

    public LoadmoreView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        initLayout();
    }

    public LoadmoreView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        initLayout();
    }
    @TargetApi(21)
    public LoadmoreView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.context = context;
        initLayout();
    }

    private void initLayout() {
        inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = (LinearLayout) inflater.inflate(R.layout.layout_loadmore, this);
        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
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
