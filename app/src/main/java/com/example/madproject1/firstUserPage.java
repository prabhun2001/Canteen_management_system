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
import android.widget.SearchView;
import android.widget.Switch;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;




import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.ValueEventListener;

public class firstUserPage extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private DrawerLayout drawerLayout;
    private RecyclerView recyclerView;
    private MainAdapterUser mainAdapterUser;

    private TextView username,useremail;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_user_page);



        drawerLayout = findViewById(R.id.nav_viewUser);
        Toolbar toolbar =findViewById(R.id.toolbarUser);
        NavigationView navigationView = findViewById(R.id.navigation_viewUser);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.navigation_draw_open,R.string.navigation_draw_close);

        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        recyclerView = findViewById(R.id.recyclerviewUser);
        recyclerView.setLayoutManager((new LinearLayoutManager(this)));

        FirebaseRecyclerOptions<ItemModel> options =
                new FirebaseRecyclerOptions.Builder<ItemModel>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("foodItems"), ItemModel.class)
                        .build();
        mainAdapterUser = new MainAdapterUser(options);
        recyclerView.setAdapter(mainAdapterUser);

        //search
        SearchView searchView = (SearchView) findViewById(R.id.searchUser);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                txtSearch(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                txtSearch(query);
                return false;
            }
        });

        View headerview = navigationView.getHeaderView(0);

        username = (TextView) headerview.findViewById(R.id.nameofuser);
        useremail = (TextView) headerview.findViewById(R.id.emailofuser);

        FirebaseDatabase.getInstance().getReference("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        User user = snapshot.getValue(User.class);
                        String name = user.name;
                        String email = user.email;
//
                        username.setText(name);
                        useremail.setText(email);

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){

            case R.id.cart:
                startActivity(new Intent(this,Cart.class));
                break;

            case R.id.qr:
                startActivity(new Intent(this,QRactivity.class));
                break;

            case R.id.logout:
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(this,Login.class));
                finish();
                break;
        }

        drawerLayout.closeDrawer(GravityCompat.START);

        return true;
    }

    @Override
    public void onBackPressed() {
        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }
        else{
            super.onBackPressed();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        mainAdapterUser.startListening();



    }

//    @Override
//    protected void onStop() {
//        super.onStop();
//        mainAdapter.stopListening();
//    }



    private void txtSearch(String str){
        FirebaseRecyclerOptions<ItemModel> options =
                new FirebaseRecyclerOptions.Builder<ItemModel>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("foodItems").orderByChild("name").startAt(str).endAt(str+"~"), ItemModel.class)
                        .build();

        mainAdapterUser = new MainAdapterUser(options);
        mainAdapterUser.startListening();
        recyclerView.setAdapter(mainAdapterUser);
    }
}