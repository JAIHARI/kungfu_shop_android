package com.example.samuray.myapplication;



import com.example.samuray.myapplication.model.ProductModel;
import com.example.samuray.myapplication.model.Login;
import com.example.samuray.myapplication.model.SummaryModel;
import com.example.samuray.myapplication.model.User;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface PostApi {


    String root = "http://192.168.0.103:8000/";

    String API_URL = root + "api/v1/";





    @POST("accounts/login/")
    Call<User> login(@Body Login login);

    @POST("accounts/register/")
    Call<User> registrationUser(@Body User userModel);


    @GET("list/")
    Call<List<ProductModel>> getProductList();

    @GET("detail/{slug}/")
    Call<ProductModel> getDetailProduct(@Path(value = "slug", encoded = true) String slug);

    @GET("create-cart/{slug}/")
    Call<ResponseBody> getCreateCart(@Header("Authorization")  String authToken, @Path(value = "slug", encoded = true) String slug);


    @GET("summary/")
    Call<SummaryModel> getSummaryList(@Header("Authorization")  String authToken);

    @GET("clear/")
    Call<ResponseBody> getClearCart(@Header("Authorization")  String authToken);

}