package com.example.shayanetan.borrowise.Fragments;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.shayanetan.borrowise.Adapters.ContactsCursorAdapter;
import com.example.shayanetan.borrowise.Models.CustomDate;
import com.example.shayanetan.borrowise.Models.DatabaseOpenHelper;
import com.example.shayanetan.borrowise.Models.ItemTransaction;
import com.example.shayanetan.borrowise.Models.User;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by ShayaneTan on 3/12/2016.
 */
public abstract class AddTransactionAbstractFragment extends Fragment{


    protected int selected_contactID;
    protected String selected_name;
    protected String selected_contact_number;
    protected DatabaseOpenHelper databaseOpenHelper;
    protected ContactsCursorAdapter contactsCursorAdapter;

    protected FragmentActivity myContext;
    protected AutoCompleteTextView atv_person_name;
    protected Button btn_borrowed, btn_lent;
    protected Button btn_end_date, btn_start_date;
    protected ImageButton img_btn_switch;

    protected DatePickerDialog.OnDateSetListener mOnDateSetListener;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onAttach(Activity activity) {
        myContext=(FragmentActivity) activity;
        super.onAttach(activity);
    }

    @Override
    public  abstract View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState);


    protected abstract void onFragmentSwitch();

    public void init() {

        Cursor contacts = null;
        contactsCursorAdapter = new ContactsCursorAdapter(myContext, contacts, 0);
        databaseOpenHelper = new DatabaseOpenHelper(myContext);
        atv_person_name.setAdapter(contactsCursorAdapter);
        atv_person_name.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // TODO Auto-generated method stub
                Cursor cursor = (Cursor) parent.getItemAtPosition(position);
                selected_contactID = position;
                selected_name = cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                selected_contact_number = cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.NUMBER));
                atv_person_name.setText(selected_name + " <" + selected_contact_number + ">");
            }
        });

        CustomDate d = new CustomDate();
        btn_start_date.setText(d.formatDateCommas(d.getCurrentDate()));
        btn_end_date.setText(d.formatDateCommas(d.getCurrentDate()));

        btn_start_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerFragment datePickerFragment = new DatePickerFragment();
                datePickerFragment.setButtonDateSelector(btn_start_date);
                datePickerFragment.show(getFragmentManager(), "DatePicker");
            }
        });

        btn_end_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerFragment datePickerFragment = new DatePickerFragment();
                datePickerFragment.setButtonDateSelector(btn_end_date);
                datePickerFragment.show(getFragmentManager(), "DatePicker");
            }
        });

        onFragmentSwitch();

    }

    public long parseDateToMillis(String toParse){
        long millis=0;
        try {
            SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy"); // I assume d-M, you may refer to M-d for month-day instead.
            Date date = formatter.parse(toParse); // You will need try/catch around this
            millis = date.getTime();

        } catch (ParseException e){
            e.printStackTrace();
        }
        return millis;
    }



}
