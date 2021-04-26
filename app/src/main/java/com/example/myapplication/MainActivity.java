package com.example.myapplication;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.documentfile.provider.DocumentFile;

import android.os.Environment;
import android.os.StrictMode;
import android.view.View;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;

import java.io.*;
import java.net.*;
import java.util.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class MainActivity extends AppCompatActivity {
    private static Context context;
    private TextView tvIP, tvdiv2,textView4;
    private Button mButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MainActivity.context = getApplicationContext();
        setContentView(R.layout.activity_main);

        tvIP = findViewById(R.id.tvIP);
        tvdiv2 = findViewById(R.id.tvdiv2);
        textView4 = findViewById(R.id.textView4);

        setTvdiv2();
        mButton = findViewById(R.id.button);
//        filehandler filehandlerobj = new filehandler();
//        filehandlerobj.navigateToAppDirectory();
        mButton.setOnClickListener(
                new View.OnClickListener()
                {
                    public void onClick(View view)
                    {
                        System.out.println( "Permission is revoked fuck");
                        navigateToAppDirectory();
                    }
                });

        ;

        tvIP.setText("TCPServer Waiting for client on port 5000");
//        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
//        StrictMode.setThreadPolicy(policy);

                httphandle httphandleobj = new httphandle();
                Thread httphandlethread = new Thread(httphandleobj);
                httphandlethread.start();


    }
    public static Context getAppContext() {
        return MainActivity.context;
    }
    public void setTvdiv2(){
        if (filehandleobj.getInstence().getSize() > 0) {
            tvdiv2.setText("Sharing file from \"" + filehandleobj.getInstence().getBasefilenme() + "\"\n");
            textView4.setText(filehandleobj.getInstence().getFileList());
        }else {
            tvdiv2.setText("Nothings being Shared \nPlease Set A File To Share File location");
            String list = "";
        }
    }

    public boolean isStoragePermissionGranted() {
        String TAG = "Storage Permission";
        if (Build.VERSION.SDK_INT >= 23) {
            if (this.checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                System.out.println( "Permission is granted");
                return true;
            } else {
                System.out.println( "Permission is revoked fuck");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                return false;
            }
        }
        else { //permission is automatically granted on sdk<23 upon installation
            System.out.println( "Permission is granted");
            return true;
        }
    }

        @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    public void navigateToAppDirectory() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT_TREE);
        intent.putExtra("android.content.extra.SHOW_ADVANCED", true);
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        String line = "";
        if(requestCode==1 && resultCode==RESULT_OK && data!=null){
            InputStream stream = null;
            DocumentFile rootDocFile = DocumentFile.fromTreeUri(this, data.getData());

            filehandleobj fileobj = filehandleobj.getInstence();
            fileobj.setFileobj(rootDocFile);
            setTvdiv2();
        }
    }

}