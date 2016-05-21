package com.mirrorchannelth.internship.service;

import com.mirrorchannelth.internship.model.UserProfile;
import com.mirrorchannelth.internship.net.Connection;

/**
 * Created by boss on 5/14/16.
 */
public class ServiceDao {

    private Connection connection;
    private String endpoint;
    public ServiceDao(String endpoint){
        this.endpoint = endpoint;

    }

    public void requestNews(String pageId, Connection.OnConnectionCallBackListener listener, UserProfile userProfile){
        connection = new Connection(endpoint);

        connection.setDelayed(500);
        connection.addPostData("function", "newsList");
        connection.addPostData("android_id", "1");
        connection.addPostData("mac_id", "1");
        connection.addPostData("device_type", "A:android");
        connection.addPostData("token_key", userProfile.getToken_key());
        connection.addPostData("user_id", userProfile.getUser_id());
        connection.addPostData("user_group", userProfile.getUser_group());
        connection.addPostData("user_type", userProfile.getUser_type());
        connection.addPostData("page_id", pageId);
        connection.setOnConnectionCallBackListener(listener);
        connection.execute();

    }
    public void addActivity(UserProfile userProfile, String activityTitle, String activityDate, String activityDescription, String activityFile, Connection.OnConnectionCallBackListener listener){
        connection = new Connection(endpoint);

        connection.setDelayed(500);
        connection.addPostData("function", "activitySave");
        connection.addPostData("android_id", "1");
        connection.addPostData("mac_id", "1");
        connection.addPostData("device_type", "A:android");
        connection.addPostData("token_key", userProfile.getToken_key());
        connection.addPostData("user_id", userProfile.getUser_id());
        connection.addPostData("user_group", userProfile.getUser_group());
        connection.addPostData("user_type", userProfile.getUser_type());
        connection.addPostData("activity_title", activityTitle);
        connection.addPostData("activity_date", activityDate);
        connection.addPostData("activity_description", activityDescription);
        connection.addPostData("activity_file", activityFile);
        connection.setOnConnectionCallBackListener(listener);
        connection.execute();

    }

    public void getActivityList(UserProfile userProfile, String pageId, Connection.OnConnectionCallBackListener listener){
        connection = new Connection(endpoint);

        connection.setDelayed(500);
        connection.addPostData("function", "activityList");
        connection.addPostData("android_id", "1");
        connection.addPostData("mac_id", "1");
        connection.addPostData("device_type", "A:android");
        connection.addPostData("token_key", userProfile.getToken_key());
        connection.addPostData("user_id", userProfile.getUser_id());
        connection.addPostData("user_group", userProfile.getUser_group());
        connection.addPostData("user_type", userProfile.getUser_type());
        connection.addPostData("page_id", pageId);
        connection.setOnConnectionCallBackListener(listener);
        connection.execute();

    }

    public void getTaskList(UserProfile userProfile, String pageId, String taskUserId, Connection.OnConnectionCallBackListener listener){
        connection = new Connection(endpoint);

        connection.setDelayed(500);
        connection.addPostData("function", "taskList");
        connection.addPostData("android_id", "1");
        connection.addPostData("mac_id", "1");
        connection.addPostData("device_type", "A:android");
        connection.addPostData("token_key", userProfile.getToken_key());
        connection.addPostData("user_id", userProfile.getUser_id());
        connection.addPostData("user_group", userProfile.getUser_group());
        connection.addPostData("user_type", userProfile.getUser_type());
        connection.addPostData("page_id", pageId);
        connection.addPostData("task_user_id", taskUserId);
        connection.setOnConnectionCallBackListener(listener);
        connection.execute();

    }

    public void getUserList(UserProfile userProfile, Connection.OnConnectionCallBackListener listener){
        connection = new Connection(endpoint);

        connection.setDelayed(500);
        connection.addPostData("function", "taskUserList");
        connection.addPostData("android_id", "1");
        connection.addPostData("mac_id", "1");
        connection.addPostData("device_type", "A:android");
        connection.addPostData("token_key", userProfile.getToken_key());
        connection.addPostData("user_id", userProfile.getUser_id());
        connection.addPostData("user_group", userProfile.getUser_group());
        connection.addPostData("user_type", userProfile.getUser_type());

        connection.setOnConnectionCallBackListener(listener);
        connection.execute();

    }


}
