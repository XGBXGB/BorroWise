package com.example.shayanetan.borrowise.Fragments;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;

import com.example.shayanetan.borrowise.Adapters.ContactsCursorAdapter;
import com.example.shayanetan.borrowise.Models.DatabaseOpenHelper;
import com.example.shayanetan.borrowise.Models.MoneyTransaction;
import com.example.shayanetan.borrowise.Models.User;
import com.example.shayanetan.borrowise.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class AddMoneyFragment extends AddTransactionAbstractFragment {
    //TODO:UI components

    private FragmentTransaction transaction;
    private EditText et_AMAmount;

    public AddMoneyFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View layout = inflater.inflate(R.layout.fragment_add_money, container, false);

        img_btn_switch = (ImageButton) layout.findViewById(R.id.btn_MoneyToItem);
        btn_borrowed = (Button) layout.findViewById(R.id.btn_AMBorrow);
        btn_lent = (Button) layout.findViewById(R.id.btn_AMLend);

        et_AMAmount = (EditText) layout.findViewById(R.id.et_AMAmount);
        atv_person_name = (AutoCompleteTextView) layout.findViewById(R.id.atv_AMPersonName);
        btn_start_date = (Button) layout.findViewById(R.id.btn_AMStartDate);
        btn_end_date = (Button) layout.findViewById(R.id.btn_AMEndDate);

        init(); //method can be found in abstract class

        btn_borrowed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

<<<<<<< HEAD
                databaseOpenHelper.insertTransaction(new MoneyTransaction(
                        "Money",selected_contactID, "borrow", 1,
                        parseDateToMillis(btn_start_date.getText().toString()),
                        parseDateToMillis(btn_end_date.getText().toString()),
                        0.0,
                        Double.parseDouble(et_AMAmount.getText().toString()), 0.0));

                if(databaseOpenHelper.checkUserIfExists(selected_name, selected_contact_number)){
                    System.out.println("USER ALREADY EXISTS!");
                }else{
                    System.out.println("USER doesnt EXISTS!");
                    databaseOpenHelper.insertUser(new User(selected_name, selected_contact_number, 0));
                }
=======

                Cursor contactDetail = (Cursor) spnr_AMPersonName.getSelectedItem();
                String name = contactDetail.getString(contactDetail.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                String number = contactDetail.getString(contactDetail.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));

                int id = dbHelper.checkUserIfExists(name, number);
                if(id == -1){
                    id = (int) dbHelper.insertUser(new User(name, number, 0));
                }else{
                    System.out.println("USER doesnt EXISTS!");
                }


                dbHelper.insertTransaction(new MoneyTransaction(
                        "Money", id, "borrow", 0,
                        parseDateToMillis(et_AMStartDate.getText().toString()), parseDateToMillis(et_AMEndDate.getText().toString()),
                        0.0,
                        Double.parseDouble(et_AMAmount.getText().toString()), 0.0));


>>>>>>> origin/master
            }
        });

        btn_lent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

<<<<<<< HEAD
                databaseOpenHelper.insertTransaction(new MoneyTransaction(
                        "Money",selected_contactID, "lend", 1,
                        parseDateToMillis(btn_start_date.getText().toString()),
                        parseDateToMillis(btn_end_date.getText().toString()),
                        0.0,
                        Double.parseDouble(et_AMAmount.getText().toString()), 0.0));

                if(databaseOpenHelper.checkUserIfExists(selected_name, selected_contact_number)){
                    System.out.println("USER ALREADY EXISTS!");
                }else{
                    System.out.println("USER doesnt EXISTS!");
                    databaseOpenHelper.insertUser(new User(selected_name, selected_contact_number, 0));
=======
                Cursor contactDetail = (Cursor) spnr_AMPersonName.getSelectedItem();
                String name = contactDetail.getString(contactDetail.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                String number = contactDetail.getString(contactDetail.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));

                int id = dbHelper.checkUserIfExists(name, number);
                if(id == -1){
                    id = (int) dbHelper.insertUser(new User(name, number, 0));
                }else{
                    System.out.println("USER doesnt EXISTS!");
>>>>>>> origin/master
                }


                dbHelper.insertTransaction(new MoneyTransaction(
                        "Money", id, "lend", 0,
                        parseDateToMillis(et_AMStartDate.getText().toString()), parseDateToMillis(et_AMEndDate.getText().toString()),
                        0.0,
                        Double.parseDouble(et_AMAmount.getText().toString()), 0.0));


            }
        });

        return layout;
    }

    @Override
    public void onFragmentSwitch() {

        img_btn_switch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddItemFragment fragment = new AddItemFragment();
                FragmentManager fm = myContext.getSupportFragmentManager();
                transaction = fm.beginTransaction();
                transaction.replace(R.id.fragment_container, fragment);
                transaction.commit();
            }
        });
    }


}
