package com.example.shayanetan.borrowise2.Fragments;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

import com.example.shayanetan.borrowise2.Models.CustomDate;

import org.w3c.dom.Text;

/**
 * Created by ShayaneTan on 3/12/2016.
 */
public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {


    private TextView tv_date;

    public DatePickerFragment(){}

    public void setTv_date(TextView tv_date) {
        this.tv_date = tv_date;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        CustomDate d = new CustomDate();
        return new DatePickerDialog(getActivity(), this, d.getYear(), d.getMonth(), d.getDay());
    }

    public void onDateSet(DatePicker view, int yy, int mm, int dd) {
        populateSetDate(yy, mm+1, dd);
    }
    public void populateSetDate(int year, int month, int day) {
        String currentDate = month+"/"+day+"/"+year;
        CustomDate d = new CustomDate();
        tv_date.setText(d.formatDateCommas(currentDate));
    }

}
