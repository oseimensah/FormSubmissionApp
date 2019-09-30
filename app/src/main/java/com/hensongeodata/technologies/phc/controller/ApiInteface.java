package com.hensongeodata.technologies.phc.controller;

import com.hensongeodata.technologies.phc.model.Result;
import com.hensongeodata.technologies.phc.model.SendData;

import org.json.JSONObject;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface ApiInteface {

//	@Multipart
//	@POST("login")
//	Call<User> user_login(
//              @Part("email") RequestBody u_email,
//              @Part("password") RequestBody u_password
//      );
//
//	@Multipart
//	@POST("signup")
//	Call<User> user_signup(
//              @Part("firstName") RequestBody u_firstname,
//              @Part("lastName") RequestBody u_lastName,
//              @Part("email") RequestBody u_email,
//              @Part("password") RequestBody u_password,
//              @Part("phone") RequestBody u_phone
//      );

	@FormUrlEncoded
	@POST("myloc")
	Call<Result> latlong(
              @Field("longitude") String longitude,
              @Field("latitude") String latitude
      );

	@Headers({
		"Accept: application/json",
		"Content-Type: application/json"
	})
	@POST(".")
	Call<SendData> postRawJSON(
		@Body String jsonObject
		);

}
