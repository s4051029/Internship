package com.mirrorchannelth.internship.model;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by boss on 5/14/16.
 */
public class UserItem implements Parcelable {

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


    protected UserItem(Parcel in) {
        userId = in.readString();
        name = in.readString();
        picture = in.readString();
        title = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(userId);
        dest.writeString(name);
        dest.writeString(picture);
        dest.writeString(title);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<UserItem> CREATOR = new Parcelable.Creator<UserItem>() {
        @Override
        public UserItem createFromParcel(Parcel in) {
            return new UserItem(in);
        }

        @Override
        public UserItem[] newArray(int size) {
            return new UserItem[size];
        }
    };
}
