package com.example.shayanetan.borrowise2.Activities;

import android.database.Cursor;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.shayanetan.borrowise2.Adapters.HistoryCursorAdapter;
import com.example.shayanetan.borrowise2.Adapters.TransactionsCursorAdapter;
import com.example.shayanetan.borrowise2.Adapters.ViewPagerAdapter;
import com.example.shayanetan.borrowise2.Fragments.HistoryAbstractFragment;
import com.example.shayanetan.borrowise2.Fragments.HistoryBorrowedFragment;
import com.example.shayanetan.borrowise2.Fragments.HistoryLendFragment;
import com.example.shayanetan.borrowise2.Fragments.ViewBorrowedFragment;
import com.example.shayanetan.borrowise2.Fragments.ViewLentFragment;
import com.example.shayanetan.borrowise2.Models.DatabaseOpenHelper;
import com.example.shayanetan.borrowise2.R;
import com.example.shayanetan.borrowise2.Views.SlidingTabLayout;

public class HistoryActivity extends BaseActivity  implements HistoryAbstractFragment.OnFragmentInteractionListener{
    private ViewPager viewPager;
    private ViewPagerAdapter viewPagerAdapter;
    private SlidingTabLayout slidingTabLayout;

    private static String TITLE_TAB1 = "BORROWED FROM";
    private static String TITLE_TAB2 = "LENT TO";

    private HistoryBorrowedFragment historyBorrowedFragment;
    private HistoryLendFragment historyLendFragment;

    private DatabaseOpenHelper dbHelper;

    public HistoryActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        dbHelper = DatabaseOpenHelper.getInstance(getBaseContext());

        viewPagerAdapter = new ViewPagerAdapter(this.getSupportFragmentManager());

        viewPager = (ViewPager) findViewById(R.id.Hview_pager);

        historyBorrowedFragment = new HistoryBorrowedFragment();
        historyBorrowedFragment.setOnFragmentInteractionListener(this);
        historyLendFragment = new HistoryLendFragment();
        historyLendFragment.setOnFragmentInteractionListener(this);

        viewPagerAdapter.addFragment(historyBorrowedFragment, TITLE_TAB1);
        viewPagerAdapter.addFragment(historyLendFragment, TITLE_TAB2);
        viewPager.setAdapter(viewPagerAdapter);

        slidingTabLayout = (SlidingTabLayout)findViewById(R.id.Hsliding_tab);
        // True if Tabs are same Width
        slidingTabLayout.setDistributeEvenly(true);
        slidingTabLayout.setViewPager(viewPager);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_history, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void deleteTransaction(HistoryCursorAdapter adapter, int id, String type, String classification) {
//        dbHelper.deleteTransaction(id, classification);
//        retrieveTransaction(adapter, type);
    }

    @Override
    public void retrieveTransaction(HistoryCursorAdapter adapter, String type) {

        Cursor cursor = null;
        switch(type){
            case HistoryCursorAdapter.TYPE_BORROWED:
                cursor = dbHelper.querryBorrowTransactionsJoinUser("1,-1");
                break;
            case HistoryCursorAdapter.TYPE_LEND:
                cursor = dbHelper.querryLendTransactionsJoinUser("1,-1");
                break;
        }
        if(cursor != null)
             adapter.swapCursor(cursor);
    }

    @Override
    public void retrieveTransaction(HistoryCursorAdapter adapter, String type, String filter) {

        Cursor cursor = null;
        switch(type){
            case HistoryCursorAdapter.TYPE_BORROWED:
                cursor = dbHelper.querryBorrowTransactionsJoinUser("1,-1", filter);
                break;
            case HistoryCursorAdapter.TYPE_LEND:
                cursor = dbHelper.querryLendTransactionsJoinUser("1,-1", filter);
                break;
        }
        if(cursor != null)
            adapter.swapCursor(cursor);

       /* Cursor cursor = null;

        if(type.equalsIgnoreCase(ViewLentFragment.VIEW_TYPE)){
            cursor = dbHelper.querryLendTransactionsJoinUser("1", filter);
        }
        else if(type.equalsIgnoreCase(ViewBorrowedFragment.VIEW_TYPE )) {
            cursor = dbHelper.querryBorrowTransactionsJoinUser("1", filter);
        }
        adapter.swapCursor(cursor);
        */
    }


}
