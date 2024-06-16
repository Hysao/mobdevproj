package com.myprograms.dapprapplication.admin;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.myprograms.dapprapplication.R;
import com.myprograms.dapprapplication.adapter.ProductAdminAdapter;
import com.myprograms.dapprapplication.model.Products;

import java.util.ArrayList;
import java.util.List;

public class ProductActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseUser mUser;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference productsRef = db.collection("Products");

    private List<Products> productsList;
    private ProductAdminAdapter productAdminAdapter;

    private ImageButton addProductBtn;

    private RecyclerView adminProductRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_product);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();

        addProductBtn = findViewById(R.id.addImageButton);
        adminProductRecyclerView = findViewById(R.id.adminProductRecycler);


        addProductBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProductActivity.this, AddProductActivity.class);
                startActivity(intent);
            }
        });

        adminProductRecyclerView.setHasFixedSize(true);
        adminProductRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));

        productsList = new ArrayList<>();

        productsRef.get().addOnSuccessListener(queryDocumentSnapshots -> {

            for(QueryDocumentSnapshot product : queryDocumentSnapshots) {

                Products products = product.toObject(Products.class);

                productsList.add(products);

            }

            productAdminAdapter = new ProductAdminAdapter(productsList,
                    ProductActivity.this);

            adminProductRecyclerView.setAdapter(productAdminAdapter);

            productAdminAdapter.notifyDataSetChanged();


        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(ProductActivity.this,
                        "Something went wrong",
                        Toast.LENGTH_SHORT).show();
            }
        });

    }
}