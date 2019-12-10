package com.hfad.odometer;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.util.Locale;

public class MainActivity extends Activity {

    private static final String TAG = "ODO APP";
    private final int PERMISSION_REQUEST_CODE = 698;
    private final int NOTIFICATION_ID = 423;
    final String LOCATION_PROVIDER = LocationManager.GPS_PROVIDER;


    private LocationListener listener;
    private LocationManager locManager;
    private static long distanceInMeters;
    private static Location lastLocation = null;
    public static final String PERMISSION_STRING
            = android.Manifest.permission.ACCESS_FINE_LOCATION;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setLocationListener();
        trackDistance();
    }

    private void setLocationListener() {
        listener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                if (lastLocation == null) {
                    lastLocation = location;
                }
                distanceInMeters += location.distanceTo(lastLocation);
                lastLocation = location;
            }

            @Override
            public void onProviderDisabled(String arg0) {
            }

            @Override
            public void onProviderEnabled(String arg0) {
            }

            @Override
            public void onStatusChanged(String arg0, int arg1, Bundle bundle) {
            }
        };

        locManager = (LocationManager) getSystemService (Context.LOCATION_SERVICE);
    }

    private void trackDistance() {
        if (ContextCompat.checkSelfPermission(this, PERMISSION_STRING)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[] {PERMISSION_STRING}, PERMISSION_REQUEST_CODE);
            return;
        }

        locManager.requestLocationUpdates(LOCATION_PROVIDER, 1000, 1, listener);


        displayDistance();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.d(TAG, "onRequestPermissionsResult granted!");
                trackDistance();
            } else {
                Log.d(TAG, "onRequestPermissionsResult NOT granted!");
            }
        }
    }


    private void displayDistance() {
        final TextView distanceView = findViewById(R.id.distance);
        final Handler handler = new Handler();
        handler.post(new Runnable() {
            @Override
            public void run() {

                String distanceStr = String.format(Locale.getDefault(),
                        "%1d meter", distanceInMeters);
                distanceView.setText(distanceStr);
                handler.postDelayed(this, 1000);
            }
        });
    }

    public void resetDistance(View view) {
        distanceInMeters = 0;
        displayDistance();
    }

    private double getDistanceInKm() {
        return distanceInMeters / 1000;
    }

    private double getDistanceInMiles() {
        return distanceInMeters / 1609.344;
    }
}
