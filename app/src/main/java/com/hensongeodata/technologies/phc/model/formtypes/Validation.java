package com.hensongeodata.technologies.phc.model.formtypes;

import java.util.ArrayList;

public class Validation {
      private boolean error =false;//by default, no error is error
      private String message;
      private ArrayList results;

      public boolean isError() {
            return error;
      }

      public void setError(boolean error) {
            this.error = error;
      }

      public String getMessage() {
            return message;
      }

      public void setMessage(String message) {
            this.message = message;
      }

      public ArrayList getResults() {
            return results;
      }

      public void setResults(ArrayList results) {
            this.results = results;
      }
}
