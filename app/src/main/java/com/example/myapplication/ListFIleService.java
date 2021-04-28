package com.example.myapplication;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ListFIleService {
//    https://run.mocky.io/v3/eda8c118-f974-469e-bbdf-941a0c5372a7

    @GET("eda8c118-f974-469e-bbdf-941a0c5372a7")
    Call<fileObject> getFileList();
}
