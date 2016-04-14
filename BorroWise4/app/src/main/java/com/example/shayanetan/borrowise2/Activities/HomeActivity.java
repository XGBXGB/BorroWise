package com.example.shayanetan.borrowise2.Activities;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.shayanetan.borrowise2.Fragments.AddItemFragment;
import com.example.shayanetan.borrowise2.Fragments.AddAbstractFragment;
import com.example.shayanetan.borrowise2.Models.DatabaseOpenHelper;
import com.example.shayanetan.borrowise2.Models.Transaction;
import com.example.shayanetan.borrowise2.Models.User;
import com.example.shayanetan.borrowise2.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class HomeActivity extends BaseActivity implements AddAbstractFragment.OnFragmentInteractionListener {


    private AddItemFragment itemFragment;
    private DatabaseOpenHelper dbHelper;

    final static String SP_KEY_BORROW_TIME = "BORROWTIME";
    final static String SP_KEY_BORROW_DAYS = "BORROWDAYS";
    final static String SP_KEY_LEND_TIME = "LENDTIME";
    final static String SP_KEY_LEND_DAYS = "LENDDAYS";

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
        setItemAlarm((int)itemId, t.getDueDate(), t.getClassification(), t.getType());
    }

    public void setItemAlarm(int item_id, long end, String classification, String type){

        Toast.makeText(getBaseContext(), "TYPE: " + type, Toast.LENGTH_LONG);

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        String borrowDay = sp.getString(SP_KEY_BORROW_DAYS, null);
        String borrowTime = sp.getString(SP_KEY_BORROW_TIME, null);
        String lendDay = sp.getString(SP_KEY_LEND_DAYS, null);
        String lendTime = sp.getString(SP_KEY_LEND_TIME, null);

        if(type.equalsIgnoreCase(Transaction.BORROWED_ACTION)){
            //Create an intent to broadcast
            Log.v("TYPE", "TYPEEE: "+type);
            Intent intent = new Intent();
            intent.setClass(getBaseContext(), AlarmReceiver.class);//even receivers receive intents
            intent.putExtra(Transaction.COLUMN_ID, item_id);
            intent.putExtra(Transaction.COLUMN_CLASSIFICATION, classification);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(getBaseContext(), item_id, intent, PendingIntent.FLAG_CANCEL_CURRENT);

            Calendar alarm = Calendar.getInstance();
            alarm.setTimeInMillis(end);
            alarm.set(Calendar.HOUR_OF_DAY, 14);
            alarm.set(Calendar.MINUTE, 30);
            alarm.set(Calendar.SECOND, 0);

            String dateToday = new SimpleDateFormat("MM/dd/yyyy").format(new Date(Calendar.getInstance().getTimeInMillis()));
            String dueDate = new SimpleDateFormat("MM/dd/yyyy").format(new Date(end));


          if(!dateToday.equalsIgnoreCase(dueDate)) {
              Log.v("CHECKKK","CALENDAR: " + dateToday+ " DUE: "+dueDate);
              alarm.add(Calendar.DAY_OF_MONTH, -1);
          }

            AlarmManager alarmManager = (AlarmManager)getSystemService(Service.ALARM_SERVICE);
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, alarm.getTimeInMillis(), pendingIntent);


        }else if(type.equalsIgnoreCase(Transaction.LEND_ACTION)){
            Log.v("TYPE", "TYPEEE: " + type);
            Transaction tran = dbHelper.queryTransaction(item_id);
            User u = dbHelper.queryUser(tran.getUserID());
            Log.v("USERRR", "USERNAMEEE: " + u.getName() + " " + u.getContactInfo());

            Intent intent = new Intent(getBaseContext(), SMSReceiver.class);
            intent.putExtra(SMSReceiver.NUMBER,u.getContactInfo());
            intent.putExtra(SMSReceiver.MESSAGE, "BorroWise Trial");
            intent.putExtra(Transaction.COLUMN_ID, item_id);

            PendingIntent pendingIntent = PendingIntent.getBroadcast(getBaseContext(), item_id, intent, PendingIntent.FLAG_CANCEL_CURRENT);

            AlarmManager alarmManager = (AlarmManager)getSystemService(ALARM_SERVICE);

            Calendar smsAlarm = Calendar.getInstance();
            smsAlarm.setTimeInMillis(end);
            smsAlarm.set(Calendar.HOUR_OF_DAY, 14);
            smsAlarm.set(Calendar.MINUTE, 30);
            smsAlarm.set(Calendar.SECOND, 60);

            alarmManager.setExact(AlarmManager.RTC_WAKEUP, smsAlarm.getTimeInMillis(), pendingIntent);

        }


    }
}
