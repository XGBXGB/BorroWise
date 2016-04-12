package com.example.shayanetan.borrowise2.Fragments;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.SystemClock;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.shayanetan.borrowise2.Activities.AlarmReceiver;
import com.example.shayanetan.borrowise2.Models.ItemTransaction;
import com.example.shayanetan.borrowise2.Models.Transaction;
import com.example.shayanetan.borrowise2.R;

import java.io.ByteArrayOutputStream;
import java.io.File;


public class AddItemFragment extends AddAbstractFragment {

    private FragmentTransaction transaction;
    private EditText et_AIItemName,
            et_AIDescription;
    private ImageView img_camera;
    private View card_camera;
    private String filePath="";

    public AddItemFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View layout = inflater.inflate(R.layout.fragment_add_item, container, false);
        img_camera = (ImageView) layout.findViewById(R.id.img_camera);
        card_camera = (View) layout.findViewById(R.id.card_camera);
        btn_addContact = (ImageView) layout.findViewById(R.id.btn_addContact);
        img_btn_switch = (FloatingActionButton) layout.findViewById(R.id.btn_ItemToMoney);
        et_AIItemName = (EditText) layout.findViewById(R.id.et_AIItemName);
//        et_AIDescription = (EditText) layout.findViewById(R.id.et_AIDescription);
        atv_person_name = (AutoCompleteTextView) layout.findViewById(R.id.atv_AIPersonName);

        btn_borrowed = (Button) layout.findViewById(R.id.btn_AIBorrow);
        btn_lent = (Button) layout.findViewById(R.id.btn_AILend);
        btn_start_date = (Button) layout.findViewById(R.id.btn_AIStartDate);
        btn_end_date = (Button) layout.findViewById(R.id.btn_AIEndDate);

        init(); // method found in abstact class

        card_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, 0);
            }
        });

        btn_addContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_INSERT,
                        ContactsContract.Contacts.CONTENT_URI);
                startActivity(intent);
            }
        });

        btn_borrowed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mListener != null) {
                    int id = mListener.onAddNewUser(selected_name, selected_contact_number);

                    ItemTransaction it = new ItemTransaction(Transaction.ITEM_TYPE, id, Transaction.BORROWED_ACTION, 0,
                            parseDateToMillis(btn_start_date.getText().toString()),
                            parseDateToMillis(btn_end_date.getText().toString()),
                            0, 0.0,
                            et_AIItemName.getText().toString(), "", filePath);
                    mListener.onAddTransactions(it);

                    printAddAcknowledgement(et_AIItemName.getText().toString(), "borrowed");


                }

                clearAllFields();
            }
        });

        btn_lent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mListener != null) {
                    int id = mListener.onAddNewUser(selected_name, selected_contact_number);

                    ItemTransaction it = new ItemTransaction(Transaction.ITEM_TYPE, id, Transaction.LEND_ACTION, 0,
                            parseDateToMillis(btn_start_date.getText().toString()),
                            parseDateToMillis(btn_end_date.getText().toString()),
                            0, 0.0,
                            et_AIItemName.getText().toString(), "", filePath);


                    mListener.onAddTransactions(it);
                    printAddAcknowledgement(et_AIItemName.getText().toString(), "lent");
                }

                clearAllFields();
            }
        });

        return layout;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);

        //Bitmap bp = (Bitmap) data.getExtras().get("data");
        //iv.setImageBitmap(bp);
        if(requestCode == 0 && resultCode == Activity.RESULT_OK) {
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            //imageView.setImageBitmap(photo);
            //knop.setVisibility(Button.VISIBLE);


            // CALL THIS METHOD TO GET THE URI FROM THE BITMAP
            Uri tempUri = getImageUri(getActivity().getApplicationContext(), photo);

            // CALL THIS METHOD TO GET THE ACTUAL PATH
            //File finalFile = new File(getRealPathFromURI(tempUri));

            filePath = getRealPathFromURI(tempUri);

            img_camera.setImageBitmap(photo);
          //  img_camera.set;
            img_camera.setScaleType(ImageView.ScaleType.FIT_XY);

            System.out.println("CAMERA SAVED FILEPATH: " + getRealPathFromURI(tempUri));
            Toast.makeText(getActivity(), "CAMERA SAVED FILEPATH: " + getRealPathFromURI(tempUri), Toast.LENGTH_SHORT).show();
        }
    }

    public void printAddAcknowledgement(String entry_name, String type){
        Toast.makeText(getActivity(),entry_name+ " has been successfully "+ type + " !", Toast.LENGTH_SHORT).show();
    }

    public void clearAllFields(){
        et_AIItemName.setText("");
        atv_person_name.setText("");
        img_camera.setImageResource(R.drawable.ic_camera);
        setDateToCurrent();
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    public String getRealPathFromURI(Uri uri) {
        Cursor cursor = getActivity().getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
        return cursor.getString(idx);
    }

    @Override
    protected void onFragmentSwitch() {
        img_btn_switch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddMoneyFragment fragment = new AddMoneyFragment();
                FragmentManager fm = myContext.getSupportFragmentManager();
                transaction = fm.beginTransaction();
                transaction.replace(R.id.fragment_container, fragment);
                transaction.commit();
            }
        });
    }

    public void setOnFragmentInteractionListener(OnFragmentInteractionListener mListener){
        this.mListener = mListener;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

}
