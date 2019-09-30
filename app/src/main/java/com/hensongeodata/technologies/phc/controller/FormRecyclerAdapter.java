package com.hensongeodata.technologies.phc.controller;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hensongeodata.technologies.phc.R;
import com.hensongeodata.technologies.phc.model.Form;

import java.util.List;

public class FormRecyclerAdapter extends RecyclerView.Adapter<FormRecyclerAdapter.Myholder> {

      List<Form> formModel;

      public FormRecyclerAdapter(List<Form> formModel) {
            this.formModel = formModel;
      }

      @NonNull
      @Override
      public Myholder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.question_content, null);

            return new Myholder(view);
      }

      @Override
      public void onBindViewHolder(@NonNull Myholder myholder, int i) {
            Form form = formModel.get(i);
            myholder.textView1.setText(form.getForm_name());
            myholder.textView2.setText(form.getLabel());
            myholder.textView3.setText(form.getCode());
            myholder.textView4.setText(form.getType());
      }

      @Override
      public int getItemCount() {
//            Log.e(TAG, "getItemCount: " + formModel.size() );
            return formModel.size();
      }


      public class Myholder extends RecyclerView.ViewHolder{
            TextView textView1, textView2, textView3, textView4;

            public Myholder(@NonNull View itemView) {
                  super(itemView);

                  textView1 = itemView.findViewById(R.id.title1);
                  textView2 = itemView.findViewById(R.id.title2);
                  textView3 = itemView.findViewById(R.id.title3);
                  textView4 = itemView.findViewById(R.id.title4);

            }
      }
}
