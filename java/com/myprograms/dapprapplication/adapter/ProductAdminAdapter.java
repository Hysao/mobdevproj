package com.myprograms.dapprapplication.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.myprograms.dapprapplication.R;
import com.myprograms.dapprapplication.admin.EditProductActivity;
import com.myprograms.dapprapplication.model.Products;

import java.util.List;

public class ProductAdminAdapter extends RecyclerView.Adapter<ProductAdminAdapter.ProductAdminViewHolder>{

    private final List<Products> productsList;
    private final Context context;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference productsRef = db.collection("Products");

    public ProductAdminAdapter(List<Products> productsList, Context context) {
        this.productsList = productsList;
        this.context = context;
    }

    @NonNull
    @Override
    public ProductAdminViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.itemview_admin_products, parent, false);
        return new ProductAdminViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductAdminViewHolder holder, int position) {
        Products products = productsList.get(position);

        holder.tvProductName.setText(products.getProductName());
        holder.tvProductStock.setText(String.valueOf(products.getProductStock()));

        String imageUrl = products.getProductImg();

        Glide.with(context)
                .load(imageUrl)
                .fitCenter()
                .into(holder.ivProductImage);


        productsRef.whereEqualTo("productName", products.getProductName())
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()) {
                        String documentId = queryDocumentSnapshots.getDocuments().get(0).getId();
                        products.setDocumentId(documentId);

                        // Delete listener
                        holder.btnDelete.setOnClickListener(v -> {
                            if (documentId != null) {
                                productsRef.document(documentId)
                                        .delete()
                                        .addOnSuccessListener(aVoid -> {
                                            Toast.makeText(context, "Product deleted successfully", Toast.LENGTH_SHORT).show();
                                            // Update UI (remove item from list and notify adapter)
                                            productsList.remove(position);
                                            notifyItemRemoved(position);
                                            notifyItemRangeChanged(position, productsList.size());
                                        })
                                        .addOnFailureListener(e -> {
                                            Toast.makeText(context, "Failed to delete product", Toast.LENGTH_SHORT).show();
                                        });
                            } else {
                                Toast.makeText(context, "Error: Missing document ID", Toast.LENGTH_SHORT).show();
                            }
                        });

                        // Edit listener (placeholder for now)
                        holder.btnEdit.setOnClickListener(v -> {
                            Intent intent = new Intent(context, EditProductActivity.class);
                            intent.putExtra("productName", products.getProductName());
                            context.startActivity(intent);
                        });

                    } else {
                        // Handle case where no matching document is found
                    }
                })
                .addOnFailureListener(e -> {
                    // ... error handling ...
                });


    }

    @Override
    public int getItemCount() {
        return productsList.size();
    }

    public static class ProductAdminViewHolder extends RecyclerView.ViewHolder {

        public TextView tvProductName, tvProductStock, textView;
        public ImageView ivProductImage;
        public ImageButton btnDelete, btnEdit;

        public ProductAdminViewHolder(@NonNull View itemView) {
            super(itemView);

            tvProductName = itemView.findViewById(R.id.productName);
            tvProductStock = itemView.findViewById(R.id.productStock);
            ivProductImage = itemView.findViewById(R.id.imageProductAdmin);
            btnDelete = itemView.findViewById(R.id.removeButton);
            btnEdit = itemView.findViewById(R.id.editButton);
            textView = itemView.findViewById(R.id.textView4);

        }

    }
}
