package com.hensongeodata.technologies.phc.view;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.hensongeodata.technologies.phc.MyApplication;
import com.hensongeodata.technologies.phc.R;
import com.hensongeodata.technologies.phc.database.FormDatabaseHelper;
import com.hensongeodata.technologies.phc.helpers.JsonConvert;
import com.hensongeodata.technologies.phc.services.DataDelivery;
import com.hensongeodata.technologies.phc.services.LocscastService;
import com.karumi.dexter.BuildConfig;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.util.List;

public class StartPage extends AppCompatActivity {
      private static final String TAG = StartPage.class.getSimpleName();
      public static final String mBroadcastStringLong = "com.hensongeodata.broadcast.integer";
      MyApplication myApplication;
      JsonConvert jsonConvert;
      FormDatabaseHelper formDatabaseHelper;

      TextView myTitle,downTitle;
      ListView myList;
      Button checkbutton;

      List<String> questions;

      @Override
      protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_start_page);

            init();
      }

      private void init(){
            myTitle = findViewById(R.id.tool_title);
            downTitle = findViewById(R.id.form_title);
            checkbutton = findViewById(R.id.check_data);
            myTitle.setText(this.getString(R.string.PHC));
            myTitle.setPadding(0, 20, 0,0);
            downTitle.setVisibility(View.GONE);

            myApplication = new MyApplication();
            jsonConvert = new JsonConvert(this);
            formDatabaseHelper = new FormDatabaseHelper(this);

            myList = findViewById(R.id.content_list);

            checkPermission();

            String[] values = new String[] {
                    "PHC FORM 1A",
                    "PHC FORM 1B"
            };

            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                    android.R.layout.simple_list_item_1, android.R.id.text1, values);

            final String form_code = "PHC FORM 1A";

            // Assign adapter to ListView
            myList.setAdapter(adapter);

            // ListView Item Click Listener
            myList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                  @Override
                  public void onItemClick(AdapterView<?> parent, View view,
                                          int position, long id) {

                        // ListView Clicked item index
                        int itemPosition = position;
                        // ListView Clicked item value
                        String  itemValue    = (String) myList.getItemAtPosition(itemPosition);
//                        myApplication.toastLongMessage(StartPage.this, itemValue);
                        Intent newIntent = new Intent(getApplicationContext(), JsonMain.class);
                        newIntent.putExtra("form", itemValue);
                        startActivity(newIntent);
                  }

            });

            checkbutton.setOnClickListener(new View.OnClickListener() {
                  @Override
                  public void onClick(View v) {
//                        readData(form_code);
//                        startService(new Intent(getBaseContext(), LocscastService.class));
                        startService(new Intent(StartPage.this, DataDelivery.class));
                  }
            });

//            startService(new Intent(getBaseContext(), DataDelivery.class));
      }

      private void readJson(String form_name){
            jsonConvert.saveJson(StartPage.this, form_name);
      }

      private void readData(){
            formDatabaseHelper.getResults(this);
      }

      private BroadcastReceiver mReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                  if (intent.getAction().equals(mBroadcastStringLong )) {
                        if (intent.getStringExtra("DataLat") != null){
                              myApplication.toastShortMessage(StartPage.this, "Latitude is here");
                        }else
                              myApplication.toastShortMessage(StartPage.this, "No message");
//                        String latitude = intent.getStringExtra("DataLat");
//                        String longitude = intent.getStringExtra("DataLon");
//
//                        myApplication.toastShortMessage(StartPage.this, latitude);
//                        networkCalls.sendLocscast(latitude, longitude);
                  }
            }
      };

      public void checkPermission(){

            // Requesting ACCESS_FINE_LOCATION using Dexter library
            Dexter.withActivity(this)
                    .withPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                    .withListener(new PermissionListener() {
                          @Override
                          public void onPermissionGranted(PermissionGrantedResponse response) {

                          }

                          @Override
                          public void onPermissionDenied(PermissionDeniedResponse response) {
                                if (response.isPermanentlyDenied()) {
                                      // open device settings when the permission is
                                      // denied permanently
                                      openSettings();
                                }
                          }

                          @Override
                          public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
                                token.continuePermissionRequest();
                          }
                    }).check();

      }

      private boolean checkPermissions() {
            int permissionState = ActivityCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION);
            return permissionState == PackageManager.PERMISSION_GRANTED;
      }

      private void openSettings() {

            checkPermissions();
            Intent intent = new Intent();
            intent.setAction(
                    Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            Uri uri = Uri.fromParts("package",
                    BuildConfig.APPLICATION_ID, null);
            intent.setData(uri);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
      }
}
