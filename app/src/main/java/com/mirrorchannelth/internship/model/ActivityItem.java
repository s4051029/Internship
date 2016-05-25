package com.mirrorchannelth.internship.model;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by boss on 5/14/16.
 */
public class ActivityItem implements Parcelable {

    private String activityId;
    private String activityTitle;
    private String activityDescription;
    private String activityIcon;
    private Date activityDate;

    public ActivityItem(JSONObject jsonObject){
        activityId = jsonObject.optString("activity_id");
        activityTitle = jsonObject.optString("activity_title");
        activityDescription = jsonObject.optString("activity_description");
        activityIcon = jsonObject.optString("activity_icon");
        String date =jsonObject.optString("activity_date");
        SimpleDateFormat dfm = new SimpleDateFormat("yyyy-MM-dd");
        try {
            activityDate = dfm.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }

    public String getActivityId() {
        return activityId;
    }

    public void setActivityId(String activityId) {
        this.activityId = activityId;
    }

    public String getActivityTitle() {
        return activityTitle;
    }

    public void setActivityTitle(String activityTitle) {
        this.activityTitle = activityTitle;
    }

    public String getActivityDescription() {
        return activityDescription;
    }

    public void setActivityDescription(String activityDescription) {
        this.activityDescription = activityDescription;
    }

    public String getActivityIcon() {
        return activityIcon;
    }

    public void setActivityIcon(String activityIcon) {
        this.activityIcon = activityIcon;
    }

    public Date getActivityDate() {
        return activityDate;
    }

    public void setActivityDate(Date activityDate) {
        this.activityDate = activityDate;
    }

    @Override
    public boolean equals(Object o) {
        ActivityItem temp = (ActivityItem) o;
        if(temp.getActivityId().equalsIgnoreCase(activityId)){
            return true;
        } else {
            return false;
        }
    }

    protected ActivityItem(Parcel in) {
        activityId = in.readString();
        activityTitle = in.readString();
        activityDescription = in.readString();
        activityIcon = in.readString();
        long tmpActivityDate = in.readLong();
        activityDate = tmpActivityDate != -1 ? new Date(tmpActivityDate) : null;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(activityId);
        dest.writeString(activityTitle);
        dest.writeString(activityDescription);
        dest.writeString(activityIcon);
        dest.writeLong(activityDate != null ? activityDate.getTime() : -1L);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<ActivityItem> CREATOR = new Parcelable.Creator<ActivityItem>() {
        @Override
        public ActivityItem createFromParcel(Parcel in) {
            return new ActivityItem(in);
        }

        @Override
        public ActivityItem[] newArray(int size) {
            return new ActivityItem[size];
        }
    };
}