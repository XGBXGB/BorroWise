package com.example.shayanetan.borrowise.Adapters;

import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by G301 on 2/24/2016.
 */
public class ContactsCursorAdapter extends CursorAdapter {


    public ContactsCursorAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {

        View v = LayoutInflater.from(parent.getContext()).inflate(com.example.shayanetan.borrowise.R.layout.spinner_item, parent, false);
        return v;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        LinearLayout container = (LinearLayout) view.findViewById(com.example.shayanetan.borrowise.R.id.container);
        TextView tv_name = (TextView) view.findViewById(com.example.shayanetan.borrowise.R.id.tv_name);
        TextView tv_number = (TextView) view.findViewById(com.example.shayanetan.borrowise.R.id.tv_number);

        String name = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
        String number = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
        String id = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone._ID));
        tv_name.setText(name);
        tv_number.setText(number);
        container.setTag(id);
    }






}
