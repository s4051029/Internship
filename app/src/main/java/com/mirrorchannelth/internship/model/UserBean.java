package com.mirrorchannelth.internship.model;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by boss on 5/14/16.
 */
public class UserBean implements Parcelable {
    private List<UserItem> userList = new ArrayList<UserItem>();
    public UserBean(JSONObject jsonObject){
        JSONArray activity = jsonObject.optJSONArray("result");
        if(activity !=null) {
            for (int i = 0; i < activity.length(); i++) {
                JSONObject userTmp = activity.optJSONObject(i);
                UserItem userItem = new UserItem(userTmp);
                if(!userList.contains(userItem)) {
                    userList.add(userItem);
                }
            }
        }
    }

    public void AddUserItem(JSONObject jsonObject){
        JSONArray activity = jsonObject.optJSONArray("result");
        if(activity !=null) {
            for (int i = 0; i < activity.length(); i++) {
                JSONObject userTmp = activity.optJSONObject(i);
                UserItem userItem = new UserItem(userTmp);
                userList.add(userItem);
            }
        }
    }

    public void insertUser(JSONObject jsonObject){
        JSONArray activity = jsonObject.optJSONArray("result");
        if(activity !=null) {
            for (int i = 0; i < activity.length(); i++) {
                JSONObject userTmp = activity.optJSONObject(i);
                UserItem userItem = new UserItem(userTmp);
                if(!userList.contains(userItem)) {
                    userList.add(i, userItem);
                }
            }
        }
    }

    public UserItem getUser(int position){
        return userList.get(position);
    }

    public int getUserListSize(){
        return userList.size();

    }

    protected UserBean(Parcel in) {
        if (in.readByte() == 0x01) {
            userList = new ArrayList<UserItem>();
            in.readList(userList, UserItem.class.getClassLoader());
        } else {
            userList = null;
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (userList == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(userList);
        }
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<UserBean> CREATOR = new Parcelable.Creator<UserBean>() {
        @Override
        public UserBean createFromParcel(Parcel in) {
            return new UserBean(in);
        }

        @Override
        public UserBean[] newArray(int size) {
            return new UserBean[size];
        }
    };
}