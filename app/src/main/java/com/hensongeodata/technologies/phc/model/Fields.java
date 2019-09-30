package com.hensongeodata.technologies.phc.model;

public class Fields {

//      Question types
      public static String TEXT="text";
      public static String RADIO="radio";
      public static String TEXTAREA="textarea";
      public static String DROPDOWN="dropdown";
      public static String CHECKBOX="checkbox";
      public static String EMAIL="email";
      public static String DATE="date";
      public static String NUMERIC="numeric";
      public static String FILE="file";
      public static String PHONE="phone";

//      Form details
      public static String label;
      public static String code;
      public static String type;
      protected Boolean required = false;

      public Fields() {
      }

      public static String getLabel() {
            return label;
      }

      public static void setLabel(String label) {
            Fields.label = label;
      }

      public static String getCode() {
            return code;
      }

      public static void setCode(String code) {
            Fields.code = code;
      }

      public static String getType() {
            return type;
      }

      public static void setType(String type) {
            Fields.type = type;
      }

      public Boolean getRequired() {
            return required;
      }

      public void setRequired(Boolean required) {
            this.required = required;
      }
}
