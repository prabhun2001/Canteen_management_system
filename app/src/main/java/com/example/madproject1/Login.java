package com.example.madproject1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;


import android.text.TextUtils;
import android.util.Patterns;
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

public class Login extends AppCompatActivity {
TextView regist,toReset,toAdmin;
EditText uemail,upassword;
Button login;
FirebaseAuth fAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        regist=findViewById(R.id.toregister);
        toReset=findViewById(R.id.toreset);
        toAdmin = findViewById(R.id.toadmin);
        uemail = findViewById(R.id.editTextTextEmailAddress);
        upassword = findViewById(R.id.editTextTextPassword);
        login = findViewById(R.id.login);
        fAuth = FirebaseAuth.getInstance();



        regist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Login.this, register.class));
                finish();
            }
        });
        toReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Login.this,forgotpassword.class));
                finish();
            }
        });
        toAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Login.this,admin_login.class));
                finish();
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = uemail.getText().toString().trim();
                String password = upassword.getText().toString().trim();

                if(TextUtils.isEmpty(email)){
                    uemail.setError("Email is Required.");
                    uemail.requestFocus();
                    return;
                }
                if(TextUtils.isEmpty(password)){
                    upassword.setError("Password is Required.");
                    upassword.requestFocus();
                    return;
                }
                if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                    uemail.setError("Please Provide Valid Email!");
                    uemail.requestFocus();
                    return;
                }
                if(password.length() < 6){
                    upassword.setError("Password Must be >= 6 Characters!");
                    upassword.requestFocus();
                    return;
                }

                fAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            startActivity(new Intent(Login.this,firstUserPage.class));
                            Toast.makeText(Login.this, "Login Successfull.", Toast.LENGTH_SHORT).show();
                            finish();
                        }else{
                            Toast.makeText(Login.this, "Failed to login! Please check your credentials!", Toast.LENGTH_LONG).show();
                        }
                    }
                });

            }
        });

    }




}