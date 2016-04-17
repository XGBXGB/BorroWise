package com.example.shayanetan.borrowise2.Adapters;

/**
 * Created by ShayaneTan on 3/11/2016.
 */

import android.app.DialogFragment;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.shayanetan.borrowise2.Fragments.RatingDialogFragment;
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
public class TransactionsCursorAdapter extends CursorRecyclerViewAdapter<RecyclerView.ViewHolder>{
    public static final int BTN_TYPE_RETURN = 1;
    public static final int BTN_TYPE_LOST = 2;
    public static final int BTN_TYPE_PARTIAL = 3;

    public static final int TYPE_ITEM = 1;
    public static final int TYPE_MONEY = 2;
    public static final int TRAN_ID = 0;
    private OnButtonClickListener mOnClickListener;

    public TransactionsCursorAdapter(Context context, Cursor cursor) {
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
    public void onBindViewHolder(final RecyclerView.ViewHolder viewHolder, Cursor cursor) {

        String name = cursor.getString(cursor.getColumnIndex(User.COLUMN_NAME));
        int id = cursor.getInt(cursor.getColumnIndex(Transaction.COLUMN_ID));
        String dueDate = parseMillisToDate(cursor.getLong(cursor.getColumnIndex(Transaction.COLUMN_DUE_DATE)));
        String transactionAttribute1 = cursor.getString(cursor.getColumnIndex("Attribute1")); //item and description
        String transactionAttribute2 = cursor.getString(cursor.getColumnIndex("Attribute2"));
        String transactionAttribute3 = cursor.getString(cursor.getColumnIndex("Attribute3")); //path ng picture file

        switch (viewHolder.getItemViewType()) {
            case TYPE_ITEM:
                File imgFile = new  File(transactionAttribute3);
                if(imgFile.exists()){
                   // ItemTransaction.bmpOptions.inSampleSize = 8;
                    Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                    ((BorrowedItemViewHolder)viewHolder).img_item.setImageBitmap(myBitmap);
                    ((BorrowedItemViewHolder)viewHolder).img_item.setScaleType(ImageView.ScaleType.CENTER_CROP);

                }
                ((BorrowedItemViewHolder)viewHolder).item_container.setTag(R.id.key_entry_id, id);
                ((BorrowedItemViewHolder)viewHolder).item_container.setTag(R.id.key_entry_type, TYPE_ITEM);
                ((BorrowedItemViewHolder)viewHolder).tv_account_item.setText(name);
                ((BorrowedItemViewHolder)viewHolder).tv_duedate_val.setText(dueDate);
                ((BorrowedItemViewHolder)viewHolder).tv_itemname.setText(transactionAttribute1);
                ((BorrowedItemViewHolder)viewHolder).btn_returned.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        BorrowedItemViewHolder vh = (BorrowedItemViewHolder) viewHolder;

                        int tran_id = Integer.parseInt(vh.item_container.getTag(R.id.key_entry_id).toString());
                        int tran_type = Integer.parseInt(vh.item_container.getTag(R.id.key_entry_type).toString());
                        mOnClickListener.onButtonClick(tran_id, tran_type, BTN_TYPE_RETURN);
                    }
                });
                ((BorrowedItemViewHolder)viewHolder).btn_lost.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        BorrowedItemViewHolder vh = (BorrowedItemViewHolder) viewHolder;

                        int tran_id = Integer.parseInt(vh.item_container.getTag(R.id.key_entry_id).toString());
                        int tran_type = Integer.parseInt(vh.item_container.getTag(R.id.key_entry_type).toString());
                        mOnClickListener.onButtonClick(tran_id, tran_type, BTN_TYPE_LOST);
                    }
                });
                break;

            case TYPE_MONEY:
                ((BorrowedMoneyViewHolder)viewHolder).money_container.setTag(R.id.key_entry_id, id);
                ((BorrowedMoneyViewHolder)viewHolder).money_container.setTag(R.id.key_entry_type, TYPE_MONEY);
                ((BorrowedMoneyViewHolder)viewHolder).tv_account_money.setText(name);
                ((BorrowedMoneyViewHolder)viewHolder).tv_duedate_val.setText(dueDate);
                ((BorrowedMoneyViewHolder)viewHolder).tv_amount.setText(transactionAttribute2);
                ((BorrowedMoneyViewHolder)viewHolder).btn_full.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        BorrowedMoneyViewHolder vh = (BorrowedMoneyViewHolder) viewHolder;

                        int tran_id = Integer.parseInt(vh.money_container.getTag(R.id.key_entry_id).toString());
                        int tran_type = Integer.parseInt(vh.money_container.getTag(R.id.key_entry_type).toString());
                        mOnClickListener.onButtonClick(tran_id, tran_type, BTN_TYPE_RETURN);

                    }
                });
                // ((BorrowedMoneyViewHolder)viewHolder).btn_partial.setTag(cursor.getInt(cursor.getColumnIndex(Transaction.COLUMN_ID)));
                ((BorrowedMoneyViewHolder)viewHolder).btn_partial.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        BorrowedMoneyViewHolder vh = (BorrowedMoneyViewHolder) viewHolder;

                        int tran_id = Integer.parseInt(vh.money_container.getTag(R.id.key_entry_id).toString());
                        int tran_type = Integer.parseInt(vh.money_container.getTag(R.id.key_entry_type).toString());
                        mOnClickListener.onButtonClick(tran_id, tran_type, BTN_TYPE_PARTIAL);
                    }
                });
                break;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(viewType == TYPE_ITEM){
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_transaction, parent, false); //same size as parent but not binded to the parent
            return new BorrowedItemViewHolder(v);
        }else if(viewType == TYPE_MONEY){
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_money_transaction, parent, false); //same size as parent but not binded to the parent
            return new BorrowedMoneyViewHolder(v);
        }

        return null;
    }

    /*********************************************
     * ITEM VIEW HOLDER
     *********************************************/
    public class BorrowedItemViewHolder extends RecyclerView.ViewHolder {
        // TODO

        TextView tv_account_item, tv_itemname, tv_duedate_val;
        Button btn_lost, btn_returned;
        ImageView img_item;
        View item_container;

        public BorrowedItemViewHolder(View itemView) {
            super(itemView);
            tv_account_item = (TextView) itemView.findViewById(R.id.tv_account_item);
            tv_itemname = (TextView) itemView.findViewById(R.id.tv_itemname);
            tv_duedate_val = (TextView) itemView.findViewById(R.id.tv_duedateitem_val);

            img_item = (ImageView) itemView.findViewById(R.id.img_item);
            btn_lost = (Button) itemView.findViewById(R.id.btn_lost);
            btn_returned = (Button) itemView.findViewById(R.id.btn_returned);
            item_container = itemView.findViewById(R.id.item_container);
        }

    }

    /*********************************************
     * MONEY VIEW HOLDER
     *********************************************/
    public class BorrowedMoneyViewHolder extends RecyclerView.ViewHolder{
        // TODO

        TextView tv_account_money, tv_amount, tv_duedate_val;
        Button btn_partial, btn_full;
        View money_container;

        public BorrowedMoneyViewHolder(View itemView) {
            super(itemView);
            tv_account_money = (TextView) itemView.findViewById(R.id.tv_account_money);
            tv_amount = (TextView) itemView.findViewById(R.id.tv_amount);
            tv_duedate_val = (TextView) itemView.findViewById(R.id.lbl_duedatemoney_val);

            btn_partial = (Button) itemView.findViewById(R.id.btn_partial);
            btn_full = (Button) itemView.findViewById(R.id.btn_full);
            money_container = itemView.findViewById(R.id.money_container);
        }
    }

    public void setmOnClickListener(OnButtonClickListener m){
        this.mOnClickListener = m;
    }

    public String parseMillisToDate(long millis){
        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy");
        Date resultdate = new Date(millis);
        return sdf.format(resultdate);
    }

    public interface OnButtonClickListener{
        public void onButtonClick(int id, int type, int btnType);
    }
}

