package com.hensongeodata.technologies.phc.view;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hensongeodata.technologies.phc.MyApplication;
import com.hensongeodata.technologies.phc.R;
import com.hensongeodata.technologies.phc.controller.FormRecyclerAdapter;
import com.hensongeodata.technologies.phc.database.FormDatabaseHelper;
import com.hensongeodata.technologies.phc.helpers.JsonConvert;
import com.hensongeodata.technologies.phc.model.Form;
import com.hensongeodata.technologies.phc.model.Holders;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

      private static final String TAG = MainActivity.class.getSimpleName();
      LinearLayout mainView;
      MyApplication myApplication;
      JsonConvert jsonConvert;
      TextView myTitle, myformTitle;

      FormRecyclerAdapter recyclerAdapter;
      FormDatabaseHelper formDatabaseHelper;
      RecyclerView recyclerView;
      List<Form> formList;

      private static final String extra1= "PHC FORM 1A";
      private static final String extra2= "PHC FORM 1B";

      @Override
      protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);

            init();

      }

      private void init(){
            mainView = findViewById(R.id.linearMain);
            myTitle = findViewById(R.id.tool_title);
            myformTitle = findViewById(R.id.form_title);
            recyclerView = findViewById(R.id.question_recycler);

            myApplication = new MyApplication();
            formDatabaseHelper = new FormDatabaseHelper(this);

            if (!myApplication.hasNetworkConnection(this)){
                  myApplication.showInternetError(this);
            }else {
                  myApplication.toastShortMessage(this, "Has Internet");
            }
            jsonConvert = new JsonConvert(this);
            String mycontent = getIntent().getStringExtra("form");

            String form_name = "";
            if (mycontent.equalsIgnoreCase(extra1)){
                  form_name = "form1.json";
//                  myApplication.toastLongMessage(this, form_name);
                  readJson(form_name);
            }
            else if (mycontent.equalsIgnoreCase(extra2)){
                  form_name = "form2.json";
//                  myApplication.toastLongMessage(this, form_name);
                  readJson(form_name);
            }

            String column_name = myTitle.getText().toString();

            formList = new ArrayList<Form>();
            formList = formDatabaseHelper.getForm(column_name);
            recyclerAdapter = new FormRecyclerAdapter(formList);
//            RecyclerView.LayoutManager reLayoutManager = new LinearLayoutManager(MainActivity.this);
//            recyclerView.setLayoutManager(reLayoutManager);
            LinearLayoutManager layoutManager = new LinearLayoutManager(MainActivity.this, LinearLayoutManager.HORIZONTAL, false);
            SnapHelper snapHelper = new PagerSnapHelper();
            recyclerView.setLayoutManager(layoutManager);
            snapHelper.attachToRecyclerView(recyclerView);
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.setAdapter(recyclerAdapter);
            recyclerView.setNestedScrollingEnabled(false);

      }

      private void createDynamicView(){
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT
            );
            LinearLayout ll1 = new LinearLayout(this);
            ll1.setLayoutParams(params);
            ll1.setOrientation(LinearLayout.VERTICAL);
            ll1.setBackgroundColor(getApplicationContext().getResources().getColor(R.color.colorGreyWhite));

            mainView.addView(ll1);
      }

      private void dynamicLayout(){

            final LinearLayout linearL = findViewById(R.id.mainLinear);
            int count = 0;
            // create the layout params that will be used to define how your
            // button will be displayed
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

            //Create four
            for (int j = 0; j <= 6; j++) {
                  if (count == 0){
                        // Create LinearLayout
                        LinearLayout ll = new LinearLayout(this);
                        ll.setOrientation(LinearLayout.HORIZONTAL);

                        // Create TextView
                        TextView product = new TextView(this);
                        product.setText(" Product" + j + "    ");
                        ll.addView(product);

                        // Create TextView
                        TextView price = new TextView(this);
                        price.setText("  $" + j + "     ");
                        ll.addView(price);

                        // Create Button
                        final Button btn = new Button(this);
                        // Give button an ID
                        btn.setId(j + 1);
                        btn.setText("Add To Cart");
                        // set the layoutParams on the button
                        btn.setLayoutParams(params);

                        final int index = j;
                        // Set click listener for button
                        btn.setOnClickListener(new View.OnClickListener() {
                              public void onClick(View v) {

                                    Log.i("TAG", "index :" + index);

                                    Toast.makeText(getApplicationContext(),
                                            "Clicked Button Index :" + index,
                                            Toast.LENGTH_LONG).show();

                              }
                        });

                        //Add button to LinearLayout
                        ll.addView(btn);
                        //Add button to LinearLayout defined in XML
                        linearL.addView(ll);
                  }
                  else if (count  == 1){
                        // Create LinearLayout
                        LinearLayout ll = new LinearLayout(this);
                        ll.setOrientation(LinearLayout.HORIZONTAL);

                        // Create TextView
                        TextView product = new TextView(this);
                        product.setText(" Product" + j + "    ");
                        ll.addView(product);

                        // Create TextView
                        TextView price = new TextView(this);
                        price.setText("  $" + j + "     ");
                        ll.addView(price);

                        // Create Button
                        final Button btn = new Button(this);
                        // Give button an ID
                        btn.setId(j + 1);
                        btn.setText("Add To Cart");
                        // set the layoutParams on the button
                        btn.setLayoutParams(params);

                        final int index = j;
                        // Set click listener for button
                        btn.setOnClickListener(new View.OnClickListener() {
                              public void onClick(View v) {

                                    Log.i("TAG", "index :" + index);

                                    Toast.makeText(getApplicationContext(),
                                            "Clicked Button Index :" + index,
                                            Toast.LENGTH_LONG).show();

                              }
                        });

                        //Add button to LinearLayout
                        ll.addView(btn);
                        //Add button to LinearLayout defined in XML
                        linearL.addView(ll);
                  }
            }

      }

      private void readJson(String form_name){

            jsonConvert.saveJson(MainActivity.this, form_name);
            if (Holders.getForm() != null){
                  myTitle.setText(Holders.getForm());
                  myformTitle.setText(Holders.getTitle());
            }

      }

}
