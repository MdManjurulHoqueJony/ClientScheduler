package com.wordpress.jonyonandroidcraftsmanship.clientscheduler;

import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.List;
import java.util.Locale;

@SuppressWarnings("ResourceType")
public class ScheduleInformationActivity extends AppCompatActivity {

    private EditText etClientName;
    private DatePicker dpScheduleDate;
    private TimePicker tpStartTime;
    private TimePicker tpEndTime;
    private Button btnCreateSchedule;
    private Button btnViewScheduleList;
    private Button btnClearScheduleList;
    private Button btnExit;

    private LocationManager locationManager;
    private static final int TWO_MINUTES = 1000 * 60 * 2;
    private Location location;
    public static String locationAddress;
    private DBAdapter dbAdapter;

    private LocationListener locationListener = new LocationListener() {

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }

        @Override
        public void onLocationChanged(Location location) {
            ScheduleInformationActivity.this.location = location;
        }
    };

    private View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.btnCreateSchedule:
                    if (!etClientName.getText().toString().equals("")) {
                        Schedule schedule = createSchedule();

                        dbAdapter.open();
                        long inserted = dbAdapter.insertSchedule(schedule);
                        dbAdapter.close();

                        if (inserted >= 0) {
                            etClientName.getText().clear();
                        }
                    } else {
                        Toast.makeText(getApplicationContext(),
                                getResources().getString(R.string.client_name_validation), Toast.LENGTH_LONG).show();
                    }
                    break;
                case R.id.btnViewScheduleList:
                    dbAdapter.open();
                    boolean isEmpty = dbAdapter.isDatabaseEmpty();
                    dbAdapter.close();

                    if (isEmpty) {
                        Toast.makeText(getApplicationContext(),
                                getResources().getString(R.string.no_schedule), Toast.LENGTH_LONG).show();
                    } else {
                        Intent intent = new Intent(ScheduleInformationActivity.this,
                                ScheduleListActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    }
                    break;
                case R.id.btnClearScheduleList:
                    dbAdapter.open();
                    if (dbAdapter.clear() >= 0) {
                        Toast.makeText(getApplicationContext(),
                                getResources().getString(R.string.clear), Toast.LENGTH_LONG).show();
                    }
                    dbAdapter.close();
                    break;
                case R.id.btnExit:
                    finish();
                    break;
                default:
                    break;
            }
        }
    };

    private Schedule createSchedule() {
        Schedule schedule;
        String clientName = etClientName.getText().toString();
        String scheduleDate = dpScheduleDate.getDayOfMonth() + "/" + (dpScheduleDate.getMonth() + 1) + "/" + dpScheduleDate.getYear();
        String startTime = tpStartTime.getCurrentHour() + ":" + tpStartTime.getCurrentMinute();
        String endTime = tpEndTime.getCurrentHour() + ":" + tpEndTime.getCurrentMinute();
        schedule = new Schedule(clientName, scheduleDate, startTime, endTime, locationAddress);
        return schedule;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule_information);

        initialize();
    }

    @Override
    protected void onStart() {
        super.onStart();
        boolean gpsEnabled = locationManager
                .isProviderEnabled(LocationManager.GPS_PROVIDER);

        if (!gpsEnabled) {
            Toast.makeText(getApplicationContext(),
                    getResources().getString(R.string.gps_validation), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        setup();
        if (location != null) {
            try {
                Geocoder geo = new Geocoder(getApplicationContext(), Locale.getDefault());
                List<Address> addresses = geo.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                if (addresses.isEmpty()) {
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.waiting), Toast.LENGTH_LONG).show();
                } else {
                    if (addresses.size() > 0) {
                        locationAddress = addresses.get(0).getFeatureName();
                        locationAddress += ", " + addresses.get(0).getLocality();
                        locationAddress += ", " + addresses.get(0).getAdminArea();
                        locationAddress += ", " + addresses.get(0).getCountryName();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void setup() {
        Location gpsLocation = null;
        Location networkLocation = null;

        locationManager.removeUpdates(locationListener);
        gpsLocation = requestUpdatefromProvider(LocationManager.GPS_PROVIDER);
        networkLocation = requestUpdatefromProvider(LocationManager.NETWORK_PROVIDER);

        if (gpsLocation != null && networkLocation != null) {
            Location myLocation = getBetterLocation(gpsLocation,
                    networkLocation);
            location = myLocation;
        } else if (gpsLocation != null) {
            location = gpsLocation;
        } else if (networkLocation != null) {
            location = networkLocation;
        } else {
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.problem), Toast.LENGTH_LONG).show();
        }
    }

    private Location requestUpdatefromProvider(String provider) {
        Location location = null;
        if (locationManager.isProviderEnabled(provider)) {
            locationManager.requestLocationUpdates(provider, 1000, 10, locationListener);
            location = locationManager.getLastKnownLocation(provider);
        } else {
            Toast.makeText(getApplicationContext(), provider + getResources().getString(R.string.not_enabled), Toast.LENGTH_LONG).show();
        }
        return location;
    }

    private Location getBetterLocation(Location newLocation, Location currentBestLocation) {
        long timeDelta = newLocation.getTime() - currentBestLocation.getTime();
        boolean isSignificantlyNewer = timeDelta > TWO_MINUTES;
        boolean isSignificantlyOlder = timeDelta < -TWO_MINUTES;
        boolean isNewer = timeDelta > 0;
        if (isSignificantlyNewer) {
            return newLocation;
        } else if (isSignificantlyOlder) {
            return currentBestLocation;
        }

        int accuracyDelta = (int) (newLocation.getAccuracy() - currentBestLocation
                .getAccuracy());
        boolean isLessAccurate = accuracyDelta > 0;
        boolean isMoreAccurate = accuracyDelta < 0;

        if (isMoreAccurate) {
            return newLocation;
        } else if (isNewer && !isLessAccurate) {
            return newLocation;
        }
        return currentBestLocation;
    }


    private void initialize() {
        etClientName = (EditText) findViewById(R.id.etClientName);
        dpScheduleDate = (DatePicker) findViewById(R.id.dpScheduleDate);
        tpStartTime = (TimePicker) findViewById(R.id.tpStartTime);
        tpEndTime = (TimePicker) findViewById(R.id.tpEndTime);
        btnCreateSchedule = (Button) findViewById(R.id.btnCreateSchedule);
        btnViewScheduleList = (Button) findViewById(R.id.btnViewScheduleList);
        btnClearScheduleList = (Button) findViewById(R.id.btnClearScheduleList);
        btnExit = (Button) findViewById(R.id.btnExit);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        dpScheduleDate.setMinDate(System.currentTimeMillis() - 1000);
        dbAdapter = new DBAdapter(this);
        setClickListeners();
    }

    private void setClickListeners() {
        btnCreateSchedule.setOnClickListener(clickListener);
        btnViewScheduleList.setOnClickListener(clickListener);
        btnClearScheduleList.setOnClickListener(clickListener);
        btnExit.setOnClickListener(clickListener);
    }

}