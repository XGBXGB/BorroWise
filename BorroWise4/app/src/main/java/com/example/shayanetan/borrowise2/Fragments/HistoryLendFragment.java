package com.example.shayanetan.borrowise2.Fragments;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.example.shayanetan.borrowise2.Adapters.HistoryCursorAdapter;
import com.example.shayanetan.borrowise2.Models.Transaction;
import com.example.shayanetan.borrowise2.R;

public class HistoryLendFragment extends HistoryAbstractFragment {

    public Button btn_HLend_all, btn_HLend_item, btn_HLend_money;
    private String filterType;


    public HistoryLendFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        init();
        filterType = "All";

        View layout = inflater.inflate(R.layout.fragment_history_lend, container, false);
        recyclerView = (RecyclerView)layout.findViewById(R.id.recyclerview_history_lent);

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(historyCursorAdapter);
        mListener.retrieveTransaction(historyCursorAdapter, HistoryCursorAdapter.TYPE_LEND);

        if(filterType.equalsIgnoreCase("All"))
            mListener.retrieveTransaction(historyCursorAdapter, HistoryCursorAdapter.TYPE_BORROWED);
        else
            mListener.retrieveTransaction(historyCursorAdapter, HistoryCursorAdapter.TYPE_BORROWED, filterType);

        btn_HLend_all = (Button) layout.findViewById(R.id.btn_HLend_all);
        btn_HLend_item = (Button) layout.findViewById(R.id.btn_HLend_item);
        btn_HLend_money = (Button) layout.findViewById(R.id.btn_HLend_money);

        btn_HLend_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filterType = "All";
                mListener.retrieveTransaction(historyCursorAdapter, HistoryCursorAdapter.TYPE_BORROWED);
            }
        });

        btn_HLend_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filterType = Transaction.ITEM_TYPE;
                mListener.retrieveTransaction(historyCursorAdapter, HistoryCursorAdapter.TYPE_BORROWED, Transaction.ITEM_TYPE);
            }
        });

        btn_HLend_money.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filterType = Transaction.MONEY_TYPE;
                mListener.retrieveTransaction(historyCursorAdapter, HistoryCursorAdapter.TYPE_BORROWED, Transaction.MONEY_TYPE);
            }
        });



        return layout;
    }

}
