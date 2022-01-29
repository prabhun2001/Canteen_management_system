package com.example.madproject1;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.BidirectionalTypeConverter;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

public class QRactivity extends AppCompatActivity {

    public static ImageView qrview;
    ImageButton backtouser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bitmap bitmap = (Bitmap) this.getIntent().getParcelableExtra("Bitmap");
        setContentView(R.layout.activity_qractivity);


        qrview = (ImageView) findViewById(R.id.QRimageView);
        backtouser = (ImageButton) findViewById(R.id.backtouserhomeQr);
        qrview.setImageBitmap(bitmap);

        backtouser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(QRactivity.this,firstUserPage.class));
                finish();
            }
        });


    }
}