package com.example.shayanetan.borrowise2.Activities;

import android.app.DialogFragment;
import android.database.Cursor;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.shayanetan.borrowise2.Adapters.TransactionsCursorAdapter;
import com.example.shayanetan.borrowise2.Adapters.ViewPagerAdapter;
import com.example.shayanetan.borrowise2.Fragments.AmountDialogFragment;
import com.example.shayanetan.borrowise2.Fragments.RatingDialogFragment;
import com.example.shayanetan.borrowise2.Fragments.ViewBorrowedFragment;
import com.example.shayanetan.borrowise2.Fragments.ViewLentFragment;
import com.example.shayanetan.borrowise2.Models.DatabaseOpenHelper;
import com.example.shayanetan.borrowise2.Models.ItemTransaction;
import com.example.shayanetan.borrowise2.Models.MoneyTransaction;
import com.example.shayanetan.borrowise2.Models.Transaction;
import com.example.shayanetan.borrowise2.Views.SlidingTabLayout;
import com.example.shayanetan.borrowise2.R;

import java.util.StringTokenizer;

public class ViewTransactionActivity extends BaseActivity
                                     implements ViewBorrowedFragment.OnFragmentInteractionListener,
                                                ViewLentFragment.OnFragmentInteractionListener,
                                                AmountDialogFragment.OnFragmentInteractionListener,
                                                RatingDialogFragment.OnFragmentInteractionListener{

    private ViewPager viewPager;
    private ViewPagerAdapter viewPagerAdapter;
    private SlidingTabLayout slidingTabLayout;

    private static String TITLE_TAB1 = "BORROWED FROM";
    private static String TITLE_TAB2 = "LENT TO";

    private ViewBorrowedFragment borrowFragment;
    private ViewLentFragment lentFragment;
    private DatabaseOpenHelper dbHelper;

    private int TempID, currType, currBtn;
   // private TransactionsCursorAdapter transactionsCursorAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_transaction);

        dbHelper = DatabaseOpenHelper.getInstance(getBaseContext());
        //transactionsCursorAdapter = new TransactionsCursorAdapter(getBaseContext(),null);

        viewPagerAdapter = new ViewPagerAdapter(this.getSupportFragmentManager());

        viewPager = (ViewPager)findViewById(R.id.view_pager);

        borrowFragment = new ViewBorrowedFragment();
        borrowFragment.setOnFragmentInteractionListener(this);
        //borrowFragment.setTransactionsCursorAdapter(transactionsCursorAdapter);

        lentFragment = new ViewLentFragment();
        lentFragment.setOnFragmentInteractionListener(this);
       // lentFragment.setTransactionsCursorAdapter(transactionsCursorAdapter);

        viewPagerAdapter.addFragment(borrowFragment, TITLE_TAB1);
        viewPagerAdapter.addFragment(lentFragment, TITLE_TAB2);

       // viewPagerAdapter.addFragment(new TransactionLentFragment(), TITLE_TAB2);
        viewPager.setAdapter(viewPagerAdapter);

        slidingTabLayout = (SlidingTabLayout) findViewById(R.id.sliding_tab);
        // True if Tabs are same Width
        slidingTabLayout.setDistributeEvenly(true);
        slidingTabLayout.setViewPager(viewPager);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_view_transaction, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void updateTransaction(int id, int type, int btnType, TransactionsCursorAdapter adapter, String viewType, String filterType) {
        this.currBtn = btnType;
        switch (type) {
            case TransactionsCursorAdapter.TYPE_MONEY:
                currType = TransactionsCursorAdapter.TYPE_MONEY;
                break;
            case TransactionsCursorAdapter.TYPE_ITEM:
                currType = TransactionsCursorAdapter.TYPE_ITEM;
        }
        this.TempID = id;
        Log.v("update TRANS ID!!!!!! ", "" + id);
      //  Toast.makeText(getBaseContext(), "update TRANS ID!!!!!! " +id, Toast.LENGTH_LONG).show();

        if(currBtn == TransactionsCursorAdapter.BTN_TYPE_PARTIAL) {

            AmountDialogFragment df = new AmountDialogFragment();
            df.setOnFragmentInteractionListener(this);
            df.setTransactionsCursorAdapter(adapter);
            df.setViewType(viewType);
            df.setFilterType(filterType);
            df.show(getFragmentManager(), "");
        }
        else
        {
            RatingDialogFragment df = new RatingDialogFragment();
            df.setOnFragmentInteractionListener(this);
            df.setTransactionsCursorAdapter(adapter);
            df.setViewType(viewType);
            df.setFilterType(filterType);
            df.show(getFragmentManager(), "");
        }

    }

    @Override
    public void retrieveTransaction(TransactionsCursorAdapter adapter, String viewType) {
        Cursor cursor = null;

        Log.v("PASOKKKKK", "HALU");
        if(viewType.equalsIgnoreCase(ViewLentFragment.VIEW_TYPE)) {
             cursor= dbHelper.querryLendTransactionsJoinUser("0");
        }
        else if(viewType.equalsIgnoreCase(ViewBorrowedFragment.VIEW_TYPE)){
            cursor = dbHelper.querryBorrowTransactionsJoinUser("0");
        }
        adapter.swapCursor(cursor);
    }

    @Override
    public void retrieveTransaction(TransactionsCursorAdapter adapter, String viewType, String filterType) {
        Cursor cursor = null;

         if(viewType.equalsIgnoreCase(ViewLentFragment.VIEW_TYPE)){
            cursor = dbHelper.querryLendTransactionsJoinUser("0", filterType);
        }
        else if(viewType.equalsIgnoreCase(ViewBorrowedFragment.VIEW_TYPE )) {
            cursor = dbHelper.querryBorrowTransactionsJoinUser("0", filterType);
        }
        adapter.swapCursor(cursor);
    }

    @Override
    public void updateAmount(TransactionsCursorAdapter adapter, String viewType, String filterType, double partialAmt) {


        MoneyTransaction m = (MoneyTransaction) dbHelper.queryTransaction(TempID);
        double computedAmount = m.getAmountDeficit() - partialAmt;
        m.setAmountDeficit(computedAmount);
        m.setReturnDate(System.currentTimeMillis());
        m.setStatus(0);

      //  Toast.makeText(this, "Deficit " + m.getAmountDeficit(), Toast.LENGTH_LONG).show();

        if(m.getAmountDeficit() == 0)
        {
            RatingDialogFragment df = new RatingDialogFragment();
            df.setOnFragmentInteractionListener(this);
            df.setTransactionsCursorAdapter(adapter);
            df.setViewType(viewType);
            df.setFilterType(filterType);
            df.show(getFragmentManager(), "");

            MoneyTransaction m2 = (MoneyTransaction) dbHelper.queryTransaction(TempID);
            m2.setAmountDeficit(m.getAmountDeficit() - partialAmt);
            m2.setReturnDate(System.currentTimeMillis());
            m2.setStatus(1);
            Log.v("BEFORE TEMPID: ", ""+TempID);
            TempID = dbHelper.updateTransaction(m2);
            Log.v("AFTER TEMPID: ",""+TempID);
        }
        else
            TempID = dbHelper.updateTransaction(m);

        if(filterType.equalsIgnoreCase("All"))
            retrieveTransaction(adapter, viewType);
        else
            retrieveTransaction(adapter, viewType, filterType);
    }

    @Override
    public void updateRating(TransactionsCursorAdapter adapter, String viewType, String filterType, double rating) {
        int transID = 0;
        switch (currType) {
            case TransactionsCursorAdapter.TYPE_MONEY:
                Log.v("rating TRANS ID!!!!!! ",""+TempID);
           //     Toast.makeText(getBaseContext(), "rating TRANS ID!!!!!! " +TempID, Toast.LENGTH_LONG).show();
                if(currBtn == TransactionsCursorAdapter.BTN_TYPE_RETURN) {
                    MoneyTransaction m = (MoneyTransaction) dbHelper.queryTransaction(TempID);
                    m.setAmountDeficit(m.getAmountDeficit());
                    m.setReturnDate(System.currentTimeMillis());
                    m.setStatus(1);
                    m.setRate(rating);
                    transID = dbHelper.updateTransaction(m);
                }else{
                    //Toast.makeText(this, "PARTIALS NOT YET SUPPORTED", Toast.LENGTH_SHORT).show();
                    MoneyTransaction m = (MoneyTransaction) dbHelper.queryTransaction(TempID);
                    m.setRate(rating);
                    transID = dbHelper.updateTransaction(m);
                }
                break;
            case TransactionsCursorAdapter.TYPE_ITEM:
                currType = TransactionsCursorAdapter.TYPE_ITEM;
                if(currBtn == TransactionsCursorAdapter.BTN_TYPE_RETURN) {

                    ItemTransaction i = (ItemTransaction) dbHelper.queryTransaction(TempID);
                    i.setReturnDate(System.currentTimeMillis());
                    i.setStatus(1);
                    i.setRate(rating);
                    transID = dbHelper.updateTransaction(i);
                }else{
                    ItemTransaction i = (ItemTransaction) dbHelper.queryTransaction(TempID);
                    i.setStatus(-1);
                    i.setRate(rating);
                    i.setReturnDate(System.currentTimeMillis());
                    transID = dbHelper.updateTransaction(i);
                }
                break;
        }
        Transaction transaction = dbHelper.queryTransaction(transID);
        dbHelper.updateUserRating(transaction.getUserID());
        if(filterType.equalsIgnoreCase("All"))
            retrieveTransaction(adapter, viewType);
        else
            retrieveTransaction(adapter, viewType, filterType);
    }
}

