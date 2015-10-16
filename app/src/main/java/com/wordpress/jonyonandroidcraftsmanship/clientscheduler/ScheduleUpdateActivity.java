package com.wordpress.jonyonandroidcraftsmanship.clientscheduler;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

public class ScheduleUpdateActivity extends AppCompatActivity {

    private EditText etClientName;
    private DatePicker dpScheduleDate;
    private TimePicker tpStartTime;
    private TimePicker tpEndTime;
    private Button btnUpdateSchedule;
    private DBAdapter dbAdapter;

    private View.OnClickListener clickListener=new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.btnUpdateSchedule:
                    if (!etClientName.getText().toString().equals("")) {
                        Schedule schedule = createSchedule();

                        dbAdapter.open();
                        long updated = dbAdapter.updateSchedule(getIntent().getExtras().getInt(getResources().getString(R.string.position))+1, schedule);
                        dbAdapter.close();

                        if (updated >= 0) {
                            Intent intent = new Intent(ScheduleUpdateActivity.this,
                                    ScheduleListActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                        }
                    }else {
                        Toast.makeText(getApplicationContext(),
                                getResources().getString(R.string.client_name_validation), Toast.LENGTH_LONG).show();
                    }
                    break;
                default:
                    break;
            }
        }
    };

    private Schedule createSchedule() {
        Schedule schedule;
        String clientName=etClientName.getText().toString();
        String scheduleDate= dpScheduleDate.getDayOfMonth() + "/" + (dpScheduleDate.getMonth() + 1) + "/" + dpScheduleDate.getYear();
        String startTime=tpStartTime.getCurrentHour() + ":" + tpStartTime.getCurrentMinute();
        String endTime=tpEndTime.getCurrentHour() + ":" + tpEndTime.getCurrentMinute();
        schedule=new Schedule(clientName,scheduleDate,startTime,endTime,ScheduleInformationActivity.locationAddress);
        return schedule;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule_update);

        initialize();
    }

    private void initialize() {
        etClientName= (EditText) findViewById(R.id.etClientName);
        dpScheduleDate= (DatePicker) findViewById(R.id.dpScheduleDate);
        tpStartTime= (TimePicker) findViewById(R.id.tpStartTime);
        tpEndTime= (TimePicker) findViewById(R.id.tpEndTime);
        btnUpdateSchedule= (Button) findViewById(R.id.btnUpdateSchedule);
        dpScheduleDate.setMinDate(System.currentTimeMillis() - 1000);
        dbAdapter = new DBAdapter(this);
        setClickListeners();
    }

    private void setClickListeners() {
        btnUpdateSchedule.setOnClickListener(clickListener);
    }

}