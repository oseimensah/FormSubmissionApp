package com.hensongeodata.technologies.phc.adapters;

import android.content.Context;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AlertDialog;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.hensongeodata.technologies.phc.MyApplication;
import com.hensongeodata.technologies.phc.R;
import com.hensongeodata.technologies.phc.database.FormDatabaseHelper;
import com.hensongeodata.technologies.phc.model.Options;
import com.hensongeodata.technologies.phc.view.JsonMain;

import java.util.List;

public class BlankFragment extends Fragment{

      private static final String ARG_PARAM1= "question";
      private static final String ARG_PARAM2= "question_code";
      private static final String ARG_PARAM3= "question_type";
      private static final String ARG_PARAM4= "question_hint";
      private static final String ARG_PARAM5= "question_required";
      private static final String ARG_PARAM6= "form_code";

      private static final String TAG = BlankFragment.class.getSimpleName();
      private FormDatabaseHelper dbHelper;
      private MyApplication myApplication;
      List<Options> optionList;
      private OnFragmentInteractionListener mListener;
      LayoutInflater inflater;

      LinearLayout baseView, checkboxLay, textArea, textViewArea, areaRadio;
      TextView question_text, txtHint;
      ImageButton imgPrevButton, imgNextButton;
      Button nextButton,btnSaveData;
      EditText editText, editTextbig;
      RadioGroup radioGroup;
      View viewgroup;

      String question = "one goal";
      String q_code = "one goal";
      String q_type = "one goal";
      String q_hint = "one goal";
      String q_required = "one goal";
      String form_name = "form_name";

      int checkCount = 0;

      public static BlankFragment newInstance(String param1, String param2, String param3, String param4, String param5, String param6) {
            BlankFragment fragment = new BlankFragment();
            Bundle args = new Bundle();
            args.putString(ARG_PARAM1, param1);
            args.putString(ARG_PARAM2, param2);
            args.putString(ARG_PARAM3, param3);
            args.putString(ARG_PARAM4, param4);
            args.putString(ARG_PARAM5, param5);
            args.putString(ARG_PARAM6, param6);
            fragment.setArguments(args);
            return fragment;
      }

      @Override
      public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            if (getArguments() != null) {
//                  mParam1 = getArguments().getString(ARG_PARAM1);
//                  mParam2 = getArguments().getString(ARG_PARAM2);
            }
      }

      @Override
      public View onCreateView(LayoutInflater inflater, ViewGroup container,
                               Bundle savedInstanceState) {
            // Inflate the layout for this fragment
            viewgroup = inflater.inflate(R.layout.fragment_blank, container, false);

            question = getArguments().getString(ARG_PARAM1);
            q_code = getArguments().getString(ARG_PARAM2);
            q_type = getArguments().getString(ARG_PARAM3);
            q_hint = getArguments().getString(ARG_PARAM4);
            q_required = getArguments().getString(ARG_PARAM5);
            form_name = getArguments().getString(ARG_PARAM6);

           init();

            return viewgroup;
      }

      private void init(){
            question_text = viewgroup.findViewById(R.id.real_questions);
            txtHint = viewgroup.findViewById(R.id.txt_hint);
            baseView = viewgroup.findViewById(R.id.baseView);
            textViewArea = viewgroup.findViewById(R.id.textarea);
            textArea = viewgroup.findViewById(R.id.textareabig);
            areaRadio = viewgroup.findViewById(R.id.model_radio);
            checkboxLay = viewgroup.findViewById(R.id.model_checkbox);
            radioGroup = viewgroup.findViewById(R.id.radio_group);

            //    Edit text boxes
            editText = viewgroup.findViewById(R.id.et_answer_text);
            editTextbig = viewgroup.findViewById(R.id.et_answer_textarea);

            imgNextButton = (ImageButton) viewgroup.findViewById(R.id.imgNextButFrag);
            imgPrevButton = (ImageButton) viewgroup.findViewById(R.id.imgPrevButFrag);
            nextButton = (Button) getActivity().findViewById(R.id.savePreview);
            btnSaveData = (Button) getActivity().findViewById(R.id.saveData);

            dbHelper = new FormDatabaseHelper(getContext());
            myApplication = new MyApplication();

            question_text.setText("" + q_code + ". "+ question);
            txtHint.setText("" + q_hint);
            textArea.setVisibility(View.GONE);
            textViewArea.setVisibility(View.GONE);
            areaRadio.setVisibility(View.GONE);
            checkboxLay.setVisibility(View.GONE);

            imgPrevButton.setVisibility(View.GONE);

            String text = "";

            if (q_type.equalsIgnoreCase("text")){
                  textViewArea.setVisibility(View.VISIBLE);
            }
            else if (q_type.equalsIgnoreCase("textarea")){
                  textArea.setVisibility(View.VISIBLE);
            }
            else if (q_type.equalsIgnoreCase("radio")){
                  areaRadio.setVisibility(View.VISIBLE);
                  String option_code = form_name+q_code;
                  optionList = dbHelper.getOptions(option_code);
                  addRadioButtons();
            }
            else if (q_type.equalsIgnoreCase("checkbox")){
                  checkboxLay.setVisibility(View.VISIBLE);
                  String option_code = form_name+q_code;
                  optionList = dbHelper.getOptions(option_code);
                  addCheckBox();
            }
            else if (q_type.equalsIgnoreCase("numeric")){
                  textViewArea.setVisibility(View.VISIBLE);
                  editText.setInputType(InputType.TYPE_CLASS_PHONE);
            }

            ((JsonMain)getActivity()).pageChange(imgPrevButton, imgNextButton);

            imgNextButton.setOnClickListener(new View.OnClickListener() {
                  @Override
                  public void onClick(View v) {
                        if (q_type.equalsIgnoreCase("text")) {
                              String text = editText.getText().toString();
                              checkNext(text, editText);
                        }
                        else if(q_type.equalsIgnoreCase("textarea")){
                              String text = editTextbig.getText().toString();
                              checkNext(text, editTextbig);
                        }
                        else if (q_type.equalsIgnoreCase("radio")){
                              if (radioGroup.getCheckedRadioButtonId() == -1){
                                    myApplication.toastLongMessage(getContext(), "Required Field");
                              }
                              else {
//                                    ((JsonMain)getActivity()).saveData(form_name, q_code, question, textarea);
                                    ((JsonMain) getActivity()).next();
                              }
                        }
                        else if (q_type.equalsIgnoreCase("numeric")) {
                              String text = editText.getText().toString();
                              checkNext(text, editText);
                        }

                  }
            });

            imgPrevButton.setOnClickListener(new View.OnClickListener() {
                  @Override
                  public void onClick(View v) {
                        ((JsonMain)getActivity()).prev();
                  }
            });

            nextButton.setOnClickListener(new View.OnClickListener() {
                  @Override
                  public void onClick(View v) {
                        ((JsonMain)getActivity()).createCustom(form_name);
                  }
            });

            btnSaveData.setOnClickListener(new View.OnClickListener() {
                  @Override
                  public void onClick(View v) {
                        ((JsonMain)getActivity()).saveAnswerData(form_name);
                  }
            });

      }
      private void checkNext(String textarea, EditText editTextBox){
            if (q_required.equalsIgnoreCase("true")){
                  if (!myApplication.textFieldEmpty(textarea, editTextBox) || !myApplication.textFieldHasSpace(textarea, editTextBox)){
                        Log.d(TAG, "checkNext: Error");
                  }
                  else {
                        ((JsonMain)getActivity()).saveData(form_name, q_code, question, textarea);
//                        ((JsonMain)getActivity()).savePref(q_code, textarea);
                        ((JsonMain) getActivity()).next();
                  }
            }
            else {
//                  ((JsonMain)getActivity()).savePref(q_code, textarea);
                  ((JsonMain)getActivity()).saveData(form_name, q_code, question, textarea);
                  ((JsonMain) getActivity()).next();
            }
      }

      private void addRadioButtons(){

            Options formOptions = new Options();
            for (int row=0; row<1; row++){
                  radioGroup.setOrientation(LinearLayout.VERTICAL);

                  for (int i = 0;  i< optionList.size(); i++){
                        formOptions = optionList.get(i);
                        RadioButton rbn = new RadioButton(getContext());
                        rbn.setId(View.generateViewId());
                        rbn.setText(formOptions.getOptionValue());
                        radioGroup.addView(rbn);
                  }
//                  radioGroup.setOnCheckedChangeListener(handleRadio(rbn));
                  radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(RadioGroup group, int checkedId) {
                              for (int i=0; i<group.getChildCount(); i++){
                                    RadioButton btn = (RadioButton) group.getChildAt(i);
                                    if (btn.getId() == checkedId){
                                          String text = btn.getText().toString();
//                                          ((JsonMain)getActivity()).savePref(q_code, text);
                                          ((JsonMain)getActivity()).saveData(form_name,q_code, question, text);
                                          myApplication.toastShortMessage(getContext(), "Radio clicked: " + text);
                                    }
                              }
                        }
                  });
            }
      }

      private void addCheckBox(){

            Options formOptions = new Options();
            for (int i = 0; i<optionList.size(); i++){
                  formOptions = optionList.get(i);
                  CheckBox checkBox = new CheckBox(getContext());
                  checkBox.setId(View.generateViewId());
                  checkBox.setText(formOptions.getOptionValue());
                  checkBox.setTag(formOptions.getOptionValue());
                  checkboxLay.addView(checkBox);

                  checkBox.setOnCheckedChangeListener(handleCheck(checkBox));
            }
      }

      private CompoundButton.OnCheckedChangeListener handleCheck(final CheckBox checkBox) {
            return new CompoundButton.OnCheckedChangeListener() {

                  @Override
                  public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        // TODO Auto-generated method stub
                        if (isChecked)
                        {
                              myApplication.toastShortMessage(getContext(), "You checked "+ checkBox.getTag() );
                              String text = checkBox.getText().toString();
                              ((JsonMain)getActivity()).savePref(q_code, text);
                        }
                  }
            };
      }

      @Override
      public void onAttach(Context context) {
            super.onAttach(context);

      }

      @Override
      public void onDetach() {
            super.onDetach();
      }

      public interface OnFragmentInteractionListener {
            // TODO: Update argument type and name
            public void changeFragment(int id);
      }

}
