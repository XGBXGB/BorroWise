package com.example.shayanetan.borrowise2.Activities;

import android.database.Cursor;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.example.shayanetan.borrowise2.Adapters.TransactionsCursorAdapter;
import com.example.shayanetan.borrowise2.Adapters.UsersCursorAdapter;
import com.example.shayanetan.borrowise2.Adapters.ViewPagerAdapter;
import com.example.shayanetan.borrowise2.Fragments.ViewBorrowedFragment;
import com.example.shayanetan.borrowise2.Fragments.ViewBorrowerFragment;
import com.example.shayanetan.borrowise2.Fragments.ViewLenderFragment;
import com.example.shayanetan.borrowise2.Fragments.ViewLentFragment;
import com.example.shayanetan.borrowise2.Models.DatabaseOpenHelper;
import com.example.shayanetan.borrowise2.Models.ItemTransaction;
import com.example.shayanetan.borrowise2.Models.MoneyTransaction;
import com.example.shayanetan.borrowise2.Views.SlidingTabLayout;
import com.example.shayanetan.borrowise2.R;

public class ViewUserActivity extends BaseActivity
        implements ViewLenderFragment.OnFragmentInteractionListener, ViewBorrowerFragment.OnFragmentInteractionListener{

    private ViewPager viewPager;
    private ViewPagerAdapter viewPagerAdapter;
    private SlidingTabLayout slidingTabLayout;

    private static String TITLE_TAB1 = "BORROWERS";
    private static String TITLE_TAB2 = "LENDERS";

    private ViewBorrowerFragment borrowFragment;
    private ViewLenderFragment lentFragment;
    private DatabaseOpenHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_user);

        dbHelper = DatabaseOpenHelper.getInstance(getBaseContext());
        // transactionsCursorAdapter = new TransactionsCursorAdapter(getBaseContext(),null);
        viewPagerAdapter = new ViewPagerAdapter(this.getSupportFragmentManager());

        viewPager = (ViewPager)findViewById(R.id.view_pager);

        borrowFragment = new ViewBorrowerFragment();
        borrowFragment.setOnFragmentInteractionListener(this);
        lentFragment = new ViewLenderFragment();
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
    public void retrieveTransaction(UsersCursorAdapter adapter, String viewType) {
        Cursor cursor = null;
        if(viewType.equalsIgnoreCase(ViewLenderFragment.VIEW_TYPE)) {
            cursor= dbHelper.querryUsersType("borrow", "0");
        }
        else if(viewType.equalsIgnoreCase(ViewBorrowerFragment.VIEW_TYPE)){
            cursor= dbHelper.querryUsersType("lend", "0");
        }
        adapter.swapCursor(cursor);
    }
}
