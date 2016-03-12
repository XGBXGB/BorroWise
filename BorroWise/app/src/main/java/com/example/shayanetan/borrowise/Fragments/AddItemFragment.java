
/**
 * Created by ShayaneTan on 3/11/2016.
 */
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
import com.example.shayanetan.borrowise.MainActivity;
import com.example.shayanetan.borrowise.Models.DatabaseOpenHelper;
import com.example.shayanetan.borrowise.Models.ItemTransaction;
import com.example.shayanetan.borrowise.Models.User;
import com.example.shayanetan.borrowise.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AddItemFragment extends Fragment {


    private FragmentTransaction transaction;
    private FragmentActivity myContext;
    private ContactsCursorAdapter contactsCursorAdapter;
    private ImageButton btn_ItemToMoney;
    private EditText et_AIItemName, et_AIStartDate, et_AIEndDate, et_AIDescription;
    private Button btn_AIBorrow, btn_AILend;
    private Spinner spnr_AIPersonName;

    private DatabaseOpenHelper dbHelper;


    //TODO: UI components
    public AddItemFragment() {
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View layout = inflater.inflate(R.layout.fragment_add_item, container, false);

        btn_ItemToMoney = (ImageButton) layout.findViewById(R.id.btn_ItemToMoney);

        et_AIItemName = (EditText) layout.findViewById(R.id.et_AIItemName);
        spnr_AIPersonName = (Spinner) layout.findViewById(R.id.spnr_AIPersonName);
        et_AIStartDate = (EditText) layout.findViewById(R.id.et_AIStartDate);
        et_AIEndDate = (EditText) layout.findViewById(R.id.et_AIEndDate);
        et_AIDescription = (EditText) layout.findViewById(R.id.et_AIDescription);

        contactsCursorAdapter = new ContactsCursorAdapter(myContext, null, 0);
        spnr_AIPersonName.setAdapter(contactsCursorAdapter);

        Cursor cursor = myContext.getContentResolver().query(
                ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                null,
                null,
                null,
                null);

        contactsCursorAdapter.swapCursor(cursor);

        btn_AIBorrow = (Button) layout.findViewById(R.id.btn_AIBorrow);
        btn_AILend = (Button) layout.findViewById(R.id.btn_AILend);

        dbHelper = new DatabaseOpenHelper(myContext);
        btn_ItemToMoney.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AddMoneyFragment fragment = new AddMoneyFragment();

                FragmentManager fm = myContext.getSupportFragmentManager();
                transaction = fm.beginTransaction();
                transaction.replace(R.id.fragment_container, fragment);
                transaction.commit();

            }
        });

        btn_AIBorrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Cursor contactDetail = (Cursor) spnr_AIPersonName.getSelectedItem();
                String name = contactDetail.getString(contactDetail.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                String number = contactDetail.getString(contactDetail.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));

                int id = dbHelper.checkUserIfExists(name, number);
                if(id == -1){
                    id = (int) dbHelper.insertUser(new User(name, number, 0));
                }else{
                    System.out.println("USER doesnt EXISTS!");
                }


                dbHelper.insertTransaction(new ItemTransaction(
                        "Item", id, "borrow", 0,
                        parseDateToMillis(et_AIStartDate.getText().toString()), parseDateToMillis(et_AIEndDate.getText().toString()),
                        0.0, et_AIItemName.getText().toString(), et_AIDescription.getText().toString()));


            }
        });

        btn_AILend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Cursor contactDetail = (Cursor) spnr_AIPersonName.getSelectedItem();
                String name = contactDetail.getString(contactDetail.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                String number = contactDetail.getString(contactDetail.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));

                int id = dbHelper.checkUserIfExists(name, number);
                if(id == -1){
                    id = (int) dbHelper.insertUser(new User(name, number, 0));
                }else{
                    System.out.println("USER doesnt EXISTS!");
                }


                dbHelper.insertTransaction(new ItemTransaction(
                        "Item", id, "lend", 0,
                        parseDateToMillis(et_AIStartDate.getText().toString()), parseDateToMillis(et_AIEndDate.getText().toString()),
                        0.0, et_AIItemName.getText().toString(), et_AIDescription.getText().toString()));
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