package com.mirrorchannelth.internship.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mirrorchannelth.internship.R;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class DateView extends LinearLayout {

    private Context context;
    private LayoutInflater inflater;
    private TextView dateTextview;
    private TextView monthTextview;
    private TextView yearTextview;
    private View view ;
    private Date date;
    private String[] monthTh = {"ม.ค.", "ก.พ", "มี.ค.", "เม.ย.", "พ.ค.", "ก.ค.", "ส.ค.", "ก.ย. ",
            "ต.ค.", "พ.ย.", "ธ.ค."};

    public DateView(Context context) {
        super(context);
        this.context = context;
        initLayout();
    }

    public DateView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        initLayout();
    }

    public DateView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        initLayout();
    }
    @TargetApi(21)
    public DateView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.context = context;
        initLayout();
    }

    private void initLayout() {
        inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.layout_date, this);
        dateTextview = (TextView) view.findViewById(R.id.dateTextview);
        monthTextview = (TextView) view.findViewById(R.id.monthTextview);
        yearTextview = (TextView) view.findViewById(R.id.yearTextview);
    }

    public void setDate(String date){
        dateTextview.setText(date);
    }
    public void setDate(Date date){
        this.date = date;
        Calendar cal = Calendar.getInstance();
        cal.setTime(this.date);
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);
        setDate(String.valueOf(day));
        setMonth(monthTh[month]);
        setYear(String.valueOf(year+543));
    }

    public void setMonth(String month){
        monthTextview.setText(month);
    }

    public  void setYear(String year){
        yearTextview.setText(year);
    }


}
