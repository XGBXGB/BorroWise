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
import android.widget.Button;
import android.widget.Spinner;

import com.example.shayanetan.borrowise2.Adapters.TransactionsCursorAdapter;
import com.example.shayanetan.borrowise2.Models.Transaction;
import com.example.shayanetan.borrowise2.R;

public class ViewBorrowedFragment extends Fragment {

    public static String VIEW_TYPE = "borrowed_viewtype";
    private String filterType;

    private RecyclerView recyclerView;
    private TransactionsCursorAdapter transactionsCursorAdapter;

    private OnFragmentInteractionListener mListener;

    private Button btn_TBorrowed_all, btn_TBorrowed_item, btn_TBorrowed_money;

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

        filterType = "All";
        transactionsCursorAdapter = new TransactionsCursorAdapter(getActivity().getBaseContext(), null);

        // Inflate the layout for this fragment
        View layout = inflater.inflate(R.layout.fragment_view_borrowed, container, false);

        //initiate adapter and set recycler view adapter
        recyclerView = (RecyclerView)layout.findViewById(R.id.recyclerview_transaction_borrowed);

        transactionsCursorAdapter.setmOnClickListener(new TransactionsCursorAdapter.OnButtonClickListener() {
            @Override
            public void onButtonClick(int id, int type, int btnType) {
                mListener.updateTransaction(id, type, btnType, transactionsCursorAdapter, VIEW_TYPE, filterType);
                //    mListener.retrieveTransaction(transactionsCursorAdapter,VIEW_TYPE);
            }
        });

        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        recyclerView.setAdapter(transactionsCursorAdapter);

        if(filterType.equalsIgnoreCase("All"))
            mListener.retrieveTransaction(transactionsCursorAdapter,VIEW_TYPE);
        else
            mListener.retrieveTransaction(transactionsCursorAdapter, VIEW_TYPE, filterType);

        /*********************************************************************/
        btn_TBorrowed_all = (Button) layout.findViewById(R.id.btn_TBorrowed_all);
        btn_TBorrowed_item = (Button) layout.findViewById(R.id.btn_TBorrowed_item);
        btn_TBorrowed_money = (Button) layout.findViewById(R.id.btn_TBorrowed_money);

        btn_TBorrowed_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filterType = "All";
                mListener.retrieveTransaction(transactionsCursorAdapter, VIEW_TYPE);

                btn_TBorrowed_all.setBackgroundResource(R.color.accentBlueColor);
                btn_TBorrowed_item.setBackgroundResource(R.color.text_primaryColor);
                btn_TBorrowed_money.setBackgroundResource(R.color.text_primaryColor);
            }
        });

        btn_TBorrowed_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filterType = Transaction.ITEM_TYPE;
                mListener.retrieveTransaction(transactionsCursorAdapter, VIEW_TYPE, Transaction.ITEM_TYPE);

                btn_TBorrowed_item.setBackgroundResource(R.color.accentBlueColor);
                btn_TBorrowed_all.setBackgroundResource(R.color.text_primaryColor);
                btn_TBorrowed_money.setBackgroundResource(R.color.text_primaryColor);
            }
        });

        btn_TBorrowed_money.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filterType = Transaction.MONEY_TYPE;
                mListener.retrieveTransaction(transactionsCursorAdapter, VIEW_TYPE, Transaction.MONEY_TYPE);

                btn_TBorrowed_money.setBackgroundResource(R.color.accentBlueColor);
                btn_TBorrowed_item.setBackgroundResource(R.color.text_primaryColor);
                btn_TBorrowed_all.setBackgroundResource(R.color.text_primaryColor);
            }
        });




        return layout;
    }


    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    public interface OnFragmentInteractionListener{
        public void updateTransaction(int id, int type, int btnType,TransactionsCursorAdapter adapter, String viewType, String filterType);
        public void retrieveTransaction(TransactionsCursorAdapter adapter, String viewType);
        public void retrieveTransaction(TransactionsCursorAdapter adapter, String viewType, String filterType);
    }
    public void setOnFragmentInteractionListener(OnFragmentInteractionListener mListener){
        this.mListener = mListener;
    }

}
