package com.blackdev.thaparhelper;

public class UserFacultyModelClass {
    public UserFacultyModelClass() {
    }

    String name;
    String uid;

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String type) {
        this.designation = type;
    }

    String designation;
    int userType;

    String bio;

    public String getBio() {
        return bio;
    }

    public UserFacultyModelClass(String name, String uid, String designation, int userType, String bio, String department, String email, String mobNumber, String profileImageLink) {
        this.name = name;
        this.uid = uid;
        this.designation = designation;
        this.userType = userType;
        this.bio = bio;
        this.department = department;
        this.email = email;
        this.mobNumber = mobNumber;
        this.profileImageLink = profileImageLink;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public int getUserType() {
        return userType;
    }

    public void setUserType(int userType) {
        this.userType = userType;
    }



    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    String department;

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

    String mobNumber;

    public String getProfileImageLink() {
        return profileImageLink;
    }

    public void setProfileImageLink(String profileImageLink) {
        this.profileImageLink = profileImageLink;
    }

    String profileImageLink;

}

