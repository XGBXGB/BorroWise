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


public class AddMoneyFragment extends Fragment {
    //TODO:UI components

    private FragmentTransaction transaction;
    private FragmentActivity myContext;
    private DatabaseOpenHelper dbHelper;
    private ContactsCursorAdapter contactsCursorAdapter;

    private ImageButton btn_MoneyToItem;
    private EditText et_AMAmount, et_AMStartDate, et_AMEndDate;
    private Button btn_AMBorrow, btn_AMLend;
    private Spinner spnr_AMPersonName;

    public AddMoneyFragment() {
        // Required empty public constructor
    }


    @Override
    public void onAttach(Activity activity) {
        myContext=(FragmentActivity) activity;
        super.onAttach(activity);
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

        btn_MoneyToItem = (ImageButton) layout.findViewById(R.id.btn_MoneyToItem);

        et_AMAmount = (EditText) layout.findViewById(R.id.et_AMAmount);
        spnr_AMPersonName = (Spinner) layout.findViewById(R.id.spnr_AMPersonName);
        et_AMStartDate = (EditText) layout.findViewById(R.id.et_AMStartDate);
        et_AMEndDate = (EditText) layout.findViewById(R.id.et_AMEndDate);

        contactsCursorAdapter = new ContactsCursorAdapter(myContext, null, 0);
        spnr_AMPersonName.setAdapter(contactsCursorAdapter);

        Cursor cursor = myContext.getContentResolver().query(
                ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                null,
                null,
                null,
                null);

        contactsCursorAdapter.swapCursor(cursor);

        btn_AMBorrow = (Button) layout.findViewById(R.id.btn_AMBorrow);
        btn_AMLend = (Button) layout.findViewById(R.id.btn_AMLend);

        dbHelper = new DatabaseOpenHelper(myContext);

        btn_MoneyToItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AddItemFragment fragment = new AddItemFragment();

                FragmentManager fm = myContext.getSupportFragmentManager();
                transaction = fm.beginTransaction();
                transaction.replace(R.id.fragment_container, fragment);
                transaction.commit();


            }
        });

        btn_AMBorrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                dbHelper.insertTransaction(new MoneyTransaction(
                        "Money",spnr_AMPersonName.getSelectedItemPosition(), "borrow", 1,
                        parseDateToMillis(et_AMStartDate.getText().toString()), parseDateToMillis(et_AMEndDate.getText().toString()),
                        0.0,
                        Double.parseDouble(et_AMAmount.getText().toString()), 0.0));

                Cursor contactDetail = (Cursor) spnr_AMPersonName.getSelectedItem();
                String name = contactDetail.getString(contactDetail.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                String number = contactDetail.getString(contactDetail.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));

                if(dbHelper.checkUserIfExists(name, number)){
                    System.out.println("USER ALREADY EXISTS!");
                }else{
                    System.out.println("USER doesnt EXISTS!");
                    dbHelper.insertUser(new User(name, number, 0));
                }


            }
        });

        btn_AMLend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dbHelper.insertTransaction(new MoneyTransaction(
                        "Money",spnr_AMPersonName.getSelectedItemPosition(), "lend", 1,
                        parseDateToMillis(et_AMStartDate.getText().toString()), parseDateToMillis(et_AMEndDate.getText().toString()),
                        0.0,
                        Double.parseDouble(et_AMAmount.getText().toString()), 0.0));

                Cursor contactDetail = (Cursor) spnr_AMPersonName.getSelectedItem();
                String name = contactDetail.getString(contactDetail.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                String number = contactDetail.getString(contactDetail.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));

                if(dbHelper.checkUserIfExists(name, number)){
                    System.out.println("USER ALREADY EXISTS!");
                }else{
                    System.out.println("USER doesnt EXISTS!");
                    dbHelper.insertUser(new User(name, number, 0));
                }

            }
        });

        return layout;
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
