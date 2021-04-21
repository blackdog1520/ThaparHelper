package com.blackdev.thaparhelper.dashboard.Explore.Models;

public class ModelPost {
    String postId;
    String postImage;
    String postDesc;
    String postLocation;
    String postTime;
    String uid;
    String uEmail;
    String uDp;
    long likes;
    int uType;

    public int getuType() {
        return uType;
    }

    public void setuType(int uType) {
        this.uType = uType;
    }

    public ModelPost(String postId, String postImage, String postDesc, String postLocation, String postTime, String uid, String uEmail, String uDp, long likes, int uType, String uName) {
        this.postId = postId;
        this.postImage = postImage;
        this.postDesc = postDesc;
        this.postLocation = postLocation;
        this.postTime = postTime;
        this.uid = uid;
        this.uEmail = uEmail;
        this.uDp = uDp;
        this.likes = likes;
        this.uType = uType;
        this.uName = uName;
    }

    public ModelPost() {

    }


    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public String getPostImage() {
        return postImage;
    }

    public void setPostImage(String postImage) {
        this.postImage = postImage;
    }

    public String getPostDesc() {
        return postDesc;
    }

    public void setPostDesc(String postDesc) {
        this.postDesc = postDesc;
    }

    public String getPostLocation() {
        return postLocation;
    }

    public void setPostLocation(String postLocation) {
        this.postLocation = postLocation;
    }

    public String getPostTime() {
        return postTime;
    }

    public void setPostTime(String postTime) {
        this.postTime = postTime;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getuEmail() {
        return uEmail;
    }

    public void setuEmail(String uEmail) {
        this.uEmail = uEmail;
    }

    public String getuDp() {
        return uDp;
    }

    public void setuDp(String uDp) {
        this.uDp = uDp;
    }

    public String getuName() {
        return uName;
    }

    public void setuName(String uName) {
        this.uName = uName;
    }

    public long getLikes() {
        return likes;
    }

    public void setLikes(long likes) {
        this.likes = likes;
    }


    String uName;
}
