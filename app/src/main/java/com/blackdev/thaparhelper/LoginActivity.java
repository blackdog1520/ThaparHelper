package com.blackdev.thaparhelper;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.blackdev.thaparhelper.allutils.Constants;
import com.blackdev.thaparhelper.allutils.CredentialChecker;
import com.blackdev.thaparhelper.allutils.CustomButtonWithPD;
import com.blackdev.thaparhelper.allutils.MySharedPref;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private TextInputEditText emailInput,passwordInput;
    private TextInputLayout emailLayout,passwordLayout;
    private ConstraintLayout rootLayout;
    private FirebaseAuth mAuth;
    private final int studentRI = R.id.studentUserRB;
    private final int facultyRI = R.id.facultyUserRB;
    private final int adminRI = R.id.administrationUserRB;
    CustomButtonWithPD loginUser;
    RadioGroup radioGroup;
    String TAGLOGIN = "Login: ";
    private int userType;


    void init(){
        mAuth = FirebaseAuth.getInstance();
        rootLayout =  findViewById(R.id.rootLoginLayout);
        emailInput = findViewById(R.id.emailLogin);
        passwordInput = findViewById(R.id.passwordLogin);
        emailLayout = findViewById(R.id.emailLayoutLogin);
        passwordLayout = findViewById(R.id.passwordLayoutLogin);
        radioGroup = findViewById(R.id.userTypeLoginRG);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        init();
        loginUser = findViewById(R.id.loginUser);
        TextView forgotPassword = findViewById(R.id.forgotPasswordLogin);
        loginUser.setOnClickListener(this);
        forgotPassword.setOnClickListener(this);


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.loginUser:
                loginUser.showLoading();
                loginUser.setClickable(false);
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
                            loginUser.hideLoading();
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

    void updateUI(){
        Intent loginIntent = new Intent(LoginActivity.this, DashBoardActivity.class);
        MySharedPref mySharedPref = new MySharedPref(this,Utils.getStringPref(mAuth.getUid()),Constants.TYPE_SHARED_PREF);
        mySharedPref.saveUserType(userType);
        startActivity(loginIntent);
        finish();
    }

    void validateUser(String email, String pass) {
        switch (radioGroup.getCheckedRadioButtonId()) {
            case studentRI:
                userType = Constants.USER_STUDENT;
                break;
            case facultyRI:
                userType = Constants.USER_FACULTY;
                break;
            case adminRI:
                userType = Constants.USER_ADMINISTRATION;
                break;
        }

        if(!Utils.isNetworkAvailable(this)){
            Snackbar.make(rootLayout, "No Internet connection.", Snackbar.LENGTH_SHORT).show();
        } else {
            mAuth.signInWithEmailAndPassword(email, pass).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        Log.d(TAGLOGIN, "SignIn: Success");
                        FirebaseUser user = mAuth.getCurrentUser();
                        checkUserIsValid(userType,user);
                    } else {
                        Log.w(TAGLOGIN, "SignIn: Failure");
                        Snackbar.make(rootLayout, "Incorrect email or password.", Snackbar.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    private void checkUserIsValid(final int userType, final FirebaseUser user) {
        DatabaseReference mRef = Utils.getRefForBasicData(userType,user.getUid());
        mRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.hasChildren()) {
                    Log.i("INFO","UT:" +userType +" checked "+ radioGroup.getCheckedRadioButtonId() +  " snap "+snapshot.getChildren().toString());
                    updateFirebaseToken();
                    loginUser.hideLoading();
                    loginUser.setClickable(true);
                    updateUI();
                } else {
                    loginUser.hideLoading();
                    loginUser.setClickable(true);
                    mAuth.signOut();
                    Snackbar.make(rootLayout, "Incorrect User Type", Snackbar.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        
        
    }

    private void updateFirebaseToken() {
        int userType = Utils.getCurrentUserType(this,mAuth.getUid());
        final DatabaseReference databaseReference = Utils.getRefForBasicData(userType, mAuth.getUid());
        // choose path based on user type ;
        final Map<String, Object> map= new HashMap<>();
        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(new OnCompleteListener<String>() {
            @Override
            public void onComplete(@NonNull Task<String> task) {
                map.put("token",task.getResult());
                new MySharedPref(LoginActivity.this,Utils.getStringPref(mAuth.getUid()),Constants.TOKEN_SHARED_PREF).saveToken(task.getResult());
                databaseReference.updateChildren(map);
            }
        });

    }

}