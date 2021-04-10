package com.blackdev.thaparhelper.dashboard.Chat.Models;

public class ModelChatOneToOne {
    String receiver,sender,timestamp,message;
    Boolean isSeen;

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public Boolean getSeen() {
        return isSeen;
    }

    public void setSeen(Boolean seen) {
        isSeen = seen;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ModelChatOneToOne(String receiver, String sender, String timestamp, String message, Boolean isSeen) {
        this.receiver = receiver;
        this.sender = sender;
        this.timestamp = timestamp;
        this.message = message;
        this.isSeen = isSeen;
    }
}
