package com.example.shayanetan.borrowise.Adapters;

/**
 * Created by ShayaneTan on 3/11/2016.
 */

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
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
public class TransactionsCursorAdapter extends CursorRecyclerViewAdapter<RecyclerView.ViewHolder> {

    private static final int TYPE_ITEM = 1;
    private static final int TYPE_MONEY = 2;
    private OnItemClickListener mOnItemClickListener;

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
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, Cursor cursor) {
        String name = cursor.getString(cursor.getColumnIndex(User.COLUMN_NAME));
        String dueDate = parseMillisToDate(cursor.getLong(cursor.getColumnIndex(Transaction.COLUMN_DUE_DATE)));
        String transactionAttribute1 = cursor.getString(cursor.getColumnIndex("Attribute1"));
        switch (viewHolder.getItemViewType()) {
            case TYPE_ITEM:
                String itemName = cursor.getString(cursor.getColumnIndex(ItemTransaction.COLUMN_NAME));
                ((BorrowedItemViewHolder)viewHolder).tv_account_item.setText(name);
                ((BorrowedItemViewHolder)viewHolder).tv_duedate_val.setText(dueDate);
                ((BorrowedItemViewHolder)viewHolder).tv_itemname.setText(transactionAttribute1);
                break;

            case TYPE_MONEY:
                ((BorrowedMoneyViewHolder)viewHolder).tv_account_money.setText(name);
                ((BorrowedMoneyViewHolder)viewHolder).tv_duedate_val.setText(dueDate);
                ((BorrowedMoneyViewHolder)viewHolder).tv_amount.setText(transactionAttribute1);
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

    public void setmOnItemClickListener(OnItemClickListener m){
        this.mOnItemClickListener = m;
    }

    public interface OnItemClickListener{
        public void onItemClick(int id);
        public void onItemLongClick(int id);
    }

    public String parseMillisToDate(long millis){
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
        Date resultdate = new Date(millis);
        return sdf.format(resultdate);
    }
}

