package com.example.madproject1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth fAuth;
    private String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fAuth = FirebaseAuth.getInstance();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                FirebaseUser user = fAuth.getCurrentUser();

                if(user!=null){
                    userID = user.getUid();
                    if(userID.equals("I4rUAilQg1gMZy4xxOUWOndQFto1")){
                        startActivity(new Intent(MainActivity.this,firstAdminpage.class));
                        finish();
                    }else{
                        startActivity(new Intent(MainActivity.this,firstUserPage.class));
                        finish();
                    }

                }
                else{
                    startActivity(new Intent(MainActivity.this,Login.class));
                    finish();
                }

            }
        },2000);
    }
}