package com.example.shayanetan.borrowise2.Fragments;

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

import com.example.shayanetan.borrowise2.Adapters.TransactionsCursorAdapter;
import com.example.shayanetan.borrowise2.R;

public class ViewBorrowedFragment extends Fragment {

    public static String VIEW_TYPE = "borrowed_viewtype";

    private RecyclerView recyclerView;
    private Spinner filter;
    private TransactionsCursorAdapter transactionsCursorAdapter;

    private OnFragmentInteractionListener mListener;

    public ViewBorrowedFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        transactionsCursorAdapter = new TransactionsCursorAdapter(getActivity().getBaseContext(),null);

        // Inflate the layout for this fragment
        View layout = inflater.inflate(R.layout.fragment_view_borrowed, container, false);

        //initiate adapter and set recycler view adapter
        recyclerView = (RecyclerView)layout.findViewById(R.id.recyclerview_transaction_borrowed);

        transactionsCursorAdapter.setmOnClickListener(new TransactionsCursorAdapter.OnButtonClickListener() {
            @Override
            public void onButtonClick(int id, int type, int btnType) {
                mListener.updateTransaction(id, type, btnType);
                mListener.retrieveTransaction(transactionsCursorAdapter, VIEW_TYPE);
            }
        });

        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        recyclerView.setAdapter(transactionsCursorAdapter);
        mListener.retrieveTransaction(transactionsCursorAdapter, VIEW_TYPE);

        return layout;
    }


    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    public interface OnFragmentInteractionListener{
        public void updateTransaction(int id, int type, int btnType);
        public void retrieveTransaction(TransactionsCursorAdapter adapter, String viewType);
    }
    public void setOnFragmentInteractionListener(OnFragmentInteractionListener mListener){
        this.mListener = mListener;
    }

}
