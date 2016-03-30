package com.example.shayanetan.borrowise2.Adapters;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.shayanetan.borrowise2.R;

/**
 * Created by G301 on 2/24/2016.
 */
public class ContactsCursorAdapter extends CursorAdapter implements Filterable{


    private ContentResolver mContent;

    public ContactsCursorAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
        mContent = context.getContentResolver();
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.spinner_item, parent, false);
        return v;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        LinearLayout container = (LinearLayout) view.findViewById(R.id.container);
        TextView tv_name = (TextView) view.findViewById(R.id.tv_name);
        TextView tv_number = (TextView) view.findViewById(R.id.tv_number);

        String name = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
        String number = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
        String id = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone._ID));
        tv_name.setText(name);
        tv_number.setText(number);
        container.setTag(id);
//        container.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mOnItemClickListener.onItemClick(Integer.parseInt(v.getTag().toString()));
//            }
//        });
    }

    @Override
    public String convertToString(Cursor cursor) {
        int nameCol = cursor.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);
        String name = cursor.getString(nameCol);
        return name;
    }

    @Override
    public Cursor runQueryOnBackgroundThread(CharSequence constraint) {
        // this is how you query for suggestions
        // notice it is just a StringBuilder building the WHERE clause of a cursor which is the used to query for results
        if (constraint==null)
            return null;

        if (getFilterQueryProvider() != null) { return getFilterQueryProvider().runQuery(constraint); }

        String[] projection = new String[] {
                ContactsContract.CommonDataKinds.Phone._ID,
                ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
                ContactsContract.CommonDataKinds.Phone.TYPE,
                ContactsContract.CommonDataKinds.Phone.NUMBER};

        return mContent.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, projection,
                "UPPER(" + ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + ") LIKE '%" + constraint.toString().toUpperCase() + "%' or UPPER(" + ContactsContract.CommonDataKinds.Phone.NUMBER + ") LIKE '%" + constraint.toString().toUpperCase() + "%' ", null,
                ContactsContract.Contacts.DISPLAY_NAME + " COLLATE LOCALIZED ASC");
    }






}
