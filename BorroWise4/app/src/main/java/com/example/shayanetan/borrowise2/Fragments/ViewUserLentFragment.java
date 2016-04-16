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

import com.example.shayanetan.borrowise2.Adapters.HistoryCursorAdapter;
import com.example.shayanetan.borrowise2.Adapters.TransactionsCursorAdapter;
import com.example.shayanetan.borrowise2.Adapters.UsersCursorAdapter;
import com.example.shayanetan.borrowise2.R;

public class ViewUserLentFragment extends Fragment {

    public static String VIEW_TYPE = "lender_viewtype";

    private RecyclerView recyclerView;
    private HistoryCursorAdapter historyCursorAdapter;

    private OnFragmentInteractionListener mListener;

    public ViewUserLentFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        historyCursorAdapter = new HistoryCursorAdapter(getActivity().getBaseContext(),null);
        historyCursorAdapter.setmOnLongClickListener(new HistoryCursorAdapter.OnButtonClickListener() {
            @Override
            public void onButtonClick(int id, String type, String classification) {
                //    Toast.makeText(getActivity().getBaseContext(), "KEN LEE: " + id, Toast.LENGTH_LONG);
                //mListener.deleteTransaction(historyCursorAdapter,id, type, classification);
            }
        });
        // Inflate the layout for this fragment
        View layout = inflater.inflate(R.layout.fragment_users_lent, container, false);

        //initiate adapter and set recycler view adapter
        recyclerView = (RecyclerView)layout.findViewById(R.id.recyclerview_users_lent);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        recyclerView.setAdapter(historyCursorAdapter);
        mListener.retrieveTransaction(historyCursorAdapter, VIEW_TYPE);

        return layout;
    }


    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    public interface OnFragmentInteractionListener{
        public void deleteTransaction(HistoryCursorAdapter adapter,int id, String type, String classification);
        public void retrieveTransaction(HistoryCursorAdapter adapter, String viewType);
    }
    public void setOnFragmentInteractionListener(OnFragmentInteractionListener mListener){
        this.mListener = mListener;
    }

}
