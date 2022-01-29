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
import com.google.firebase.database.FirebaseDatabase;

import java.util.regex.Pattern;

public class register extends AppCompatActivity {
TextView tologin;
EditText remail,rphone,rname,rpass;
Button register;
FirebaseAuth fAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        remail=findViewById(R.id.neweditTextTextEmailAddress);
        rphone=findViewById(R.id.neweditTextTextphone);
        rname = findViewById(R.id.editTextTextPersonName);
        rpass=findViewById(R.id.neweditTextTextPassword);
        register = findViewById(R.id.register);

        fAuth = FirebaseAuth.getInstance();

        tologin = findViewById(R.id.backlogin1);

//        if(fAuth.getCurrentUser() != null){
//            startActivity(new Intent(register.this,firstUserPage.class));
//            finish();
//        }

        tologin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(register.this, Login.class));
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = rname.getText().toString().trim();
                String phone = rphone.getText().toString().trim();
                String email = remail.getText().toString().trim();
                String password = rpass.getText().toString().trim();

                if(TextUtils.isEmpty(name)){
                    rname.setError("Name is Required.");
                    rname.requestFocus();
                    return;
                }
                if(TextUtils.isEmpty(phone)){
                    rphone.setError("Phone Number is Required.");
                    rphone.requestFocus();
                    return;
                }
                if(TextUtils.isEmpty(email)){
                    remail.setError("Email ID is Required.");
                    remail.requestFocus();
                    return;
                }
                if(TextUtils.isEmpty(password)){
                    rpass.setError("Password is Required.");
                    rpass.requestFocus();
                    return;
                }
                if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                    remail.setError("Please Provide Valid Email!");
                    remail.requestFocus();
                    return;
                }
                if(password.length() < 6){
                    rpass.setError("Password Must be >= 6 Characters.");
                    rpass.requestFocus();
                    return;
                }

                fAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            User user = new User(name,phone,email);

                            FirebaseDatabase.getInstance().getReference("users")
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        Toast.makeText(register.this, "User Registered Successfully", Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(register.this,Login.class));
                                    }
                                }
                            });
                        }else{
                            Toast.makeText(register.this, "Error in User creation!!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }
}