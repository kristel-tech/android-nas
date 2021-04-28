package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
//import com.google.gson.GsonBuilder;

import com.google.firebase.auth.FirebaseAuth;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class MainActivity2 extends AppCompatActivity implements ListFilesAdapter.ListItemListener{

    Button logoutButtton;
    private ArrayList<fileObject> fileObjectList;
//    private ListFilesAdapter recycler;
    Button active;
   private RecyclerView recyclerView;
//    ListFilesAdapter lfa;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        logoutButtton  =findViewById(R.id.logOutButton);
        recyclerView = findViewById(R.id.filesListView);
        active = findViewById(R.id.button);
        fileObjectList = new ArrayList<>();


        //makeAPIcall
        Call<fileObject> theFileList = ApiClient.getFileListService().getFileList();
        theFileList.enqueue(new Callback<fileObject>() {
            @Override
            public void onResponse(Call<fileObject> call, Response<fileObject> response) {
                if(response.isSuccessful()){
//                    Log.e("success", response.body().toString());
                    Log.e("FileList", response.body().getFileList().toString());

                    List<fileObject> fileListiter = response.body().getFileList();
                    for (fileObject f:fileListiter) {
                        fileObjectList.add(new fileObject(f.getFileName(),f.getFileSize(),f.getHref()));
                    }
                    setAdapter();


                }
            }

            @Override
            public void onFailure(Call<fileObject> call, Throwable t) {
                Log.e("failure", t.getLocalizedMessage());
            }
        });

        active.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent advanceFirstPage = new Intent(MainActivity2.this, MainActivity.class);
                startActivity(advanceFirstPage);
            }
        });


        logoutButtton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent advanceFirstPage = new Intent(MainActivity2.this, Login1.class);
                startActivity(advanceFirstPage);
            }
        });

    }



    private void setAdapter(){
        ListFilesAdapter adapter = new ListFilesAdapter(fileObjectList, this);
        RecyclerView.LayoutManager LayoutMan = new LinearLayoutManager(getApplicationContext());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(LayoutMan);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        //comment

   }

    @Override
    public void onListItemCLick(int position) {
        Log.d("test", "listitemCLicked: clicked");
        Log.d("pos", "positoion:"+position);
        String downloadLink =  fileObjectList.get(position).getHref();
        String fileName = fileObjectList.get(position).getFileName();
        downloadFile("https://images-na.ssl-images-amazon.com/images/I/8116cB9j95L._AC_SL1425_.jpg", "test2.jpg");
    }


    private void downloadFile(String Link, String fileName) {
        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl("http://localhost:5000/");

        Retrofit retrofit = builder.build();

        FileDownloadClient fileDownloadClient = retrofit.create(FileDownloadClient.class);
        String linkWithid = Link + '/' + setLogin();
        Call<ResponseBody> call = fileDownloadClient.downloadFile(linkWithid);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.isSuccessful()){
                    boolean downloadSuccess = writeResponseBodyToDisk(response.body(), fileName);
                    Toast.makeText(MainActivity2.this, "Download Initiated", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(MainActivity2.this, "Download Failed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public static String setLogin() {
        String UID = FirebaseAuth.getInstance().getUid();

        DatabaseInfo DBDataInst = new DatabaseInfo();
        DBDataInst.SQLCall(DBCallType.DELETELOGIN);
        DBDataInst.setLoginPostData(UID);
        System.out.println(DBDataInst.getRequestFileHistoryData());
        NASDatabase DBInst = new NASDatabase(DBDataInst);
        DBInst.execute();

        DBDataInst = new DatabaseInfo();
        DBDataInst.SQLCall(DBCallType.CREATELOGIN);
        DBDataInst.setLoginPostData(UID);
        System.out.println(DBDataInst.getRequestFileHistoryData());
        DBInst = new NASDatabase(DBDataInst);
        DBInst.execute();

        return UID;
    }

    private boolean writeResponseBodyToDisk(ResponseBody body, String fileName) {
        try {
            File fileThatsGettingGot = new File(getExternalFilesDir(null) + File.separator + fileName);

            InputStream inputStream = null;
            OutputStream outputStream = null;

            try {
                byte[] fileReader = new byte[4096];

                long fileSize = body.contentLength();
                long fileSizeDownloaded = 0;

                inputStream = body.byteStream();
                outputStream = new FileOutputStream(fileThatsGettingGot);

                while (true) {
                    int read = inputStream.read(fileReader);

                    if (read == -1) {
                        break;
                    }

                    outputStream.write(fileReader, 0, read);

                    fileSizeDownloaded += read;

                    Log.d("Debug", "file download: " + fileSizeDownloaded + " of " + fileSize);
                }

                outputStream.flush();

                return true;
            } catch (IOException e) {
                return false;
            } finally {
                if (inputStream != null) {
                    inputStream.close();
                }

                if (outputStream != null) {
                    outputStream.close();
                }
            }
        } catch (IOException e) {
            return false;
        }
    }
}