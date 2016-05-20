package com.mirrorchannelth.internship.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mirrorchannelth.internship.R;

import java.util.Calendar;
import java.util.Date;

public class DefaultDisplayView extends RelativeLayout {

    private Context context;
    private LayoutInflater inflater;
    private View view ;
    private ImageView displayImageview;
    private TextView displayTextview;

    public DefaultDisplayView(Context context) {
        super(context);
        this.context = context;
        initLayout();
    }

    public DefaultDisplayView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        initLayout();
    }

    public DefaultDisplayView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        initLayout();
    }
    @TargetApi(21)
    public DefaultDisplayView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.context = context;
        initLayout();
    }

    private void initLayout() {
        inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.layout_default_display, this);
        displayImageview = (ImageView) findViewById(R.id.displayImageview);
        displayTextview = (TextView) findViewById(R.id.displayTextview);
        displayImageview.setAlpha(0.5f);
    }

    public void setImage(Drawable imageResource){

        displayImageview.setImageDrawable(imageResource);
    }

    public void setText(String text){

        displayTextview.setText(text);
    }

    public void setImageOnclicklistener(View.OnClickListener listener){
        displayImageview.setOnClickListener(listener);

    }


}
