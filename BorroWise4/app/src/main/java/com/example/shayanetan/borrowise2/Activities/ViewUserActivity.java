package com.example.shayanetan.borrowise2.Activities;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;

import com.example.shayanetan.borrowise2.Adapters.UsersCursorAdapter;
import com.example.shayanetan.borrowise2.Models.DatabaseOpenHelper;
import com.example.shayanetan.borrowise2.Models.User;
import com.example.shayanetan.borrowise2.R;

public class ViewUserActivity extends BaseActivity {

    private DatabaseOpenHelper dbHelper;
    private RecyclerView recView;
    private UsersCursorAdapter uca;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_user);

        dbHelper = DatabaseOpenHelper.getInstance(getBaseContext());
        uca = new UsersCursorAdapter(getBaseContext(),null);
        uca.setmOnItemClickListener(new UsersCursorAdapter.onItemClickListener() {
            @Override
            public void onItemClick(int id) {
                Intent userProf = new Intent();
                userProf.setClass(getBaseContext(), ViewUserProfileActivity.class);
                userProf.putExtra(User.COLUMN_ID, id);
                startActivity(userProf);
            }
        });

        recView = (RecyclerView) findViewById(R.id.rec_view_user);
        recView.setAdapter(uca);
        recView.setLayoutManager(new LinearLayoutManager(getBaseContext()));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_view_transaction, menu);
        return true;
    }

    @Override
    protected void onResume() {
        //activity cycle
        //the update function of the rec_view is in the onCreate()
        //but after calling the addNoteAtivity it will proceed to onResume to place t update code statements to onResume();
        super.onResume();
        Cursor cursor = dbHelper.queryAllUsersC();
        uca.swapCursor(cursor); //change data source
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

}
