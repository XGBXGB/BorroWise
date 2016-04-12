package com.example.shayanetan.borrowise2.Activities;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.example.shayanetan.borrowise2.Fragments.AddItemFragment;
import com.example.shayanetan.borrowise2.Fragments.AddAbstractFragment;
import com.example.shayanetan.borrowise2.Models.DatabaseOpenHelper;
import com.example.shayanetan.borrowise2.Models.Transaction;
import com.example.shayanetan.borrowise2.Models.User;
import com.example.shayanetan.borrowise2.R;

import java.util.Calendar;

public class HomeActivity extends BaseActivity implements AddAbstractFragment.OnFragmentInteractionListener {


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
       long itemId =  dbHelper.insertTransaction(t);
        setItemAlarm((int)itemId, t.getReturnDate(), t.getClassification(), t.getType());
    }

    public void setItemAlarm(int item_id, long end, String classification, String type){

        if(type.equalsIgnoreCase(Transaction.BORROWED_ACTION)){
            //Create an intent to broadcast
            Intent intent = new Intent();
            intent.setClass(getBaseContext(), AlarmReceiver.class);//even receivers receive intents
            intent.putExtra(Transaction.COLUMN_ID, item_id);
            intent.putExtra(Transaction.COLUMN_CLASSIFICATION, classification);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(getBaseContext(), item_id, intent, PendingIntent.FLAG_UPDATE_CURRENT);

            Calendar alarm = Calendar.getInstance();
            alarm.setTimeInMillis(end);
            alarm.set(Calendar.HOUR_OF_DAY, 9);
            alarm.set(Calendar.MINUTE, 7);

           // if()
            alarm.add(Calendar.DAY_OF_MONTH, -1);

            AlarmManager alarmManager = (AlarmManager)getSystemService(Service.ALARM_SERVICE);
            alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, alarm.getTimeInMillis(), pendingIntent);
        }else if(type.equalsIgnoreCase(Transaction.LEND_ACTION)){

            Transaction tran = dbHelper.queryTransaction(item_id);
            User u = dbHelper.queryUser(tran.getUserID());

            Intent intent = new Intent(getBaseContext(), SMSReceiver.class);
            intent.putExtra(SMSReceiver.NUMBER,u.getContactInfo());
            intent.putExtra(SMSReceiver.MESSAGE, "BorroWise Trial");

            PendingIntent pendingIntent = PendingIntent.getService(getBaseContext(), 0, intent, 0);

            AlarmManager alarmManager = (AlarmManager)getSystemService(ALARM_SERVICE);

            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(System.currentTimeMillis());
            calendar.add(Calendar.SECOND,20);

            alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
        }


    }
}
