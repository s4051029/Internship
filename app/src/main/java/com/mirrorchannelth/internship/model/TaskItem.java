package com.mirrorchannelth.internship.model;

import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by boss on 5/14/16.
 */
public class TaskItem {

    private String taskId;
    private String taskTitle;
    private String taskDescription;
    private String taskHours;
    private int taskApproveMenu;
    private int taskApproveStatus;
    private Date taskDate;

    public TaskItem(JSONObject jsonObject){
        taskId = jsonObject.optString("task_id");
        taskTitle = jsonObject.optString("task_title");
        taskDescription = jsonObject.optString("task_description");
        taskHours = jsonObject.optString("task_hours");
        taskApproveMenu = jsonObject.optInt("task_approve_menu");
        taskApproveStatus = jsonObject.optInt("task_approve_status");
        String date =jsonObject.optString("task_date");
        SimpleDateFormat dfm = new SimpleDateFormat("yyyy-MM-dd");
        try {
            taskDate = dfm.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getTaskTitle() {
        return taskTitle;
    }

    public void setTaskTitle(String taskTitle) {
        this.taskTitle = taskTitle;
    }

    public String getTaskDescription() {
        return taskDescription;
    }

    public void setTaskDescription(String taskDescription) {
        this.taskDescription = taskDescription;
    }


    public Date getTaskDate() {
        return taskDate;
    }

    public void setTaskDate(Date activityDate) {
        this.taskDate = activityDate;
    }

    @Override
    public boolean equals(Object o) {
        TaskItem temp = (TaskItem) o;
        if(temp.getTaskId().equalsIgnoreCase(taskId)){
            return true;
        } else {
            return false;
        }
    }

    public String getTaskHours() {
        return taskHours;
    }

    public int getTaskApproveMenu() {
        return taskApproveMenu;
    }

    public int getTaskApproveStatus() {
        return taskApproveStatus;
    }
}
