package com.myprograms.dapprapplication.client;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.myprograms.dapprapplication.R;
import com.myprograms.dapprapplication.auth.LoginActivity;
import com.myprograms.dapprapplication.model.Cart;
import com.myprograms.dapprapplication.model.Products;

import java.util.Objects;

public class ProductDetailActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseUser mUser;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference productsRef = db.collection("Products");

    private CollectionReference cartRef = db.collection("Cart");

    private FirebaseStorage storage = FirebaseStorage.getInstance();



    private TextView productTitle, productPrice;
    private ImageView productImage;
    private Button addToCartButton;

    private ImageButton closeButton, cartButton;
    private RadioGroup sizeRadioGroup;
    private RadioButton size1, size2, size3, size4, size5;
    private RadioButton color1, color2, color3;

    private String imageUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_product_detail);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        mAuth = FirebaseAuth.getInstance();
        mAuthListener = firebaseAuth -> {
            mUser = firebaseAuth.getCurrentUser();
            if (mUser == null) {
                finish();
                startActivity(new Intent(this, LoginActivity.class));
            }
        };

        addToCartButton = findViewById(R.id.addToCartButton);
        productImage = findViewById(R.id.productDetailImage);
        productTitle = findViewById(R.id.productDetailName);
        productPrice = findViewById(R.id.productDetailPrice);
        closeButton = findViewById(R.id.closeButton);
        cartButton = findViewById(R.id.cartButton);

        sizeRadioGroup = findViewById(R.id.productDetailRadioGroup);
        size1 = findViewById(R.id.sSize);
        size2 = findViewById(R.id.mSize);
        size3 = findViewById(R.id.lSize);
        size4 = findViewById(R.id.xlSize);
        size5 = findViewById(R.id.xxlSize);

        color1 = findViewById(R.id.greenColor);
        color2 = findViewById(R.id.whiteColor);
        color3 = findViewById(R.id.blackColor);

        closeButton.setOnClickListener(v -> finish());
        cartButton.setOnClickListener(v -> startActivity(new Intent(this, CartActivity.class)));


        String productName = getIntent().getStringExtra("product_name");



        Query prodQuery = productsRef.whereEqualTo("productName", productName);
        prodQuery.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null){
                    Log.e("ProductDetailActivity", "onEvent: error");
                    return;

                }

                assert value != null;
                for (DocumentSnapshot doc : value.getDocuments()){
                    Products product = doc.toObject(Products.class);
                    if (product != null){
                        imageUrl = product.getProductImg();
                        int productPriceDetail = product.getProductPrice();
                        String productTitleDetail = product.getProductName();

                        productTitle.setText(productTitleDetail);
                        productPrice.setText(String.valueOf(productPriceDetail));

                        StorageReference imageRef = storage.getReferenceFromUrl(imageUrl);
                        imageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                            Glide.with(ProductDetailActivity.this)
                                    .load(uri)
                                    .into(productImage);
                        });

                    }
                }
            }
        });

        addToCartButton.setOnClickListener(v -> addToCart());


    }

    public void addToCart(){
        String size;
        int quantity = 1;
        String itemName = productTitle.getText().toString();
        int price = Integer.parseInt(productPrice.getText().toString());
        String userId = Objects.requireNonNull(mAuth.getCurrentUser()).getUid();
        String color;

        if (size1.isChecked()){
            size = "S";
        } else if (size2.isChecked()) {
            size = "M";
        }else if (size3.isChecked()) {
            size = "L";
        }else if (size4.isChecked()) {
            size = "XL";
        }else {
            size = "XXL";
        }

        if (color1.isChecked()){
            color = "green";
        } else if (color2.isChecked()) {
            color = "white";
        }else {
            color = "black";
        }


        Cart cart = new Cart(userId, itemName, quantity, price, color, size, imageUrl);
        cartRef.add(cart);


        Toast.makeText(this, "Added to cart", Toast.LENGTH_SHORT).show();
    }
}