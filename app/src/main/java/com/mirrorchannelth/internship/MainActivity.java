package com.mirrorchannelth.internship;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;


import com.mirrorchannelth.internship.fragment.ActivityHistoryFragment;
import com.mirrorchannelth.internship.fragment.AddActivityFragment;
import com.mirrorchannelth.internship.fragment.NewsFragment;
import com.mirrorchannelth.internship.fragment.StudentMapFragment;
import com.mirrorchannelth.internship.fragment.TaskFragment;
import com.mirrorchannelth.internship.fragment.TestFragment;
import com.mirrorchannelth.internship.fragment.UserFragment;
import com.mirrorchannelth.internship.model.ShareData;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.BottomBarFragment;

public class MainActivity extends AppCompatActivity {
    private BottomBar bottomBar = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bottomBar = BottomBar.attach(this, savedInstanceState);
        bottomBar.noTopOffset();
        initWidget();


    }

    public void initWidget() {

        if(ShareData.getUserProfile().getUser_type().equals("E")){

            bottomBar.setFragmentItems(getSupportFragmentManager(), R.id.fragmentContainer,
                    new BottomBarFragment(NewsFragment.newInstance(), R.drawable.ic_motorcycle_white_24dp, R.string.main_menu_news),
                    new BottomBarFragment(UserFragment.newInstance(), R.drawable.ic_assignment_black_18dp, R.string.user_toolbar_title),
                    new BottomBarFragment(new TestFragment(), R.drawable.ic_motorcycle_white_24dp, R.string.main_menu_map),
                    new BottomBarFragment(new TestFragment(), R.drawable.ic_motorcycle_white_24dp, R.string.main_menu_map),
                    new BottomBarFragment(new StudentMapFragment(), R.drawable.ic_motorcycle_white_24dp, R.string.main_menu_profile)

            );

        } else {
            bottomBar.setFragmentItems(getSupportFragmentManager(), R.id.fragmentContainer,
                    new BottomBarFragment(NewsFragment.newInstance(), R.drawable.ic_assignment_black_18dp, R.string.main_menu_news),
                    new BottomBarFragment(TaskFragment.newInstance(), R.drawable.ic_assignment_black_18dp, R.string.main_menu_task),
                    new BottomBarFragment(AddActivityFragment.newInstance(), R.drawable.ic_assignment_black_18dp, R.string.main_menu_activity),
                    new BottomBarFragment(new TestFragment(), R.drawable.ic_assignment_black_18dp, R.string.main_menu_map),
                    new BottomBarFragment(new StudentMapFragment(), R.drawable.ic_assignment_black_18dp, R.string.main_menu_profile)

            );

        }
        // Setting colors for different tabs when there's more than three of them.


        bottomBar.mapColorForTab(0, "#F44336");
        bottomBar.mapColorForTab(1, "#F44336");
        bottomBar.mapColorForTab(2, "#F44336");
        bottomBar.mapColorForTab(3, "#F44336");
        bottomBar.mapColorForTab(4, "#F44336");

    }


}
