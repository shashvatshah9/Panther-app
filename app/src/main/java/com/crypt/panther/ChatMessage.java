package com.crypt.panther;

import java.util.Date;

/**
 * Created by mynameislt on 3/31/2017.
 */

public class ChatMessage {

    private String messageText;
    private String messageUser;
    private long messageTime;
    private String userID;

    public ChatMessage(String messageText, String messageUser, String userID) {
        this.messageText = messageText;
        this.messageUser = messageUser;
        // Initialize to current time
        messageTime = new Date().getTime();
        this.userID  = userID;

    }

    public ChatMessage(){

    }

    public String getMessageText() {
        return messageText;
    }

    public void setMessageText(String messageText) {
        this.messageText = messageText;
    }

    public String getMessageUser() {
        return messageUser;
    }

    public void setMessageUser(String messageUser) {
        this.messageUser = messageUser;
    }

    public long getMessageTime() {
        return messageTime;
    }

    public void setMessageTime(long messageTime) {
        this.messageTime = messageTime;
    }

    public void setUserID(String userID){this.userID = userID;}

    public String getUserID(){return userID;}

}