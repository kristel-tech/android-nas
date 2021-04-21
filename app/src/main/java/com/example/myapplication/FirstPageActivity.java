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

public class FirstPageActivity extends AppCompatActivity {


    EditText emailUserID;
    EditText userPassword;
    TextView signInPageText;
    Button signUpButton;
    FirebaseAuth authenticationObject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_page);

        signInPageText = findViewById(R.id.textView);
        authenticationObject = FirebaseAuth.getInstance();
        emailUserID = findViewById(R.id.editTextTextEmailAddress);
        userPassword =findViewById(R.id.editTextTextPassword);
        signUpButton= findViewById(R.id.button2);
        signUpButton.setOnClickListener(new View.OnClickListener() {//lambda this son of a gun
            @Override
            public void onClick(View view){
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
                    Toast.makeText(FirstPageActivity.this, "fields are emtpy", Toast.LENGTH_LONG);
                }
                else if (!(email.isEmpty() && pwd.isEmpty())){
                    authenticationObject.createUserWithEmailAndPassword(email,pwd).addOnCompleteListener(FirstPageActivity.this, new OnCompleteListener<AuthResult>() {
                        //after successful process procede to do
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (!task.isSuccessful()){//or check if successful
                                Toast.makeText(FirstPageActivity.this, "task failure", Toast.LENGTH_LONG);
                            }else {
                                startActivity(new Intent(FirstPageActivity.this, HomeActivity.class));
                            }
                        }
                    });
                }
                else {
                    Toast.makeText(FirstPageActivity.this, "fatality!!!!! ", Toast.LENGTH_LONG);
                }
            }
        });

        signInPageText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(FirstPageActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }
}