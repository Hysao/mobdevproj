package com.myprograms.dapprapplication.admin;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.myprograms.dapprapplication.R;
import com.myprograms.dapprapplication.model.Products;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class AdminActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseUser mUser;

    private CardView productCard;

    private TextView productTotal;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference products = db.collection("Products");

    private List<Products> productsList ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_admin);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        productCard = findViewById(R.id.productCard);
        productTotal = findViewById(R.id.totalProducts);


        productCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(AdminActivity.this,
                        ProductActivity.class);
                startActivity(i);
            }
        });

        productsList = new ArrayList<>();
        AtomicInteger allStocks = new AtomicInteger(); // Initialize allStocks to 0

        products.get().addOnSuccessListener(queryDocumentSnapshots -> {
            for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                Products product = documentSnapshot.toObject(Products.class);
                productsList.add(product);

                // Calculate total stock directly within the outer loop
                allStocks.addAndGet(product.getProductStock());
            }

            productTotal.setText(String.valueOf(allStocks));
            // Now you have the total stock in 'allStocks'
            // ... (Use 'allStocks' as needed) ...
        });




    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mAuth.signOut();
    }

}