package com.example.shayanetan.borrowise2.Activities;

import android.database.Cursor;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.shayanetan.borrowise2.Adapters.TransactionsCursorAdapter;
import com.example.shayanetan.borrowise2.Adapters.ViewPagerAdapter;
import com.example.shayanetan.borrowise2.Fragments.ViewBorrowedFragment;
import com.example.shayanetan.borrowise2.Fragments.ViewLentFragment;
import com.example.shayanetan.borrowise2.Models.DatabaseOpenHelper;
import com.example.shayanetan.borrowise2.Models.ItemTransaction;
import com.example.shayanetan.borrowise2.Models.MoneyTransaction;
import com.example.shayanetan.borrowise2.Views.SlidingTabLayout;
import com.example.shayanetan.borrowise2.R;

public class ViewTransactionActivity extends BaseActivity
                                     implements ViewBorrowedFragment.OnFragmentInteractionListener, ViewLentFragment.OnFragmentInteractionListener{

    private ViewPager viewPager;
    private ViewPagerAdapter viewPagerAdapter;
    private SlidingTabLayout slidingTabLayout;

<<<<<<< HEAD
    private static String TITLE_TAB1 = "BORROWED FROM";
    private static String TITLE_TAB2 = "LENT TO";
=======
    private static String TITLE_TAB1 = "BORROWED";
    private static String TITLE_TAB2 = "LENT";
>>>>>>> origin/master

    private ViewBorrowedFragment borrowFragment;
    private ViewLentFragment lentFragment;
    private DatabaseOpenHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_transaction);

        dbHelper = DatabaseOpenHelper.getInstance(getBaseContext());
       // transactionsCursorAdapter = new TransactionsCursorAdapter(getBaseContext(),null);
        viewPagerAdapter = new ViewPagerAdapter(this.getSupportFragmentManager());

        viewPager = (ViewPager)findViewById(R.id.view_pager);

        borrowFragment = new ViewBorrowedFragment();
        borrowFragment.setOnFragmentInteractionListener(this);
        lentFragment = new ViewLentFragment();
        lentFragment.setOnFragmentInteractionListener(this);

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
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void updateTransaction(int id, int type, int btnType) {
        switch (type) {
            case TransactionsCursorAdapter.TYPE_MONEY:
                if(btnType == TransactionsCursorAdapter.BTN_TYPE_RETURN) {
                    MoneyTransaction m = (MoneyTransaction) dbHelper.queryTransaction(id);
                    m.setAmountDeficit(0);
                    m.setReturnDate(System.currentTimeMillis());
                    m.setStatus(1);
                    dbHelper.updateTransaction(m);
                }else{
                    Toast.makeText(this, "PARTIALS NOT YET SUPPORTED", Toast.LENGTH_SHORT).show();
                }
                break;
            case TransactionsCursorAdapter.TYPE_ITEM:
                if(btnType == TransactionsCursorAdapter.BTN_TYPE_RETURN) {
                    ItemTransaction i = (ItemTransaction) dbHelper.queryTransaction(id);
                    i.setReturnDate(System.currentTimeMillis());
                    i.setStatus(1);
                    dbHelper.updateTransaction(i);
                }else{
                    ItemTransaction i = (ItemTransaction) dbHelper.queryTransaction(id);
                    i.setStatus(-1);
                    dbHelper.updateTransaction(i);
                }
                break;
        }
    }

    @Override
    public void retrieveTransaction(TransactionsCursorAdapter adapter, String viewType) {
        Cursor cursor = null;
        if(viewType.equalsIgnoreCase(ViewLentFragment.VIEW_TYPE)) {
             cursor= dbHelper.querryLendTransactionsJoinUser("0");
        }
        else if(viewType.equalsIgnoreCase(ViewBorrowedFragment.VIEW_TYPE)){
            cursor = dbHelper.querryBorrowTransactionsJoinUser("0");
        }
        adapter.swapCursor(cursor);
    }
}
