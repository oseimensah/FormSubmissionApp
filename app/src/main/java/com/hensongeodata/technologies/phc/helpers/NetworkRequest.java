package com.hensongeodata.technologies.phc.helpers;

import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.hensongeodata.technologies.phc.MyApplication;
import com.hensongeodata.technologies.phc.controller.ApiInteface;
import com.hensongeodata.technologies.phc.database.FormDatabaseHelper;
import com.hensongeodata.technologies.phc.model.Holders;
import com.hensongeodata.technologies.phc.model.Result;
import com.hensongeodata.technologies.phc.model.SendData;

import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NetworkRequest {
      private String TAG = NetworkRequest.class.getSimpleName();
      private Context context;
      public ProgressDialog dialog;
      MyApplication myApplication;
      FormDatabaseHelper formDatabaseHelper;

      public NetworkRequest(Context context) {
            this.context = context;
      }

      public void sendLocscast(final Context ctx, String longitude, String latitude){
            myApplication = new MyApplication();
            Retrofit.Builder builder = new Retrofit.Builder()
                    .baseUrl(Keys.BASE)
                    .addConverterFactory(GsonConverterFactory.create());
            Retrofit sendlocs = builder.build();

            ApiInteface apiInteface = sendlocs.create(ApiInteface.class);
            Call<Result> locCall = apiInteface.latlong(longitude, latitude);
            Log.e(TAG, "sendLocscast: "+ longitude  + ", " + latitude );
            locCall.enqueue(new Callback<Result>() {
                  @Override
                  public void onResponse(Call<Result> call, Response<Result> response) {
                        if (response.isSuccessful()){
                              myApplication.toastShortMessage(ctx, "Sent");
                        }
                  }
                  @Override
                  public void onFailure(Call<Result> call, Throwable t) {
                        myApplication.toastShortMessage(ctx, "Error: " + t.getMessage());
                  }
            });
      }

      public void sendData(final String jsonObject){

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(Keys.BASE_SEND)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            ApiInteface api = retrofit.create(ApiInteface.class);
            Call<SendData> call = api.postRawJSON(jsonObject);
            call.enqueue(new Callback<SendData>() {
                  @Override
                  public void onResponse(Call<SendData> call, Response<SendData> response) {

                        if (response.isSuccessful()){
                              if (response.body().getStatus().toString().equalsIgnoreCase("success")){
                                    Toast.makeText(context, "Sent All", Toast.LENGTH_SHORT).show();
                                    JSONObject jObject = null;
                                    try {
                                          jObject = new JSONObject(String.valueOf(jsonObject));
                                          if (jObject != null){

                                                if (jObject.has("sub_id")){
                                                      String subid = jObject.getString("sub_id");
                                                      Toast.makeText(context, "Data " + subid, Toast.LENGTH_SHORT).show();
                                                      boolean result = formDatabaseHelper.updateData(context, subid, 1);
                                                }
                                                else
                                                      myApplication.toastLongMessage(context, "Has No Form Name");

                                          }

                                    } catch (JSONException e) {
                                          e.printStackTrace();
                                    }
                              }
                              else
                                    Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show();
                        }
                  }

                  @Override
                  public void onFailure(Call<SendData> call, Throwable t) {
                        Toast.makeText(context, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                  }
            });

      }
}
