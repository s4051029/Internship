package com.mirrorchannelth.internship.model;

import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by boss on 5/14/16.
 */
public class UserItem {

    private String userId;
    private String name;
    private String picture;
    private String title;


    public UserItem(JSONObject jsonObject){
        userId = jsonObject.optString("user_id");
        name = jsonObject.optString("name");
        picture = jsonObject.optString("picture");
        title = jsonObject.optString("title");

    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public boolean equals(Object o) {
        UserItem temp = (UserItem) o;
        if(temp.getUserId().equalsIgnoreCase(userId)){
            return true;
        } else {
            return false;
        }
    }

}
