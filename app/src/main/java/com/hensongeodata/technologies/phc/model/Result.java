package com.hensongeodata.technologies.phc.model;

import com.google.gson.annotations.SerializedName;

public class Result {

      @SerializedName("error")
      private Boolean longlatError;
      @SerializedName("erro_msg")
      private String longlatMessage;

      public Boolean getLonglatError() {
            return longlatError;
      }

      public void setLonglatError(Boolean longlatError) {
            this.longlatError = longlatError;
      }

      public String getLonglatMessage() {
            return longlatMessage;
      }

      public void setLonglatMessage(String longlatMessage) {
            this.longlatMessage = longlatMessage;
      }
}
