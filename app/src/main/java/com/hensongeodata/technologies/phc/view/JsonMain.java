package com.hensongeodata.technologies.phc.view;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import com.google.android.material.tabs.TabLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.gson.Gson;
import com.hensongeodata.technologies.phc.MyApplication;
import com.hensongeodata.technologies.phc.R;
import com.hensongeodata.technologies.phc.adapters.BlankFragment;
import com.hensongeodata.technologies.phc.adapters.PreviewRecyclerAdapter;
import com.hensongeodata.technologies.phc.database.FormDatabaseHelper;
import com.hensongeodata.technologies.phc.helpers.JsonConvert;
import com.hensongeodata.technologies.phc.model.Form;
import com.hensongeodata.technologies.phc.model.Holders;
import com.hensongeodata.technologies.phc.model.PreviewModel;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

public class JsonMain extends AppCompatActivity {

      private static final String TAG = JsonMain.class.getSimpleName();

      private TabLayout tabLayout;
      private ViewPager viewPager;
      private RecyclerView recyclerView;
      View previewLayout;
      List<Form> formList;
      FormDatabaseHelper formDatabaseHelper;
      PreviewRecyclerAdapter recyclerAdapter;
      MyApplication myApplication;
      JsonConvert jsonConvert;
      AlertDialog alertDialog;

      TextView myTitle, myformTitle;
      Button nextButton, btnSaveData;
      ImageButton prev_close;
      private static final String extra1= "PHC FORM 1A";
      private static final String extra2= "PHC FORM 1B";

      //Fragment List
      private final List<Fragment> mFragmentList = new ArrayList<>();
      //Title List
      private final List<String> mFragmentTitleList = new ArrayList<>();
      // Question List
      private final List<String> mFragmentQuestion = new ArrayList<>();
      // Question List
      private final List<String> mFragmentHint = new ArrayList<>();
      //  Question Type
      private final List<String> mFragmentQuestionType = new ArrayList<>();
      //    Question Required
      private List<String> mFragmentRequired = new ArrayList<>();
      // Preview
      List<PreviewModel> previewModels;
      List<PreviewModel> previewModel;

      List<String> arrPackage = new ArrayList<>();
      SharedPreferences sharedPreferences;

      private ViewPagerAdapter adapter;

      @Override
      protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_json_main);

            init();
      }

      private void init(){
            viewPager = (ViewPager) findViewById(R.id.fragmentTab);
            tabLayout = (TabLayout) findViewById(R.id.tablay);
            myTitle = findViewById(R.id.tool_title);
            myformTitle = findViewById(R.id.form_title);
            nextButton = findViewById(R.id.savePreview);
            previewLayout = findViewById(R.id.preview_model);
            recyclerView = findViewById(R.id.preview_list);
            prev_close = findViewById(R.id.preview_close);
            btnSaveData = findViewById(R.id.saveData);

            nextButton.setVisibility(View.GONE);
            previewLayout.setVisibility(View.GONE);
            myApplication = new MyApplication();
            jsonConvert = new JsonConvert(this);
            formDatabaseHelper = new FormDatabaseHelper(this);
            previewModels = new ArrayList<PreviewModel>();
            recyclerView.setAdapter(recyclerAdapter);

            prev_close.setOnClickListener(new View.OnClickListener() {
                  @Override
                  public void onClick(View v) {
                        previewLayout.setVisibility(View.GONE);
                        nextButton.setVisibility(View.VISIBLE);
                  }
            });

            sharedPreferences = getSharedPreferences("USER",MODE_PRIVATE);

            String mycontent = getIntent().getStringExtra("form");
            String form_name = "";
            if (mycontent.equalsIgnoreCase(extra1)){
                  form_name = "form1.json";
                  readJson(form_name);
            }
            else if (mycontent.equalsIgnoreCase(extra2)){
                  form_name = "form2.json";
                  readJson(form_name);
            }

            String column_name = myTitle.getText().toString();
            formList = formDatabaseHelper.getForm(column_name);
            loaddata();
//            pageChange();

      }

      private void readJson(String form_name){

            jsonConvert.saveJson(JsonMain.this, form_name);
            if (Holders.getForm() != null){
                  myTitle.setText(Holders.getForm());
                  myformTitle.setText(Holders.getTitle());
            }

      }

      //Loading the JSON data, which will be used to create dynamically content
      //such as tabs and info for the Fragments
      private void loaddata() {
            //create multiple titles, but use OneFragment() for every new tab
            for(int i = 0; i < formList.size(); i++) {
                  Form form = formList.get(i);
                  mFragmentList.add(new BlankFragment());
                  mFragmentTitleList.add(form.getCode());
                  mFragmentQuestion.add(form.getLabel());
                  mFragmentQuestionType.add(form.getType());
                  mFragmentHint.add(form.getHint());
                  mFragmentRequired.add(form.getRequired());
            }
            setupViewPager(viewPager);
            tabLayout.setupWithViewPager(viewPager);
            // Tab ViewPager setting
            viewPager.setOffscreenPageLimit(1);
            if (tabLayout.getTabCount() == 2) {
                  tabLayout.setTabMode(TabLayout.MODE_FIXED);
            } else {
                  tabLayout.setTabMode(TabLayout.
                          MODE_SCROLLABLE);
            }
            viewPager.setOffscreenPageLimit(mFragmentList.size());
            tabLayout.setupWithViewPager(viewPager);
      }

      public void savePref(String q_code, String value){
            arrPackage.add(q_code);
            arrPackage.add(value);

            myApplication.toastLongMessage(JsonMain.this, value);

            Gson gson = new Gson();
            String json = gson.toJson(arrPackage);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("Set", json);
            editor.apply();
      }

      public void saveData(String form_code, String q_code, String question, String q_answer){
            formDatabaseHelper.saveTempAnswer(JsonMain.this, form_code, q_code, question, q_answer);
      }

      public void saveAnswerData(final String form_name){
            previewModel = formDatabaseHelper.getEditted(form_name);

            AlertDialog.Builder builder = new AlertDialog.Builder(JsonMain.this);
            builder.setCancelable(false)
                    .setTitle("Are you sure all answers are correct?")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                          public void onClick(DialogInterface dialog, int id) {
                                String sub_code = md5(form_name);
                                for (int i=0; i<previewModel.size(); i++){
                                      PreviewModel prevMode = previewModel.get(i);
                                      String q_code = prevMode.getQuestion_code();
                                      String q_answer = prevMode.getQuestion_answer();

                                      formDatabaseHelper.saveAnsweredForm(sub_code, form_name, q_code, q_answer);
                                }

                                finish();
                                startActivity(new Intent(getApplicationContext(), StartPage.class));
                                overridePendingTransition(R.anim.slide_out_left, R.anim.slide_in_right );

                          }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                          public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                          }
                    });
            AlertDialog alert = builder.create();
            alert.show();

      }

      public String md5(String s) {
            try {
                  // Create MD5 Hash
                  MessageDigest digest = java.security.MessageDigest.getInstance("MD5");
                  digest.update(s.getBytes());
                  byte messageDigest[] = digest.digest();

                  // Create Hex String
                  StringBuffer hexString = new StringBuffer();
                  for (int i=0; i<messageDigest.length; i++)
                        hexString.append(Integer.toHexString(0xFF & messageDigest[i]));

                  return hexString.toString();
            }catch (NoSuchAlgorithmException e) {
                  e.printStackTrace();
            }
            return "";
      }

      @SuppressLint("ClickableViewAccessibility")
      private void setupViewPager(ViewPager viewPager) {
            String form_code = myTitle.getText().toString();
            adapter = new ViewPagerAdapter(getSupportFragmentManager(), mFragmentList, mFragmentTitleList, mFragmentQuestion,
                    mFragmentQuestionType, mFragmentHint,mFragmentRequired, form_code);
            viewPager.setAdapter(adapter);

            viewPager.setOnTouchListener(new View.OnTouchListener() {
                  @Override
                  public boolean onTouch(View v, MotionEvent event) {
                        return true;
                  }
            });
      }

      public void pageChange(final ImageButton prev, final ImageButton next){
            viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                  @Override
                  public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                  }

                  @Override
                  public void onPageSelected(int position) {

                        if (position < 1){
                              prev.setVisibility(View.GONE);
                              next.setVisibility(View.VISIBLE);
                              nextButton.setVisibility(View.GONE);
                        }
                        else if(position < (formList.size() - 1)){
                              String size = String.valueOf(formList.size());
                              prev.setVisibility(View.VISIBLE);
                              next.setVisibility(View.VISIBLE);
                              nextButton.setVisibility(View.GONE);
                        }
                        else if (position == (formList.size() - 1)){
                              String size = String.valueOf(formList.size());
                              prev.setVisibility(View.VISIBLE);
                              next.setVisibility(View.GONE);
                              nextButton.setVisibility(View.VISIBLE);
                        }
                  }

                  @Override
                  public void onPageScrollStateChanged(int state) {

                  }
            });
      }

      public void prev() {
            viewPager.setCurrentItem(viewPager.getCurrentItem() - 1, true);
      }

      public void next() {
            viewPager.setCurrentItem(viewPager.getCurrentItem() + 1, true);
      }

      public void createCustom(String form_name){
            nextButton.setVisibility(View.GONE);
            previewLayout.setVisibility(View.VISIBLE);

            previewModels = formDatabaseHelper.getPreview(JsonMain.this, form_name);
            recyclerAdapter = new PreviewRecyclerAdapter(previewModels, JsonMain.this);

            RecyclerView.LayoutManager reLayoutManager = new LinearLayoutManager(JsonMain.this);
            recyclerView.setLayoutManager(reLayoutManager);
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.setAdapter(recyclerAdapter);
//            recyclerView.addItemDecoration(itemDecorator);

            if (recyclerAdapter.getItemCount() == 0){
                  myApplication.toastLongMessage(getApplicationContext(), "Recycler is empty");
            }else {
                  String total = String.valueOf(recyclerAdapter.getItemCount());
//                  myApplication.toastLongMessage(getApplicationContext(), total);
            }
      }

      //ViewPagerAdapter settings
      class ViewPagerAdapter extends FragmentPagerAdapter {
            private String form_code;
            private List<Fragment> mFragmentList = new ArrayList<>();
            private List<String> mFragmentTitleList = new ArrayList<>();
            private List<String> mFragmentQuestion = new ArrayList<>();
            private List<String> mFragmentHint = new ArrayList<>();
            private List<String> mFragmentQuestionType = new ArrayList<>();
            private List<String> mFragmentRequired = new ArrayList<>();

            public ViewPagerAdapter( FragmentManager fm, List<Fragment> fragments, List<String> titleLists, List<String> questions,
                                     List<String> questionType, List<String> questionHint,List<String> mFragmentRequired, String form_code) {
                  super(fm);
                  this.form_code = form_code;
                  this.mFragmentList = fragments;
                  this.mFragmentTitleList = titleLists;
                  this.mFragmentQuestion = questions;
                  this.mFragmentQuestionType = questionType;
                  this.mFragmentHint = questionHint;
                  this.mFragmentRequired = mFragmentRequired;
            }

            @Override
            public Fragment getItem(int position) {
//                  return mFragmentList.get(position);
                  return BlankFragment.newInstance(mFragmentQuestion.get(position), mFragmentTitleList.get(position),
                          mFragmentQuestionType.get(position), mFragmentHint.get(position), mFragmentRequired.get(position), form_code);
            }


            @Override
            public int getCount() {
                  return mFragmentList == null ? 0 : mFragmentList.size();
            }

            @Override
            public CharSequence getPageTitle(int position) {
                  return mFragmentTitleList.get(position);
            }


      }
}
