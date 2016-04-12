package com.example.shayanetan.borrowise2.Activities;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

import com.example.shayanetan.borrowise2.Models.DatabaseOpenHelper;
import com.example.shayanetan.borrowise2.Models.ItemTransaction;
import com.example.shayanetan.borrowise2.Models.MoneyTransaction;
import com.example.shayanetan.borrowise2.Models.Transaction;
import com.example.shayanetan.borrowise2.R;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by ShayaneTan on 4/12/2016.
 */
public class AlarmReceiver extends BroadcastReceiver {

    private final static int NOTIF_ID = 101;
    private final static int MA_PENDING_INTENT = 0;
    private final static int SA_PENDING_INTENT = 1;
    private static int COUNT = 0;

    private DatabaseOpenHelper dbHelper;

    @Override
    public void onReceive(Context context, Intent intent2) {


        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.
        //go back to main activity
        Intent intent = new Intent();
        intent.setClass(context, HomeActivity.class);

        //also have a request code because you can have two pending intents with diffirent activity
        PendingIntent pendingIntent = PendingIntent.getActivity(context,MA_PENDING_INTENT, intent, PendingIntent.FLAG_UPDATE_CURRENT);//flag_update_current depends on the extras
        //note: flag_update_current won't allow android to create a new pending intent

        //go back to second activity
        Intent secondact_intent= new Intent();

        intent.setClass(context, ViewTransactionActivity.class);
        //also have a request code because you can have two pending intents with diffirent activity
        PendingIntent second_pendingIntent = PendingIntent.getActivity(context,SA_PENDING_INTENT, secondact_intent, PendingIntent.FLAG_UPDATE_CURRENT);//flag_update_current depends on the extras
        //note: flag_update_current won't allow android to create a new pending intent

        //create a notification
        NotificationCompat.Builder notif_builder = null;

        dbHelper = DatabaseOpenHelper.getInstance(context);
        int transactionId = Integer.parseInt(intent2.getExtras().getString(Transaction.COLUMN_ID));
        String tranType = intent2.getExtras().getString(Transaction.COLUMN_CLASSIFICATION);

        Toast.makeText(context,"TRAN TYPE: "+ tranType,Toast.LENGTH_LONG);

        if(tranType.equalsIgnoreCase(Transaction.ITEM_TYPE)){
            ItemTransaction it = (ItemTransaction) dbHelper.queryTransaction(transactionId);
            String dateString = new SimpleDateFormat("MM/dd/yyyy").format(new Date(it.getReturnDate()));
            notif_builder = new NotificationCompat.Builder(context)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle("BorroWise pending item")
                        .setContentText("Hurry!" + it.getName() + " will already be due on " + dateString)
                        .setContentIntent(pendingIntent)
                        .setTicker("Ticker")
                        .setNumber(COUNT)
                        .addAction(R.mipmap.ic_launcher, "Jump to Second",second_pendingIntent);
        }else if(tranType.equalsIgnoreCase(Transaction.MONEY_TYPE)){
            MoneyTransaction mt = (MoneyTransaction) dbHelper.queryTransaction(transactionId);
            String dateString = new SimpleDateFormat("MM/dd/yyyy").format(new Date(mt.getReturnDate()));
            notif_builder = new NotificationCompat.Builder(context)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentTitle("BorroWise pending item")
                    .setContentText("Hurry!" + mt.getTotalAmountDue() + " will already be due on " + dateString)
                    .setContentIntent(pendingIntent)
                    .setTicker("Ticker")
                    .setNumber(COUNT)
                    .addAction(R.mipmap.ic_launcher, "Jump to Second",second_pendingIntent);
        }

        if(notif_builder != null) {
            //submit notification to the notification manager
            NotificationManager notif_manager = (NotificationManager) context.getSystemService(Service.NOTIFICATION_SERVICE);
            notif_manager.notify(NOTIF_ID, notif_builder.build());
            COUNT++;
        }
    }
}
