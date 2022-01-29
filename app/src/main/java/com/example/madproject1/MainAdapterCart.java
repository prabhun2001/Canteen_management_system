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

public class MainAdapterCart extends FirebaseRecyclerAdapter<ItemModelCart,MainAdapterCart.myViewHolderCart> {



    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public MainAdapterCart(@NonNull FirebaseRecyclerOptions<ItemModelCart> options) {
        super(options);
    }


    @Override
    protected void onBindViewHolder(@NonNull MainAdapterCart.myViewHolderCart holder, @SuppressLint("RecyclerView") int position, @NonNull ItemModelCart model) {
        holder.name.setText(model.getName());
        holder.price.setText("Price "+model.getPrice());
        holder.count.setText(model.getCount());

        Glide.with(holder.img.getContext())
                .load(model.getUrl())
                .placeholder(R.drawable.common_google_signin_btn_icon_dark)
                .circleCrop()
                .error(R.drawable.common_google_signin_btn_icon_dark_normal)
                .into(holder.img);



        holder.plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String cnt = model.getCount();
                int c = Integer.parseInt(cnt);
                if(c<30){
                    c=c+1;
                    holder.count.setText(String.valueOf(c));
                    StringBuffer s = new StringBuffer();
                    s.append(model.getPrice());
                    s.deleteCharAt(s.length()-1);
                    s.deleteCharAt(s.length()-1);
                    totalamountcart.total = totalamountcart.total + Integer.parseInt(s.toString());


                    FirebaseDatabase.getInstance().getReference("AddtoCart").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(getRef(position).getKey())
                            .child("count").setValue(String.valueOf(c));

                    FirebaseDatabase.getInstance().getReference("TotalAmount/"+FirebaseAuth.getInstance().getCurrentUser().getUid()).child("amount")
                            .setValue(String.valueOf(totalamountcart.total));

                    Cart.totalamount.setText(String.valueOf(totalamountcart.total)+" Rs");
                }
            }
        });

        holder.minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String cnt = model.getCount();
                int c = Integer.parseInt(cnt);
                if(c>0){

                    c=c-1;

                    holder.count.setText(String.valueOf(c));
                    StringBuffer s = new StringBuffer();
                    s.append(model.getPrice());
                    s.deleteCharAt(s.length()-1);
                    s.deleteCharAt(s.length()-1);
                    totalamountcart.total = totalamountcart.total - Integer.parseInt(s.toString());

                    FirebaseDatabase.getInstance().getReference("AddtoCart/"+FirebaseAuth.getInstance().getCurrentUser().getUid()).child(getRef(position).getKey())
                            .child("count").setValue(String.valueOf(c));

                    FirebaseDatabase.getInstance().getReference("TotalAmount/"+FirebaseAuth.getInstance().getCurrentUser().getUid()).child("amount")
                            .setValue(String.valueOf(totalamountcart.total));

                    Cart.totalamount.setText(String.valueOf(totalamountcart.total)+" Rs");
                }
            }
        });

    }

    @NonNull
    @Override
    public MainAdapterCart.myViewHolderCart onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.main_item_cart,parent,false);
        return new MainAdapterCart.myViewHolderCart(view);
    }

    class myViewHolderCart extends RecyclerView.ViewHolder{

        CircleImageView img;
        TextView name,price;
        TextView count;
        ImageButton plus,minus;



        public myViewHolderCart(@NonNull View itemView) {
            super(itemView);

            img = (CircleImageView) itemView.findViewById(R.id.img3);
            name = (TextView) itemView.findViewById(R.id.nametextcart);
            price = (TextView) itemView.findViewById(R.id.pricetextcart);
            count = (TextView) itemView.findViewById(R.id.count);
            plus = (ImageButton) itemView.findViewById(R.id.addone);
            minus = (ImageButton) itemView.findViewById(R.id.removeone);

        }
    }
}
