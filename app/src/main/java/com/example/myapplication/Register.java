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

public class Register extends AppCompatActivity {



    EditText emailUserID;
    EditText userPassword;
    EditText userName;
    TextView signInPageText;
    Button signUpButton;
    FirebaseAuth authenticationObject;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        userName = findViewById(R.id.personName);
        emailUserID =  findViewById(R.id.EmailAddress);
        userPassword =  findViewById(R.id.Password1);
        signInPageText =  findViewById(R.id.textView2);
        signUpButton =  findViewById(R.id.register);

        authenticationObject = FirebaseAuth.getInstance();

        FirebaseUser instanceFireBaseUser = authenticationObject.getCurrentUser();

        if (instanceFireBaseUser != null) {
            Toast.makeText(Register.this, "You have successfully Logged in", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent( Register.this , MainActivity2.class);
            startActivity(intent);
            finish();
        }

        signUpButton.setOnClickListener( new View.OnClickListener() {
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

                authenticationObject.createUserWithEmailAndPassword(email,pwd).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(Register.this, "User Created ", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(),MainActivity2.class));
                        }else {
                            Toast.makeText(Register.this, "task failure "+ task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                });

            }
        });

        signInPageText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(Register.this, Login1.class);
                startActivity(intent);
            }
        });
    }


}