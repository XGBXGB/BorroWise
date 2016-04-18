package com.example.shayanetan.borrowise2.Fragments;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.format.DateFormat;
import android.text.format.Time;
import android.util.Log;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
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

    private TextView tv_alarm;

    public TimePickerFragment(){}



    public void setTv_alarm(TextView tv_alarm) {
        this.tv_alarm = tv_alarm;
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

            String hourString = "";
            String result ="";

            if(hourOfDay == 0)
                hourString = "24";
            else
                hourString = String.valueOf(hourOfDay);

            if(minute>=0 && minute <=9)
                result = hourString +":0"+minute;
            else
                result = hourString+":"+minute;

            tv_alarm.setText(result);

    }

}
