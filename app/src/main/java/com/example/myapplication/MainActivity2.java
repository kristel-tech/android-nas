package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

public class MainActivity2 extends AppCompatActivity {

    private TextView textView4;
    Button logoutButtton;
    Button active;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        textView4=findViewById(R.id.textView4);

        logoutButtton  =findViewById(R.id.logOutButton);

        active = findViewById(R.id.button);
        textView4.setText(FirebaseAuth.getInstance().getUid());
        Log.d("my log",FirebaseAuth.getInstance().getUid());


        setLogin();
//        Log.d("my log",FirebaseAuth.getInstance().i);
//        Log.d("my log",FirebaseAuth.getInstance().getTenantId());
//        Log.d("my log",FirebaseAuth.getInstance().getCurrentUser());

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
}