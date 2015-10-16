package com.wordpress.jonyonandroidcraftsmanship.clientscheduler;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class ScheduleAdapter extends ArrayAdapter<Schedule> {

    private ArrayList<Schedule> schedules;
    private Activity context;

    public ScheduleAdapter(Activity context, ArrayList<Schedule> schedules) {
        super(context,R.layout.schedule_item ,schedules);
        this.schedules = schedules;
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = context.getLayoutInflater();
            view = inflater.inflate(R.layout.schedule_item, null, false);

            TextView tvClientName = (TextView) view.findViewById(R.id.tvClientName);
            TextView tvScheduleDate=(TextView)view.findViewById(R.id.tvScheduleDate);
            TextView tvStartTime=(TextView)view.findViewById(R.id.tvStartTime);
            TextView tvEndTime=(TextView)view.findViewById(R.id.tvEndTime);
            TextView tvLocationAddress=(TextView)view.findViewById(R.id.tvLocationAddress);

            Schedule schedule=schedules.get(position);
            tvClientName.setText(schedule.getClientName());
            tvScheduleDate.setText(schedule.getScheduleDate());
            tvStartTime.setText(schedule.getStartTime());
            tvEndTime.setText(schedule.getEndTime());
            tvLocationAddress.setText(schedule.getLocationAddress());
        }

        return view;
    }
}
