package com.hensongeodata.technologies.phc.model.formtypes;

import com.hensongeodata.technologies.phc.model.Fields;

import java.util.ArrayList;

public class RadioField extends Fields {
      private ArrayList<String> options;

      public RadioField(){}

      public ArrayList<String> getOptions() {
            return options;
      }

      public void setOptions(ArrayList<String> options) {
            this.options = options;
      }
}
