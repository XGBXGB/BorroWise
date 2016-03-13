package com.example.shayanetan.borrowise.Adapters;

import android.content.Context;
import android.database.Cursor;
import android.media.Image;
import android.media.Rating;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shayanetan.borrowise.Models.User;
import com.example.shayanetan.borrowise.R;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Blu on 3/13/2016.
 */
public class UsersCursorAdapter extends CursorRecyclerViewAdapter<UsersCursorAdapter.UsersViewHolder> {

    //private static final int TYPE_ITEM = 1;
    //private static final int TYPE_MONEY = 2;
    //AdapterView.OnItemClickListener mOnItemClickListener;

    Context c;
    String namefortoast;

    public UsersCursorAdapter(Context context, Cursor cursor) {
        super(context, cursor);
        c = context;
    }

    @Override
    public void onBindViewHolder(UsersViewHolder viewHolder, Cursor cursor) {
        String name = cursor.getString(cursor.getColumnIndex(User.COLUMN_NAME));
        float rating = (float) cursor.getDouble(cursor.getColumnIndex(User.COLUMN_TOTAL_RATE));

        namefortoast = name;

        viewHolder.tv_username.setText(name);
        viewHolder.tv_userrating.setText(""+rating+" stars out of 5 stars");
        viewHolder.rb_ratinguser.setRating(rating);
        viewHolder.user_container.setTag(cursor.getInt(cursor.getColumnIndex(User.COLUMN_ID)));
        viewHolder.user_container.setOnClickListener(
                new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(c, "Username is " + namefortoast,
                                Toast.LENGTH_LONG).show();

                    }
                }
        );
    }

   /* @Override
    public int getItemViewType(int position) {
        Cursor itemCursor = super.getCursor();
        itemCursor.moveToPosition(position);
        if(itemCursor.getString(itemCursor.getColumnIndex(Transaction.COLUMN_CLASSIFICATION)).equalsIgnoreCase("item"))
            return TYPE_ITEM;
        else
            return TYPE_MONEY;
    }*/

    @Override
    public UsersCursorAdapter.UsersViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_user, parent, false); //same size as parent but not binded to the parent
            return new UsersViewHolder(v);
    }

    public class UsersViewHolder extends RecyclerView.ViewHolder
    {
        ImageView iv_user;
        LinearLayout user_container;
        TextView tv_username, tv_userrating;
        RatingBar rb_ratinguser;

        public UsersViewHolder(View itemView)
        {
            super(itemView);
            user_container = (LinearLayout) itemView.findViewById(R.id.user_container);
            iv_user = (ImageView) itemView.findViewById(R.id.iv_user);
            tv_username = (TextView) itemView.findViewById(R.id.tv_username);
            rb_ratinguser = (RatingBar) itemView.findViewById(R.id.rb_ratinguser);
            tv_userrating = (TextView) itemView.findViewById(R.id.tv_userrating);

        }
    }
}
