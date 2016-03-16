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
import android.widget.Spinner;

import com.example.shayanetan.borrowise2.Adapters.HistoryCursorAdapter;
import com.example.shayanetan.borrowise2.R;

public class HistoryLendFragment extends HistoryAbstractFragment {

    public HistoryLendFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        init();

        View layout = inflater.inflate(R.layout.fragment_history_lend, container, false);
        recyclerView = (RecyclerView)layout.findViewById(R.id.recyclerview_history_lent);
        filter = (Spinner) layout.findViewById(R.id.Hspinner_type_lent);

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(historyCursorAdapter);
        mListener.retrieveTransaction(historyCursorAdapter, HistoryCursorAdapter.TYPE_LEND);

        ArrayAdapter<CharSequence> adapter
                = ArrayAdapter.createFromResource(getActivity(), R.array.array_transaction_type, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        filter.setAdapter(adapter);
        return layout;
    }

}
