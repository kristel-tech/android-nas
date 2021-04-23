package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Login1 extends AppCompatActivity {

    EditText emailUserID;
    EditText userPassword;
    TextView signUpPageText;
    Button signInButton;
    FirebaseAuth authenticationObject;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login1);


        signUpPageText = findViewById(R.id.HintText);
        authenticationObject = FirebaseAuth.getInstance();
        emailUserID = findViewById(R.id.EmailAddress);
        userPassword =findViewById(R.id.Password1);
        signInButton= findViewById(R.id.Login);


        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailUserID.getText().toString().trim();
                String pwd= userPassword.getText().toString().trim();

                if (email.isEmpty()){
                    emailUserID.setError("Please entere your email");
                    return;
                }
                else if (pwd.isEmpty()){
                    userPassword.setError("please provide password");
                    return;
                }
                else if (pwd.length() < 6){
                    userPassword.setError("password should be longer then 6 characters");
                    return;
                }

                //authenticate user via login
                authenticationObject.signInWithEmailAndPassword(email,pwd).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(Login1.this, "User has successfully Login in ", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(),MainActivity2.class));
                        }else
                        {
                            Toast.makeText(Login1.this, "task failure "+ task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });
    }
}