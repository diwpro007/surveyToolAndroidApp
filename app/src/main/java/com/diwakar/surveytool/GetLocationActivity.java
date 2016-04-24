package com.diwakar.surveytool;

import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class GetLocationActivity extends AppCompatActivity {

    TextView lat;
    TextView longi;

    LocationManager locationManager;
    LocationListener locationListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_location);

        lat = (TextView) findViewById(R.id.textViewForLatitude);
        longi = (TextView) findViewById(R.id.textViewForLongitude);

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
    }

    public void getLocation(View view) {

    }
}
