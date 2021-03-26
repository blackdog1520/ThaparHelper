package com.blackdev.thaparhelper;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.blackdev.thaparhelper.dashboard.DashBoardActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener {

    TextInputEditText emailIdInput, passwordInput, mobNumberInput, rollNumberInput, rePasswordInput, nameInput;
    TextInputLayout emailLayout, passwordLayout, mobNumberLayout, rollNumberLayout, rePasswordLayout, nameLayout;
    ConstraintLayout rootLayout;
    CredentialChecker checker;
    Button signUpButton;
    FirebaseAuth mAuth;
    FirebaseDatabase database;
    DatabaseReference mRef;
    boolean matchPassFlag = false;


    void init() {
        emailIdInput = findViewById(R.id.userEmailSignUpInput);
        emailLayout = findViewById(R.id.userEmailSignUpLayout);
        passwordInput = findViewById(R.id.userPasswordSignUpInput);
        passwordLayout = findViewById(R.id.userPasswordSignUpLayout);
        mobNumberInput = findViewById(R.id.userMobileSignUpInput);
        mobNumberLayout = findViewById(R.id.userMobileSignUpLayout);
        rollNumberInput = findViewById(R.id.userRollSignUpInput);
        rollNumberLayout = findViewById(R.id.userRollSignUpLayout);
        rootLayout = findViewById(R.id.rootSignUpLayout);
        signUpButton = findViewById(R.id.signUpButtonSign);
        rePasswordInput = findViewById(R.id.userRePasswordSignUpInput);
        rePasswordLayout = findViewById(R.id.userRePasswordSignUpLayout);
        nameInput = findViewById(R.id.userNameSignUpInput);
        nameLayout = findViewById(R.id.userNameSignUpLayout);
        checker = new CredentialChecker();
        database = FirebaseDatabase.getInstance();
        mRef = database.getReference("Users").child("BasicData").child("Students");
        mAuth = FirebaseAuth.getInstance();

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        init();
        signUpButton.setOnClickListener(this);
        //tempFun();

        rePasswordInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String currPass = passwordInput.getText().toString().trim();
                if(charSequence.toString().equals(currPass)){
                    Log.e("Pass",charSequence.toString()+" "+currPass);
                    matchPassFlag = true;
                }else{
                    Log.e("PassIncorrect",charSequence.toString()+" "+currPass);
                    matchPassFlag = false;
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    private void tempFun() {
        final ArrayList<String> test = new ArrayList<>();
        test.add("Associate Professor-I");
        test.add("Professor");
        test.add("Associate Professor-II");
        test.add("Lecturer");
        final ArrayList<String> test2 = new ArrayList<>();
        test2.add("CSE");
        test2.add("Mechanical");
        test2.add("Chemical");
        test2.add("Electrical");
        for(int i=0;i<15;i++) {
            final int finalI = i;

            mAuth.createUserWithEmailAndPassword("test"+i+"_be18@thapar.edu", "Ashish1@").addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(!task.isSuccessful()){
                        Snackbar.make(rootLayout,"Email Already Registered",Snackbar.LENGTH_SHORT).show();
                        mAuth.signInWithEmailAndPassword("test"+finalI+"_be18@thapar.edu","Ashish1@").addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                            @Override
                            public void onSuccess(AuthResult authResult) {
                                addUserDetails("test"+finalI+"_be18@thapar.edu", "Test.SUser"+ finalI, "1234567890", "10181600"+finalI, mAuth.getCurrentUser().getUid(),"", "CS0"+(finalI%3+1));
                                mAuth.signOut();
                            }
                        });

                    } else {
                        //addUserDetails("test"+finalI+"_be18@thapar.edu", "Test.SUser"+ finalI, "1234567890", "10181600"+finalI, mAuth.getCurrentUser().getUid(),"", "CS0"+(finalI%3+1));
                    }
                }
            });
        }
    }

    void setError(TextInputLayout layout, boolean val, String error){
        layout.setErrorEnabled(val);
        layout.setError(error);
        layout.setFocusable(val);
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.signUpButtonSign:
                boolean flag = true;
                final String email = emailIdInput.getText().toString().trim();
                final String userName = nameInput.getText().toString().trim();
                final String mobNumber = mobNumberInput.getText().toString().trim();
                final String rollNumber = rollNumberInput.getText().toString().trim();
                String password = passwordInput.getText().toString().trim();
                if(email.isEmpty()) {
                    flag = false;
                    setError(emailLayout,true,"This field can't be empty.");
                } else {
                    setError(emailLayout,false,null);
                    int val = checker.validateEmail(email);
                    if(val !=-1) {
                        flag = false;
                        setError(emailLayout,true,checker.getError(val));
                    }
                }

                if(mobNumber.isEmpty()) {
                    flag = false;
                    setError(mobNumberLayout,true,"This field can't be empty.");
                } else {
                    setError(mobNumberLayout,false,null);
                    int val = checker.validateMobile(mobNumber);
                    if(val !=-1) {
                        flag = false;
                        setError(mobNumberLayout,true,checker.getError(val));
                    }
                }

                if(rollNumber.isEmpty()){
                    setError(rollNumberLayout,true,"This field can't be empty.");
                    flag = false;
                }else{
                    setError(rollNumberLayout,false,null);
                    int val = checker.validateRollNumber(rollNumber);
                    if(val !=-1) {
                        flag = false;
                        setError(rollNumberLayout,true,checker.getError(val));
                    }
                }

                if(password.isEmpty()){
                    setError(passwordLayout,true,"This field can't be empty.");
                    flag = false;
                }else{
                    setError(passwordLayout,false,null);
                    int val = checker.validatePassword(password);
                    if(val !=-1) {
                        flag = false;
                        setError(passwordLayout,true,checker.getError(val));
                    }
                }

                if(!matchPassFlag){
                    flag = false;
                    setError(rePasswordLayout,true,"Password didn't matched");
                }
                if(flag) {
                        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(!task.isSuccessful()){
                                    Snackbar.make(rootLayout,"Email Already Registered",Snackbar.LENGTH_SHORT).show();
                                } else {
                                    addUserDetails(email, userName, mobNumber, rollNumber, mAuth.getCurrentUser().getUid(),"","");
                                }
                            }
                        });

                }
        }
    }
    private void addUserDetails(String email, String userName, String mobNumber, String rollNumber,String uid,String link, String batch) {
        UserPersonalData data = new UserPersonalData(userName,email,mobNumber,rollNumber,uid,link,batch);
        updateUI(data);
        mRef.child(mAuth.getUid()).setValue(data);
    }
    private void addUserFacultyDetails(String email, String userName, String mobNumber, String dept,String uid,String link, String type) {
        UserFacultyModelClass data = new UserFacultyModelClass(userName,uid,type,dept,email,mobNumber,link);
        //updateUI(data);
        mRef.child(mAuth.getUid()).setValue(data);
    }

    private void updateUI(UserPersonalData data) {
        Intent intent = new Intent(this, DashBoardActivity.class);
        MySharedPref pref = new MySharedPref(this,"User-"+mAuth.getUid());
        pref.saveUser(data);
        startActivity(intent);
        finish();
    }
}