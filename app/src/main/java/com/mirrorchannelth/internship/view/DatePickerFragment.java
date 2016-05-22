package com.mirrorchannelth.internship.view;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;

import java.util.Calendar;

/**
 * Created by boss on 5/22/16.
 */
public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener{


    private Context context;
    private TextView view;

    public static DatePickerFragment getInstance(Context context, TextView view){
        DatePickerFragment datePickerFragment = new DatePickerFragment();
        datePickerFragment.context = context;
        datePickerFragment.view = view;
        return datePickerFragment;
    }



    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        String date = String.format("%s/%s/%s", dayOfMonth, monthOfYear, year);
        this.view.setText(date);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        return new DatePickerDialog(getActivity(), this, year, month, day);

    }


}
