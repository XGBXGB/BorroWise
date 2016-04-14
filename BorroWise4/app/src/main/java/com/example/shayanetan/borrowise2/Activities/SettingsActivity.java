package com.example.shayanetan.borrowise2.Activities;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.shayanetan.borrowise2.Adapters.UsersCursorAdapter;
import com.example.shayanetan.borrowise2.Fragments.TimePickerFragment;
import com.example.shayanetan.borrowise2.Models.DatabaseOpenHelper;
import com.example.shayanetan.borrowise2.Models.ItemTransaction;
import com.example.shayanetan.borrowise2.Models.MoneyTransaction;
import com.example.shayanetan.borrowise2.Models.Transaction;
import com.example.shayanetan.borrowise2.Models.User;
import com.example.shayanetan.borrowise2.R;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class SettingsActivity extends BaseActivity {

    private EditText et_borrowDays, et_lendDays;
    private Button btn_borrowTime, btn_lendTime, btn_save, btn_cancel;
    private DatabaseOpenHelper dbHelper;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        dbHelper = DatabaseOpenHelper.getInstance(getBaseContext());
        btn_borrowTime = (Button) findViewById(R.id.btn_borrowTime);
        btn_lendTime = (Button) findViewById(R.id.btn_lendTime);
        et_borrowDays = (EditText) findViewById(R.id.et_borrowDays);
        et_lendDays = (EditText) findViewById(R.id.et_lendDays);
        btn_save = (Button) findViewById(R.id.btn_save);
        btn_cancel = (Button) findViewById(R.id.btn_cancel);

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        String borrowDay = sp.getString(HomeActivity.SP_KEY_BORROW_DAYS, null);
        String borrowTime = sp.getString(HomeActivity.SP_KEY_BORROW_TIME, null);
        String lendDay = sp.getString(HomeActivity.SP_KEY_LEND_DAYS, null);
        String lendTime = sp.getString(HomeActivity.SP_KEY_LEND_TIME, null);

        if(borrowTime != null)
            btn_borrowTime.setText(borrowTime);
        if(lendTime != null)
            btn_lendTime.setText(lendTime);
        if(borrowDay != null)
            et_borrowDays.setText(borrowDay);
        if(lendDay != null)
            et_lendDays.setText(lendDay);


            btn_borrowTime.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    TimePickerFragment timePickerFragment = new TimePickerFragment();
                    timePickerFragment.setButtonDateSelector(btn_borrowTime);
                    timePickerFragment.show(getSupportFragmentManager(), "BorrowTimePicker");
                }
            });


        btn_lendTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerFragment timePickerFragment = new TimePickerFragment();
                timePickerFragment.setButtonDateSelector(btn_lendTime);
                timePickerFragment.show(getSupportFragmentManager(), "LendTimePicker");
            }
        });

        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
                SharedPreferences.Editor spEditor = sp.edit();
                if(!et_borrowDays.getText().toString().isEmpty())
                    spEditor.putString(HomeActivity.SP_KEY_BORROW_DAYS, et_borrowDays.getText().toString());
                if(!btn_borrowTime.getText().toString().isEmpty())
                    spEditor.putString(HomeActivity.SP_KEY_BORROW_TIME, btn_borrowTime.getText().toString());
                if(!et_lendDays.getText().toString().isEmpty())
                    spEditor.putString(HomeActivity.SP_KEY_LEND_DAYS, et_lendDays.getText().toString());
                if(!btn_lendTime.getText().toString().isEmpty())
                    spEditor.putString(HomeActivity.SP_KEY_LEND_TIME, btn_lendTime.getText().toString());


                spEditor.commit();
                updateAlarms();
                Toast.makeText(getBaseContext(), "Changes Saved! btime: "+btn_borrowTime.getText().toString(), Toast.LENGTH_SHORT).show();
                finish();
            }
        });

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
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

    public void updateAlarms(){
        Cursor cursor = dbHelper.querryAllTransactionsJoinUser("0");

        if(cursor.moveToFirst()){
            do {
                int id = cursor.getInt(cursor.getColumnIndex(Transaction.COLUMN_ID));
                String classification = cursor.getString(cursor.getColumnIndex(Transaction.COLUMN_CLASSIFICATION));
                String type = cursor.getString(cursor.getColumnIndex(Transaction.COLUMN_TYPE));
                long dueDate = cursor.getLong(cursor.getColumnIndex(Transaction.COLUMN_DUE_DATE));

                setItemAlarm(id, dueDate, classification, type);
            }while(cursor.moveToNext());
        }
    }

    public void setItemAlarm(int item_id, long end, String classification, String type){

        Toast.makeText(getBaseContext(), "TYPE: " + type, Toast.LENGTH_LONG);

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        String borrowDay = sp.getString(HomeActivity.SP_KEY_BORROW_DAYS, null);
        String borrowTime = sp.getString(HomeActivity.SP_KEY_BORROW_TIME, null);
        String lendDay = sp.getString(HomeActivity.SP_KEY_LEND_DAYS, null);
        String lendTime = sp.getString(HomeActivity.SP_KEY_LEND_TIME, null);

        if(type.equalsIgnoreCase(Transaction.BORROWED_ACTION)){
            //Create an intent to broadcast
            Log.v("TYPE", "TYPEEE: " + type);
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
                        + " Hi " + u.getName() + "! Please be reminded to return the borrowed item " + it.getName()+ "today!";
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
                Log.v("In lend: CHECKKK", "CALENDAR: " + dateToday + " DUE: " + dueDate);
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
