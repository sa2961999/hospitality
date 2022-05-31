package com.example.hospital;

import android.app.Activity;
import android.location.Location;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.util.Log;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.Task;

@SuppressWarnings("unchecked")
public class CurrentLocationPicker {
    private static final long PERIOD = 200;
    private LocationRequest mLocationRequest;
    private final FusedLocationProviderClient mFusedLocationClient;
    private final LocationCallback mLocationCallback;
    private final Handler mServiceHandler;
    private static final String TAG = "CurrentLocationPicker";

    // Check for location settings
    public void settingsCheck(Activity activity) {
        Log.d(TAG, "settingsCheck: ");
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(mLocationRequest);

        SettingsClient client = LocationServices.getSettingsClient(activity);
        Task<LocationSettingsResponse> task = client.checkLocationSettings(builder.build());
        task.addOnSuccessListener(activity, locationSettingsResponse -> {
            // All location settings are satisfied. The client can initialize
            // location requests here.
            Log.d(TAG, "onSuccess: settingsCheck");

        });

        task.addOnFailureListener(activity, e -> {
            if (e instanceof ResolvableApiException) {
                // Location settings are not satisfied, but this can be fixed
                // by showing the user a dialog.
                Log.d(TAG, "onFailure: settingsCheck");
                try {
                    // Show the dialog by calling startResolutionForResult(),
                    // and check the result in onActivityResult().
                    ResolvableApiException resolvable = (ResolvableApiException) e;
                    resolvable.startResolutionForResult(activity,
                            456);
                    Log.d(TAG, "onFailure: try check ");
                    createLocationRequest();
                } catch (Exception sendEx) {
                    // Ignore the error.
                    sendEx.printStackTrace();
                }
            }
        });
    }


    public CurrentLocationPicker(Activity activity, OnLocationPicked locationPicked) {
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(activity);
        mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);
                if (locationResult != null) {

                    Location loction = locationResult.getLastLocation();
                    if (loction != null && locationPicked != null) {
                        locationPicked.locationPicked(loction);
                        mFusedLocationClient.removeLocationUpdates(this);
                        mServiceHandler.removeCallbacksAndMessages(null);
                    }
                }
            }
        };
        createLocationRequest();
        HandlerThread handlerThread = new HandlerThread(TAG);
        handlerThread.start();
        mServiceHandler = new Handler(handlerThread.getLooper());
        requestLocationUpdates();

        settingsCheck(activity);

    }


    public void requestLocationUpdates() {
        Log.i(TAG, "Requesting location updates");
        try {
            mFusedLocationClient.requestLocationUpdates(mLocationRequest,
                    mLocationCallback, Looper.myLooper());
        } catch (SecurityException unlikely) {
            Log.e(TAG, "Lost location permission. Could not request updates. " + unlikely);
        }
    }

    private void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(PERIOD);
        mLocationRequest.setFastestInterval(PERIOD);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    public interface OnLocationPicked {
        void locationPicked(Location location);
    }
}


