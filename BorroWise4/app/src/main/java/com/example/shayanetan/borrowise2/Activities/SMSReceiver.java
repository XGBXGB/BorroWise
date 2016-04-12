package com.example.shayanetan.borrowise2.Activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.ContactsContract;
import android.telephony.SmsManager;
import android.util.Log;

import com.example.shayanetan.borrowise2.Models.DatabaseOpenHelper;

/**
 * Created by ShayaneTan on 4/12/2016.
 */
public class SMSReceiver extends BroadcastReceiver {
    public static final String NUMBER = "Number";
    public static final String MESSAGE = "Message";

    private DatabaseOpenHelper dbHelper;

    @Override
    public void onReceive(Context context, Intent intent) {


        String phoneNumber = intent.getExtras().getString(NUMBER);
        String message = intent.getExtras().getString(MESSAGE);

        Log.v("CHECK SMS", "PHONE NUMBER: "+ phoneNumber + " MESSAGE: "+message);
//        SmsManager smsManager = SmsManager.getDefault();
//        smsManager.sendTextMessage(phoneNumber, null, message, null, null);

        Intent smsIntent = new Intent(Intent.ACTION_VIEW);
        smsIntent.setData(Uri.parse("sms:" + phoneNumber));
        smsIntent.putExtra("sms_body", message);
        context.startService(smsIntent);

    }
}
