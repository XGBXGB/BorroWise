package com.example.shayanetan.borrowise.Fragments;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.Button;
import android.widget.DatePicker;

import com.example.shayanetan.borrowise.Models.CustomDate;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by ShayaneTan on 3/12/2016.
 */
public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

    private Button btn_date_selector;

    public DatePickerFragment(){}

    public void setButtonDateSelector(Button btn_date_selector){
        this.btn_date_selector = btn_date_selector;
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
        btn_date_selector.setText(d.formatDateCommas(currentDate));
    }

}
