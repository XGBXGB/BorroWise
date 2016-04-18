package com.example.shayanetan.borrowise2.Fragments;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
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

public class HistoryBorrowedFragment extends HistoryAbstractFragment {

    public Button btn_HBorrowed_all, btn_HBorrowed_item, btn_HBorrowed_money;
    private String filterType;


    public HistoryBorrowedFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        init();
        filterType = "All";

        View layout = inflater.inflate(R.layout.fragment_history_borrowed, container, false);
        recyclerView = (RecyclerView)layout.findViewById(R.id.recyclerview_history_borrowed);

        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        recyclerView.setAdapter(historyCursorAdapter);
        mListener.retrieveTransaction(historyCursorAdapter, HistoryCursorAdapter.TYPE_BORROWED);

        if(filterType.equalsIgnoreCase("All"))
            mListener.retrieveTransaction(historyCursorAdapter, HistoryCursorAdapter.TYPE_BORROWED);
        else
            mListener.retrieveTransaction(historyCursorAdapter, HistoryCursorAdapter.TYPE_BORROWED, filterType);

        btn_HBorrowed_all = (Button) layout.findViewById(R.id.btn_HBorrowed_all);
        btn_HBorrowed_item = (Button) layout.findViewById(R.id.btn_HBorrowed_item);
        btn_HBorrowed_money = (Button) layout.findViewById(R.id.btn_HBorrowed_money);

        btn_HBorrowed_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filterType = "All";
                mListener.retrieveTransaction(historyCursorAdapter, HistoryCursorAdapter.TYPE_BORROWED);

                btn_HBorrowed_all.setBackgroundResource(R.color.accentBlueColor);
                btn_HBorrowed_item.setBackgroundResource(R.color.text_primaryColor);
                btn_HBorrowed_money.setBackgroundResource(R.color.text_primaryColor);
            }
        });

        btn_HBorrowed_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filterType = Transaction.ITEM_TYPE;
                mListener.retrieveTransaction(historyCursorAdapter, HistoryCursorAdapter.TYPE_BORROWED, Transaction.ITEM_TYPE);
                btn_HBorrowed_item.setBackgroundResource(R.color.accentBlueColor);
                btn_HBorrowed_all.setBackgroundResource(R.color.text_primaryColor);
                btn_HBorrowed_money.setBackgroundResource(R.color.text_primaryColor);
            }
        });

        btn_HBorrowed_money.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filterType = Transaction.MONEY_TYPE;
                mListener.retrieveTransaction(historyCursorAdapter, HistoryCursorAdapter.TYPE_BORROWED, Transaction.MONEY_TYPE);
                btn_HBorrowed_money.setBackgroundResource(R.color.accentBlueColor);
                btn_HBorrowed_item.setBackgroundResource(R.color.text_primaryColor);
                btn_HBorrowed_all.setBackgroundResource(R.color.text_primaryColor);
            }
        });

        return layout;
    }


}
