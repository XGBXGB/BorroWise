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
import com.example.shayanetan.borrowise2.R;

public class RatingDialogFragment extends DialogFragment {

    View v;
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState)
    {
        v = LayoutInflater
                .from(getActivity())
                .inflate(R.layout.dialog_input, null);

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity())
                .setTitle("Are you sure?")
                .setView(v)
                .setMessage("How much would you rate this experience?")
                .setPositiveButton("Rate", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                /* EditText etxtName = (EditText) v.findViewById(R.id.etxt_name);
                                ((MainActivity)getActivity()).onNoSelected(etxtName.getText().toString());*/
                                RatingBar rb = (RatingBar) v.findViewById(R.id.DialogRB);
                                ((ViewTransactionActivity)getActivity()).setExpRating(rb.getRating());
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