package com.example.dietycare;

import com.google.firebase.database.ServerValue;

import java.util.ArrayList;

public class Post {

    private String postKey;
    private String body_text;
    private String image_uri;
    private String userID;
    private Object timeStamp;
    private ArrayList<String> user_liked;
    private ArrayList<String> attached_comment;

    public Post(String body_text, String image_uri, String userID, String postKey) {
        this.body_text = body_text;
        this.image_uri = image_uri;
        this.userID = userID;
        this.postKey = postKey;
        this.timeStamp = ServerValue.TIMESTAMP;
        this.attached_comment = new ArrayList<String>();
        this.user_liked = new ArrayList<String>();

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

    public ArrayList<String> getUser_liked() {
        return user_liked;
    }

    public ArrayList<String> getAttached_comment() {
        return attached_comment;
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

    public void setUser_liked(ArrayList<String> user_liked) {
        this.user_liked = user_liked;
    }

    public void setAttached_comment(ArrayList<String> attached_comment) {
        this.attached_comment = attached_comment;
    }

    public void appendLikedUser(String UID){
        this.user_liked.add(UID);
    }

    public void appendComment(String comment){
        this.attached_comment.add(comment);
    }
}
