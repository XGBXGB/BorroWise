package com.example.shayanetan.borrowise;

import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.shayanetan.borrowise.Fragments.AddItemFragment;
import com.example.shayanetan.borrowise.Fragments.TransactionBorrowedFragment;
import com.example.shayanetan.borrowise.Fragments.TransactionFragment;
import com.example.shayanetan.borrowise.Models.DatabaseOpenHelper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity{

    private Toolbar toolbar;
    private DrawerLayout drawer_layout;
    private NavigationView navigation_view;
    private ActionBarDrawerToggle drawer_toggle;
    private ViewGroup fragment_container;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar)findViewById(R.id.appbar);
        drawer_layout = (DrawerLayout)findViewById(R.id.drawer_layout);
        navigation_view = (NavigationView)findViewById(R.id.navigation_view);
        fragment_container = (ViewGroup)findViewById(R.id.fragment_container);

        setSupportActionBar(toolbar);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        navigation_view.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                selectDrawerItem(menuItem);
                return true;

            }
        });


        drawer_toggle = new ActionBarDrawerToggle(this, drawer_layout, toolbar, R.string.drawer_open, R.string.drawer_close){
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, slideOffset);
                MainActivity.this.invalidateOptionsMenu();
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                MainActivity.this.invalidateOptionsMenu();
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }
        };

        drawer_layout.setDrawerListener(drawer_toggle);

        drawer_layout.post(new Runnable() {
            @Override
            public void run() {
                drawer_toggle.syncState();
            }
        });

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, new AddItemFragment())
                .commit();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (item.getItemId()) {
            case android.R.id.home:
                drawer_layout.openDrawer(GravityCompat.START);
                return true;
        }



        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }

        return super.onOptionsItemSelected(item);
    }

    private void changeToNextFragment(Fragment nextFragment){
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, nextFragment)
                .addToBackStack(null)
                .commit();
    }




    public void selectDrawerItem(MenuItem menuItem) {
        // Create a new fragment and specify the planet to show based on
        // position
        Fragment fragment = null;

        Class fragmentClass;
        switch(menuItem.getItemId()) {
            case R.id.menuitem_home:
                fragmentClass = AddItemFragment.class;
                break;
            case R.id.menuitem_transaction:
                fragmentClass = TransactionFragment.class;
                break;
//            case R.id.menuitem_history:
//                fragmentClass = ThirdFragment.class;
//                break;
            default:
                fragmentClass = AddItemFragment.class;
        }

        try {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Insert the fragment by replacing any existing fragment
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.fragment_container, fragment).commit();

        // Highlight the selected item, update the title, and close the drawer
        // Highlight the selected item has been done by NavigationView
        // menuItem.setChecked(true);
        if(menuItem.getItemId() == R.id.menuitem_home)
            setTitle("BorroWise");
        else
            setTitle(menuItem.getTitle());
        drawer_layout.closeDrawers();
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
    }


}
