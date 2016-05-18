package com.mirrorchannelth.internship.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mirrorchannelth.internship.R;
import com.mirrorchannelth.internship.adapter.UserRecyclerViewAdapter;
import com.mirrorchannelth.internship.listener.RecyclerViewItemClickListener;

/**
 * A simple {@link Fragment} subclass.
 */
public class UserFragmentView extends Fragment implements RecyclerViewItemClickListener {

    private TextView toolbarTitleTextview;
    private RecyclerView userRecycleView;
    private UserRecyclerViewAdapter adapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ImageView backButton;
    public UserFragmentView() {
        // Required empty public constructor
    }

    public static UserFragmentView newInstance(){
        UserFragmentView userFragment = new UserFragmentView();
        return userFragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootview = inflater.inflate(R.layout.fragment_user, container, false);

        initInstance(rootview);

        return rootview;
    }

    private void initInstance(View rootview) {
        toolbarTitleTextview = (TextView) rootview.findViewById(R.id.toolbar_title);
        userRecycleView = (RecyclerView) rootview.findViewById(R.id.userRecycleview);

        adapter = new UserRecyclerViewAdapter(this);
        mLayoutManager = new LinearLayoutManager(getContext());

        toolbarTitleTextview.setText(R.string.user_toolbar_title);

        userRecycleView.setAdapter(adapter);
        userRecycleView.setLayoutManager(mLayoutManager);

    }




    @Override
    public void onItemClickListener(RecyclerView.ViewHolder caller, View v) {
        getActivity().getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(R.anim.from_right, R.anim.to_left, R.anim.from_left, R.anim.to_right)
                .replace(R.id.fragmentContainer, TaskHistoryFragment.newInstance())
                .addToBackStack("taskHistory")
                .commit();
    }

    @Override
    public void onClick(View v) {

    }
}
