package com.example.shayanetan.borrowise2.Adapters;

/**
 * Created by ShayaneTan on 3/11/2016.
 */

import android.content.ClipData;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shayanetan.borrowise2.Models.ItemTransaction;
import com.example.shayanetan.borrowise2.Models.Transaction;
import com.example.shayanetan.borrowise2.Models.User;
import com.example.shayanetan.borrowise2.R;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by ShayaneTan on 3/11/2016.
 */
public class HistoryCursorAdapter extends CursorRecyclerViewAdapter<RecyclerView.ViewHolder> {

    public  static final int TYPE_ITEM = 1;
    public  static final int TYPE_MONEY = 2;
    public  static final String TYPE_BORROWED = "borrowed";
    public  static final String TYPE_LEND= "lend";

    private String viewTypeFinal;
    private OnButtonClickListener mOnClickListener;

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
    public void onBindViewHolder(final RecyclerView.ViewHolder viewHolder, final Cursor cursor) {
        String name = cursor.getString(cursor.getColumnIndex(User.COLUMN_NAME));
        String returnDate="";
        String dueDate = parseMillisToDate(cursor.getLong(cursor.getColumnIndex(Transaction.COLUMN_DUE_DATE)));
        String startDate = parseMillisToDate(cursor.getLong(cursor.getColumnIndex(Transaction.COLUMN_START_DATE)));
        double rating = cursor.getDouble(cursor.getColumnIndex(Transaction.COLUMN_RATE));
        String transactionAttribute1 = cursor.getString(cursor.getColumnIndex("Attribute1"));
        String transactionAttribute3 = cursor.getString(cursor.getColumnIndex("Attribute3"));
        String type = cursor.getString(cursor.getColumnIndex(Transaction.COLUMN_TYPE));
        int statusInteger = cursor.getInt(cursor.getColumnIndex(Transaction.COLUMN_STATUS));


        if(type.equalsIgnoreCase("borrow"))
            viewTypeFinal = TYPE_BORROWED;
        else
            viewTypeFinal = TYPE_LEND;

        if( statusInteger == -1 || statusInteger == 0) {
            returnDate = "N/A";
        }
        else{
            returnDate = parseMillisToDate(cursor.getLong(cursor.getColumnIndex(Transaction.COLUMN_RETURN_DATE)));
        }

        String status = cursor.getString(cursor.getColumnIndex(Transaction.COLUMN_STATUS));
        String statusFinal = "";
        switch (status){
            case "1": statusFinal = "Returned"; break;
            case "-1": statusFinal = "Lost"; break;
            case "0": statusFinal = "Ongoing"; break;
        }

        switch (viewHolder.getItemViewType()) {
            case TYPE_ITEM:

                File imgFile = new  File(transactionAttribute3);
                if(imgFile.exists()){
                  //  ItemTransaction.bmpOptions.inSampleSize = 8;
                    Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                    ((BorrowedItemViewHolder)viewHolder).img_Hitem.setImageBitmap(myBitmap);
                    ((BorrowedItemViewHolder)viewHolder).img_Hitem.setScaleType(ImageView.ScaleType.CENTER_CROP);
                }
                ((BorrowedItemViewHolder)viewHolder).tv_Haccount_item.setText(name);
                ((BorrowedItemViewHolder)viewHolder).tv_Hduedateitem_val.setText(dueDate);
                ((BorrowedItemViewHolder)viewHolder).tv_Hitemname.setText(transactionAttribute1);
                ((BorrowedItemViewHolder)viewHolder).tv_Hretdateitem_val.setText(returnDate);
                ((BorrowedItemViewHolder)viewHolder).tv_Hstartdateitem_val.setText(startDate);

                ((BorrowedItemViewHolder)viewHolder).tv_Hstatusitem_val.setText(statusFinal);
                ((BorrowedItemViewHolder)viewHolder).rb_Hratingitem.setRating((float) rating);

                ((BorrowedItemViewHolder)viewHolder).item_container.setTag(cursor.getInt(cursor.getColumnIndex(Transaction.COLUMN_ID)));
                ((BorrowedItemViewHolder)viewHolder).item_container.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        mOnClickListener.onButtonClick(Integer.parseInt(v.getTag().toString()), viewTypeFinal, Transaction.ITEM_TYPE);
                        return true;
                    }
                });
                break;

            case TYPE_MONEY:
                ((BorrowedMoneyViewHolder)viewHolder).tv_Haccount_money.setText(name);
                ((BorrowedMoneyViewHolder)viewHolder).tv_Hduedatemoney_val.setText(dueDate);
                ((BorrowedMoneyViewHolder)viewHolder).tv_Hstartdatemoney_val.setText(startDate);
                ((BorrowedMoneyViewHolder)viewHolder).tv_Hretdatemoney_val.setText(returnDate);
                ((BorrowedMoneyViewHolder)viewHolder).tv_Hstatusmoney_val.setText(statusFinal);
                ((BorrowedMoneyViewHolder)viewHolder).tv_Hamount.setText(transactionAttribute1);
                ((BorrowedMoneyViewHolder)viewHolder).rb_Hratingmoney.setRating((float) rating);
                ((BorrowedMoneyViewHolder)viewHolder).money_container.setTag(cursor.getInt(cursor.getColumnIndex(Transaction.COLUMN_ID)));
                ((BorrowedMoneyViewHolder)viewHolder).money_container.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {

                        mOnClickListener.onButtonClick(Integer.parseInt(v.getTag().toString()), viewTypeFinal, Transaction.MONEY_TYPE);
                        return true;
                    }
                });
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

            img_Hitem = (ImageView) itemView.findViewById(R.id.img_Hitem);
            item_container = itemView.findViewById(R.id.Hitem_container);
        }

    }

    /*********************************************
     * MONEY VIEW HOLDER
     *********************************************/
    public class BorrowedMoneyViewHolder extends RecyclerView.ViewHolder{
        // TODO

        TextView tv_Haccount_money, tv_Hamount, tv_Hstartdatemoney_val,tv_Hduedatemoney_val,tv_Hretdatemoney_val, tv_Hstatusmoney_val;
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
            money_container = itemView.findViewById(R.id.Hmoney_container);
            tv_Hstatusmoney_val = (TextView) itemView.findViewById(R.id.tv_Hstatusmoney_val);
        }
    }

    public void setmOnLongClickListener(OnButtonClickListener m){
        this.mOnClickListener = m;
    }

    public interface OnButtonClickListener{
        public void onButtonClick(int id, String type, String classification);
    }
    public String parseMillisToDate(long millis){
        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy");
        Date resultdate = new Date(millis);
        return sdf.format(resultdate);
    }
}

