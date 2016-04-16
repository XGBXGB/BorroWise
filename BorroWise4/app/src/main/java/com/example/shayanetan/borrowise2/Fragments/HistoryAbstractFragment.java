package com.example.shayanetan.borrowise2.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.shayanetan.borrowise2.Adapters.HistoryCursorAdapter;
import com.example.shayanetan.borrowise2.R;

/**
 * Created by ShayaneTan on 3/16/2016.
 */
public abstract class HistoryAbstractFragment extends Fragment {

    protected OnFragmentInteractionListener mListener;
    protected RecyclerView recyclerView;
    protected HistoryCursorAdapter historyCursorAdapter;
    protected Spinner filter;

    @Override
    public  abstract View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState);

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public void init(){

        historyCursorAdapter = new HistoryCursorAdapter(getActivity().getBaseContext(),null);
        historyCursorAdapter.setmOnLongClickListener(new HistoryCursorAdapter.OnButtonClickListener() {
            @Override
            public void onButtonClick(int id, String type, String classification) {
                Toast.makeText(getActivity().getBaseContext(), "KEN LEE: " + id, Toast.LENGTH_LONG);
                mListener.deleteTransaction(historyCursorAdapter,id, type, classification);
            }
        });
    }

    public void setOnFragmentInteractionListener(OnFragmentInteractionListener mListener){
        this.mListener = mListener;
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void deleteTransaction(HistoryCursorAdapter adapter,int id, String type, String classification);
        public void retrieveTransaction(HistoryCursorAdapter adapter, String type);
        public void retrieveTransaction(HistoryCursorAdapter adapter, String type, String filter);
    }
}
