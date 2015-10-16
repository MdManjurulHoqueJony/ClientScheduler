package com.wordpress.jonyonandroidcraftsmanship.clientscheduler;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

public class ScheduleListActivity extends AppCompatActivity {

    private ListView lvSchedule;
    private ArrayList<Schedule> schedules;
    private ScheduleAdapter scheduleAdapter;
    private DBAdapter dbAdapter;

    private AdapterView.OnItemClickListener itemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Intent intent = new Intent(ScheduleListActivity.this,
                    ScheduleUpdateActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.putExtra(getResources().getString(R.string.position), position);
            startActivity(intent);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule_list);

        initialize();
    }

    @Override
    protected void onResume() {
        super.onResume();
        fillSchedules();
    }

    private void fillSchedules() {
        dbAdapter.open();
        schedules = dbAdapter.getAllSchedules();
        scheduleAdapter = new ScheduleAdapter(this, schedules);
        lvSchedule.setAdapter(scheduleAdapter);
        dbAdapter.close();
    }

    private void initialize() {
        lvSchedule = (ListView) findViewById(R.id.lvSchedule);
        lvSchedule.setOnItemClickListener(itemClickListener);
        dbAdapter = new DBAdapter(this);
    }
}