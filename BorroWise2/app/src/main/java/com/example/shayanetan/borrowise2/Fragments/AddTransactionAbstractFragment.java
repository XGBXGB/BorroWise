package com.example.shayanetan.borrowise2.Fragments;

import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageButton;

import com.example.shayanetan.borrowise2.Adapters.ContactsCursorAdapter;
import com.example.shayanetan.borrowise2.Models.CustomDate;
import com.example.shayanetan.borrowise2.Models.Transaction;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by ShayaneTan on 3/12/2016.
 */
public abstract class AddTransactionAbstractFragment extends Fragment {

    protected int selected_contactID;
    protected String selected_name;
    protected String selected_contact_number;
    protected ContactsCursorAdapter contactsCursorAdapter;

    protected FragmentActivity myContext;
    protected AutoCompleteTextView atv_person_name;
    protected Button btn_end_date, btn_start_date;
    protected Button btn_borrowed, btn_lent;
    protected ImageButton img_btn_switch;

    protected OnFragmentInteractionListener mListener;

    @Override
    public void onCreate(Bundle savedInstanceState) { super.onCreate(savedInstanceState);}

    @Override
    public  abstract View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState);

    protected abstract void onFragmentSwitch();

    public interface OnFragmentInteractionListener{
        //TODO: Update argument type and name
        public int onAddNewUser(String name, String contact_info);
        public void onAddTransactions(Transaction t);
    }

    public void setOnFragmentInteractionListener(OnFragmentInteractionListener mListener){this.mListener = mListener; }

    public void init() {

        myContext = (FragmentActivity) getActivity();
        Cursor contacts = null;
        contactsCursorAdapter = new ContactsCursorAdapter(myContext, contacts, 0);
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

        setDateToCurrent();

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

    public void setDateToCurrent(){
        CustomDate d = new CustomDate();
        String currentDate = (d.getMonth()+1)+ "/" + d.getDay()+ "/ "+ d.getYear();
        btn_start_date.setText(d.formatDateCommas(currentDate));
        btn_end_date.setText(d.formatDateCommas(currentDate));
    }
}
