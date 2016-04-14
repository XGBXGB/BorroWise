package com.example.shayanetan.borrowise2.Fragments;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.format.DateFormat;
import android.text.format.Time;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TimePicker;

import com.example.shayanetan.borrowise2.Models.CustomDate;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by ShayaneTan on 3/12/2016.
 */
public class TimePickerFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener {

    private Button btn_time_selector;

    public TimePickerFragment(){}

    public void setButtonDateSelector(Button btn_time_selector){
        this.btn_time_selector = btn_time_selector;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current time as the default values for the picker
        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        // Create a new instance of TimePickerDialog and return it
        return new TimePickerDialog(getActivity(), this, hour, minute,
                DateFormat.is24HourFormat(getActivity()));
    }

    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        // Do something with the time chosen by the user
        SimpleDateFormat curFormatter = new SimpleDateFormat("HH:mm");
        Date dateObj = null;
        try {
            dateObj = curFormatter.parse(hourOfDay + ":" + minute);
        }catch (ParseException e){
            e.printStackTrace();
        }
        SimpleDateFormat postFormatter = new SimpleDateFormat("HH:mm");
        String result = postFormatter.format(dateObj);

        btn_time_selector.setText(hourOfDay+":"+minute);
    }

}
