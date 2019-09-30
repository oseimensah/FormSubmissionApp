package com.hensongeodata.technologies.phc.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.drawable.AnimatedStateListDrawable;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.google.gson.Gson;
import com.hensongeodata.technologies.phc.MyApplication;
import com.hensongeodata.technologies.phc.model.Form;
import com.hensongeodata.technologies.phc.model.Options;
import com.hensongeodata.technologies.phc.model.PreviewModel;
import com.hensongeodata.technologies.phc.model.answerjson.Answer;
import com.hensongeodata.technologies.phc.model.answerjson.Answers;
import com.hensongeodata.technologies.phc.view.JsonMain;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class FormDatabaseHelper extends SQLiteOpenHelper {
      private static final String TAG = FormDatabaseHelper.class.getSimpleName();

      //      Database Name
      private static final String DATABASE_NAME = "json_form_database";
      // Database Version
      private static final int DATABASE_VERSION = 1;
      // Define database and table properties
      private static final String TABLE_PAGE = "pages";
      private static final String TABLE_FIELDS = "fields";
      public static String TABLE_OPTIONS ="options";
      public static String TABLE_PREVIEW ="preview";
      public static String TABLE_ANSWER = "answer";

      public static String ID="id";

      //      Field Pages
      public static String REPEAT="repeat";
      public static String LABEL="label";
      public static String FORM_NAME="form_name";

      //Field fields
      public static String QUESTION_CODE="code";
      public static String QUESTION="label";
      public static String TYPE="type";
      public static String HINT="hint";
      public static String REQUIRED="required";

      //Options fields
      public static String OPTION="options";
      public static String OPTION_CODE="options_code";

      // Preview For Data
      public static String PREV_ANSWER = "prev_answer";

      // Answer field
      public static String SUBID = "submission_id";
      public static String SENT = "delivered";

      public FormDatabaseHelper(@Nullable Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
      }

      @Override
      public void onCreate(SQLiteDatabase db) {
            createTable(db, TABLE_PAGE);
            createTable(db, TABLE_FIELDS);
            createTable(db, TABLE_OPTIONS);
            createTable(db, TABLE_PREVIEW);
            createTable(db, TABLE_ANSWER);
      }

      @Override
      public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_PAGE + " ;");
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_FIELDS + " ;");
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_OPTIONS + " ;");
      }

      private boolean createTable(SQLiteDatabase db, String tableName) {
            if (tableName != null) {
                  if(tableName.trim().equalsIgnoreCase(TABLE_PAGE)) {
                        db.execSQL("CREATE TABLE IF NOT EXISTS " + tableName
                                + "(" + ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                                "" + FORM_NAME + " VARCHAR," +
                                "" + LABEL + " VARCHAR," +
                                "" + REPEAT + " VARCHAR,"+
                                "" + QUESTION_CODE + " INTEGER);");
                        return true;
                  }
                  else if(tableName.trim().equalsIgnoreCase(TABLE_FIELDS)){

                        db.execSQL("CREATE TABLE IF NOT EXISTS " + tableName
                                + "(" + ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                                "" + FORM_NAME + " VARCHAR," +
                                "" + QUESTION_CODE + " VARCHAR," +
                                "" + QUESTION + " VARCHAR," +
                                "" + HINT + " VARCHAR," +
                                "" + TYPE + " VARCHAR," +
                                "" + REQUIRED + " VARCHAR);");

                        return true;
                  }
                  else if(tableName.trim().equalsIgnoreCase(TABLE_OPTIONS)){

                        db.execSQL("CREATE TABLE IF NOT EXISTS " + tableName
                                + "(" + ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                                "" + FORM_NAME + " VARCHAR," +
                                "" + OPTION_CODE + " VARCHAR," +
                                "" + OPTION+ " VARCHAR);");

                        return true;
                  }
                  else if (tableName.trim().equalsIgnoreCase(TABLE_PREVIEW)){
                        db.execSQL("CREATE TABLE IF NOT EXISTS " + tableName
                                + "(" + ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                                "" + FORM_NAME + " VARCHAR," +
                                "" + QUESTION_CODE + " VARCHAR," +
                                "" + QUESTION + " VARCHAR," +
                                "" + PREV_ANSWER+ " VARCHAR);");

                        return true;
                  }
                  else if (tableName.trim().equalsIgnoreCase(TABLE_ANSWER)){
                        db.execSQL("CREATE TABLE IF NOT EXISTS " + tableName
                                + "(" + ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                                "" + SUBID + " VARCHAR," +
                                "" + FORM_NAME + " VARCHAR," +
                                "" + QUESTION_CODE + " VARCHAR," +
                                "" + PREV_ANSWER+ " VARCHAR," +
                                "" + SENT+ " INTEGER);");

                        return true;
                  }
            }
            return false;
      }

      //      Function to save form details
      public boolean saveForm(Context ctx, JSONObject jsonObject) throws JSONException {

            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues contentValues;

            String form_name = "";
            if (jsonObject.has("form"))
                  form_name = jsonObject.getString("form");

            JSONArray pageArray = jsonObject.optJSONArray(TABLE_PAGE);

            for (int a=0; a<pageArray.length(); a++){
                  JSONObject pageObject = pageArray.optJSONObject(a);
                  contentValues = new ContentValues();
                  contentValues.put(REPEAT, pageObject.optString(REPEAT));
                  contentValues.put(LABEL, pageObject.optString(LABEL));

//                  save fields details
                  JSONArray fieldArray = pageObject.optJSONArray(TABLE_FIELDS);
                  for (int b=0; b<fieldArray.length(); b++){
                        JSONObject fieldObject = fieldArray.optJSONObject(b);
                        ContentValues cv1 = new ContentValues();
                        String code = fieldObject.optString(QUESTION_CODE);
                        cv1.put(FORM_NAME, form_name);
                        cv1.put(QUESTION_CODE, code);
                        cv1.put(QUESTION, fieldObject.optString(QUESTION));
                        cv1.put(HINT, fieldObject.optString(HINT));
                        cv1.put(TYPE, fieldObject.optString(TYPE));
                        cv1.put(REQUIRED, fieldObject.optString(REQUIRED));

                        if (exists(TABLE_FIELDS, form_name, code ))
                              db.update(TABLE_FIELDS, cv1, QUESTION_CODE + "= ?" , new String[]{code});
                        else
                              db.insert(TABLE_FIELDS, null, cv1);

                        // options field
                        if (fieldObject.has("options")) {

                              JSONArray optionsArray = fieldObject.optJSONArray("options");
                              String arraylen = String.valueOf(optionsArray.length());

                              for (int c=0; c<optionsArray.length(); c++){
                                    JSONObject optionObject = optionsArray.optJSONObject(c);
                                    ContentValues cv = new ContentValues();
                                    String optionCode = form_name+code+optionObject.optString(ID);
                                    String option_form = form_name+code;
                                    cv.put(FORM_NAME, option_form);
                                    cv.put(OPTION_CODE, optionCode);
                                    cv.put(OPTION, optionObject.optString("value"));

                                    if (optionExist(optionCode)) {
                                          db.update(TABLE_OPTIONS, cv, OPTION_CODE + "=?", new String[]{optionCode});
                                    }
                                    else {
                                          db.insert(TABLE_OPTIONS, null, cv);
                                    }
                              }
                        }
                  }
            }
            db.close();
            return true;
      }

      public boolean saveTempAnswer(Context ctx, String form_code, String q_code, String question, String q_answer){

            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues cv = new ContentValues();

            if(form_code != null){
                  cv.put(FORM_NAME, form_code);
                  cv.put(QUESTION_CODE, q_code);
                  cv.put(QUESTION, question);
                  cv.put(PREV_ANSWER, q_answer);

                  if (exists(TABLE_PREVIEW, form_code, q_code))
                        db.update(TABLE_PREVIEW, cv, QUESTION_CODE + "= ?", new String[]{q_code});
                  else
                        db.insert(TABLE_PREVIEW, null, cv);

                  MyApplication myApplication = new MyApplication();
//                  myApplication.toastShortMessage(ctx, q_answer);
            }

            db.close();
            return true;

      }

      public boolean updateField(Context context, String form_code, String q_code, String q_answer){

            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            MyApplication application= new MyApplication();

            if (q_code != null && q_answer !=null && form_code != null){
                  values.put(PREV_ANSWER, q_answer);
                  values.put(QUESTION_CODE, q_code);

                  if (checkExit(QUESTION_CODE, TABLE_PREVIEW, q_code)) {
                        db.update(TABLE_PREVIEW, values, QUESTION_CODE + " = ?", new String[]{q_code});
                        application.toastLongMessage(context, "Saved");
                  }
                  else
                        application.toastLongMessage(context, "Not exist");
            }

            db.close();
            return true;
      }

      public boolean saveAnsweredForm(String submission_id, String form_code, String q_code, String q_answer){
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues cv = new ContentValues();

            if (form_code != null){
                  cv.put(SUBID, submission_id);
                  cv.put(FORM_NAME, form_code);
                  cv.put(QUESTION_CODE, q_code);
                  cv.put(PREV_ANSWER, q_answer);
                  cv.put(SENT, 0);

                  db.insert(TABLE_ANSWER, null, cv);
            }

            db.close();
            return true;
      }

      public boolean optionExist(Object itemID) {
            SQLiteDatabase db = this.getWritableDatabase();
            Cursor pointer = db.rawQuery("SELECT " + OPTION_CODE + " FROM " + TABLE_OPTIONS + " WHERE " + OPTION_CODE + "='" + itemID + "'", null);
            if (pointer.getCount() > 0) {
                  pointer.close();
                  return true;
            } else {
                  pointer.close();
                  return false;
            }
      }

      private boolean checkExit(String option, String table, Object option_code){
            SQLiteDatabase db = this.getWritableDatabase();
            Cursor pointer = db.rawQuery("SELECT " + option + " FROM " + table + " WHERE " + option + " = '" + option_code + "'", null);
            if (pointer.getCount() > 0) {
                  pointer.close();
                  return true;
            } else {
                  Log.e(TAG, "checkExit: "+ pointer.getCount() );
                  pointer.close();
                  return false;
            }
      }

      public boolean exists( String tableName, Object form_name, Object itemID) {

            SQLiteDatabase db = this.getWritableDatabase();
            Cursor pointer = db.rawQuery("SELECT " + QUESTION_CODE + " FROM " + tableName + " WHERE " +FORM_NAME + "='" +
                    form_name + "'" + " AND " + QUESTION_CODE + "='" + itemID + "'", null);
            if (pointer.getCount() > 0) {
                  pointer.close();
                  return true;
            } else {
                  pointer.close();
                  return false;
            }
      }

      private boolean dropTable(String table_name){
            SQLiteDatabase db = this.getWritableDatabase();
            db.execSQL("DROP TABLE IF EXISTS " + table_name + " ;");
            return true;
      }

      private boolean reCreateTable(String table_name){
            SQLiteDatabase db = this.getWritableDatabase();
            createTable(db, table_name);
            return true;
      }

      public List<Form> getForm(String column){

            List<Form> formData = new ArrayList<>();
            SQLiteDatabase db = this.getWritableDatabase();
            Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_FIELDS + " WHERE " + FORM_NAME + "='" + column + "'" + " ORDER BY '" + QUESTION_CODE + "' ASC;", null);
//            Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_FIELDS + ";", null);

            StringBuffer buffer = new StringBuffer();
            Form formModel = null;
            while (cursor.moveToNext()){
                  formModel = new Form();
                  String formName = cursor.getString(cursor.getColumnIndexOrThrow("form_name"));
                  String code = cursor.getString(cursor.getColumnIndexOrThrow("code"));
                  String label = cursor.getString(cursor.getColumnIndexOrThrow("label"));
                  String type = cursor.getString(cursor.getColumnIndexOrThrow("type"));
                  String hint = cursor.getString(cursor.getColumnIndexOrThrow("hint"));
                  String required = cursor.getString(cursor.getColumnIndexOrThrow("required"));

                  formModel.setForm_name(formName);
                  formModel.setLabel(label);
                  formModel.setHint(hint);
                  formModel.setType(type);
                  formModel.setCode(code);
                  formModel.setRequired(required);

//                  Log.e(TAG, "getForm: " + formModel.getForm_name() + ", " + formModel.getCode() );
                  buffer.append(formModel);

                  formData.add(formModel);
            }
            cursor.close();
            db.close();

            return formData;
      }

      public List<Options> getOptions(String option_code){
            List<Options> optionForm = new ArrayList<>();
            SQLiteDatabase db = this.getWritableDatabase();
            Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_OPTIONS + " WHERE "
                    + FORM_NAME + "='" + option_code + "';" , null);
            StringBuffer buffer = new StringBuffer();
            Options optionsModel = null;

            while( cursor.moveToNext()){
                  optionsModel = new Options();
                  String optionValue = cursor.getString(cursor.getColumnIndexOrThrow("options"));

//                  Log.e(TAG, "getForm: "+optionValue );

                  optionsModel.setOptionValue(optionValue);
                  buffer.append(optionsModel);
                  optionForm.add(optionsModel);
            }
            cursor.close();
            db.close();

            return optionForm;

      }

      public List<PreviewModel> getPreview(Context ctx, String form_name){

            List<PreviewModel> previewModelList = new ArrayList<>();
            SQLiteDatabase db = this.getWritableDatabase();
            Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_PREVIEW + " WHERE "
                    + FORM_NAME + "='" + form_name + "';" , null);
            StringBuffer buffer = new StringBuffer();
            MyApplication myApplication = new MyApplication();

            while (cursor.moveToNext()){
                  PreviewModel previewModel = new PreviewModel();
                  String question_num = cursor.getString(cursor.getColumnIndexOrThrow(QUESTION_CODE));
                  String question = cursor.getString(cursor.getColumnIndexOrThrow(QUESTION));
                  String answer = cursor.getString(cursor.getColumnIndexOrThrow(PREV_ANSWER));

                  previewModel.setForm_code(form_name);
                  previewModel.setQuestion_code(question_num);
                  previewModel.setQuestion(question);
                  previewModel.setQuestion_answer(answer);
                  buffer.append(previewModel);
                  previewModelList.add(previewModel);
            }
            cursor.close();
            db.close();

            return previewModelList;
      }

      public List<PreviewModel> getEditted(String form_name){
            List<PreviewModel> previewModelList = new ArrayList<>();
            SQLiteDatabase db = this.getWritableDatabase();
            Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_PREVIEW + " WHERE "
                    + FORM_NAME + "='" + form_name + "';" , null);
            StringBuffer buffer = new StringBuffer();
            while (cursor.moveToNext()){
                  PreviewModel previewModel = new PreviewModel();
                  String qustion_code = cursor.getString(cursor.getColumnIndexOrThrow(QUESTION_CODE));
                  String qustion_answer = cursor.getString(cursor.getColumnIndexOrThrow(PREV_ANSWER));

                  previewModel.setQuestion_code(qustion_code);
                  previewModel.setQuestion_answer(qustion_answer);
                  buffer.append(previewModel);
                  previewModelList.add(previewModel);
            }
            cursor.close();
            db.close();
            return previewModelList;
      }

      public String getResults(Context context){
            String json = null;
            FormDatabaseHelper formdb = null;
            formdb = new FormDatabaseHelper(context);
            Gson gson = new Gson();

            List<Answers> answersList = new ArrayList<Answers>();
            StringBuffer buffer = new StringBuffer();
            SQLiteDatabase db = formdb.getWritableDatabase();

            Cursor cursor = db.rawQuery("SELECT  * FROM " + TABLE_ANSWER + " WHERE " + SENT + " = '0';" , null);

            cursor.moveToFirst();
            int count = cursor.getCount();
            if(count>0){
                  Answer answer = new Answer();
                  String sub_id = cursor.getString(cursor.getColumnIndexOrThrow(SUBID));
                  answer.setForm_code(cursor.getString(cursor.getColumnIndexOrThrow(FORM_NAME)));
                  answer.setSub_id(sub_id);

                  cursor = db.rawQuery("SELECT * FROM " + TABLE_ANSWER +  " WHERE " + SUBID + " = '" + sub_id + "';", null);
                  while (cursor.moveToNext()){
                        Answers answers = new Answers();
                        String q_code = cursor.getString(cursor.getColumnIndexOrThrow(QUESTION_CODE));
                        String q_answer = cursor.getString(cursor.getColumnIndexOrThrow(PREV_ANSWER));
                        Log.e(TAG, "getResults: " + sub_id );
                        answers.setQuestion_code(q_code);
                        answers.setAnswer(q_answer);

                        buffer.append(answers);
                        answersList.add(answers);
                  }
                  answer.setSubmission(answersList);
                  json = gson.toJson(answer);
            }
            else
                  return null;

            cursor.close();
            db.close();

            Log.e(TAG, "getResults: " + json );

            return json;
      }

      public boolean updateData(Context ctx, String id, int status){
            SQLiteDatabase db = this.getWritableDatabase();
            boolean results = false;
            ContentValues values = new ContentValues();
            values.put(SUBID,id);
            values.put(SENT,status);

            if (id != null){
                  db.update(TABLE_ANSWER, values, SUBID + " = ?", new String[]{id});
                  Toast.makeText(ctx, "Updated", Toast.LENGTH_SHORT).show();

                  results = true;
            }
            db.close();
            return results;
      }

      public boolean checkData(Context context){

            boolean result = false;
            FormDatabaseHelper formdb = null;
            formdb = new FormDatabaseHelper(context);
            SQLiteDatabase db = formdb.getWritableDatabase();

            Cursor pointer = db.rawQuery("SELECT  " + SUBID + " FROM " + TABLE_ANSWER + " WHERE " + SENT + " = '0'" , null);

            if (pointer != null){
                  if (pointer.moveToFirst()){
                        String sub_code = pointer.getString(pointer.getColumnIndexOrThrow(SUBID));
                        Toast.makeText(context, "Yes " + sub_code, Toast.LENGTH_SHORT).show();
                        result = true;
                  }
            }

           if (pointer != null)
                 pointer.close();
            db.close();
            return result;

      }

}
