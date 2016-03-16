package com.example.shayanetan.borrowise2.Fragments;

import android.app.Activity;
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
import android.widget.Toast;

import com.example.shayanetan.borrowise2.Models.MoneyTransaction;
import com.example.shayanetan.borrowise2.R;


public class AddMoneyFragment extends AddAbstractFragment {

    private FragmentTransaction transaction;
    private EditText et_AMAmount;

    public AddMoneyFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) { super.onCreate(savedInstanceState);}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
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

                int id = mListener.onAddNewUser(selected_name, selected_contact_number);

                MoneyTransaction m = new MoneyTransaction("Money", id, "borrow", 0,
                                     parseDateToMillis(btn_start_date.getText().toString()),
                                     parseDateToMillis(btn_end_date.getText().toString()),
                                     0,0.0, Double.parseDouble(et_AMAmount.getText().toString()), 0.0);
                mListener.onAddTransactions(m);
                printAddAcknowledgement(et_AMAmount.getText().toString(), "borrowed");
            }
        });

        btn_lent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int id = mListener.onAddNewUser(selected_name, selected_contact_number);

                MoneyTransaction m = new MoneyTransaction("Money", id, "lend", 0,
                                    parseDateToMillis(btn_start_date.getText().toString()),
                                    parseDateToMillis(btn_end_date.getText().toString()),
                                    0,0.0, Double.parseDouble(et_AMAmount.getText().toString()), 0.0);

                mListener.onAddTransactions(m);
                printAddAcknowledgement(et_AMAmount.getText().toString(), "lent");
            }
        });

        return layout;
    }

    public void printAddAcknowledgement(String entry_name, String type){
        Toast.makeText(getActivity(), "PHP "+ entry_name + " has been successfully " + type + " !", Toast.LENGTH_SHORT).show();
    }

    public void clearAllFields(){
        et_AMAmount.setText("");
        atv_person_name.setText("");
        setDateToCurrent();
    }

    @Override
    protected void onFragmentSwitch() {
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

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }



}
