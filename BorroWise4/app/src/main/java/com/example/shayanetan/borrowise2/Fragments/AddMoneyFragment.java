package com.example.shayanetan.borrowise2.Fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.shayanetan.borrowise2.Models.MoneyTransaction;
import com.example.shayanetan.borrowise2.Models.Transaction;
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

        img_btn_switch = (FloatingActionButton) layout.findViewById(R.id.btn_MoneyToItem);
        btn_borrowed = (Button) layout.findViewById(R.id.btn_AMBorrow);
        btn_lent = (Button) layout.findViewById(R.id.btn_AMLend);
        btn_addContact = (ImageView) layout.findViewById(R.id.btn_addContact);
        et_AMAmount = (EditText) layout.findViewById(R.id.et_AMAmount);
        atv_person_name = (AutoCompleteTextView) layout.findViewById(R.id.atv_AMPersonName);


        layout_startDate = (View) layout.findViewById(R.id.layout_money_startDate);
        layout_endDate = (View) layout.findViewById(R.id.layout_money_endDate);

        tv_endDate = (TextView) layout.findViewById(R.id.tv_money_endDate);
        tv_startDate = (TextView) layout.findViewById(R.id.tv_money_startDate);

        init(); //method can be found in abstract class

        btn_addContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_INSERT,
                        ContactsContract.Contacts.CONTENT_URI);
                startActivity(intent);
            }
        });

        btn_borrowed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String money = et_AMAmount.getText().toString();

                if(!money.isEmpty() && !selected_name.isEmpty()){
                    int id = mListener.onAddNewUser(selected_name, selected_contact_number);

                    double amount =  Double.parseDouble(money);
                    MoneyTransaction m = new MoneyTransaction(Transaction.MONEY_TYPE, id, Transaction.BORROWED_ACTION, 0,
                            parseDateToMillis(tv_startDate.getText().toString()),
                            parseDateToMillis(tv_endDate.getText().toString()),
                            0,0.0,amount, amount);
                    mListener.onAddTransactions(m);
                    printAddAcknowledgement(et_AMAmount.getText().toString(), "borrowed");

                    clearAllFields();
                }else
                    printRejectDialog();

            }
        });

        btn_lent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String money = et_AMAmount.getText().toString();

                if(!money.isEmpty() && !selected_name.isEmpty()){
                    int id = mListener.onAddNewUser(selected_name, selected_contact_number);

                    double amount =  Double.parseDouble(money);
                    MoneyTransaction m = new MoneyTransaction(Transaction.MONEY_TYPE, id, Transaction.LEND_ACTION, 0,
                            parseDateToMillis(tv_startDate.getText().toString()),
                            parseDateToMillis(tv_endDate.getText().toString()),
                            0,0.0, amount,amount);

                    mListener.onAddTransactions(m);
                    printAddAcknowledgement(et_AMAmount.getText().toString(), "lent");

                    clearAllFields();
                }else{
                    printRejectDialog();
                }

            }
        });

        return layout;
    }

    public void printAddAcknowledgement(String entry_name, String type){
        if(type.equalsIgnoreCase("lent")) {

            LayoutInflater factory = LayoutInflater.from(getActivity());
            final View view = factory.inflate(R.layout.add_transaction_confirmation, null);
            TextView tv_confirmation = (TextView) view.findViewById(R.id.tv_confirmation);
            tv_confirmation.setText("PHP " + entry_name + " has been successfully " + type + " to " + selected_name + "!");

            AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
            alertDialog.setView(view);
            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            alertDialog.show();
        }else {
            LayoutInflater factory = LayoutInflater.from(getActivity());
            final View view = factory.inflate(R.layout.add_transaction_confirmation, null);
            TextView tv_confirmation = (TextView) view.findViewById(R.id.tv_confirmation);
            tv_confirmation.setText("PHP " + entry_name + " has been successfully " + type + " from " + selected_name + "!");

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
