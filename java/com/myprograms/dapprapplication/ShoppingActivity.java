package com.myprograms.dapprapplication;

import static android.widget.LinearLayout.HORIZONTAL;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.myprograms.dapprapplication.adapter.HighlightProductAdapter;
import com.myprograms.dapprapplication.auth.LoginActivity;
import com.myprograms.dapprapplication.client.CartActivity;
import com.myprograms.dapprapplication.client.FavoritesActivity;
import com.myprograms.dapprapplication.client.NotificationsActivity;
import com.myprograms.dapprapplication.client.ProductsActivity;
import com.myprograms.dapprapplication.client.ProfileActivity;
import com.myprograms.dapprapplication.model.Products;

import java.util.ArrayList;
import java.util.List;

public class ShoppingActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseUser mUser;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference productsRef = db.collection("Products");
     private ImageButton cartBtn;
     private TextView homeBtn, productsBtn, favoritesBtn, notificationsBtn, profileBtn;
     private RecyclerView highlightRecycler;
     private List<Products> productsList;
     private HighlightProductAdapter highlightProductAdapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();


        cartBtn = findViewById(R.id.cartBtn);
        homeBtn = findViewById(R.id.homeBtn);
        productsBtn = findViewById(R.id.productsBtn);
        favoritesBtn = findViewById(R.id.favoritesBtn);
        notificationsBtn = findViewById(R.id.notificationsBtn);
        profileBtn = findViewById(R.id.profileBtn);


        highlightRecycler = findViewById(R.id.productRecycler);

        if (mUser != null) {
            // User is signed in
            // ...
        } else {
            // User is signed out
            // ...
        }

       cartBtn.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               Intent i;
               if(mUser != null) {
                   i = new Intent(ShoppingActivity.this,
                           CartActivity.class);
                   startActivity(i);
               } else {
                   i = new Intent(ShoppingActivity.this,
                           LoginActivity.class);
                   startActivity(i);
               }
           }
       });

        productsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i;
                if(mUser != null) {
                    i = new Intent(ShoppingActivity.this,
                            ProductsActivity.class);
                    startActivity(i);
                }
                else {
                    i = new Intent(ShoppingActivity.this,
                            LoginActivity.class);
                    startActivity(i);
                }
            }
        });

        favoritesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i;
                if(mUser != null) {
                    i = new Intent(ShoppingActivity.this,
                            FavoritesActivity.class);
                    startActivity(i);
                }
                else {
                    i = new Intent(ShoppingActivity.this,
                            LoginActivity.class);
                    startActivity(i);
                }
            }
        });

        notificationsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i;
                if(mUser != null) {
                    i = new Intent(ShoppingActivity.this,
                            NotificationsActivity.class);
                    startActivity(i);
                }
                else {
                    i = new Intent(ShoppingActivity.this,
                            LoginActivity.class);
                    startActivity(i);
                }
            }
        });

        profileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i;
                if(mUser != null) {
                    i = new Intent(ShoppingActivity.this,
                            ProfileActivity.class);
                    startActivity(i);
                }
                else {
                    i = new Intent(ShoppingActivity.this,
                            LoginActivity.class);
                    startActivity(i);
                }
            }
        });

        highlightRecycler.setHasFixedSize(true);
//        highlightRecycler.setLayoutManager(new LinearLayoutManager( LinearLayoutManager.HORIZONTAL, false));
        highlightRecycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        Query highlightQuery = productsRef.whereEqualTo("productHighLight", true);

        productsList = new ArrayList<>();

        highlightQuery.get().addOnSuccessListener(queryDocumentSnapshots -> {
            for(QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                documentSnapshot.toObject(Products.class);

                productsList.add(documentSnapshot.toObject(Products.class));

            }
            
            highlightProductAdapter = new HighlightProductAdapter(productsList, ShoppingActivity.this);

            highlightRecycler.setAdapter(highlightProductAdapter);

            highlightProductAdapter.notifyDataSetChanged();



        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(ShoppingActivity.this,
                        "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        });



    }
}