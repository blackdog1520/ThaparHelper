package com.blackdev.thaparhelper;

public class UserPersonalData {
    public UserPersonalData() {

    }
    UserPersonalData(String name, String email, String mobNumber, String rollNumber,String uid,String profileImageLink,String batch){
        this.email = email;
        this.name = name;
        this.mobNumber = mobNumber;
        this.rollNumber = rollNumber;
        this.profileImageLink = profileImageLink;
        this.uid = uid;
        this.batch = batch;
    }
    String name;
    String uid;

    public String getBatch() {
        return batch;
    }

    public void setBatch(String batch) {
        this.batch = batch;
    }

    String batch;

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    String email;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobNumber() {
        return mobNumber;
    }

    public void setMobNumber(String mobNumber) {
        this.mobNumber = mobNumber;
    }

    public String getRollNumber() {
        return rollNumber;
    }

    public void setRollNumber(String rollNumber) {
        this.rollNumber = rollNumber;
    }

    String mobNumber;
    String rollNumber;

    public String getProfileImageLink() {
        return profileImageLink;
    }

    public void setProfileImageLink(String profileImageLink) {
        this.profileImageLink = profileImageLink;
    }

    String profileImageLink;

}
