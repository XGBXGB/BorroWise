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
import com.example.shayanetan.borrowise.Models.MoneyTransaction;
import com.example.shayanetan.borrowise.R;


public class AddMoneyFragment extends Fragment {
    //TODO:UI components

    private FragmentTransaction transaction;
    private FragmentActivity myContext;
    private DatabaseOpenHelper dbHelper;


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
                        Long.parseLong(et_AMStartDate.getText().toString()), Long.parseLong(et_AMEndDate.getText().toString()),
                        0.0,
                        Double.parseDouble(et_AMAmount.getText().toString()), 0.0));


            }
        });

        btn_AMLend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dbHelper.insertTransaction(new MoneyTransaction(
                        "Money",spnr_AMPersonName.getSelectedItemPosition(), "lend", 1,
                        Long.parseLong(et_AMStartDate.getText().toString()), Long.parseLong(et_AMEndDate.getText().toString()),
                        0.0,
                        Double.parseDouble(et_AMAmount.getText().toString()), 0.0));

            }
        });

        return layout;
    }

}
