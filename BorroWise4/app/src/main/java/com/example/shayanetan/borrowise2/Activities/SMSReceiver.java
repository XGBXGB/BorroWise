package com.example.shayanetan.borrowise2.Activities;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.telephony.SmsManager;
import android.util.Log;
import android.widget.Toast;

import com.example.shayanetan.borrowise2.Models.DatabaseOpenHelper;
import com.example.shayanetan.borrowise2.Models.Transaction;

/**
 * Created by ShayaneTan on 4/12/2016.
 */
public class SMSReceiver extends BroadcastReceiver {
    public static final String NUMBER = "Number";
    public static final String MESSAGE = "Message";

    public static final String SENT = "SMS_SENT";
    public static final String DELIVERED = "SMS_DELIVERED";

    private DatabaseOpenHelper dbHelper;

    @Override
    public void onReceive(final Context context, Intent intent) {


        String phoneNumber = intent.getExtras().getString(NUMBER);
        String message = intent.getExtras().getString(MESSAGE);
        int item_id = intent.getExtras().getInt(Transaction.COLUMN_ID);

        Log.v("CHECK SMS", "PHONE NUMBER: "+ phoneNumber + " MESSAGE: "+message);

        PendingIntent sentPI = PendingIntent.getBroadcast(context,item_id,new Intent(SENT),PendingIntent.FLAG_UPDATE_CURRENT);
        PendingIntent deliveredPI = PendingIntent.getBroadcast(context, item_id, new Intent(DELIVERED), PendingIntent.FLAG_UPDATE_CURRENT);

        SmsManager smsManager = SmsManager.getDefault();
        smsManager.sendTextMessage(phoneNumber, null, message, null,null);

    }
}
