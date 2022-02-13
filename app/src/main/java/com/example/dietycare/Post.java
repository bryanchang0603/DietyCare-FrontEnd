package com.example.dietycare;

import com.google.firebase.database.ServerValue;

public class Post {

    private String postKey;
    private String body_text;
    private String image_uri;
    private String userID;
    private Object timeStamp;

    public Post(String body_text, String image_uri, String userID, String postKey) {
        this.body_text = body_text;
        this.image_uri = image_uri;
        this.userID = userID;
        this.postKey = postKey;
        this.timeStamp = ServerValue.TIMESTAMP;
    }

    public String getPostKey() {
        return postKey;
    }

    public String getBody_text() {
        return body_text;
    }

    public String getImage_uri() {
        return image_uri;
    }

    public String getUserID() {
        return userID;
    }

    public Object getTimeStamp() {
        return timeStamp;
    }

    public void setPostKey(String postKey) {
        this.postKey = postKey;
    }

    public void setBody_text(String body_text) {
        this.body_text = body_text;
    }

    public void setImage_uri(String image_uri) {
        this.image_uri = image_uri;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public void setTimeStamp(Object timeStamp) {
        this.timeStamp = timeStamp;
    }
}
