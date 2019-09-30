package com.hensongeodata.technologies.phc.services;

import android.Manifest;
import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.hensongeodata.technologies.phc.model.LongLat;
import com.hensongeodata.technologies.phc.view.StartPage;

public class LocscastService extends Service implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {

      private static String TAG=LocscastService.class.getSimpleName();
      private GoogleApiClient mGoogleApiClient;
      private LocationRequest mLocationRequest;
      LongLat longLat;

      public LocscastService() {
      }

      protected void startLocationUpdate(){
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){
                  return;
            }
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, (com.google.android.gms.location.LocationListener) this);

      }

      private void setLocation(Location location){
            if (location != null){
                  String lat = String.valueOf(location.getLatitude()) ;
                  String lon = String.valueOf(location.getLongitude());

                  Intent broadcastIntent = new Intent();
                  broadcastIntent.setAction(StartPage.mBroadcastStringLong);
                  broadcastIntent.putExtra("DataLat", lat);
                  broadcastIntent.putExtra("DataLon", lon);
                  sendBroadcast(broadcastIntent);

                  Log.e(TAG, "setLocation: " + lat + ", " + lon );

            }else
                  Log.d(TAG, "setLocation: Location is null");
      }

      private void checkForLocationEnabled() {
            Log.v(TAG, "Check enabled");
            showLocationDialog();
      }

      public void showLocationDialog() {
            LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                    .addLocationRequest(mLocationRequest);
            PendingResult<LocationSettingsResult> result =
                    LocationServices.SettingsApi.checkLocationSettings(mGoogleApiClient,
                            builder.build());

            result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
                  @Override
                  public void onResult(@NonNull LocationSettingsResult locationSettingsResult) {
                        final Status status = locationSettingsResult.getStatus();
                        //MainActivity.isShowPinActOnResume(false);
                        switch (status.getStatusCode()) {
                              case LocationSettingsStatusCodes.SUCCESS:
                                    Log.v(TAG, "Check enabled 0");
                                    // All location settings are satisfied. The client can
                                    // initialize location requests here.
                                    break;
                              case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:

                                    break;
                              case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:

                      /*  Log.v("Check Enabled", "Check enabled 2");
                        if (!mGoogleApiClient.isConnected())
                            new MaterialDialog.Builder(MainActivity.getsContext()).content("An error occurred while fetching your location.")
                                    .title("Warning")
                                    .positiveText("Try Again")
                                    .negativeText("Cancel")
                                    .theme(Theme.LIGHT)
                                    .callback(new MaterialDialog.ButtonCallback() {
                                        @Override
                                        public void onPositive(MaterialDialog dialog) {
                                            super.onPositive(dialog);
                                            Intent intent = new Intent(LocationService.this, DashBoardActivity.class);
                                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                            startActivity(intent);
                                        }

                                    }).show();
*/
                                    break;
                        }
                  }
            });
      }

      @Override
      public IBinder onBind(Intent intent) {
            // TODO: Return the communication channel to the service.
            return null;
      }

      @Override
      public void onCreate() {
            super.onCreate();
            if (mGoogleApiClient == null){
                  mGoogleApiClient = new GoogleApiClient.Builder(this)
                          .addConnectionCallbacks(this)
                          .addOnConnectionFailedListener(this)
                          .addApi(LocationServices.API)
                          .build();
            }
            if (mLocationRequest == null){
                  mLocationRequest = new LocationRequest();
                  mLocationRequest.setInterval(60000);
                  mLocationRequest.setFastestInterval(30000);
                  mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            }
            longLat = new LongLat();
      }

      @Override
      public int onStartCommand(Intent intent, int flags, int startId) {

            if (!mGoogleApiClient.isConnected()){
                  mGoogleApiClient.connect();
            }
            checkForLocationEnabled();
            return START_STICKY;
      }

      @Override
      public void onDestroy() {
            super.onDestroy();
      }

      @Override
      public void onLocationChanged(Location location) {
            setLocation(location);
      }

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
      public void onConnected(@Nullable Bundle bundle) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                  return;
            }
            Location mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
            setLocation(mLastLocation);
            startLocationUpdate();
      }

      @Override
      public void onConnectionSuspended(int i) {

      }

      @Override
      public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

      }
}
