package com.hensongeodata.technologies.phc.model;

import com.google.gson.annotations.SerializedName;

public class SendData {

      @SerializedName("status")
      private String status;
      @SerializedName("submission_id")
      private String submission_id;

      public String getStatus() {
            return status;
      }

      public void setStatus(String status) {
            this.status = status;
      }

      public String getSubmission_id() {
            return submission_id;
      }

      public void setSubmission_id(String submission_id) {
            this.submission_id = submission_id;
      }
}
