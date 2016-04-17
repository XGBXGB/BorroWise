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
import com.example.shayanetan.borrowise2.Models.ItemTransaction;
import com.example.shayanetan.borrowise2.Models.MoneyTransaction;
import com.example.shayanetan.borrowise2.Models.Transaction;
import com.example.shayanetan.borrowise2.Models.User;
import com.example.shayanetan.borrowise2.R;

import java.text.DateFormat;
import java.text.ParseException;
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

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//
//        int id = item.getItemId();
//
////        if (id == R.id.action_settings) {
////            return true;
////        }
//
//        return super.onOptionsItemSelected(item);
//    }

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
        Log.v("NEW TRANS ID!!!!!! ", "" + itemId);
     //   Toast.makeText(getBaseContext(), "NEW TRANS ID!!!!!! " +itemId, Toast.LENGTH_LONG).show();
        setItemAlarm((int) itemId, t.getDueDate(), t.getClassification(), t.getType());
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
            PendingIntent pendingIntent = PendingIntent.getBroadcast(getBaseContext(), item_id, intent, PendingIntent.FLAG_UPDATE_CURRENT);




            Calendar alarm = Calendar.getInstance();
            alarm.setTimeInMillis(end);
            Log.v("BEFORE ALARM:", "" + alarm.getTimeInMillis());
            if(borrowTime != null) {
                DateFormat sdf = new SimpleDateFormat("HH:mm");
                Date date = null;
                try {
                    date = sdf.parse(borrowTime);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(date);

                alarm.set(Calendar.HOUR_OF_DAY, calendar.get(Calendar.HOUR_OF_DAY));
                Log.v("cHour: ", calendar.get(Calendar.HOUR_OF_DAY) + "");
                alarm.set(Calendar.MINUTE, calendar.get(Calendar.MINUTE));
                Log.v("cMinute: ", calendar.get(Calendar.MINUTE) + "");
                alarm.set(Calendar.SECOND, 0);
                Log.v("AFTER ALARM:", ""+alarm.getTimeInMillis());
            }else{
                alarm.set(Calendar.HOUR_OF_DAY, 10);
                alarm.set(Calendar.MINUTE,0);
                alarm.set(Calendar.SECOND, 0);
            }

            String dateToday = new SimpleDateFormat("MM/dd/yyyy").format(new Date(Calendar.getInstance().getTimeInMillis()));
            String dueDate = new SimpleDateFormat("MM/dd/yyyy").format(new Date(end));


          if(!dateToday.equalsIgnoreCase(dueDate)) {
              Log.v("CHECKKK", "CALENDAR: " + dateToday + " DUE: " + dueDate);
              if(borrowDay != null) {
                  int duration = Integer.parseInt(borrowDay);
                  Log.v("inside if"," duration: "+duration);
                  for(int i=duration; i>0; i--){
                      alarm.add(Calendar.DAY_OF_MONTH, -i);
                  }
              }else {
                  Log.v("inside if"," duration: else");
                  alarm.add(Calendar.DAY_OF_MONTH, -1);
              }
          }

            AlarmManager alarmManager = (AlarmManager)getSystemService(Service.ALARM_SERVICE);
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, alarm.getTimeInMillis(), pendingIntent);


        }else if(type.equalsIgnoreCase(Transaction.LEND_ACTION)){
            User u = null;

            String message = "";

            if(classification.equalsIgnoreCase(Transaction.ITEM_TYPE)) {
                ItemTransaction it = (ItemTransaction) dbHelper.queryTransaction(item_id);
                u = dbHelper.queryUser(it.getUserID());

                message = "[BorroWise Reminder] \n"
                        + " Hi " + u.getName() + "! Please be reminded to return the borrowed item " + it.getName()+ " today!";
            }else{
                MoneyTransaction mt = (MoneyTransaction) dbHelper.queryTransaction(item_id);
                u = dbHelper.queryUser(mt.getUserID());

                message = "[BorroWise Reminder] \n"
                        + " Hi " + u.getName() + "! Please be reminded to return the borrowed money PHP " + mt.getAmountDeficit()+ " today!";
            }

            Intent intent = new Intent(getBaseContext(), SMSReceiver.class);
            intent.putExtra(SMSReceiver.NUMBER,u.getContactInfo());
            intent.putExtra(SMSReceiver.MESSAGE, message);
            intent.putExtra(Transaction.COLUMN_ID, item_id);

            PendingIntent pendingIntent = PendingIntent.getBroadcast(getBaseContext(), item_id, intent, PendingIntent.FLAG_UPDATE_CURRENT);

            AlarmManager alarmManager = (AlarmManager)getSystemService(ALARM_SERVICE);

            Calendar smsAlarm = Calendar.getInstance();
            smsAlarm.setTimeInMillis(end);

            if(lendTime != null) {
                DateFormat sdf = new SimpleDateFormat("HH:mm");
                Date date = null;
                try {
                    date = sdf.parse(lendTime);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(date);

                smsAlarm.set(Calendar.HOUR_OF_DAY, calendar.get(Calendar.HOUR_OF_DAY));
                smsAlarm.set(Calendar.MINUTE, calendar.get(Calendar.MINUTE));
                smsAlarm.set(Calendar.SECOND, 0);
            }else{
                smsAlarm.set(Calendar.HOUR_OF_DAY, 10);
                smsAlarm.set(Calendar.MINUTE,0);
                smsAlarm.set(Calendar.SECOND, 0);
            }


            String dateToday = new SimpleDateFormat("MM/dd/yyyy").format(new Date(Calendar.getInstance().getTimeInMillis()));
            String dueDate = new SimpleDateFormat("MM/dd/yyyy").format(new Date(end));


            if(!dateToday.equalsIgnoreCase(dueDate)) {
              //  Log.v("In lend: CHECKKK", "CALENDAR: " + dateToday + " DUE: " + dueDate);
                if(lendDay != null) {
                    int duration = Integer.parseInt(lendDay);
                    for(int i=duration; i>0; i--){
                        smsAlarm.add(Calendar.DAY_OF_MONTH, -i);
                    }
                }else {
                    smsAlarm.add(Calendar.DAY_OF_MONTH, -1);
                }
            }

            alarmManager.setExact(AlarmManager.RTC_WAKEUP, smsAlarm.getTimeInMillis(), pendingIntent);

        }


    }
}
