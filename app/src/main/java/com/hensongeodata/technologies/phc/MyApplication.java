package com.hensongeodata.technologies.phc;

import android.app.Application;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import androidx.appcompat.app.AlertDialog;
import android.util.Patterns;
import android.widget.EditText;
import android.widget.Toast;

public class MyApplication extends Application {
      private static String TAG=MyApplication.class.getSimpleName();
      public static Context context;
      public ProgressDialog dialog;

      @Override
      public void onCreate() {
            super.onCreate();
      }

      public boolean hasNetworkConnection(Context context){
            boolean hasWifi=false;
            boolean hasMobileData=false;

            ConnectivityManager connectivityManager= (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo[] networkInfos=connectivityManager.getAllNetworkInfo();

            for(NetworkInfo ni:networkInfos){
                  if(ni.getTypeName().equalsIgnoreCase("WIFI")){
                        if(ni.isConnected())
                              hasWifi=true;
                  }

                  if(ni.getTypeName().equalsIgnoreCase("MOBILE")){
                        if(ni.isConnected())
                              hasMobileData=true;
                  }
            }

            return hasMobileData|| hasWifi;
      }

      public void showMessage(Context context,String header,String message){
            new AlertDialog.Builder(context)
                    .setTitle(header)
                    .setMessage(message)
                    .setNegativeButton("OK", new DialogInterface.OnClickListener() {
                          @Override
                          public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                          }
                    })
                    .show();
      }

      public void showInternetError(Context context){
            new android.app.AlertDialog.Builder(context)
                    .setTitle("No internet connection.")
                    .setMessage("Please ensure you are connected to the internet and try again.")
                    .setNegativeButton("OK", new DialogInterface.OnClickListener() {
                          @Override
                          public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();
                          }
                    }).show();
      }

      public void toastShortMessage(Context ctx, String message){
            Toast.makeText(ctx, message, Toast.LENGTH_SHORT).show();
      }

      public void toastLongMessage(Context ctx, String message){
            Toast.makeText(ctx, message, Toast.LENGTH_LONG).show();
      }

      public boolean isEmailValid(String email) {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
      }

      public boolean textFieldEmpty(String text, EditText editText){
            if (text.isEmpty()){
                  editText.setError("Required Field");
                  editText.requestFocus();
                  return false;
            }
            return true;
      }

      public boolean phoneNumber(String inputText, EditText editText){
            if (!Patterns.PHONE.matcher(inputText).matches() || inputText.length() < 10) {
                  editText.requestFocus();
                  editText.setError("Incorrect Phone Number");
                  return false;
            }
            return true;
      }

      public boolean textFieldHasSpace(String text, EditText editText){
            if (text.matches(" ")){
                  editText.requestFocus();
                  editText.setError("Empty field (Required) ");
                  return false;
            }
            return true;
      }

      public void showDialog(Context context, String message){
            dialog = new ProgressDialog(context);
            dialog.setCanceledOnTouchOutside(false);
            dialog.setMessage(message);
            dialog.show();
      }

}
