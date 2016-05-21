package com.mirrorchannelth.internship.model;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by boss on 5/14/16.
 */
public class TaskBean {
    private List<TaskItem> taskList = new ArrayList<TaskItem>();
    private int itemTotal;
    private String totalHours;
    private String taskUserId;
    public TaskBean(JSONObject jsonObject){
        itemTotal = jsonObject.optInt("item_total");
        taskUserId = jsonObject.optString("task_user_id");
        totalHours = jsonObject.optString("task_total_hours");

        JSONArray activity = jsonObject.optJSONArray("task_list");
        if(activity !=null) {
            for (int i = 0; i < activity.length(); i++) {
                JSONObject activityTmp = activity.optJSONObject(i);
                TaskItem taskItem = new TaskItem(activityTmp);
                if(!taskList.contains(taskItem)) {
                    taskList.add(taskItem);
                }
            }
        }
    }

    public void AddTask(JSONObject jsonObject){
        itemTotal = jsonObject.optInt("item_total");
        JSONArray activity = jsonObject.optJSONArray("task_list");
        if(activity !=null) {
            for (int i = 0; i < activity.length(); i++) {
                JSONObject activityTmp = activity.optJSONObject(i);
                TaskItem taskItem = new TaskItem(activityTmp);
                taskList.add(taskItem);
            }
        }
    }

    public void AddTaskFromFront(JSONObject jsonObject){
        itemTotal = jsonObject.optInt("item_total");
        JSONArray activity = jsonObject.optJSONArray("activity_list");
        if(activity !=null) {
            for (int i = 0; i < activity.length(); i++) {
                JSONObject activityTmp = activity.optJSONObject(i);
                TaskItem taskItem = new TaskItem(activityTmp);
                if(!taskList.contains(taskItem)) {
                    taskList.add(i, taskItem);
                }
            }
        }
    }

    public TaskItem getActivity(int position){
        return taskList.get(position);
    }

    public int getTaskSize(){
        return taskList.size();

    }

    public int getItemTotal(){
        return itemTotal;
    }
    public double getTotalHours(){
        double total = 0;
        for (int i = 0; i < taskList.size(); i++) {
            total += Double.parseDouble(taskList.get(i).getTaskHours());
        }
        return total;
    }
}
