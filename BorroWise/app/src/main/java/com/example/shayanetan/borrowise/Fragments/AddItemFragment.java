
/**
 * Created by ShayaneTan on 3/11/2016.
 */
package com.example.shayanetan.borrowise.Fragments;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;

import android.widget.ImageButton;

import com.example.shayanetan.borrowise.Models.ItemTransaction;
import com.example.shayanetan.borrowise.Models.User;
import com.example.shayanetan.borrowise.R;


public class AddItemFragment extends AddTransactionAbstractFragment {

    private FragmentTransaction transaction;
    private EditText et_AIItemName, et_AIDescription;

    //TODO: UI components
    public AddItemFragment() {
       // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View layout = inflater.inflate(R.layout.fragment_add_item, container, false);


        img_btn_switch = (ImageButton) layout.findViewById(R.id.btn_ItemToMoney);
        et_AIItemName = (EditText) layout.findViewById(R.id.et_AIItemName);
        et_AIDescription = (EditText) layout.findViewById(R.id.et_AIDescription);
        atv_person_name = (AutoCompleteTextView) layout.findViewById(R.id.atv_AIPersonName);

        btn_borrowed = (Button) layout.findViewById(R.id.btn_AIBorrow);
        btn_lent = (Button) layout.findViewById(R.id.btn_AILend);
        btn_start_date = (Button) layout.findViewById(R.id.btn_AIStartDate);
        btn_end_date = (Button) layout.findViewById(R.id.btn_AIEndDate);

        init(); // method found in abstact class

        btn_borrowed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int id = databaseOpenHelper.checkUserIfExists(selected_name, selected_contact_number);
                if(id == -1){
                    id = (int) databaseOpenHelper.insertUser(new User(selected_name, selected_contact_number, 0));
                }else{
                    System.out.println("USER doesnt EXISTS!");
                }


                    databaseOpenHelper.insertTransaction(new ItemTransaction(
                        "Item", id, "borrow", 0,
                        parseDateToMillis(btn_start_date.getText().toString()), parseDateToMillis(btn_end_date.getText().toString()),
                        0.0, et_AIItemName.getText().toString(), et_AIDescription.getText().toString()));

            }
        });

        btn_lent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int id = databaseOpenHelper.checkUserIfExists(selected_name, selected_contact_number);
                if(id == -1){
                    id = (int) databaseOpenHelper.insertUser(new User(selected_name, selected_contact_number, 0));
                }else{
                    System.out.println("USER doesnt EXISTS!");
                }


                databaseOpenHelper.insertTransaction(new ItemTransaction(
                        "Item", id, "lend", 0,
                        parseDateToMillis(btn_start_date.getText().toString()), parseDateToMillis(btn_end_date.getText().toString()),
                        0.0, et_AIItemName.getText().toString(), et_AIDescription.getText().toString()));
            }
        });

        return layout;
    }

    @Override
    public void onFragmentSwitch() {

        img_btn_switch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddMoneyFragment fragment = new AddMoneyFragment();
                FragmentManager fm = myContext.getSupportFragmentManager();
                transaction = fm.beginTransaction();
                transaction.replace(R.id.fragment_container, fragment);
                transaction.commit();
            }
        });
    }
}