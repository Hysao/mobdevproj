package com.myprograms.dapprapplication.client;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.myprograms.dapprapplication.R;
import com.myprograms.dapprapplication.adapter.CartAdapter;
import com.myprograms.dapprapplication.model.Cart;

import java.util.ArrayList;
import java.util.List;

public class CartActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseUser user;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference cartRef = db.collection("Cart");

    private FirebaseStorage storage = FirebaseStorage.getInstance();

    private RecyclerView recyclerView;
    private TextView total, itemCount;

    private Button checkout;

    private List<Cart> cartList;
    private CartAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_cart);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        // In your Activity or Fragment



        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();


        recyclerView = findViewById(R.id.cartRecycler);
        total = findViewById(R.id.totalAmount);
        itemCount = findViewById(R.id.itemCount);
        checkout = findViewById(R.id.checkout);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        Query query = cartRef.whereEqualTo("uid", user.getUid());

        cartList = new ArrayList<>();

        query.get().addOnSuccessListener(queryDocumentSnapshots -> {
            // Remove the duplicate item addition
            for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                cartList.add(documentSnapshot.toObject(Cart.class));
            }

            itemCount.setText(String.valueOf(cartList.size()));

// Improved total calculation considering quantity
            int totalAmount = cartList.stream()
                    .mapToInt(cart -> cart.getPrice() * cart.getQuantity()) // Multiply price by quantity
                    .sum();
            total.setText(String.valueOf(totalAmount));

            adapter = new CartAdapter(CartActivity.this, cartList);
            recyclerView.setAdapter(adapter);

            adapter.notifyDataSetChanged();

        }).addOnFailureListener(e ->
                Toast.makeText(CartActivity.this, "Something went wrong!", Toast.LENGTH_SHORT).show()
        );

        checkout.setOnClickListener(v -> {
            if (cartList.isEmpty()) {
                Toast.makeText(CartActivity.this, "Your cart is empty!", Toast.LENGTH_SHORT).show();
            } else {
                // Proceed to checkout
                Intent checkoutIntent = new Intent(CartActivity.this, CheckoutActivity.class);
                checkoutIntent.putParcelableArrayListExtra("cartList", new ArrayList<>(cartList));
                startActivity(checkoutIntent);
                Toast.makeText(CartActivity.this, "Proceed to checkout!", Toast.LENGTH_SHORT).show();
            }
        });


            }

        }