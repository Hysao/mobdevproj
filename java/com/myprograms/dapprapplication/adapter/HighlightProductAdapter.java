package com.myprograms.dapprapplication.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.myprograms.dapprapplication.R;
import com.myprograms.dapprapplication.client.ProductDetailActivity;
import com.myprograms.dapprapplication.model.Products;

import java.util.List;

public class HighlightProductAdapter extends RecyclerView.Adapter<HighlightProductAdapter.HighlightProductViewHolder> {

    private final List<Products> productsList;
    private final Context context;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference productsRef = db.collection("Products");

    public HighlightProductAdapter(List<Products> productsList, Context context) {

        this.productsList = productsList;
        this.context = context;

    }


    @NonNull
    @Override
    public HighlightProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.itemview_product_client, parent, false);
        return new HighlightProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HighlightProductViewHolder holder, int position) {

        Products products = productsList.get(position);

        holder.productName.setText(products.getProductName());
        holder.productPrice.setText(String.valueOf(products.getProductPrice()));

        String imgUrl = products.getProductImg();

        Glide.with(context)
                .load(imgUrl)
                .fitCenter()
                .into(holder.productImage);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, ProductDetailActivity.class);
                i.putExtra("product_name", products.getProductName());
                context.startActivity(i);
            }
        });

        if(products.isWishListed()){
            holder.addFavorite.setImageResource(R.drawable.vc_heart_24);
        }
        if (!products.isWishListed()){
            holder.addFavorite.setImageResource(R.drawable.vc_hollow_heart_24);
        }

        productsRef.whereEqualTo("productName", products.getProductName())
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()) {
                        String documentId = queryDocumentSnapshots.getDocuments().get(0).getId();
                        products.setDocumentId(documentId);

                        // Now you can use documentId to update isWishListed in Firestore
                        updateWishListStatus(holder, products);
                    } else {
                        // Handle case where no matching document is found
                    }
                })
                .addOnFailureListener(e -> {
                    // ... error handling ...
                });

        // ... (itemView click listener - same as before) ...
    }



    private void updateWishListStatus(HighlightProductViewHolder holder, Products products) {
        if (products.isWishListed()) {

            holder.addFavorite.setOnClickListener(v -> {
                products.setWishListed(false);
                updateFirestoreWishList(products);
                holder.addFavorite.setImageResource(R.drawable.vc_hollow_heart_24);
                Toast.makeText(context, "Remove from Wish List", Toast.LENGTH_SHORT).show();
            });
        } else {

            holder.addFavorite.setOnClickListener(v -> {
                products.setWishListed(true);
                updateFirestoreWishList(products);
                holder.addFavorite.setImageResource(R.drawable.vc_heart_24);
                Toast.makeText(context, "Wish Listed", Toast.LENGTH_SHORT).show();
            });
        }
    }

    private void updateFirestoreWishList(Products products) {
        String documentId = products.getDocumentId();
        if (documentId != null) {
            productsRef.document(documentId)
                    .update("isWishListed", products.isWishListed()) // Assuming "isWishListed" is the field name
                    .addOnSuccessListener(aVoid -> {
                        // Wish list status updated successfully in Firestore
                    })
                    .addOnFailureListener(e -> {
                        // Handle the failure to update in Firestore
                        Toast.makeText(context, "Failed to update wish list status", Toast.LENGTH_SHORT).show();
                    });
        } else {
            // Handle the case where documentId is null
            Toast.makeText(context, "Error: Missing document ID", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public int getItemCount() {
        return productsList.size();
    }

    public static class HighlightProductViewHolder extends RecyclerView.ViewHolder {

        public ImageView productImage;
        public TextView productName;
        public TextView productPrice;
        public ImageButton addFavorite;

        public HighlightProductViewHolder(View itemView) {
            super(itemView);

            productImage = itemView.findViewById(R.id.productImgClient);
            productName = itemView.findViewById(R.id.productNameClient);
            productPrice = itemView.findViewById(R.id.productPriceClient);
            addFavorite = itemView.findViewById(R.id.addFavorite);
        }



    }
}
