package com.example.shayanetan.borrowise2.Fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
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
import android.widget.ImageView;
import android.widget.TextView;

import com.example.shayanetan.borrowise2.Models.ItemTransaction;
import com.example.shayanetan.borrowise2.Models.Transaction;
import com.example.shayanetan.borrowise2.R;

import java.io.ByteArrayOutputStream;


public class AddItemFragment extends AddAbstractFragment {

    private FragmentTransaction transaction;
    private EditText et_AIItemName;
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
        btn_addContact = (ImageView) layout.findViewById(R.id.btn_addContact);
        img_btn_switch = (FloatingActionButton) layout.findViewById(R.id.btn_ItemToMoney);
        et_AIItemName = (EditText) layout.findViewById(R.id.et_AIItemName);
        atv_person_name = (AutoCompleteTextView) layout.findViewById(R.id.atv_AIPersonName);

        card_camera = (View) layout.findViewById(R.id.card_camera);

        layout_startDate = (View) layout.findViewById(R.id.layout_item_startDate);
        layout_endDate = (View) layout.findViewById(R.id.layout_item_endDate);

        tv_endDate = (TextView) layout.findViewById(R.id.tv_item_endDate);
        tv_startDate = (TextView) layout.findViewById(R.id.tv_item_startDate);

        btn_borrowed = (Button) layout.findViewById(R.id.btn_AIBorrow);
        btn_lent = (Button) layout.findViewById(R.id.btn_AILend);

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

                String item = et_AIItemName.getText().toString();

                if (mListener != null && !item.isEmpty() && !selected_name.isEmpty()) {
                    int id = mListener.onAddNewUser(selected_name, selected_contact_number);

                    ItemTransaction it = new ItemTransaction(Transaction.ITEM_TYPE, id, Transaction.BORROWED_ACTION, 0,
                            parseDateToMillis(tv_startDate.getText().toString()),
                            parseDateToMillis(tv_endDate.getText().toString()),
                            0, 0.0,
                            item, "", filePath);
                    mListener.onAddTransactions(it);

                    printAddAcknowledgement(et_AIItemName.getText().toString(), "borrowed");
                    clearAllFields();
                }else{
                    printRejectDialog();
                }


            }
        });

        btn_lent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String item = et_AIItemName.getText().toString();

                if (mListener != null && !item.isEmpty() && !selected_name.isEmpty()) {
                    int id = mListener.onAddNewUser(selected_name, selected_contact_number);

                    ItemTransaction it = new ItemTransaction(Transaction.ITEM_TYPE, id, Transaction.LEND_ACTION, 0,
                            parseDateToMillis(tv_startDate.getText().toString()),
                            parseDateToMillis(tv_endDate.getText().toString()),
                            0, 0.0,
                            item, "", filePath);


                    mListener.onAddTransactions(it);
                    printAddAcknowledgement(et_AIItemName.getText().toString(), "lent");
                    clearAllFields();
                }else{
                    printRejectDialog();
                }
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
            img_camera.setScaleType(ImageView.ScaleType.CENTER_CROP);

            System.out.println("CAMERA SAVED FILEPATH: " + getRealPathFromURI(tempUri));
        }
    }

    public void printAddAcknowledgement(String entry_name, String type){
        if(type.equalsIgnoreCase("lent")) {

            LayoutInflater factory = LayoutInflater.from(getActivity());
            final View view = factory.inflate(R.layout.add_transaction_confirmation, null);
            TextView tv_confirmation = (TextView) view.findViewById(R.id.tv_confirmation);
            tv_confirmation.setText(entry_name + " has been successfully " + type + " to " + selected_name + " !");

            AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
            alertDialog.setView(view);
            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            alertDialog.show();
        }else {

            LayoutInflater factory = LayoutInflater.from(getActivity());
            final View view = factory.inflate(R.layout.add_transaction_confirmation, null);
            TextView tv_confirmation = (TextView) view.findViewById(R.id.tv_confirmation);
            tv_confirmation.setText(entry_name + " has been successfully " + type + " from " + selected_name + " !");

            AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
            alertDialog.setView(view);
            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            alertDialog.show();

        }
    }

    public void clearAllFields(){
        et_AIItemName.setText("");
        atv_person_name.setText("");
        img_camera.setImageResource(R.drawable.ic_camera_small);
        img_camera.setScaleType(ImageView.ScaleType.CENTER_CROP);
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
