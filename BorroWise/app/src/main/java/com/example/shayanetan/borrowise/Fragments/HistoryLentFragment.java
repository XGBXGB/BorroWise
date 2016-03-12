package com.example.shayanetan.borrowise.Fragments;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.support.v4.app.Fragment;

import com.example.shayanetan.borrowise.Adapters.TransactionsCursorAdapter;
import com.example.shayanetan.borrowise.Models.DatabaseOpenHelper;
import com.example.shayanetan.borrowise.Models.MoneyTransaction;
import com.example.shayanetan.borrowise.Models.Transaction;
import com.example.shayanetan.borrowise.R;

public class HistoryLentFragment extends Fragment {

    RecyclerView recyclerView;
    Spinner filter;
    TransactionsCursorAdapter transactionsCursorAdapter;
    DatabaseOpenHelper dbDatabaseOpenHelper;

    public HistoryLentFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        transactionsCursorAdapter = new TransactionsCursorAdapter(getActivity(),null);
        dbDatabaseOpenHelper = new DatabaseOpenHelper(getActivity());
        Transaction t = new MoneyTransaction();


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View layout = inflater.inflate(R.layout.fragment_history_lent, container, false);

        //initiate adapter and set recycler view adapter

        recyclerView = (RecyclerView)layout.findViewById(R.id.recyclerview_history_lent);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(transactionsCursorAdapter);

        filter = (Spinner) layout.findViewById(R.id.Hspinner_type_lent);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.array_transaction_type, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        filter.setAdapter(adapter);
        return layout;
    }

    @Override
    public void onResume() {
        super.onResume();
        //TODO: query all transactions and swapthe Cursor
//        Cursor cursor = dbHelper.queryAllUnits();
//        recyclerView.swapCursor(cursor);
    }



}
