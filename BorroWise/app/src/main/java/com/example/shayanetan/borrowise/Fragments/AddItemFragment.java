
/**
 * Created by ShayaneTan on 3/11/2016.
 */
package com.example.shayanetan.borrowise.Fragments;
import android.app.Activity;
import android.os.Bundle;
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

import com.example.shayanetan.borrowise.Models.DatabaseOpenHelper;
import com.example.shayanetan.borrowise.Models.ItemTransaction;
import com.example.shayanetan.borrowise.R;

public class AddItemFragment extends Fragment {


    private FragmentTransaction transaction;
    private FragmentActivity myContext;

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

                dbHelper.insertTransaction(new ItemTransaction(
                        "Item",spnr_AIPersonName.getSelectedItemPosition(), "borrow", 1,
                        Long.parseLong(et_AIStartDate.getText().toString()), Long.parseLong(et_AIEndDate.getText().toString()),
                        0.0, et_AIItemName.getText().toString(), et_AIDescription.getText().toString()));
            }
        });

        btn_AILend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dbHelper.insertTransaction(new ItemTransaction(
                        "Item",spnr_AIPersonName.getSelectedItemPosition(), "lend", 1,
                        Long.parseLong(et_AIStartDate.getText().toString()), Long.parseLong(et_AIEndDate.getText().toString()),
                        0.0, et_AIItemName.getText().toString(), et_AIDescription.getText().toString()));

            }
        });

        return layout;
    }
}