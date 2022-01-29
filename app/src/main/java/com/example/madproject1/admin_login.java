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

public class admin_login extends AppCompatActivity {
    TextView Aforgot;

    EditText Aemail,Apassword;
    Button Alogin;
    FirebaseAuth fAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_login);
        Aforgot=findViewById(R.id.admintoreset);
        Aemail = findViewById(R.id.admineditTextTextEmailAddress);
        Apassword = findViewById(R.id.admineditTextTextPassword);
        Alogin = findViewById(R.id.adminlogin);
        fAuth = FirebaseAuth.getInstance();

        Aforgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(admin_login.this,forgotpassword.class));
            }
        });

        String check_email= "nrsp2001@gmail.com";


        Alogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = Aemail.getText().toString().trim();
                String password = Apassword.getText().toString().trim();

                if(TextUtils.isEmpty(email)){
                    Aemail.setError("Email is Required.");
                    Aemail.requestFocus();
                    return;
                }
                if(TextUtils.isEmpty(password)){
                    Apassword.setError("Password is Required.");
                    Apassword.requestFocus();
                    return;
                }
                if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                    Aemail.setError("Please Provide Valid Email!");
                    Aemail.requestFocus();
                    return;
                }
                if(password.length() < 6){
                    Apassword.setError("Password Must be >= 6 Characters!");
                    Apassword.requestFocus();
                    return;
                }
                if(check_email.equals(email)){
                    fAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(admin_login.this, "Admin login Successfull.", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(admin_login.this,firstAdminpage.class));
                                finish();
                            }else{
                                Toast.makeText(admin_login.this, "Failed to login! Please check your credentials!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }else{
                    Toast.makeText(admin_login.this, "Only Admin's can Login!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}