package com.annabenson.knowyourgovernment;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by Anna on 3/21/2018.
 */

public class Locator {

    private static final String TAG = "Locator";
    private MainActivity owner;
    private LocationManager locationManager;
    private LocationListener locationListener;

    public Locator(MainActivity activity){
        owner = activity;

        if(checkPermission()){
            setUpLocationManager();
            determineLocation();
        }
    }

    public void setUpLocationManager(){

        if(locationManager != null){return;}
        if(!checkPermission()){ return;}

        // get system's location manager
        locationManager = (LocationManager) owner.getSystemService(Context.LOCATION_SERVICE);

        // define a listener
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                // called when new location found by network loc provider
                //Log.d(TAG, "onLocationChanged: ");
                owner.doLocationWork(location.getLatitude(), location.getLongitude());
            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {

            }
        };

        // register listener with Location Manager to receive GPS loc updates
        locationManager.requestLocationUpdates(
                LocationManager.GPS_PROVIDER,1000, 0, locationListener);

    }

    public void shutdown(){
        locationManager.removeUpdates(locationListener);
        locationManager = null;
    }

    public android.location.LocationListener getLocationListener() {
        return locationListener;
    }

    // chooses the best location provider Network ==> Passive ==> GPA
   public void determineLocation(){

        if(!checkPermission()){return;}

        if(locationManager == null){setUpLocationManager();}

       if (locationManager != null) {
           Location loc = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
           if (loc != null) {
               owner.doLocationWork(loc.getLatitude(), loc.getLongitude());
               Toast.makeText(owner, "Using " + LocationManager.NETWORK_PROVIDER + " Location provider", Toast.LENGTH_SHORT).show();
               return;
           }
       }

       if (locationManager != null) {
           Location loc = locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);
           if (loc != null) {
               owner.doLocationWork(loc.getLatitude(), loc.getLongitude());
               Toast.makeText(owner, "Using " + LocationManager.PASSIVE_PROVIDER + " Location provider", Toast.LENGTH_SHORT).show();
               return;
           }
       }

       if (locationManager != null) {
           Location loc = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
           if (loc != null) {
               owner.doLocationWork(loc.getLatitude(), loc.getLongitude());
               Toast.makeText(owner, "Using " + LocationManager.GPS_PROVIDER + " Location provider", Toast.LENGTH_SHORT).show();
               return;
           }
       }

       // If you get here, you got no location at all
       owner.noLocationAvailable();
       return;
   }

   private boolean checkPermission(){
       if (ContextCompat.checkSelfPermission(owner, Manifest.permission.ACCESS_FINE_LOCATION) !=
               PackageManager.PERMISSION_GRANTED) {
           ActivityCompat.requestPermissions(owner,
                   new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 5);
           return false;
       }
       return true;
   }
}
