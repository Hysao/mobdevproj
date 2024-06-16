package com.myprograms.dapprapplication.adapter;

import android.content.Context;
import android.view.LayoutInflater;import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.myprograms.dapprapplication.R;
import com.myprograms.dapprapplication.model.Cart;

import java.util.List;

public class CheckoutAdapter extends RecyclerView.Adapter<CheckoutAdapter.CheckoutViewHolder> {

    private Context context;
    private List<Cart> cartList;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference cartRef = db.collection("Cart");

    public CheckoutAdapter(Context context, List<Cart> cartList) {
        this.context = context;
        this.cartList = cartList;
    }

    @NonNull
    @Override
    public CheckoutViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        View view = LayoutInflater.from(context).inflate(R.layout.itemview_checkout_view, parent, false); // Use your checkout item layout
        return new CheckoutViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CheckoutViewHolder holder, int position) {

        Cart cartItem = cartList.get(position);

        holder.itemName.setText(cartItem.getItemName());
        holder.itemQuantity.setText(String.valueOf(cartItem.getQuantity()));
        holder.itemColor.setText(cartItem.getItemColor());
        holder.itemSize.setText(cartItem.getItemSize());

        // Load image using Glide
        String imgUrl = cartItem.getItemImage();

        Glide.with(context)
                .load(imgUrl)
                .fitCenter()
                .into(holder.itemImage);

        cartRef.whereEqualTo("itemName", cartItem.getItemName())
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()) {
                        String documentId = queryDocumentSnapshots.getDocuments().get(0).getId();
                        cartItem.setDocumentId(documentId); // Assuming Cart has a setDocumentId() method
                    } else {// Handle case where no matching document is found
                    }
                })
                .addOnFailureListener(e -> {
                    // ... error handling ...
                });

        int totalPrice = cartItem.getPrice() * cartItem.getQuantity();
        holder.itemPrice.setText(String.valueOf(totalPrice));

    }

    @Override
    public int getItemCount() {
        return cartList.size();
    }

    public static class CheckoutViewHolder extends RecyclerView.ViewHolder {
        public TextView itemName, itemPrice, itemQuantity, itemColor, itemSize;
        public ImageView itemImage;

        public CheckoutViewHolder(View itemView) {
            super(itemView);
            itemName = itemView.findViewById(R.id.itemName); // Adjust IDs as per your layout
            itemPrice = itemView.findViewById(R.id.itemPrice);
            itemQuantity = itemView.findViewById(R.id.itemQuantity);
            itemColor = itemView.findViewById(R.id.itemColor);
            itemSize = itemView.findViewById(R.id.itemSize);
            itemImage = itemView.findViewById(R.id.imageView);
        }
    }
}