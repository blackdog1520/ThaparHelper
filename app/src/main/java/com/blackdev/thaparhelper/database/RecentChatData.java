package com.blackdev.thaparhelper.database;


import java.io.Serializable;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "recentChat")
public class RecentChatData implements Serializable {

    @Ignore
    public RecentChatData() {

    }

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "Id")
    int id;

    @ColumnInfo(name = "chatType")
    int type;

    @ColumnInfo(name = "chatName")
    String name;

    @ColumnInfo(name = "imageLinke")
    String imageLink;

    @ColumnInfo(name = "NotificationKey")
    String nKey;

    @ColumnInfo(name = "GroupId")
    String groupId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageLink() {
        return imageLink;
    }

    public void setImageLink(String imageLink) {
        this.imageLink = imageLink;
    }

    public String getnKey() {
        return nKey;
    }

    public void setnKey(String nKey) {
        this.nKey = nKey;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public RecentChatData(int type, String name, String imageLink, String nKey, String groupId, String uid, int userType) {
        this.type = type;
        this.name = name;
        this.imageLink = imageLink;
        this.nKey = nKey;
        this.groupId = groupId;
        this.uid = uid;
        this.userType = userType;
    }

    public int getUserType() {
        return userType;
    }

    public void setUserType(int userType) {
        this.userType = userType;
    }

    @ColumnInfo(name = "Uid")
    String uid; // if chat type is one to one

    @ColumnInfo(name = "hisType")
    int userType;

}
