package com.hfad.odometer;

import android.location.LocationListener;
import android.os.Bundle;

interface LocationChangeListener extends LocationListener {

    @Override
    default void onStatusChanged(String s, int i, Bundle bundle) {
        // Space intentionally left blank
    }

    @Override
    default void onProviderEnabled(String s) {
        // Space intentionally left blank
    }

    @Override
    default void onProviderDisabled(String s) {
        // Space intentionally left blank
    }
}
