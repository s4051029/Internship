package com.mirrorchannelth.internship.model;

/**
 * Created by boss on 4/20/16.
 */
public class ShareData {
    private static UserProfile userProfile;

    public static UserProfile getUserProfile() {
        return userProfile;
    }

    public static void setUserProfile(UserProfile userProfile) {
        ShareData.userProfile = userProfile;
    }
}
