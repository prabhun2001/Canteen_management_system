package com.example.madproject1;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainAdapterUser extends FirebaseRecyclerAdapter<ItemModel,MainAdapterUser.myViewHolderUser> {

    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public MainAdapterUser(@NonNull FirebaseRecyclerOptions<ItemModel> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull MainAdapterUser.myViewHolderUser holder, @SuppressLint("RecyclerView") int position, @NonNull ItemModel model) {
        holder.name.setText(model.getName());
        holder.price.setText("Price "+model.getPrice());

        Glide.with(holder.img.getContext())
                .load(model.getUrl())
                .placeholder(R.drawable.common_google_signin_btn_icon_dark)
                .circleCrop()
                .error(R.drawable.common_google_signin_btn_icon_dark_normal)
                .into(holder.img);

        //add to cart
        holder.addtocart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                    FirebaseDatabase.getInstance().getReference().child("foodItems")
                            .child(getRef(position).getKey()).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            ItemModel itemModel = snapshot.getValue(ItemModel.class);

                            if(itemModel != null){
                                String strname = itemModel.name;
                                String strprice = itemModel.price;
                                String strurl = itemModel.url;
                                String count = "0";

                                Map<String,Object> map = new HashMap<>();
                                map.put("name",strname);
                                map.put("price",strprice);
                                map.put("url",strurl);
                                map.put("count",count);

                                FirebaseDatabase.getInstance().getReference("AddtoCart").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(getRef(position).getKey())
                                        .addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                if(snapshot.exists()){
                                                    holder.addtocart.setText("Already Added");
                                                }
                                                else{

                                                    FirebaseDatabase.getInstance().getReference("AddtoCart").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(getRef(position).getKey())
                                                            .setValue(map)
                                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                @Override
                                                                public void onSuccess(Void unused) {
                                                                    Toast.makeText(holder.name.getContext(), "Successfully Added", Toast.LENGTH_SHORT).show();
                                                                }
                                                            })
                                                            .addOnFailureListener(new OnFailureListener() {
                                                                @Override
                                                                public void onFailure(@NonNull Exception e) {
                                                                    Toast.makeText(holder.name.getContext(), "Error while insertion.", Toast.LENGTH_SHORT).show();
                                                                }
                                                            });

                                                            holder.addtocart.setText("Added");
                                                }
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {

                                            }
                                        });





                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });



            }
        });

    }
    @NonNull
    @Override
    public MainAdapterUser.myViewHolderUser onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.main_item_view,parent,false);
        return new MainAdapterUser.myViewHolderUser(view);
    }

    class myViewHolderUser extends RecyclerView.ViewHolder{

        CircleImageView img;
        TextView name,price;
        Button addtocart;


        public myViewHolderUser(@NonNull View itemView) {
            super(itemView);

            img = (CircleImageView) itemView.findViewById(R.id.img2);
            name = (TextView) itemView.findViewById(R.id.nametextview);
            price = (TextView) itemView.findViewById(R.id.pricetextview);
            addtocart = (Button) itemView.findViewById(R.id.addbutton);
        }
    }
}
