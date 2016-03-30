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
import android.widget.Spinner;

import com.example.shayanetan.borrowise2.Adapters.HistoryCursorAdapter;
import com.example.shayanetan.borrowise2.R;

public class HistoryBorrowedFragment extends HistoryAbstractFragment {

    public HistoryBorrowedFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        init();

        View layout = inflater.inflate(R.layout.fragment_history_borrowed, container, false);
        recyclerView = (RecyclerView)layout.findViewById(R.id.recyclerview_history_borrowed);

        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        recyclerView.setAdapter(historyCursorAdapter);
        mListener.retrieveTransaction(historyCursorAdapter, HistoryCursorAdapter.TYPE_BORROWED);


        return layout;
    }


}
