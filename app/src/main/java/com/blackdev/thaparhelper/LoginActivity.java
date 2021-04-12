package com.blackdev.thaparhelper;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.blackdev.thaparhelper.allutils.CredentialChecker;
import com.blackdev.thaparhelper.allutils.Utils;
import com.blackdev.thaparhelper.dashboard.DashBoardActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private TextInputEditText emailInput,passwordInput;
    private TextInputLayout emailLayout,passwordLayout;
    private ConstraintLayout rootLayout;
    private FirebaseAuth mAuth;
    String TAGLOGIN = "Login: ";


    void init(){
        mAuth = FirebaseAuth.getInstance();
        rootLayout =  findViewById(R.id.rootLoginLayout);
        emailInput = findViewById(R.id.emailLogin);
        passwordInput = findViewById(R.id.passwordLogin);
        emailLayout = findViewById(R.id.emailLayoutLogin);
        passwordLayout = findViewById(R.id.passwordLayoutLogin);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        init();
        Button loginUser = findViewById(R.id.loginUser);
        TextView forgotPassword = findViewById(R.id.forgotPasswordLogin);
        loginUser.setOnClickListener(this);
        forgotPassword.setOnClickListener(this);


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.loginUser:
                emailLayout.setErrorEnabled(false);
                passwordLayout.setErrorEnabled(false);
                String emailId = emailInput.getText().toString().trim();
                String password = passwordInput.getText().toString().trim();
                if(emailId.isEmpty()){
                    emailLayout.setErrorEnabled(true);
                    emailLayout.requestFocus();
                    emailLayout.setError("Type something here.");
                } else {
                    //emailInput.setFocusable(false);
                    //emailInput.setError(null);
                    CredentialChecker checker = new CredentialChecker();
                    int val = checker.validateEmail(emailId);
                    if(val == -1) {
                        val = checker.validatePassword(password);
                        if(val == -1) {
                            passwordLayout.setError(null);
                            passwordLayout.setErrorEnabled(false);
                            validateUser(emailId,password);
                        } else {
                            passwordLayout.setErrorEnabled(true);
                            passwordLayout.requestFocus();
                            passwordLayout.setError(checker.getError(val));
                        }
                    } else {
                        //emailLayout.setErrorEnabled(true);
                        emailLayout.requestFocus();
                        emailLayout.setError(checker.getError(val));
                    }
                    //check valid mail
                    //check valid password
                }

        }
    }

    void updateUI(FirebaseUser user){
        Intent loginIntent = new Intent(LoginActivity.this, DashBoardActivity.class);
        loginIntent.putExtra("firebaseUser", user);
        startActivity(loginIntent);
        finish();
    }

    void validateUser(String email, String pass) {
        if(!Utils.isNetworkAvailable(this)){
            Snackbar.make(rootLayout, "No Internet connection.", Snackbar.LENGTH_SHORT).show();
        } else {
            mAuth.signInWithEmailAndPassword(email, pass).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        Log.d(TAGLOGIN, "SignIn: Success");
                        FirebaseUser user = mAuth.getCurrentUser();
                        updateUI(user);
                    } else {
                        Log.w(TAGLOGIN, "SignIn: Failure");
                        Snackbar.make(rootLayout, "Incorrect email or password.", Snackbar.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }


}