package com.mirrorchannelth.internship.model;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by boss on 5/14/16.
 */
public class UserBean {
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
}
