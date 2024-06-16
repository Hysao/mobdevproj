package com.myprograms.dapprapplication.adapter;

import static androidx.core.content.ContextCompat.startActivity;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.myprograms.dapprapplication.R;
import com.myprograms.dapprapplication.client.CartActivity;
import com.myprograms.dapprapplication.model.Cart;

import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartItemViewHolder> {

    private final List<Cart> cartList;
    private final Context context;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference cartRef = db.collection("Cart");

    public CartAdapter(CartActivity cartActivity, List<Cart> cartList) {
        this.cartList = cartList;
        this.context = cartActivity;
    }


    @NonNull
    @Override
    public CartItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.itemview_cart_view, parent, false);
        return new CartItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartItemViewHolder holder, int position) {

        Cart cart = cartList.get(position);

        // Fix: Set the item name correctly
        holder.cartItemName.setText(cart.getItemName()); // Use getItemName() instead of getItemColor()
        holder.cartItemPrice.setText(String.valueOf(cart.getPrice()));
        holder.itemSize.setText(cart.getItemSize());
        holder.itemColor.setText(cart.getItemColor());
        holder.itemQuantity.setText(String.valueOf(cart.getQuantity()));

        String imageUrl = cart.getItemImage();

        Glide.with(context)
                .load(imageUrl)
                .fitCenter()
                .into(holder.cartItemImg);

        int totalPrice = cart.getPrice() * cart.getQuantity();
        holder.cartItemPrice.setText(String.valueOf(totalPrice));


        cartRef.whereEqualTo("itemName", cart.getItemName())
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()) {
                        String documentId = queryDocumentSnapshots.getDocuments().get(0).getId();
                        cart.setDocumentId(documentId); // Assuming Cart has a setDocumentId() method
                    } else {// Handle case where no matching document is found
                    }
                })
                .addOnFailureListener(e -> {
                    // ... error handling ...
                });

        holder.increaseQ.setOnClickListener(v -> {
            cart.setQuantity(cart.getQuantity() + 1);notifyItemChanged(position);

            // Update quantity in Firestore
            String documentId = cart.getDocumentId();
            if (documentId != null) {
                cartRef.document(documentId)
                        .update("quantity", cart.getQuantity()) // Assuming "quantity" is thefield name in Firestore
                        .addOnSuccessListener(aVoid -> {
                            // Quantity updated successfully in Firestore
                        })
                        .addOnFailureListener(e -> {
                            // Handle the failure to update in Firestore
                            Toast.makeText(context, "Failed to update quantity", Toast.LENGTH_SHORT).show();
                        });
            } else {
                // Handle the case where documentId is null
                Toast.makeText(context, "Error: Missing document ID", Toast.LENGTH_SHORT).show();
            }
            int newTotalPrice = cart.getPrice() * cart.getQuantity();
            holder.cartItemPrice.setText(String.valueOf(newTotalPrice));
        });

        holder.decreaseQ.setOnClickListener(v -> {
            if (cart.getQuantity() > 0) {
                cart.setQuantity(cart.getQuantity() - 1);
                if (cart.getQuantity() == 0) {
                    // Remove from Firestore using the stored document ID
                    String documentId = cart.getDocumentId();
                    if (documentId != null) {
                        cartRef.document(documentId).delete()
                                .addOnSuccessListener(aVoid -> {
                                    cartList.remove(position);
                                    notifyItemRemoved(position);
                                    notifyItemRangeChanged(position, cartList.size());
                                })
                                .addOnFailureListener(e -> {
                                    Toast.makeText(context, "Failed to remove item", Toast.LENGTH_SHORT).show();
                                });
                    } else {
                        Toast.makeText(context, "Error: Missing document ID", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    notifyItemChanged(position);
                }
                int newTotalPrice = cart.getPrice() * cart.getQuantity();
                holder.cartItemPrice.setText(String.valueOf(newTotalPrice));
            }
        });

        boolean isChecked = holder.checkOutReady.isChecked();
        if (holder.checkOutReady.isChecked()) {
            // Add item to checked list
            cartList.get(holder.getAdapterPosition());
        }

    }

    @Override
    public int getItemCount() {
        return cartList.size();
    }

    public static class CartItemViewHolder extends RecyclerView.ViewHolder{

        public ImageView cartItemImg;
        public CheckBox checkOutReady;
        public TextView cartItemName, cartItemPrice, itemQuantity, itemSize, itemColor;
        public ImageButton increaseQ, decreaseQ;

        public CartItemViewHolder(@NonNull View itemView) {
            super(itemView);

            cartItemImg = itemView.findViewById(R.id.cartItemImg);
            checkOutReady = itemView.findViewById(R.id.checkOutReady);
            cartItemName = itemView.findViewById(R.id.cartItemName);
            cartItemPrice = itemView.findViewById(R.id.cartItemPrice);
            increaseQ = itemView.findViewById(R.id.increaseQuantity);
            decreaseQ = itemView.findViewById(R.id.decreaseQuantity);
            itemQuantity = itemView.findViewById(R.id.itemQuantity);
            itemSize = itemView.findViewById(R.id.shirtSize);
            itemColor = itemView.findViewById(R.id.shirtColor);

        }
    }
}
