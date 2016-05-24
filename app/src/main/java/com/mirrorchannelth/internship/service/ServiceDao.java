package com.mirrorchannelth.internship.service;

import com.mirrorchannelth.internship.model.Image;
import com.mirrorchannelth.internship.model.UserProfile;
import com.mirrorchannelth.internship.net.Connection;

import java.util.List;

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
    public void addActivity(UserProfile userProfile, String activityTitle, String activityDate, String activityDescription, List<Image> activityFile, Connection.OnConnectionCallBackListener listener){
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
        if(activityFile != null) {
            for (int i = 0; i < activityFile.size(); i++) {
                connection.addPostData("activity_file", activityFile.get(i).getUrl());
            }
        }
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

    public void getTaskList(UserProfile userProfile, String pageId, String taskUserId, String startDate, String endDate, Connection.OnConnectionCallBackListener listener){
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
        connection.addPostData("date_start", startDate);
        connection.addPostData("date_end", endDate);
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

    public void addTask(UserProfile userProfile, String taskTitle, String taskDate, String taskDescription, String hours,List<Image> taskFile, Connection.OnConnectionCallBackListener listener){
        connection = new Connection(endpoint);

        connection.setDelayed(500);
        connection.addPostData("function", "taskSave");
        connection.addPostData("android_id", "1");
        connection.addPostData("mac_id", "1");
        connection.addPostData("device_type", "A:android");
        connection.addPostData("token_key", userProfile.getToken_key());
        connection.addPostData("user_id", userProfile.getUser_id());
        connection.addPostData("user_group", userProfile.getUser_group());
        connection.addPostData("user_type", userProfile.getUser_type());
        connection.addPostData("task_title", taskTitle);
        connection.addFileData("task_hours", hours);
        connection.addPostData("task_date", taskDate);
        connection.addPostData("task_description", taskDescription);
        if(taskFile != null) {
            for (int i = 0; i < taskFile.size(); i++) {
                connection.addPostData("task_picture", taskFile.get(i).getUrl());
            }
        }
        connection.setOnConnectionCallBackListener(listener);
        connection.execute();

    }


}
