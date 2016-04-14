package com.example.shayanetan.borrowise2.Fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RatingBar;

import com.example.shayanetan.borrowise2.Activities.ViewTransactionActivity;
import com.example.shayanetan.borrowise2.Adapters.TransactionsCursorAdapter;
import com.example.shayanetan.borrowise2.R;


public class AmountDialogFragment extends DialogFragment {
    View v;

    private OnFragmentInteractionListener mListener;
    private TransactionsCursorAdapter transactionsCursorAdapter;
    private String viewType;
    private String filterType;


    public interface OnFragmentInteractionListener{

        public void updateAmount(TransactionsCursorAdapter transactionsCursorAdapter, String viewType, String filterType, double partialAmount);
    }

    public void setFilterType(String filterType) {
        this.filterType = filterType;
    }
    public void setViewType(String viewType) {
        this.viewType = viewType;
    }

    public void setTransactionsCursorAdapter(TransactionsCursorAdapter transactionsCursorAdapter) {
        this.transactionsCursorAdapter = transactionsCursorAdapter;
    }
    public void setOnFragmentInteractionListener(OnFragmentInteractionListener mListener){
        this.mListener = mListener;
    }
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState)
    {
        v = LayoutInflater
                .from(getActivity())
                .inflate(R.layout.fragment_amount_dialog, null);

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity())
                .setTitle("PARTIAL AMOUNT")
                .setView(v)
                .setMessage("How much was the partial payment?")
                .setPositiveButton("Paid", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                EditText etPartialAmt = (EditText) v.findViewById(R.id.etPartialAmt);
                                mListener.updateAmount(transactionsCursorAdapter, viewType, filterType,Double.parseDouble(etPartialAmt.getText().toString()));
                               // ((ViewTransactionActivity) getActivity()).transactPartial();
                                //((ViewTransactionActivity) getActivity()).setExpRating(rb.getRating());
                            }
                        }
                ).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dismiss();
                    }
                });
        return dialogBuilder.create();
    }

    public void showDialog()
    {
        this.show(getFragmentManager(), "");

    }
}
