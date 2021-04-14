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

    public int getUserType() {
        return userType;
    }

    public void setUserType(int userType) {
        this.userType = userType;
    }

    public UserFacultyModelClass(int userType, String name, String uid, String designation, String department, String email, String mobNumber, String profileImageLink) {
        this.userType = userType;
        this.name = name;
        this.uid = uid;
        this.designation = designation;
        this.department = department;
        this.email = email;
        this.mobNumber = mobNumber;
        this.profileImageLink = profileImageLink;
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

