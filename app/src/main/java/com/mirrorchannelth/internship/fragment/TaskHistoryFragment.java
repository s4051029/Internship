package com.mirrorchannelth.internship.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mirrorchannelth.internship.R;
import com.mirrorchannelth.internship.adapter.ActivityRectclerViewAdapter;
import com.mirrorchannelth.internship.adapter.TaskHistoryRecyclerViewAdapter;
import com.mirrorchannelth.internship.model.ShareData;

/**
 * A simple {@link Fragment} subclass.
 */
public class TaskHistoryFragment extends Fragment implements View.OnClickListener{
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private RecyclerView.Adapter mAdapter;
    private TextView toolbartitle;
    private ImageView backButton;


    public TaskHistoryFragment() {
        // Required empty public constructor
    }
    public static TaskHistoryFragment newInstance(){
        TaskHistoryFragment taskHistory = new TaskHistoryFragment();
        Bundle bundle = new Bundle();
        taskHistory.setArguments(bundle);
        return taskHistory;

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_task_history, container, false);
        initInstance(view);
        return view;
    }

    private void initInstance(View rootview) {
        mRecyclerView = (RecyclerView) rootview.findViewById(R.id.task_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new TaskHistoryRecyclerViewAdapter();
        mRecyclerView.setAdapter(mAdapter);

        backButton = (ImageView) rootview.findViewById(R.id.leftMenu);

        backButton.setOnClickListener(this);
        backButton.setImageResource(R.drawable.ic_arrow_back_white_24dp);
        if(ShareData.getUserProfile().getUser_type().equals("E"))
            backButton.setVisibility(View.VISIBLE);
        else
            backButton.setVisibility(View.GONE);



        toolbartitle = (TextView) rootview.findViewById(R.id.toolbar_title);
        toolbartitle.setText(R.string.task_history_employee_toolbar_title);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.leftMenu:
                getActivity().getSupportFragmentManager()

                        .popBackStack("taskHistory", FragmentManager.POP_BACK_STACK_INCLUSIVE);
                break;
        }
    }
}
