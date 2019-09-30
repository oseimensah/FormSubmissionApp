package com.hensongeodata.technologies.phc.adapters;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.hensongeodata.technologies.phc.MyApplication;
import com.hensongeodata.technologies.phc.R;
import com.hensongeodata.technologies.phc.database.FormDatabaseHelper;
import com.hensongeodata.technologies.phc.model.PreviewModel;
import com.hensongeodata.technologies.phc.view.JsonMain;

import java.util.List;

public class PreviewRecyclerAdapter  extends RecyclerView.Adapter<PreviewRecyclerAdapter.Myholder>{

      private static final String TAG = PreviewRecyclerAdapter.class.getSimpleName();
      MyApplication myApplication;
      List<PreviewModel> previewModelList;
      JsonMain jsonMain;
      Context mctx;

      public PreviewRecyclerAdapter(List<PreviewModel> previewModelList, Context mctx) {
            this.previewModelList = previewModelList;
            this.mctx = mctx;
      }

      @NonNull
      @Override
      public Myholder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View preview = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.model_preview_content, null);
            return new Myholder(preview);
      }

      @Override
      public void onBindViewHolder(@NonNull final Myholder myholder, final int position) {
            myApplication = new MyApplication();
            final FormDatabaseHelper formDatabaseHelper = new FormDatabaseHelper(mctx);
            final PreviewModel previewModel=previewModelList.get(position);
            myholder.question.setText(previewModel.getQuestion());
            myholder.question_num.setText(previewModel.getQuestion_code() + ".");
            myholder.answer.setText(previewModel.getQuestion_answer());

            myholder.answer.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                  @Override
                  public void onFocusChange(View v, boolean hasFocus) {

                        if (!hasFocus){
                              String textChange = myholder.answer.getText().toString();
                              String question_num = previewModel.getQuestion_code();
                              String form_code = previewModel.getForm_code();
                              myApplication.toastShortMessage(mctx, "Data is " + form_code + ", " + question_num + ", " + textChange);
                              formDatabaseHelper.updateField(mctx, form_code, question_num, textChange);
                        }

                  }
            });

      }

      @Override
      public int getItemCount() {
            return previewModelList.size();
      }

      class Myholder extends RecyclerView.ViewHolder{
            TextView question, question_num;
            EditText answer;

            public Myholder(View itemView) {
                  super(itemView);
                  question = (TextView) itemView.findViewById(R.id.preview_question);
                  question_num = (TextView) itemView.findViewById(R.id.preview_question_number);
                  answer = (EditText) itemView.findViewById(R.id.preview_answer);

            }
      }
}
