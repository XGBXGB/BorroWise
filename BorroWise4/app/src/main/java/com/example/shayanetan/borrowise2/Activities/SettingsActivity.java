package com.example.shayanetan.borrowise2.Activities;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.shayanetan.borrowise2.Adapters.UsersCursorAdapter;
import com.example.shayanetan.borrowise2.Fragments.TimePickerFragment;
import com.example.shayanetan.borrowise2.R;

public class SettingsActivity extends BaseActivity {

    private EditText et_borrowDays, et_lendDays;
    private Button btn_borrowTime, btn_lendTime, btn_save;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        btn_borrowTime = (Button) findViewById(R.id.btn_borrowTime);
        btn_lendTime = (Button) findViewById(R.id.btn_lendTime);
        et_borrowDays = (EditText) findViewById(R.id.et_borrowDays);
        et_lendDays = (EditText) findViewById(R.id.et_lendDays);
        btn_save = (Button) findViewById(R.id.btn_save);

        btn_borrowTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerFragment timePickerFragment = new TimePickerFragment();
                timePickerFragment.setButtonDateSelector(btn_borrowTime);
                timePickerFragment.show(getSupportFragmentManager(), "BorrowTimePicker");
            }
        });

        btn_lendTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerFragment timePickerFragment = new TimePickerFragment();
                timePickerFragment.setButtonDateSelector(btn_lendTime);
                timePickerFragment.show(getSupportFragmentManager(), "LendTimePicker");
            }
        });

        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
                SharedPreferences.Editor spEditor = sp.edit();
                spEditor.putString(HomeActivity.SP_KEY_BORROW_DAYS, et_borrowDays.getText().toString());
                spEditor.putString(HomeActivity.SP_KEY_BORROW_TIME, btn_borrowTime.getText().toString());
                spEditor.putString(HomeActivity.SP_KEY_LEND_DAYS, et_lendDays.getText().toString());
                spEditor.putString(HomeActivity.SP_KEY_LEND_TIME, btn_lendTime.getText().toString());

                spEditor.commit();
            }
        });
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }




}
