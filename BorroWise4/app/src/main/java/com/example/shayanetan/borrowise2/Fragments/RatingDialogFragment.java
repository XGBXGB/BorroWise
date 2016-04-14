package com.example.shayanetan.borrowise2.Fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RatingBar;

import com.example.shayanetan.borrowise2.Activities.ViewTransactionActivity;
import com.example.shayanetan.borrowise2.Adapters.TransactionsCursorAdapter;
import com.example.shayanetan.borrowise2.R;

public class RatingDialogFragment extends DialogFragment {

    View v;

    private TransactionsCursorAdapter transactionsCursorAdapter;
    private String viewType;
    private String filterType;
    private OnFragmentInteractionListener mListener;

    public interface OnFragmentInteractionListener{
        public void updateRating(TransactionsCursorAdapter transactionsCursorAdapter, String viewType, String filterType, double rating);
    }

    public void setViewType(String viewType) {
        this.viewType = viewType;
    }

    public void setTransactionsCursorAdapter(TransactionsCursorAdapter transactionsCursorAdapter) {
        this.transactionsCursorAdapter = transactionsCursorAdapter;
    }

    public void setFilterType(String filterType) {
        this.filterType = filterType;
    }
    public void setOnFragmentInteractionListener(OnFragmentInteractionListener mListener){
        this.mListener = mListener;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState)
    {
        v = LayoutInflater
                .from(getActivity())
                .inflate(R.layout.dialog_input, null);

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity())
                .setTitle("RATE YOUR EXPERIENCE")
                .setView(v)
                .setMessage("How much would you rate this experience?")
                .setPositiveButton("Rate", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                /* EditText etxtName = (EditText) v.findViewById(R.id.etxt_name);
                                ((MainActivity)getActivity()).onNoSelected(etxtName.getText().toString());*/
                                RatingBar rb = (RatingBar) v.findViewById(R.id.DialogRB);
                                mListener.updateRating(transactionsCursorAdapter, viewType,filterType,rb.getRating());
                              //  ((ViewTransactionActivity)getActivity()).setExpRating(rb.getRating());
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