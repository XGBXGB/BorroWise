package com.example.shayanetan.borrowise2.Fragments;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.app.Service;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.SystemClock;
import android.provider.ContactsContract;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.shayanetan.borrowise2.Activities.AlarmReceiver;
import com.example.shayanetan.borrowise2.Adapters.ContactsCursorAdapter;
import com.example.shayanetan.borrowise2.Models.CustomDate;
import com.example.shayanetan.borrowise2.Models.Transaction;
import com.example.shayanetan.borrowise2.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by ShayaneTan on 3/12/2016.
 */
public abstract class AddAbstractFragment extends Fragment {

    protected int selected_contactID;
    protected String selected_name;
    protected String selected_contact_number;
    protected ContactsCursorAdapter contactsCursorAdapter;
    protected ImageView btn_addContact;

    protected FragmentActivity myContext;
    protected AutoCompleteTextView atv_person_name;
    protected View layout_startDate, layout_endDate;
    protected TextView tv_startDate, tv_endDate;
    protected Button btn_borrowed, btn_lent;
    protected FloatingActionButton img_btn_switch;

    protected OnFragmentInteractionListener mListener;

    @Override
    public void onCreate(Bundle savedInstanceState) { super.onCreate(savedInstanceState);}

    @Override
    public  abstract View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState);

    protected abstract void onFragmentSwitch();
    protected abstract void clearAllFields();
    protected abstract void printAddAcknowledgement(String entry_name, String type);

    public interface OnFragmentInteractionListener{
        //TODO: Update argument type and name
        public int onAddNewUser(String name, String contact_info);
        public void onAddTransactions(Transaction t);
    }

    public void setOnFragmentInteractionListener(OnFragmentInteractionListener mListener){this.mListener = mListener; }

    public void init() {

        selected_name = "";
        selected_contact_number = "";
        myContext = (FragmentActivity) getActivity();
        Cursor contacts = null;
        contactsCursorAdapter = new ContactsCursorAdapter(myContext, contacts, 0);

        atv_person_name.setThreshold(1);
        atv_person_name.setAdapter(contactsCursorAdapter);
        atv_person_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] projection = new String[] {
                        ContactsContract.CommonDataKinds.Phone._ID,
                        ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
                        ContactsContract.CommonDataKinds.Phone.TYPE,
                        ContactsContract.CommonDataKinds.Phone.NUMBER};

                Cursor contacts = getActivity().getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, projection,null, null,
                        ContactsContract.Contacts.DISPLAY_NAME + " COLLATE LOCALIZED ASC");
                contactsCursorAdapter = new ContactsCursorAdapter(myContext, contacts, 0);
                atv_person_name.setAdapter(contactsCursorAdapter);
                atv_person_name.showDropDown();
            }
        });
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

        setDateToCurrent();

        layout_startDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerFragment datePickerFragment = new DatePickerFragment();
                datePickerFragment.setTv_date(tv_startDate);
                datePickerFragment.show(getFragmentManager(), "DatePicker");
            }
        });

        layout_endDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerFragment datePickerFragment = new DatePickerFragment();
                datePickerFragment.setTv_date(tv_endDate);
                datePickerFragment.show(getFragmentManager(), "DatePicker");
            }
        });

        onFragmentSwitch();
    }

    public long parseDateToMillis(String toParse){
        long millis=0;
        try {
            SimpleDateFormat formatter = new SimpleDateFormat("MMM dd, yyyy"); // I assume d-M, you may refer to M-d for month-day instead.
            Date date = formatter.parse(toParse); // You will need try/catch around this
            millis = date.getTime();

        } catch (ParseException e){
            e.printStackTrace();
        }
        return millis;
    }

    public void printRejectDialog(){
        LayoutInflater factory = LayoutInflater.from(getActivity());
        final View view = factory.inflate(R.layout.add_transaction_reject, null);
        TextView tv_reject = (TextView) view.findViewById(R.id.tv_reject);
        tv_reject.setText("Please fill up all the necessary information!");

        AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
        alertDialog.setView(view);
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialog.show();
    }
    public void setDateToCurrent(){
        CustomDate d = new CustomDate();
        String currentDate = (d.getMonth()+1)+ "/" + d.getDay()+ "/ "+ d.getYear();
        tv_startDate.setText(d.formatDateCommas(currentDate));
        tv_endDate.setText(d.formatDateCommas(currentDate));
    }
}
