package com.blackdev.thaparhelper;

public class UserPersonalData {
    public UserPersonalData() {

    }



    public String getBranch() {
        return branch;
    }

    public UserPersonalData(String name, String uid, String batch, String email, String mobNumber, String rollNumber, String profileImageLink, String branch) {
        this.name = name;
        this.uid = uid;
        this.batch = batch;
        this.email = email;
        this.mobNumber = mobNumber;
        this.rollNumber = rollNumber;
        this.profileImageLink = profileImageLink;
        this.branch = branch;
    }

    String name;
    String uid;
    String token;
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

    public void setBranch(String branch) { this.branch = branch;}

    public String getBranch(String branch) { return branch;}

    String branch;

}
