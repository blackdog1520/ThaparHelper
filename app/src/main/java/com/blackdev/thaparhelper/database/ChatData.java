package com.blackdev.thaparhelper.database;

import java.io.Serializable;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "chatsTable")
public class ChatData implements Serializable {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "ID")
    private long ID;

    @ColumnInfo(name = "mFROMUID")
    private String FromUID; // if message is recieved from someone

    @ColumnInfo(name = "mSENTBYME")
    private boolean SentByMe;

    @ColumnInfo(name = "mTOUID")
    private String ToUID;     //to whom we are sending the message

    @ColumnInfo(name = "mMESSAGE")
    private String Message;

    @ColumnInfo(name = "MediaFileType")
    private boolean isMediaFile;

    public boolean isMediaFile() {
        return isMediaFile;
    }

    public void setMediaFile(boolean mediaFile) {
        isMediaFile = mediaFile;
    }

    public long getID() {
        return ID;
    }

    public void setID(long ID) {
        this.ID = ID;
    }

    public String getFromUID() {
        return FromUID;
    }

    public void setFromUID(String mFromUID) {
        this.FromUID = mFromUID;
    }

    public boolean isSentByMe() {
        return SentByMe;
    }

    public void setSentByMe(boolean sentByMe) {
        SentByMe = sentByMe;
    }

    public String getToUID() {
        return ToUID;
    }

    public void setToUID(String mToUID) {
        this.ToUID = mToUID;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String mMessage) {
        this.Message = mMessage;
    }
}
