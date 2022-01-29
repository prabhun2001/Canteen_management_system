package com.example.madproject1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class forgotpassword extends AppCompatActivity {
TextView tologin;
EditText femail;
Button reset;
FirebaseAuth fAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgotpassword);
        tologin=findViewById(R.id.tologin2);
        femail=findViewById(R.id.forgoteditTextTextEmailAddress);
        reset = findViewById(R.id.reset);

        fAuth = FirebaseAuth.getInstance();
        tologin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(forgotpassword.this,Login.class));
            }
        });
        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = femail.getText().toString().trim();
                if(email.isEmpty()){
                    femail.setError("Enter Email.");
                    femail.requestFocus();
                    return;
                }

                if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                    femail.setError("Enter Valid Email.");
                    femail.requestFocus();
                    return;
                }

                fAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(forgotpassword.this, "Check your email to reset your password!", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(forgotpassword.this,Login.class));
                        }else{
                            Toast.makeText(forgotpassword.this, "Try again! Something wrong happened!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }
}