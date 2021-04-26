package com.example.myapplication;

import android.content.Intent;

import androidx.documentfile.provider.DocumentFile;
import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.documentfile.provider.DocumentFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;
import java.io.InputStream;

public class filehandler extends AppCompatActivity {

    public filehandler() {

        setContentView(R.layout.activity_maindos);
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
        }
    }
}

