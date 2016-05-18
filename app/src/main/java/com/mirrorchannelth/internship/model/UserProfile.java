package com.mirrorchannelth.internship.model;

import org.json.JSONObject;

/**
 * Created by boss on 4/20/16.
 */
public class UserProfile {
    private String user_id;
    private String username;
    private String name;
    private String email;
    private String telephone;
    private String last_active;
    private String user_group;
    private String picture;
    private String token_key;
    private String user_type;

    public UserProfile(JSONObject profile){
        this.user_id = profile.optString("user_id");
        this.username = profile.optString("username");
        this.name = profile.optString("name");
        this.email = profile.optString("email");
        this.telephone = profile.optString("telephone");
        this.last_active = profile.optString("last_active");
        this.user_group = profile.optString("user_group");
        this.picture = profile.optString("picture");
        this.token_key = profile.optString("token_key");
        this.user_type = profile.optString("user_type");
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getLast_active() {
        return last_active;
    }

    public void setLast_active(String last_active) {
        this.last_active = last_active;
    }

    public String getUser_group() {
        return user_group;
    }

    public void setUser_group(String user_group) {
        this.user_group = user_group;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getToken_key() {
        return token_key;
    }

    public void setToken_key(String token_key) {
        this.token_key = token_key;
    }

    public String getUser_type() {
        return user_type;
    }

    public void setUser_type(String user_type) {
        this.user_type = user_type;
    }
}
