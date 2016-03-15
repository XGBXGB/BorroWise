package com.example.shayanetan.borrowise2.Activities;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.example.shayanetan.borrowise2.Fragments.AddItemFragment;
import com.example.shayanetan.borrowise2.Models.DatabaseOpenHelper;
import com.example.shayanetan.borrowise2.Models.Transaction;
import com.example.shayanetan.borrowise2.Models.User;
import com.example.shayanetan.borrowise2.R;

public class HomeActivity extends BaseActivity implements AddItemFragment.OnFragmentInteractionListener {


    private AddItemFragment itemFragment;
    private DatabaseOpenHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        dbHelper = DatabaseOpenHelper.getInstance(getBaseContext());

        itemFragment = new AddItemFragment();
        itemFragment.setOnFragmentInteractionListener(this);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, itemFragment)
                .commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public int onAddNewUser(String name, String contact_info) {
        int id = dbHelper.checkUserIfExists(name, contact_info);
        if(id == -1){
            id = (int) dbHelper.insertUser(new User(name, contact_info, 0));
        }else{
            System.out.println("USER doesnt EXISTS!");
        }
        return id;
    }

    @Override
    public void onAddTransactions(Transaction t) {
        dbHelper.insertTransaction(t);
    }
}
