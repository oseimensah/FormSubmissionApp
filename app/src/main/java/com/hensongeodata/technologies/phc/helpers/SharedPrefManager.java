package com.hensongeodata.technologies.phc.helpers;

import android.content.Context;

public class SharedPrefManager {

      private static SharedPrefManager mInstance;
      private static Context mCtx;

      private static final String SHARED_PREF_NAME = "censususer";

      private static final String KEY_USER_FIRSTNAME = "keyfirstnamename";
      private static final String KEY_USER_EMAIL = "keyuseremail";

      public SharedPrefManager(Context context) {
            mCtx = context;
      }

      public static synchronized SharedPrefManager getInstance(Context context) {
            if (mInstance == null) {
                  mInstance = new SharedPrefManager(context);
            }
            return mInstance;
      }

}
