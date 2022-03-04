package com.example.dietycare;

import com.google.firebase.database.ServerValue;

public class Comment {

    private String comment_text;
    private String userID;
    private Object timeStamp;
    private String commentKey;

    public Comment(String comment_text, String userID, String commentKey) {
        this.comment_text = comment_text;
        this.userID = userID;
        this.commentKey = commentKey;
        this.timeStamp = ServerValue.TIMESTAMP;
    }

    public String getComment_text() {
        return comment_text;
    }

    public String getUserID() {
        return userID;
    }

    public Object getTimeStamp() {
        return timeStamp;
    }

    public String getCommentKey() {
        return commentKey;
    }

    public void setComment_text(String comment_text) {
        this.comment_text = comment_text;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public void setTimeStamp(Object timeStamp) {
        this.timeStamp = timeStamp;
    }

    public void setCommentKey(String commentKey) {
        this.commentKey = commentKey;
    }
}
