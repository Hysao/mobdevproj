package com.myprograms.dapprapplication.client;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.myprograms.dapprapplication.R;
import com.myprograms.dapprapplication.adapter.CheckoutAdapter;
import com.myprograms.dapprapplication.model.Cart;
import com.myprograms.dapprapplication.model.Order;
import com.myprograms.dapprapplication.model.User;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

public class CheckoutActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseUser mUser;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference usersRef = db.collection("User");
    private CollectionReference cartRef = db.collection("Cart");
    private CollectionReference orderRef = db.collection("Order");

    private FirebaseStorage storage = FirebaseStorage.getInstance();

    private RecyclerView recyclerView;
    private CheckoutAdapter checkoutAdapter;
    private List<Cart> cartList;

    private TextView totalItemAmount, totalAmountTxt, currentDate, userName, userAddress, transactionId,
            shipping;
    private Button placeOrder;

    private RadioGroup paymentMethod, shippingMethod;
    private RadioButton cashOnDelivery, creditCard, ewallet;
    private RadioButton shippingJnt, shippingGogo, shippingLalamove;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_checkout);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();

        recyclerView = findViewById(R.id.checkoutRecycler);
        //total items only amount
        totalItemAmount = findViewById(R.id.totalCheckout);
        //total overall amount
        totalAmountTxt = findViewById(R.id.subtotal);

        //date
        currentDate = findViewById(R.id.currentDate);
        userName = findViewById(R.id.nameCheckout);
        userAddress = findViewById(R.id.addressCheckout);
        transactionId = findViewById(R.id.idCheckout);
        shipping = findViewById(R.id.shippingFee);

        shippingMethod = findViewById(R.id.deliveryMethod);
        shippingJnt = findViewById(R.id.deliveryJnt);
        shippingGogo = findViewById(R.id.deliveryGogo);
        shippingLalamove = findViewById(R.id.deliveryLalamove);


        paymentMethod = findViewById(R.id.paymentMethod);
        cashOnDelivery = findViewById(R.id.paymentCash);
        creditCard = findViewById(R.id.paymentBank);
        ewallet = findViewById(R.id.paymentEwallet);

        placeOrder = findViewById(R.id.confirmCheckout);
        


        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        AtomicInteger itemTotal = new AtomicInteger();

        AtomicInteger totalItems = new AtomicInteger();



        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date date = new Date();
        String dateStr = formatter.format(date);
        currentDate.setText(dateStr);

        Random rand = new Random();
        int min = 1000;
        int max = 9999;

        int rand1 = rand.nextInt(max - min + 1) + min;
        int rand2 = rand.nextInt(max - min + 1) + min;

        String transaction = "DAPPR" + String.valueOf(rand1) + String.valueOf(rand2);
        transactionId.setText(transaction);

        String payment;
        if(cashOnDelivery.isChecked()){
            payment = "Cash On Delivery";
        }else if(creditCard.isChecked()){
            payment = "Credit Card";
        }else if(ewallet.isChecked()){
            payment = "E-Wallet";
        }else{
            payment = "Cash On Delivery";
        }


        


        shippingJnt.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                     String shippingMethodTxt = "J&T";
                     int shippingFee = 60;
                    // Update UI elements here (e.g., shipping.setText(String.valueOf(shippingFee)))
                    shipping.setText(String.valueOf(shippingFee));
                    totalAmountTxt.setText(String.valueOf(itemTotal.get() + shippingFee));
                }
            }
        });

        shippingGogo.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    String shippingMethodTxt = "Gogo";
                    int shippingFee = 50;
                    // Update UI elements here
                    shipping.setText(String.valueOf(shippingFee));
                    totalAmountTxt.setText(String.valueOf(itemTotal.get() + shippingFee));
                }
            }
        });

        shippingLalamove.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    String shippingMethodTxt = "Lalamove";
                    int shippingFee = 50;
                    // Update UI elements here
                    shipping.setText(String.valueOf(shippingFee));
                    totalAmountTxt.setText(String.valueOf(itemTotal.get() + shippingFee));
                }
            }
        });




        Query query = cartRef.whereEqualTo("uid", mUser.getUid());

        cartList = new ArrayList<>();

        query.get().addOnSuccessListener(queryDocumentSnapshots -> {
            // Remove the duplicate item addition
            for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                cartList.add(documentSnapshot.toObject(Cart.class));
            }
                checkoutAdapter = new CheckoutAdapter(CheckoutActivity.this, cartList);
                recyclerView.setAdapter(checkoutAdapter);
                checkoutAdapter.notifyDataSetChanged();

                for (Cart cart : cartList) {
                    itemTotal.addAndGet(cart.getPrice() * cart.getQuantity());
                    totalItems.addAndGet(cart.getQuantity());
                }


                totalItemAmount.setText(String.valueOf(itemTotal.get()));

            }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(CheckoutActivity.this, "Something went wrong!", Toast.LENGTH_SHORT).show();
            }
        });

        Query userQuery = usersRef.whereEqualTo("userID", mUser.getUid());
        userQuery.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if (!queryDocumentSnapshots.isEmpty()) { // Check if any documents were returned
                    User user = queryDocumentSnapshots.getDocuments().get(0).toObject(User.class);

                    assert user != null;
                    userName.setText(user.getUserName());
                    userAddress.setText(user.getUserAddress());
                } else {
                    // Handle the case where no matching user is found
                    Log.w("CheckoutActivity", "No user found with the given ID.");
                    // You might want to display an error message or take other appropriate actions
                }
            }
        });


        placeOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                placeOrder();
            }
        });

        
    }

    private void placeOrder() {

        Toast.makeText(this, "Ordered Successfully", Toast.LENGTH_SHORT).show();

       Intent intent = new Intent(CheckoutActivity.this, OrderSuccessActivity.class);
       
       startActivity(intent);
       
    }
}