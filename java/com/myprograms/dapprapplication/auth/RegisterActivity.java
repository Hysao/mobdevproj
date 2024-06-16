package com.myprograms.dapprapplication.auth;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.myprograms.dapprapplication.R;
import com.myprograms.dapprapplication.model.User;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseUser mUser;


    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference users = db.collection("User");

    private EditText mEmail, mPassword, mConfirmPassword, mName, mPhone, mAddress;
    private Button mRegister;
    private TextView mBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        mAuth = FirebaseAuth.getInstance();

        mEmail = findViewById(R.id.email);
        mPassword = findViewById(R.id.password);
        mConfirmPassword = findViewById(R.id.confirmPassword);
        mName = findViewById(R.id.fullName);
        mRegister = findViewById(R.id.register);
        mBack = findViewById(R.id.backRegister);
        mPhone = findViewById(R.id.phoneNumber);
        mAddress = findViewById(R.id.address);

        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(RegisterActivity.this, LoginActivity.class);

                startActivity(i);
            }
        });

        mRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mPassword.getText().toString().equals(mConfirmPassword.getText().toString())) {
                    CreateUserEmailAccount(
                            mEmail.getText().toString(),
                            mPassword.getText().toString(),
                            mName.getText().toString(),
                            mPhone.getText().toString(),
                            mAddress.getText().toString(),
                            mConfirmPassword.getText().toString()
                            );
                } else {
                    Toast.makeText(RegisterActivity.this,
                            "Password does not match",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void CreateUserEmailAccount(
            String email,
            String pass,
            String username,
            String phone,
            String address,
            String confirmPass
    ) {
        if (!TextUtils.isEmpty(email)
                && !TextUtils.isEmpty(pass)
                && !TextUtils.isEmpty(username)
                && !TextUtils.isEmpty(phone)
                && !TextUtils.isEmpty(address)
                && pass.equals(confirmPass)
        ) {
            mAuth.createUserWithEmailAndPassword(
                    email, pass
            ).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {

                        mUser = mAuth.getCurrentUser();
                        String userId = mUser.getUid();


                        // Add a new document with a generated ID

//                        Map<String, Object> user = new HashMap<>();
//                        user.put("id", userId);
//                        user.put("name", username);
//                        user.put("phone", phone);
//                        user.put("address", address);
//                        user.put("email", email);
//
//                        users.document(userId).set(user);

                        User user = new User(
                                userId,
                                username,
                                phone,
                                address,
                                email
                                );

                        users.add(user);


                        // The user is created successfully!
                        Intent i = new Intent(RegisterActivity.this, LoginActivity.class);

                        Toast.makeText(RegisterActivity.this,
                                "Account is created successfully",
                                Toast.LENGTH_SHORT).show();

                        startActivity(i);
                    }
                }
            });

        }
    }

}