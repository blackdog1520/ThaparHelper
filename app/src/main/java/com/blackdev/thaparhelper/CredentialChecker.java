package com.blackdev.thaparhelper;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import androidx.annotation.NonNull;

public class CredentialChecker {
    ArrayList<String> errorCodeToString = new ArrayList<>();
    CredentialChecker(){
        errorCodeToString.add("Invalid Email Id");
        errorCodeToString.add("Login with Thapar Id");
        errorCodeToString.add("Password length must be 6.");
        errorCodeToString.add("Password must contain one Upper, lower, number and Special Character.");
        errorCodeToString.add("Password length must be less than or equal to 20.");
        errorCodeToString.add("Incorrect mobile number.");
        errorCodeToString.add("Must Contain numerical values only.");
        errorCodeToString.add("Invalid Roll Number");
    }
    /*  0-> Invalid email Id
    1-> Login with Thapar Id
    2-> Password length must be 8
    3-> Password must contain one Upper, lower, number and Special Character
    4-> Password length must be less than or equal to 20
    5-> Incorrect mobile number.
    6-> Must Contain numerical values only.
 */

    String getError(int errorCode){
        return errorCodeToString.get(errorCode);
    }
    int validateEmail(String email) {
        Pattern pattern = Pattern.compile(".*thapar.edu*$");
        Matcher matcher = pattern.matcher(email);
        if (!matcher.matches()) {
            return 1;
        } else {
            String regex = "^[_A-Za-z0-9.]+@.*thapar.edu*$";
            pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
            matcher = pattern.matcher(email);
            if (!matcher.matches()) {
                return 0;
            } else {
                return -1;
            }
        }
    }

    int validatePassword(String password){
        if(password.length()<6) {
            return 2;
        } else if(password.length()>20) {
            return 4;
        } else {
            String regex = "^(?=.*[0-9])(?=.*[A-Z])(?=.*[a-z])(?=.*[@#$%^&*()-=!]).{6,20}$";
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(password);
            if(matcher.matches()){
                return -1;
            }else{
                return 3;
            }
        }
    }

    int validateMobile(String mobNumber){
        if(mobNumber.charAt(0) == '0'){
            mobNumber = mobNumber.substring(1);
        }
        if(mobNumber.length()!=10){
            return 5;
        }else{
            String regex = "^(?=.*[0-9]).{10}$";
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(mobNumber);
            if(matcher.matches()){
                return -1;
            }else{
                return 6;
            }
        }
    }

    int validateRollNumber(String rollNumber){
        if(rollNumber.length() == 9)
            return -1;
        else
            return 7;
    }

    boolean returnVal = false;
    private void checkEmail(String email, DatabaseReference mRef) {
        Query query = mRef.orderByChild("email").equalTo(email);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    returnVal = false;
                }else{
                    returnVal = true;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}
