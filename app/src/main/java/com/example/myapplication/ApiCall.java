package com.example.myapplication;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiCall {

//    https://run.mocky.io/v3/  ca7875e5-e066-4eb0-b2e3-901ea6a67221

    @GET("ca7875e5-e066-4eb0-b2e3-901ea6a67221")
    Call<fileObject> getData();
}
