package com.blackdev.thaparhelper;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blackdev.thaparhelper.allutils.Constants;
import com.blackdev.thaparhelper.allutils.CredentialChecker;
import com.blackdev.thaparhelper.allutils.MySharedPref;
import com.blackdev.thaparhelper.allutils.Utils;
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

import org.w3c.dom.Text;

import java.util.ArrayList;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener {

    int userType;
    TextInputEditText emailIdInput, passwordInput, mobNumberInput, rollNumberInput, rePasswordInput, nameInput, branchInput, batchInput, departmentInput, designationInput;
    TextInputLayout emailLayout, passwordLayout, mobNumberLayout, rollNumberLayout, rePasswordLayout, nameLayout, branchLayout, batchLayout, departmentLayout, designationLayout;
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
        branchInput = findViewById(R.id.userBranchSignUpInput);
        branchLayout = findViewById(R.id.userBranchSignUpLayout);
        batchInput = findViewById(R.id.userBatchSignUpInput);
        batchLayout = findViewById(R.id.userBatchSignUpLayout);
        departmentInput = findViewById(R.id.userDepartmentSignUpInput);
        departmentLayout = findViewById(R.id.userDepartmentSignUpLayout);
        designationInput = findViewById(R.id.userDesignationSignUpInput);
        designationLayout = findViewById(R.id.userDesignationSignUpLayout);
        checker = new CredentialChecker();
        database = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();


    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        userType = getIntent().getIntExtra("userType", 2);
        Log.i("Value from User Type activity " + userType, "");
        init();

        switch (userType) {
            case Constants.USER_STUDENT : {
                studentUserSignUp();
                Log.i("User Type ","Student");
            }
                break;
            case Constants.USER_FACULTY: {
                facultyUserSignUp();
                Log.i("User Type ","Faculty");
            }
                break;
            case Constants.USER_ADMINISTRATION : {
                facultyUserSignUp();
                Log.i("User Type ","Administration");
            }
                break;
        }

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



//    private void tempFun() {
//        final ArrayList<String> test = new ArrayList<>();
//        test.add("Associate Professor-I");
//        test.add("Professor");
//        test.add("Associate Professor-II");
//        test.add("Lecturer");
//        final ArrayList<String> test2 = new ArrayList<>();
//        test2.add("CSE");
//        test2.add("Mechanical");
//        test2.add("Chemical");
//        test2.add("Electrical");
//        for(int i=0;i<15;i++) {
//            final int finalI = i;
//
//            mAuth.createUserWithEmailAndPassword("test"+i+"_be18@thapar.edu", "Ashish1@").addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
//                @Override
//                public void onComplete(@NonNull Task<AuthResult> task) {
//                    if(!task.isSuccessful()){
//                        Snackbar.make(rootLayout,"Email Already Registered",Snackbar.LENGTH_SHORT).show();
//                        mAuth.signInWithEmailAndPassword("test"+finalI+"_be18@thapar.edu","Ashish1@").addOnSuccessListener(new OnSuccessListener<AuthResult>() {
//                            @Override
//                            public void onSuccess(AuthResult authResult) {
//                                 addUserDetails("test"+finalI+"_be18@thapar.edu", "Test.SUser"+ finalI, "1234567890", "10181600"+finalI, mAuth.getCurrentUser().getUid(),"", "CS0"+(finalI%3+1),"TEMP");
//                                mAuth.signOut();
//                            }
//                        });
//
//                    } else {
//                        //addUserDetails("test"+finalI+"_be18@thapar.edu", "Test.SUser"+ finalI, "1234567890", "10181600"+finalI, mAuth.getCurrentUser().getUid(),"", "CS0"+(finalI%3+1));
//                    }
//                }
//            });
//        }
//    }

    void setError(TextInputLayout layout, boolean val, String error){
        layout.setErrorEnabled(val);
        layout.setError(error);
        layout.setFocusable(val);
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.signUpButtonSign:

                switch (userType)
                {
                    case Constants.USER_STUDENT : checkStudentDetail();
                    break;
                    case Constants.USER_FACULTY : checkFacultyDetail();
                    break;
                    case  Constants.USER_ADMINISTRATION : checkFacultyDetail();
                    break;
                }


        }
    }
    private void addUserDetails(String email, String userName, String mobNumber, String rollNumber,String uid,String link, String batch,String branch) {
        UserPersonalData data = new UserPersonalData(userName,email,mobNumber,rollNumber,uid,link,batch,branch);
        updateUI(data);
        mRef = Utils.getRefForBasicData(userType,mAuth.getUid());
        mRef.setValue(data);
    }
    private void addUserFacultyDetails(String email, String userName, String mobNumber, String dept,String uid,String link, String designation) {
        UserFacultyModelClass data = new UserFacultyModelClass(userType, userName,uid,designation,dept,email,mobNumber,link);
        updateUI(data);
        mRef = Utils.getRefForBasicData(userType,mAuth.getUid());
        mRef.setValue(data);
    }

    private void updateUI(UserPersonalData data) {
        Intent intent = new Intent(this, DashBoardActivity.class);
        MySharedPref pref = new MySharedPref(this,Utils.getStringPref(mAuth.getUid()),Constants.DATA_SHARED_PREF);
        pref.saveUser(data);
        startActivity(intent);
        finish();
    }

    private void updateUI(UserFacultyModelClass data) {
        Intent intent = new Intent(this, DashBoardActivity.class);
        MySharedPref pref = new MySharedPref(this,Utils.getStringPref(mAuth.getUid()),Constants.DATA_SHARED_PREF);
        pref.saveUser(data);
        startActivity(intent);
        finish();
    }

    void studentUserSignUp()
    {
        departmentLayout.setVisibility(View.GONE);
        departmentInput.setVisibility(View.GONE);
        designationInput.setVisibility(View.GONE);
        designationLayout.setVisibility(View.GONE);
        Log.i("Inside Function","student USerSignUP");
    }

    void facultyUserSignUp ()
    {
        rollNumberInput.setVisibility(View.GONE);
        rollNumberLayout.setVisibility(View.GONE);
        branchLayout.setVisibility(View.GONE);
        branchInput.setVisibility(View.GONE);
        batchInput.setVisibility(View.GONE);
        batchLayout.setVisibility(View.GONE);
    }
//    void administrationUserSignUp ()
//    {
//        rollNumberInput.setVisibility(View.GONE);
//        rollNumberInput.setVisibility(View.GONE);
//        branchInput.setVisibility(View.GONE);
//        branchInput.setVisibility(View.GONE);
//        batchInput.setVisibility(View.GONE);
//        batchInput.setVisibility(View.GONE);
//    }

    void checkStudentDetail()
    {
        boolean flag = true;
        final String email = emailIdInput.getText().toString().trim();
        final String userName = nameInput.getText().toString().trim();
        final String mobNumber = mobNumberInput.getText().toString().trim();
        final String rollNumber = rollNumberInput.getText().toString().trim();
//        final String department = departmentInput.getText().toString().trim();
//        final String designation = designationInput.getText().toString().trim();
        final String branch = branchInput.getText().toString().trim();
        final String batch = batchInput.getText().toString().trim();
        String password = passwordInput.getText().toString().trim();

        if(userName.isEmpty()){
            flag = false;
            setError(nameLayout,true,"This field can't be empty.");
        }

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

        if(branch.isEmpty()){
            flag = false;
            setError(branchLayout,true,"This field can't be empty.");
        }

        if(batch.isEmpty()){
            flag = false;
            setError(batchLayout,true,"This field can't be empty.");
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
                        addUserDetails(email, userName, mobNumber, rollNumber, mAuth.getCurrentUser().getUid(),"",batch,branch);
                    }
                }
            });

        }
    }

    void checkFacultyDetail(){

        boolean flag = true;
        final String email = emailIdInput.getText().toString().trim();
        final String userName = nameInput.getText().toString().trim();
        final String mobNumber = mobNumberInput.getText().toString().trim();
//        final String rollNumber = rollNumberInput.getText().toString().trim();
        final String department = departmentInput.getText().toString().trim();
        final String designation = designationInput.getText().toString().trim();
//        final String branch = branchInput.getText().toString().trim();
//        final String batch = batchInput.getText().toString().trim();
        String password = passwordInput.getText().toString().trim();

        if(userName.isEmpty()){
            flag = false;
            setError(nameLayout,true,"This field can't be empty.");
        }

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

        if(department.isEmpty()){
            setError(departmentLayout,true,"This field can't be empty.");
            flag = false;
        }


        if(designation.isEmpty()){
            flag = false;
            setError(designationLayout,true,"This field can't be empty.");
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
                        addUserFacultyDetails(email, userName, mobNumber, department, mAuth.getCurrentUser().getUid(),"",designation);
                    }
                }
            });

        }

    }

}