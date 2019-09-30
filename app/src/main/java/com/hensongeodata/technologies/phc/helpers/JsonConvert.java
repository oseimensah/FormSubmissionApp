package com.hensongeodata.technologies.phc.helpers;

import android.content.Context;

import com.hensongeodata.technologies.phc.MyApplication;
import com.hensongeodata.technologies.phc.database.FormDatabaseHelper;
import com.hensongeodata.technologies.phc.model.Holders;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;

public class JsonConvert {
      Context context;
      FormDatabaseHelper formDatabaseHelper;

      private static final String TAG = JsonConvert.class.getSimpleName();
      MyApplication myApplication;

      public JsonConvert(Context context) {
            this.context = context;
            formDatabaseHelper = new FormDatabaseHelper(context);
            myApplication = new MyApplication();
      }

      public String loadJsonFromAsset(Context ctx, String jsonFile){
            String json = null;
//            myApplication = new MyApplication();
            try {
                  InputStream inputStream = ctx.getAssets().open(jsonFile);
                  int size = inputStream.available();
                  byte[] buffer = new byte[size];
                  inputStream.read(buffer);
                  inputStream.close();
                  json = new String(buffer, "UTF-8");

            } catch (IOException e) {
                  e.printStackTrace();
                  return null;
            }
            return json;
      }

      public void saveJson(Context ctx, String filename){
            JSONObject jObject = null;
//            myApplication = new MyApplication();
            try {
                  jObject = new JSONObject(loadJsonFromAsset(ctx, filename));
                  if (jObject != null){
                        JSONObject fileObject = jObject.optJSONObject("PHC");

                       if (fileObject.has("form")){
                              String form = fileObject.getString("form");
                              String title = fileObject.getString("title");
                              Holders.setForm(form);
                              Holders.setTitle(title);
                        }
                        else
                              myApplication.toastLongMessage(ctx, "Has No Form Name");

                        formDatabaseHelper.saveForm(ctx, fileObject);
                  }

            } catch (JSONException e) {
                  e.printStackTrace();
            }
      }
}
