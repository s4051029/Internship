package com.mirrorchannelth.internship.model;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by boss on 5/14/16.
 */
public class ActivityBean {
    private List<ActivityItem> activityList = new ArrayList<ActivityItem>();
    private int itemTotal;
    public ActivityBean(JSONObject jsonObject){
        itemTotal = jsonObject.optInt("item_total");
        JSONArray activity = jsonObject.optJSONArray("activity_list");
        addActivityList(activity);
    }

    private void addActivityList(JSONArray activity) {
        if(activity !=null) {
            for (int i = 0; i < activity.length(); i++) {
                JSONObject activityTmp = activity.optJSONObject(i);
                ActivityItem activityItem = new ActivityItem(activityTmp);
                if(!activityList.contains(activityItem)) {
                    activityList.add(activityItem);
                }
            }
        }
    }

    public void AddActivity(JSONObject jsonObject){
        itemTotal = jsonObject.optInt("item_total");
        JSONArray activity = jsonObject.optJSONArray("activity_list");
        addActivityList(activity);
    }

    public void insertActivity(JSONObject jsonObject){
        itemTotal = jsonObject.optInt("item_total");
        JSONArray activity = jsonObject.optJSONArray("activity_list");
        if(activity !=null) {
            for (int i = 0; i < activity.length(); i++) {
                JSONObject activityTmp = activity.optJSONObject(i);
                ActivityItem activityItem = new ActivityItem(activityTmp);
                if(!activityList.contains(activityItem)) {
                    activityList.add(i, activityItem);
                }
            }
        }
    }

    public ActivityItem getActivity(int position){
        return activityList.get(position);
    }

    public int getActivitySize(){
        return activityList.size();

    }

    public int getItemTotal(){

        return itemTotal;
    }
}
