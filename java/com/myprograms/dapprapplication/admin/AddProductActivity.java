package com.myprograms.dapprapplication.admin;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.myprograms.dapprapplication.R;
import com.myprograms.dapprapplication.model.Products;

import java.util.Date;

public class AddProductActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseUser mUser;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference productsRef = db.collection("Products");

    private StorageReference mStorageRef;

    ActivityResultLauncher<String> mTakePhoto;
    Uri imageUri;

    private EditText productName, productPrice, productCategory, productStock;
    private ImageView productImage;
    private ImageButton addProductImage;
    private RadioGroup radioGroup;
    private RadioButton highlight, notHighlight;
    private Button add;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_product);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();

        productName = findViewById(R.id.productNameEdit);
        productPrice = findViewById(R.id.productPriceEdit);
        productCategory = findViewById(R.id.productCategoryEdit);
        productStock = findViewById(R.id.productStockEdit);
        productImage = findViewById(R.id.productImage);

        addProductImage = findViewById(R.id.addImageBtn);
        radioGroup = findViewById(R.id.productHighlightRadioGroup);
        highlight = findViewById(R.id.productHighlightRadioButton);
        notHighlight = findViewById(R.id.productNotHighlightRadioButton);
        add = findViewById(R.id.addProductBtn);

        mStorageRef = FirebaseStorage.getInstance().getReference();

        mTakePhoto = registerForActivityResult(
                new ActivityResultContracts.GetContent(),
                result -> {
                    productImage.setImageURI(result);

                    imageUri = result;
                }
        );

        addProductImage.setOnClickListener(v -> mTakePhoto.launch("image/*"));

        add.setOnClickListener(v -> addProduct());

    }

    private void addProduct() {
        String name = productName.getText().toString();
        int price = Integer.parseInt(String.valueOf(productPrice.getText()));
        String category = productCategory.getText().toString();
        int stock = Integer.parseInt(String.valueOf(productStock.getText()));
        boolean highlight = radioGroup.getCheckedRadioButtonId() == R.id.productHighlightRadioButton;

        if(!TextUtils.isEmpty(name)
                && !TextUtils.isEmpty(category)
        && !TextUtils.isEmpty(String.valueOf(price))
        && !TextUtils.isEmpty(String.valueOf(stock))
        && imageUri != null
        && mStorageRef != null
        && productsRef != null){
            final StorageReference photoRef =
                    mStorageRef.child("productImages/")
                            .child("my_photo_" + Timestamp.now().getSeconds());

            photoRef.putFile(imageUri)
                    .addOnSuccessListener(taskSnapshot -> photoRef.getDownloadUrl().addOnSuccessListener(uri -> {
                        String url = uri.toString();

                        Products product = new Products();
                        product.setProductName(name);
                        product.setProductPrice(price);
                        product.setProductCategory(category);
                        product.setProductStock(stock);
                        product.setProductHighLight(highlight);
                        product.setProductImg(url);
                        product.setTimestamp(new Timestamp(new Date()));

                        productsRef.add(product)
                                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                    @Override
                                    public void onSuccess(DocumentReference documentReference) {
                                        Intent i = new Intent(AddProductActivity.this,
                                                ProductActivity.class);

                                        Toast.makeText(AddProductActivity.this,
                                                "Product Successfully Added",
                                                Toast.LENGTH_SHORT).show();
                                        startActivity(i);
                                        finish();
                                    }
                                }).addOnFailureListener(e -> Toast.makeText(AddProductActivity.this,
                                        "Failed: " + e.getMessage(), Toast.LENGTH_SHORT).show());;

                    })).addOnFailureListener(e -> {

                        Toast.makeText(AddProductActivity.this,
                                "Failed!!", Toast.LENGTH_SHORT).show();
                    });
        }else{
            Toast.makeText(this,
                    "Fill al the Fields",
                    Toast.LENGTH_SHORT).show();
        }


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mAuth.signOut();
    }
}