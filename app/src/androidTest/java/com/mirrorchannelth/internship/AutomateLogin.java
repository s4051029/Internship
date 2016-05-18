package com.mirrorchannelth.internship;

import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

/**
 * Created by boss on 5/14/16.
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class AutomateLogin {
    @Rule
    public ActivityTestRule<LoginActivity> mActivityRule = new ActivityTestRule(LoginActivity.class);


    @Test
    public void automateLogin() {
        onView(withId(R.id.editUsername)).perform(typeText("student"));
        onView(withId(R.id.editPassword)).perform(typeText("1234"));
        onView(withId(R.id.btn_login)).perform(click());
        try {
            Thread.sleep(0);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


}
