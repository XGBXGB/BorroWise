package com.example.shayanetan.borrowise2.Activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.ContactsContract;

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

        Intent smsIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("sms:" + phoneNumber));
        intent.putExtra("sms_body", message);
        context.startActivity(smsIntent);

    }
}
