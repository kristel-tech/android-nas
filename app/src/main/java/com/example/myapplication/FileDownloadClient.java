package com.example.myapplication;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import retrofit2.http.Streaming;
import retrofit2.http.Url;

public interface FileDownloadClient {

//    @Streaming  //wrap the download into async task and execute if you wanna stream
    @GET
    Call<ResponseBody> downloadFile(@Url String url);
}
