package com.example.madproject1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class AddToMenu extends AppCompatActivity {

    private EditText name,price,url;
    private ImageButton back;
    private Button add;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_to_menu);

        name = (EditText) findViewById(R.id.itemname);
        price = (EditText) findViewById(R.id.itemprice);
        url = (EditText) findViewById(R.id.itemurl);
        back = (ImageButton) findViewById(R.id.backtoadminhome);
        add = (Button) findViewById(R.id.addtomenu);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AddToMenu.this,firstAdminpage.class));
                finish();
            }
        });

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insertdata();
            }
        });

    }

    private void insertdata(){
        Map<String,Object> map = new HashMap<>();
        map.put("name",name.getText().toString());
        map.put("price",price.getText().toString());
        map.put("url",url.getText().toString());

        FirebaseDatabase.getInstance().getReference().child("foodItems").push()
                .setValue(map)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(AddToMenu.this, "Successfully Added", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(AddToMenu.this, "Error while insertion.", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}