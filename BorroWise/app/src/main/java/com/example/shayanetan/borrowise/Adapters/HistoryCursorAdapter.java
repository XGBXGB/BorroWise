package com.example.shayanetan.borrowise.Adapters;

/**
 * Created by ShayaneTan on 3/11/2016.
 */

import android.content.Context;
import android.database.Cursor;
import android.media.Rating;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.shayanetan.borrowise.Models.ItemTransaction;
import com.example.shayanetan.borrowise.Models.Transaction;
import com.example.shayanetan.borrowise.Models.User;
import com.example.shayanetan.borrowise.R;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by ShayaneTan on 3/11/2016.
 */
public class HistoryCursorAdapter extends CursorRecyclerViewAdapter<RecyclerView.ViewHolder> {

    private static final int TYPE_ITEM = 1;
    private static final int TYPE_MONEY = 2;
    private OnClickListener mOnClickListener;

    public HistoryCursorAdapter(Context context, Cursor cursor) {
        super(context, cursor);
    }



    @Override
    public int getItemViewType(int position) {
        Cursor itemCursor = super.getCursor();
        itemCursor.moveToPosition(position);
        if(itemCursor.getString(itemCursor.getColumnIndex(Transaction.COLUMN_CLASSIFICATION)).equalsIgnoreCase("item"))
            return TYPE_ITEM;
        else
            return TYPE_MONEY;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, Cursor cursor) {
        String name = cursor.getString(cursor.getColumnIndex(User.COLUMN_NAME));
        String dueDate = parseMillisToDate(cursor.getLong(cursor.getColumnIndex(Transaction.COLUMN_DUE_DATE)));
        String returnDate = parseMillisToDate(cursor.getLong(cursor.getColumnIndex(Transaction.COLUMN_RETURN_DATE)));
        String startDate = parseMillisToDate(cursor.getLong(cursor.getColumnIndex(Transaction.COLUMN_START_DATE)));
        double rating = cursor.getDouble(cursor.getColumnIndex(Transaction.COLUMN_RATE));
        String transactionAttribute1 = cursor.getString(cursor.getColumnIndex("Attribute1"));
        switch (viewHolder.getItemViewType()) {
            case TYPE_ITEM:
                String status = cursor.getString(cursor.getColumnIndex(Transaction.COLUMN_STATUS));
                ((BorrowedItemViewHolder)viewHolder).tv_Haccount_item.setText(name);
                ((BorrowedItemViewHolder)viewHolder).tv_Hduedateitem_val.setText(dueDate);
                ((BorrowedItemViewHolder)viewHolder).tv_Hitemname.setText(transactionAttribute1);
                ((BorrowedItemViewHolder)viewHolder).tv_Hretdateitem_val.setText(returnDate);
                ((BorrowedItemViewHolder)viewHolder).tv_Hstartdateitem_val.setText(startDate);
                ((BorrowedItemViewHolder)viewHolder).tv_Hstatusitem_val.setText(status);
                ((BorrowedItemViewHolder)viewHolder).rb_Hratingitem.setRating((float)rating);
                break;

            case TYPE_MONEY:
                ((BorrowedMoneyViewHolder)viewHolder).tv_Haccount_money.setText(name);
                ((BorrowedMoneyViewHolder)viewHolder).tv_Hduedatemoney_val.setText(dueDate);
                ((BorrowedMoneyViewHolder)viewHolder).tv_Hstartdatemoney_val.setText(startDate);
                ((BorrowedMoneyViewHolder)viewHolder).tv_Hretdatemoney_val.setText(returnDate);
                ((BorrowedMoneyViewHolder)viewHolder).tv_Hamount.setText(transactionAttribute1);
                ((BorrowedMoneyViewHolder)viewHolder).rb_Hratingmoney.setRating((float)rating);
                break;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(viewType == TYPE_ITEM){
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_history, parent, false); //same size as parent but not binded to the parent
            return new BorrowedItemViewHolder(v);
        }else if(viewType == TYPE_MONEY){
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_money_history, parent, false); //same size as parent but not binded to the parent
            return new BorrowedMoneyViewHolder(v);
        }

        return null;
    }



    /*********************************************
     * ITEM VIEW HOLDER
     *********************************************/
    public class BorrowedItemViewHolder extends RecyclerView.ViewHolder {
        // TODO


        TextView tv_Haccount_item, tv_Hitemname, tv_Hstartdateitem_val, tv_Hduedateitem_val, tv_Hretdateitem_val, tv_Hstatusitem_val;
        ImageView img_Hitem;

        View item_container;
        RatingBar rb_Hratingitem;
        public BorrowedItemViewHolder(View itemView) {

            super(itemView);
            tv_Haccount_item = (TextView) itemView.findViewById(R.id.tv_Haccount_item);
            tv_Hitemname = (TextView) itemView.findViewById(R.id.tv_Hitemname);
            tv_Hstartdateitem_val = (TextView) itemView.findViewById(R.id.tv_Hstartdateitem_val);
            tv_Hduedateitem_val = (TextView) itemView.findViewById(R.id.tv_Hduedateitem_val);
            tv_Hretdateitem_val = (TextView) itemView.findViewById(R.id.tv_Hretdateitem_val);
            tv_Hstatusitem_val = (TextView) itemView.findViewById(R.id.tv_Hstatusitem_val);
            rb_Hratingitem = (RatingBar) itemView.findViewById(R.id.rb_Hratingitem);

            img_Hitem = (ImageView) itemView.findViewById(R.id.img_item);
            item_container = itemView.findViewById(R.id.item_container);
        }

    }

    /*********************************************
     * MONEY VIEW HOLDER
     *********************************************/
    public class BorrowedMoneyViewHolder extends RecyclerView.ViewHolder{
        // TODO

        TextView tv_Haccount_money, tv_Hamount, tv_Hstartdatemoney_val,tv_Hduedatemoney_val,tv_Hretdatemoney_val;
        RatingBar rb_Hratingmoney;
        View money_container;

        public BorrowedMoneyViewHolder(View itemView) {
            super(itemView);
            tv_Haccount_money = (TextView) itemView.findViewById(R.id.tv_Haccount_money);
            tv_Hamount = (TextView) itemView.findViewById(R.id.tv_Hamount);
            tv_Hstartdatemoney_val = (TextView) itemView.findViewById(R.id.tv_Hstartdatemoney_val);
            tv_Hduedatemoney_val = (TextView) itemView.findViewById(R.id.tv_Hduedatemoney_val);
            tv_Hretdatemoney_val = (TextView) itemView.findViewById(R.id.tv_Hretdatemoney_val);
            rb_Hratingmoney = (RatingBar) itemView.findViewById(R.id.rb_Hratingmoney);
            money_container = itemView.findViewById(R.id.money_container);
        }
    }

    public void setmOnClickListener(OnClickListener m){
        this.mOnClickListener = m;
    }

    public String parseMillisToDate(long millis){
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
        Date resultdate = new Date(millis);
        return sdf.format(resultdate);
    }
}

