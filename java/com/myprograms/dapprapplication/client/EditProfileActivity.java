package com.myprograms.dapprapplication.client;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.myprograms.dapprapplication.R;
import com.myprograms.dapprapplication.model.User;

public class EditProfileActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseUser user;

    private FirebaseFirestore dp = FirebaseFirestore.getInstance();
    private CollectionReference users = dp.collection("User");

    private EditText name, email, phone, address;
    private Button save, cancel;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_edit_profile);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();

        name = findViewById(R.id.nameEdit);
        email = findViewById(R.id.emailEdit);
        phone = findViewById(R.id.phoneEdit);
        address = findViewById(R.id.addressEdit);
        save = findViewById(R.id.save);
        cancel = findViewById(R.id.cancel);

       save.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               addUser();
           }
       });


    }
   private void addUser(){
        String uName, uEmail, uPhone, uAddress, uId;
        uName = name.getText().toString();
        uEmail = email.getText().toString();
        uPhone = phone.getText().toString();
        uAddress = address.getText().toString();
        uId = user.getUid();
        
        if(!TextUtils.isEmpty(uName) 
                && !TextUtils.isEmpty(uEmail) 
                && !TextUtils.isEmpty(uPhone) 
                && !TextUtils.isEmpty(uAddress)){

            User user1 = new User(uName,  uAddress, uEmail, uPhone, uId);
            users.add(user1).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                @Override
                public void onSuccess(DocumentReference documentReference) {
                    Toast.makeText(EditProfileActivity.this, "Added Successfully", Toast.LENGTH_SHORT).show();
                }
            });
            
        }
        
   }
}