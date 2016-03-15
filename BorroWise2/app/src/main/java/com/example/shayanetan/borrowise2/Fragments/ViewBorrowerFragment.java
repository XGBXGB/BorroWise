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

import com.example.shayanetan.borrowise2.Adapters.TransactionsCursorAdapter;
import com.example.shayanetan.borrowise2.Adapters.UsersCursorAdapter;
import com.example.shayanetan.borrowise2.R;

public class ViewBorrowerFragment extends Fragment {

    public static String VIEW_TYPE = "borrower_viewtype";

    private RecyclerView recyclerView;
    private UsersCursorAdapter usersCursorAdapter;

    private OnFragmentInteractionListener mListener;

    public ViewBorrowerFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        usersCursorAdapter = new UsersCursorAdapter(getActivity().getBaseContext(),null);

        // Inflate the layout for this fragment
        View layout = inflater.inflate(R.layout.fragment_users_borrowed, container, false);

        //initiate adapter and set recycler view adapter
        recyclerView = (RecyclerView)layout.findViewById(R.id.recyclerview_users_borrowed);



        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(usersCursorAdapter);
        mListener.retrieveTransaction(usersCursorAdapter, VIEW_TYPE);

        return layout;
    }


    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    public interface OnFragmentInteractionListener{
        public void retrieveTransaction(UsersCursorAdapter adapter, String viewType);
    }
    public void setOnFragmentInteractionListener(OnFragmentInteractionListener mListener){
        this.mListener = mListener;
    }

}
