package com.blackdev.thaparhelper;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.blackdev.thaparhelper.dashboard.DashBoardActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    @Override
    protected void onStart() {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser!=null){
            updateUI();
        }
        super.onStart();
    }

    private void updateUI() {
        Intent intent = new Intent(this, DashBoardActivity.class);
        startActivity(intent);
        finish();
    }

    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();
        Button loginInButton = findViewById(R.id.switchToLoginActivity);
        Button signUpButton = findViewById(R.id.switchToSignUpActivity);
        loginInButton.setOnClickListener(this);
        signUpButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.switchToLoginActivity:
                Intent loginIntent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(loginIntent);
                break;
            case R.id.switchToSignUpActivity:
                Intent signUpIntent = new Intent(MainActivity.this,SignUpActivity.class);
                startActivity(signUpIntent);
                break;
        }

    }
}