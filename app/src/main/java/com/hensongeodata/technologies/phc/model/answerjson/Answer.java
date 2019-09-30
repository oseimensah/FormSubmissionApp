package com.hensongeodata.technologies.phc.model.answerjson;

import java.util.List;

public class Answer {

      private String form_code;
      private String sub_id;
      private List<Answers> submission;

      public String getForm_code() {
            return form_code;
      }

      public void setForm_code(String form_code) {
            this.form_code = form_code;
      }

      public String getSub_id() {
            return sub_id;
      }

      public void setSub_id(String sub_id) {
            this.sub_id = sub_id;
      }

      public List<Answers> getSubmission() {
            return submission;
      }

      public void setSubmission(List<Answers> submission) {
            this.submission = submission;
      }
}
