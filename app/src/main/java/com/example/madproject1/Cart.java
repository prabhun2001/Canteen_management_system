package com.example.madproject1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.ImageDecoder;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.WriterException;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;
import android.graphics.Bitmap;

public class Cart extends AppCompatActivity{

    private DrawerLayout drawerLayout;
    private RecyclerView recyclerView;
    private MainAdapterCart mainAdapterCart;
    public static TextView totalamount;
    private ImageButton backtouser;

    private Button order;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        totalamount = (TextView) findViewById(R.id.totalprice);
        backtouser = (ImageButton) findViewById(R.id.backtouserhome);
        order = (Button) findViewById(R.id.placeorder);

        recyclerView = findViewById(R.id.recyclerviewcart);
        recyclerView.setLayoutManager((new LinearLayoutManager(this)));

        FirebaseRecyclerOptions<ItemModelCart> options =
                new FirebaseRecyclerOptions.Builder<ItemModelCart>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("AddtoCart/"+FirebaseAuth.getInstance().getCurrentUser().getUid()), ItemModelCart.class)
                        .build();
        mainAdapterCart = new MainAdapterCart(options);
        recyclerView.setAdapter(mainAdapterCart);



        backtouser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Cart.this,firstUserPage.class));
                finish();
            }
        });

        //order button
        order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(totalamountcart.total==0){
                    Toast.makeText(Cart.this, "Cart is Empty Add items", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(Cart.this,firstUserPage.class));
                    finish();
                }else{
                    int total = totalamountcart.total;
                    totalamountcart.setTotal(0);
                    FirebaseDatabase.getInstance().getReference("AddtoCart").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                            .removeValue();
                    totalamount.setText(String.valueOf(totalamountcart.total)+" Rs");

                    FirebaseDatabase.getInstance().getReference("TotalAmount/"+FirebaseAuth.getInstance().getCurrentUser().getUid()).child("amount")
                            .setValue(String.valueOf(0));

                    Toast.makeText(Cart.this, "Order Successfully Placed", Toast.LENGTH_SHORT).show();



                    //QR code
                    StringBuffer texttoQr = new StringBuffer();
                    texttoQr.append("User Id : "+FirebaseAuth.getInstance().getCurrentUser().getUid());
                    texttoQr.append("\nTotal Amount : "+String.valueOf(total)+" Rs");

                    String text = texttoQr.toString();



                        QRGEncoder qrgEncoder=new QRGEncoder(text,null, QRGContents.Type.TEXT,500);
                        try {
                            Bitmap qrBits=qrgEncoder.encodeAsBitmap();

                            //pass bitmap using intent
                            Intent intent = new Intent();
                            intent.setClass(Cart.this,QRactivity.class);
                            intent.putExtra("Bitmap",qrBits);
                            startActivity(intent);

                        } catch (WriterException e) {
                            e.printStackTrace();
                        }





                }







            }
        });



    }




        @Override
    protected void onStart() {
        super.onStart();

        mainAdapterCart.startListening();
        //get total amount
        FirebaseDatabase.getInstance().getReference("TotalAmount/"+FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child("amount").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    String cartamount = snapshot.getValue(String.class);
                    if(cartamount!=null){
                        totalamountcart.total=Integer.parseInt(cartamount);
                        totalamount.setText(cartamount+" Rs");
                    }else{
                        Toast.makeText(Cart.this, "no total", Toast.LENGTH_SHORT).show();
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

    }



}