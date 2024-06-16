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
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.myprograms.dapprapplication.R;
import com.myprograms.dapprapplication.ShoppingActivity;
import com.myprograms.dapprapplication.admin.AdminActivity;

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseUser mUser;


    private Button loginButton, registerButton;
    private EditText email, password;
    private TextView forgotPassword, back;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        loginButton = findViewById(R.id.loginButton);
        registerButton = findViewById(R.id.createNewAccount);
        email = findViewById(R.id.emailInput);
        password = findViewById(R.id.passwordInput);
        back = findViewById(R.id.textView);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(
                        LoginActivity.this,
                        ShoppingActivity.class
                );

                startActivity(i);
            }
        });


        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(i);
            }
        });

        mAuth = FirebaseAuth.getInstance();

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login(
                        email.getText().toString(),
                        password.getText().toString()
                );
            }
        });
    }


    private void login(
            String email, String password
    ){

        if(!TextUtils.isEmpty(email)
        && !TextUtils.isEmpty(password)){
            mAuth.signInWithEmailAndPassword(
                    email, password
            ).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                @Override
                public void onSuccess(AuthResult authResult) {
                    FirebaseUser user = mAuth.getCurrentUser();
                    String userId;
                    assert user != null;
                    userId = user.getUid();


                    if(userId.equals("CPN86Nc0CFWvMhEU7cfcQ6NLjcf1")
                    ){

                        Intent i = new Intent(LoginActivity.this, AdminActivity.class);
                        startActivity(i);
                        Toast.makeText(LoginActivity.this,
                                "Admin Login Successfully",
                                Toast.LENGTH_SHORT).show();
                    } else {
                        Intent i = new Intent(LoginActivity.this, ShoppingActivity.class);
                        startActivity(i);
                        Toast.makeText(LoginActivity.this,
                                "Login Successfully",
                                Toast.LENGTH_SHORT).show();
                    }

                }
            });

        }

    }
}