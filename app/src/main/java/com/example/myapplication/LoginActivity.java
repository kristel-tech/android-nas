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
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    EditText emailUserID;
    EditText userPassword;
    TextView signUpPageText;
    Button signInButton;
    FirebaseAuth authenticationObject;

    private FirebaseAuth.AuthStateListener authenticationStateListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        signUpPageText = findViewById(R.id.textView);
        authenticationObject = FirebaseAuth.getInstance();
        emailUserID = findViewById(R.id.editTextTextEmailAddress);
        userPassword =findViewById(R.id.editTextTextPassword);
        signInButton= findViewById(R.id.button2);

        authenticationStateListener = firebaseAuth -> {
            FirebaseUser instanceFireBaseUser = authenticationObject.getCurrentUser();

            if (instanceFireBaseUser != null) {
                Toast.makeText(LoginActivity.this, "You have successfully Logged in", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent( LoginActivity.this , HomeActivity.class);
                startActivity(intent);
            }else {
                Toast.makeText(LoginActivity.this, "Please Login ", Toast.LENGTH_SHORT).show();
            }
        };
        //after clicking sign in button
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email = emailUserID.getText().toString();
                String pwd= userPassword.getText().toString();

                //object vailidation
                if (email.isEmpty()){
                    emailUserID.setError("Please entere your email");
                    emailUserID.requestFocus();
                }
                else if (pwd.isEmpty()){
                    userPassword.setError("please provide password");
                    userPassword.requestFocus();
                }
                else if (email.isEmpty() && pwd.isEmpty()){
                    Toast.makeText(LoginActivity.this, "fields are emtpy", Toast.LENGTH_SHORT).show();
                }
                else if (!(email.isEmpty() && pwd.isEmpty())){
                    authenticationObject.signInWithEmailAndPassword(email,pwd).addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                       if (!task.isSuccessful()){
                           Toast.makeText(LoginActivity.this, "Login Failure , please try again or not!! ", Toast.LENGTH_SHORT).show();
                           Intent advanceHome = new Intent (LoginActivity.this, FirstPageActivity.class);
                           startActivity(advanceHome);
                       }
                       else{
                           Toast.makeText(LoginActivity.this, "Login Successful!! ", Toast.LENGTH_SHORT).show();
                           Intent advanceHome = new Intent (LoginActivity.this, HomeActivity.class);
                           startActivity(advanceHome);
                       }
                        }
                    });
                }
                else {
                    Toast.makeText(LoginActivity.this, "fatality!!!!! ", Toast.LENGTH_SHORT).show();
                }

            }
        });

        signUpPageText.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent advanceToSignUp = new Intent (LoginActivity.this, FirstPageActivity.class);
                startActivity(advanceToSignUp);
            }
        });
    }

    @Override
    protected void onStart(){
        super.onStart();
        authenticationObject.addAuthStateListener(authenticationStateListener);
    }
}