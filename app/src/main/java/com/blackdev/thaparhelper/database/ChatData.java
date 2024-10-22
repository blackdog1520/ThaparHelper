package com.blackdev.thaparhelper.database;

import java.io.Serializable;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "chatsTable", primaryKeys = {"mFROMUID","mTOUID","TimeStamp"})
public class ChatData implements Serializable {
    public ChatData() {
    }

    @ColumnInfo(name = "ID")
    private long ID;

    @NonNull
    @ColumnInfo(name = "mFROMUID")
    private String FromUID; // if message is recieved from someone

    @ColumnInfo(name = "mSENTBYME")
    private boolean SentByMe;

    @NonNull
    @ColumnInfo(name = "mTOUID")
    private String ToUID;     //to whom we are sending the message

    @ColumnInfo(name = "mMESSAGE")
    private String Message;

    @ColumnInfo(name = "MediaFileType")
    private boolean isMediaFile;

    @ColumnInfo(name = "MediaUrl")
    private String MediaUrl;

    @ColumnInfo(name = "mDPUrl")
    private String DpUrl;

    @ColumnInfo(name = "mTouName")
    private String ToUName;

    @ColumnInfo(name = "hisUserType")
    private int ToUType;

    public String getMediaUrl() {
        return MediaUrl;
    }

    public void setMediaUrl(String mediaUrl) {
        MediaUrl = mediaUrl;
    }

    @ColumnInfo(name = "Seen")
    private boolean isSeen;

    public String getDpUrl() {
        return DpUrl;
    }

    public void setDpUrl(String dpUrl) {
        DpUrl = dpUrl;
    }

    public String getToUName() {
        return ToUName;
    }

    public void setToUName(String toUName) {
        ToUName = toUName;
    }

    public int getToUType() {
        return ToUType;
    }

    public void setToUType(int toUType) {
        ToUType = toUType;
    }

    public ChatData(@NonNull String fromUID, boolean sentByMe, @NonNull String toUID, String message, boolean isMediaFile, String mediaUrl, String dpUrl, String toUName, int toUType, boolean isSeen, @NonNull String timeStamp) {
        FromUID = fromUID;
        SentByMe = sentByMe;
        ToUID = toUID;
        Message = message;
        this.isMediaFile = isMediaFile;
        MediaUrl = mediaUrl;
        DpUrl = dpUrl;
        ToUName = toUName;
        ToUType = toUType;
        this.isSeen = isSeen;
        TimeStamp = timeStamp;
    }

    public boolean isSeen() {
        return isSeen;
    }

    public void setSeen(boolean seen) {
        isSeen = seen;
    }

    public String getTimeStamp() {
        return TimeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        TimeStamp = timeStamp;
    }

    @NonNull
    @ColumnInfo(name = "TimeStamp")
    private String TimeStamp;

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
