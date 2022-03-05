package net.rncmobile.fmdrivetest.listeners;

import android.content.Context;
import android.location.Criteria;
import android.location.GnssStatus;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationAvailability;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

import org.jetbrains.annotations.NotNull;

import java.util.concurrent.Executor;

/**
 * Created by cedric_f25 on 26.03.2018.
 */

public class FusedLocationListener implements LocationListener {
    private static final String TAG = "GpsListener";

    public interface Listener {
        void onNewLocationAvailable(Location location);

        void onFirstFix();

        void onMockLocationsDetected();
    }

    private Listener listener;

    private final Context mContext;

    private FusedLocationProviderClient fusedLocationClient;
    private LocationRequest locationRequest;
    private LocationCallback locationCallback;

    private LocationManager locationManager;

    private Location loc;
    private double latitude;
    private double longitude;
    private float accuracy;
    private float speed;
    private boolean started = false;

    private boolean mockLocationsEnabled;

    // Mock location rejection
    private Location lastMockLocation;
    private int numGoodReadings;

    private long MIN_DISTANCE_CHANGE_FOR_UPDATES = 3;
    private long MIN_TIME_BW_UPDATES = 1000;

    public FusedLocationListener(Context context) {
        this.mContext = context;
    }

    public FusedLocationListener(Context context, long minDistance, long minTime) {
        this.mContext = context;
        this.MIN_DISTANCE_CHANGE_FOR_UPDATES = minDistance;
        this.MIN_TIME_BW_UPDATES = minTime;
    }

    public boolean isGpsEnabled() {
        try {
            final LocationManager manager = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);
            return manager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch (Exception e) {
            return false;
        }
    }

    public void startGps() {
        try {
            if (!started) {
                started = true;

                if (GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(mContext) == ConnectionResult.SUCCESS) {
                    Log.d(TAG, "Google API détecté");

                    fusedLocationClient = LocationServices.getFusedLocationProviderClient(mContext);

                    locationRequest = LocationRequest.create();
                    locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
                    locationRequest.setInterval(MIN_TIME_BW_UPDATES);
                    locationRequest.setFastestInterval(MIN_TIME_BW_UPDATES);
                    locationRequest.setMaxWaitTime(MIN_TIME_BW_UPDATES);
                    locationRequest.setSmallestDisplacement(MIN_DISTANCE_CHANGE_FOR_UPDATES);

                    locationCallback = new LocationCallback() {
                        @Override
                        public void onLocationResult(@NotNull LocationResult locationResult) {
                            for (Location location : locationResult.getLocations()) {
                                latitude = location.getLatitude();
                                longitude = location.getLongitude();

                                accuracy = location.getAccuracy();
                                speed = location.getSpeed();

                                FusedLocationListener.this.loc = location;
                            }

                            checkMockLocations();
                            if (!isLocationPlausible(loc) || mockLocationsEnabled) {
                                listener.onMockLocationsDetected();
                            }
                            if (listener != null) listener.onNewLocationAvailable(loc);
                        }

                        @Override
                        public void onLocationAvailability(@NonNull @NotNull LocationAvailability locationAvailability) {
                            super.onLocationAvailability(locationAvailability);
                            if (locationAvailability.isLocationAvailable())
                                listener.onFirstFix();
                        }
                    };

                    fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper());
                } else {
                    Log.d(TAG, "Google API pas détecté");
                    locationManager = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);

                    loc = locationManager.getLastKnownLocation(locationManager.getBestProvider(new Criteria(), false));
                    locationManager.requestLocationUpdates(locationManager.getBestProvider(new Criteria(), false),
                            MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, this);

                    started = true;
                    if (locationManager != null && loc != null) {
                        latitude = loc.getLatitude();
                        longitude = loc.getLongitude();
                        accuracy = loc.getAccuracy();
                        speed = loc.getSpeed();

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R)
                            locationManager.registerGnssStatusCallback((new TaskExecutor()), mStatusCallback);
                        else
                            locationManager.registerGnssStatusCallback(mStatusCallback, null);
                    }
                }
            }
        } catch(SecurityException e){
            Log.d(TAG, "Security Excecption");
        } catch(Exception e){
            Log.d(TAG, e.toString());
        }

    }

    private boolean isLocationPlausible(Location location) {
        if (location == null) return false;

        boolean isMock = mockLocationsEnabled || location.isFromMockProvider();
        if (isMock) {
            lastMockLocation = location;
            numGoodReadings = 0;
        } else
            numGoodReadings = Math.min(numGoodReadings + 1, 1000000); // Prevent overflow

        // We only clear that incident record after a significant show of good behavior
        if (numGoodReadings >= 20) lastMockLocation = null;

        // If there's nothing to compare against, we have to trust it
        if (lastMockLocation == null) return true;

        // And finally, if it's more than 1km away from the last known mock, we'll trust it
        double d = location.distanceTo(lastMockLocation);
        return (d > 1000);
    }

    private void checkMockLocations() {
        // Starting with API level >= 18 we can (partially) rely on .isFromMockProvider()
        // (http://developer.android.com/reference/android/location/Location.html#isFromMockProvider%28%29)
        // For API level < 18 we have to check the Settings.Secure flag
        if (!android.provider.Settings.Secure.getString(mContext.getContentResolver(), android.provider.Settings
                .Secure.ALLOW_MOCK_LOCATION).equals("0")) {
            mockLocationsEnabled = true;
            if (listener != null)
                listener.onMockLocationsDetected();
        } else
            mockLocationsEnabled = false;
    }

    public Location getLoc() {
        return loc;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public float getAccurency() {
        return accuracy;
    }

    public float getSpeed() {
        return speed;
    }

    public int getNbSat() {
        return 5;
    }

    public int getNbFixSat() {
        return 5;
    }

    public void forceRefresh() {
        listener.onNewLocationAvailable(loc);
    }

    public void stop() {
        if(GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(mContext) == ConnectionResult.SUCCESS) {
            if (fusedLocationClient != null) {
                try {
                    fusedLocationClient.removeLocationUpdates(locationCallback);
                } catch (SecurityException e) {
                    Log.d(TAG, "Security Excecption");
                } catch (Exception e) {
                    Log.d(TAG, e.toString());
                }
            }
            started = false;
        } else {
            if (locationManager != null) {
                try {
                    Log.d(TAG, "Stop GPS");
                    locationManager.removeUpdates(this);
                    loc = null;
                } catch (SecurityException e) {
                    Log.d(TAG, "Security Excecption");
                } catch (Exception e) {
                    Log.d(TAG, e.toString());
                }
            }
            started = false;
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        checkMockLocations();
        if (!isLocationPlausible(location) || mockLocationsEnabled) {
            listener.onMockLocationsDetected();
        }
        if (listener != null) listener.onNewLocationAvailable(location);
    }

    @Override
    public void onProviderEnabled(String s) {
        Log.d(TAG, "onProviderEnabled");
    }

    @Override
    public void onProviderDisabled(String s) {
        Log.d(TAG, "Stop GPS");
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        Log.d(TAG, "Stop GPS");
    }

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    /**
     * Report satellite status
     */
    private final GnssStatus.Callback mStatusCallback = new GnssStatus.Callback() {
        @Override
        public void onStarted() {
            Log.d(TAG, "onStarted");
        }

        @Override
        public void onStopped() {
            Log.d(TAG, "onStopped");
        }

        @Override
        public void onFirstFix(int ttffMillis) {
            Log.d(TAG, "onFirstFix");
            listener.onFirstFix();
        }

        @Override
        public void onSatelliteStatusChanged(GnssStatus status) {
            Log.d(TAG, "onSatelliteStatusChanged");

            for (int i = 0; i < status.getSatelliteCount(); i++)
                if(status.usedInFix(i)) {
                    listener.onFirstFix();
                }
        }
    };

    class TaskExecutor implements Executor {
        public void execute(Runnable r) {
            new Thread(r).start();
        }
    }
}
