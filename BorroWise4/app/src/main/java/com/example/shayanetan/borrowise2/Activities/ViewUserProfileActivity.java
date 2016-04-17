package com.example.shayanetan.borrowise2.Activities;

import android.database.Cursor;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.shayanetan.borrowise2.Adapters.HistoryCursorAdapter;
import com.example.shayanetan.borrowise2.Adapters.UsersCursorAdapter;
import com.example.shayanetan.borrowise2.Adapters.ViewPagerAdapter;
import com.example.shayanetan.borrowise2.Fragments.ViewBorrowedFragment;
import com.example.shayanetan.borrowise2.Fragments.ViewLentFragment;
import com.example.shayanetan.borrowise2.Fragments.ViewUserBorrowedFragment;
import com.example.shayanetan.borrowise2.Fragments.ViewUserLentFragment;
import com.example.shayanetan.borrowise2.Models.DatabaseOpenHelper;
import com.example.shayanetan.borrowise2.Models.User;
import com.example.shayanetan.borrowise2.R;
import com.example.shayanetan.borrowise2.Views.SlidingTabLayout;

import org.w3c.dom.Text;

public class ViewUserProfileActivity extends BaseActivity implements ViewUserBorrowedFragment.OnFragmentInteractionListener, ViewUserLentFragment.OnFragmentInteractionListener{

    private ImageView imageView;
    private TextView tv_name, tv_ratingbar;
    private RatingBar ratingBar;

    private SlidingTabLayout slidingTab;
    private ViewPager viewPager;
    private ViewPagerAdapter viewPagerAdapter;

    private static String TITLE_TAB1 = "BORROWED FROM YOU";
    private static String TITLE_TAB2 = "LENT TO YOU";

    private ViewUserBorrowedFragment borrowFragment;
    private ViewUserLentFragment lentFragment;
    private DatabaseOpenHelper dbHelper;

    private String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_user_profile);


        dbHelper = DatabaseOpenHelper.getInstance(getBaseContext());
        imageView = (ImageView) findViewById(R.id.img_userprofile);
        tv_name = (TextView) findViewById(R.id.tv_userprofile_name);
        tv_ratingbar = (TextView) findViewById(R.id.tv_userprofile_rating);
        ratingBar = (RatingBar) findViewById(R.id.rb_userprofile_rating);

        setProfileDetails();


        // transactionsCursorAdapter = new TransactionsCursorAdapter(getBaseContext(),null);
        viewPagerAdapter = new ViewPagerAdapter(this.getSupportFragmentManager());

        viewPager = (ViewPager)findViewById(R.id.view_pager_userprofile);

        borrowFragment = new ViewUserBorrowedFragment();
        borrowFragment.setOnFragmentInteractionListener(this);
        lentFragment = new ViewUserLentFragment();
        lentFragment.setOnFragmentInteractionListener(this);

        viewPagerAdapter.addFragment(borrowFragment, TITLE_TAB1);
        viewPagerAdapter.addFragment(lentFragment, TITLE_TAB2);

        // viewPagerAdapter.addFragment(new TransactionLentFragment(), TITLE_TAB2);
        viewPager.setAdapter(viewPagerAdapter);

        slidingTab = (SlidingTabLayout) findViewById(R.id.sliding_tab_userprofile);
        // True if Tabs are same Width
        slidingTab.setDistributeEvenly(true);
        slidingTab.setViewPager(viewPager);




    }

    public void setProfileDetails(){
        User u = dbHelper.queryUser(getIntent().getExtras().getInt(User.COLUMN_ID));
        username = u.getName();
        tv_name.setText(username);
        ratingBar.setRating((float) u.getTotalRate());
        tv_ratingbar.setText(String.valueOf(u.getTotalRate()));

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_view_user_profile, menu);
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
    public void deleteTransaction(HistoryCursorAdapter adapter, int id, String type, String classification) {
//        dbHelper.deleteTransaction(id, classification);
//        retrieveTransaction(adapter, type);
    }

    @Override
    public void retrieveTransaction(HistoryCursorAdapter adapter, String viewType) {
        Cursor cursor = null;
        if(viewType.equalsIgnoreCase(ViewUserLentFragment.VIEW_TYPE)) {
            cursor= dbHelper.queryAllLendTransactionsGivenUser(username);
        }
        else if(viewType.equalsIgnoreCase(ViewUserBorrowedFragment.VIEW_TYPE)){
            cursor= dbHelper.queryAllBorrowedTransactionsGivenUser(username);
        }
        adapter.swapCursor(cursor);
    }
}
