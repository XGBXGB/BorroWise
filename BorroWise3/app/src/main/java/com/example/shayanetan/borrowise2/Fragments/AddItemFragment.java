package com.example.shayanetan.borrowise2.Fragments;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
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

import com.example.shayanetan.borrowise2.Models.ItemTransaction;
import com.example.shayanetan.borrowise2.R;


public class AddItemFragment extends AddAbstractFragment {

    private FragmentTransaction transaction;
    private EditText et_AIItemName,
            et_AIDescription;

    public AddItemFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View layout = inflater.inflate(R.layout.fragment_add_item, container, false);

        img_btn_switch = (FloatingActionButton) layout.findViewById(R.id.btn_ItemToMoney);
        et_AIItemName = (EditText) layout.findViewById(R.id.et_AIItemName);
//        et_AIDescription = (EditText) layout.findViewById(R.id.et_AIDescription);
        atv_person_name = (AutoCompleteTextView) layout.findViewById(R.id.atv_AIPersonName);

        btn_borrowed = (Button) layout.findViewById(R.id.btn_AIBorrow);
        btn_lent = (Button) layout.findViewById(R.id.btn_AILend);
        btn_start_date = (Button) layout.findViewById(R.id.btn_AIStartDate);
        btn_end_date = (Button) layout.findViewById(R.id.btn_AIEndDate);

        init(); // method found in abstact class

        btn_borrowed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(mListener != null){
                    int id =  mListener.onAddNewUser(selected_name, selected_contact_number);

                    ItemTransaction it = new ItemTransaction( "Item", id, "borrow", 0,
                            parseDateToMillis(btn_start_date.getText().toString()),
                            parseDateToMillis(btn_end_date.getText().toString()),
                            0,0.0,
                            et_AIItemName.getText().toString(),"");
                    mListener.onAddTransactions(it);

                    printAddAcknowledgement(et_AIItemName.getText().toString(), "borrowed");
                }

                clearAllFields();
            }
        });

        btn_lent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(mListener != null){
                    int id =  mListener.onAddNewUser(selected_name, selected_contact_number);

                    ItemTransaction it = new ItemTransaction( "Item", id, "lend", 0,
                            parseDateToMillis(btn_start_date.getText().toString()),
                            parseDateToMillis(btn_end_date.getText().toString()),
                            0,0.0,
                            et_AIItemName.getText().toString(), "");
                    mListener.onAddTransactions(it);

                    printAddAcknowledgement(et_AIItemName.getText().toString(), "lent");
                }

                clearAllFields();
            }
        });

        return layout;
    }

    public void printAddAcknowledgement(String entry_name, String type){
        Toast.makeText(getActivity(),entry_name+ " has been successfully "+ type + " !", Toast.LENGTH_SHORT).show();
    }

    public void clearAllFields(){
        et_AIItemName.setText("");
        atv_person_name.setText("");
        setDateToCurrent();
    }
    @Override
    protected void onFragmentSwitch() {
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

    public void setOnFragmentInteractionListener(OnFragmentInteractionListener mListener){
        this.mListener = mListener;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

}
