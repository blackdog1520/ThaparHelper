package com.blackdev.thaparhelper.dashboard.Chat.Models;

public class ModelGroupChat {
    String senderName;
    String senderUid;
    String message;
    String TimeStamp;
    int messageType;
    int seenCount;

    public ModelGroupChat() {
    }

    public int getSeenCount() {
        return seenCount;
    }

    public void setSeenCount(int seenCount) {
        this.seenCount = seenCount;
    }

    public ModelGroupChat(String senderName, String senderUid, String message, String timeStamp, int messageType, int seenCount) {
        this.senderName = senderName;
        this.senderUid = senderUid;
        this.message = message;
        TimeStamp = timeStamp;
        this.messageType = messageType;
        this.seenCount = seenCount;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public String getSenderUid() {
        return senderUid;
    }

    public void setSenderUid(String senderUid) {
        this.senderUid = senderUid;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTimeStamp() {
        return TimeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        TimeStamp = timeStamp;
    }

    public int getMessageType() {
        return messageType;
    }

    public void setMessageType(int messageType) {
        this.messageType = messageType;
    }
}
