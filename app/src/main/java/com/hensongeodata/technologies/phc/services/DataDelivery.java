package com.hensongeodata.technologies.phc.services;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.JobIntentService;

import com.hensongeodata.technologies.phc.MyApplication;
import com.hensongeodata.technologies.phc.database.FormDatabaseHelper;
import com.hensongeodata.technologies.phc.helpers.NetworkRequest;

public class DataDelivery extends Service {

      private static final String TAG =DataDelivery.class.getSimpleName();
      private static final int JOB_ID = 1000;
      private Context context;
      private FormDatabaseHelper formDatabaseHelper;
      public Runnable mRunnable = null;
      MyApplication myApplication;
      NetworkRequest networkRequest;

      public DataDelivery() {
      }

      @Override
      public IBinder onBind(Intent intent) {
            // TODO: Return the communication channel to the service.
            return null;
      }

      @Override
      public void onCreate() {
            super.onCreate();
            myApplication = new MyApplication();
            networkRequest = new NetworkRequest(this);
      }

      @Override
      public int onStartCommand(Intent intent, int flags, int startId) {

            final Handler mHandler = new Handler();

                  mRunnable = new Runnable() {
                        @Override
                        public void run() {
                              formDatabaseHelper = new FormDatabaseHelper(DataDelivery.this);
                              networkRequest = new NetworkRequest(DataDelivery.this);
                              if (myApplication.hasNetworkConnection(DataDelivery.this)) {
//                                    boolean isInfoThere = formDatabaseHelper.checkData(getApplicationContext());
                                    String json = formDatabaseHelper.getResults(getApplicationContext());
                                    if (json != null){
                                          networkRequest.sendData(json);
                                    }
                                    else {
                                          Log.d(TAG, "run: No Data to send");
                                          myApplication.toastShortMessage(getApplicationContext(), "No Data to process");
                                    }

                              }
                              else
                                    myApplication.showInternetError(getApplicationContext());

                              mHandler.postDelayed(mRunnable, 10 * 1000);
                        }
                  };
                  mHandler.postDelayed(mRunnable, 20 * 1000);

            return START_NOT_STICKY;
      }
}
