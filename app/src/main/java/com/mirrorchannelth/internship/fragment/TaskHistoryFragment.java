package com.mirrorchannelth.internship.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationSet;
import android.widget.ImageView;

import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aspsine.irecyclerview.IRecyclerView;
import com.aspsine.irecyclerview.OnLoadMoreListener;
import com.aspsine.irecyclerview.OnRefreshListener;
import com.mirrorchannelth.internship.R;
import com.mirrorchannelth.internship.adapter.TaskHistoryRecyclerViewAdapter;
import com.mirrorchannelth.internship.config.WebAPI;
import com.mirrorchannelth.internship.listener.RecyclerViewItemClickListener;
import com.mirrorchannelth.internship.model.ShareData;
import com.mirrorchannelth.internship.model.TaskBean;
import com.mirrorchannelth.internship.net.Connection;
import com.mirrorchannelth.internship.service.ServiceDao;
import com.mirrorchannelth.internship.util.AnimationUtil;
import com.mirrorchannelth.internship.util.WindowsUtil;
import com.mirrorchannelth.internship.view.DefaultDisplayView;
import com.mirrorchannelth.internship.view.RefreshView;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * A simple {@link Fragment} subclass.
 */
public class TaskHistoryFragment extends Fragment implements View.OnClickListener, OnLoadMoreListener, OnRefreshListener, Connection.OnConnectionCallBackListener, RecyclerViewItemClickListener {
    private IRecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private RecyclerView.Adapter mAdapter;
    private TextView toolbartitle;
    private ImageView backButton;
    private boolean isLoadmore = false;
    private boolean isRefresh = false;
    private RefreshView header ;
    private RefreshView footer;
    private ProgressBar progressBar;
    private CoordinatorLayout coordinatorLayout;
    private DefaultDisplayView defaultDisplayview;
    private String pageId = "1";
    private String taskUserId;
    private ServiceDao serviceDao;
    private TaskBean taskBean;

    public TaskHistoryFragment() {
        // Required empty public constructor
    }
    public static TaskHistoryFragment newInstance(String userId){
        TaskHistoryFragment taskHistory = new TaskHistoryFragment();
        Bundle bundle = new Bundle();
        bundle.putString("userId", userId);
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

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Bundle bundle = this.getArguments();
        if(bundle != null) {
            taskUserId = bundle.getString("userId");
        }
        if(taskBean != null) {
            mAdapter = new TaskHistoryRecyclerViewAdapter(getActivity(), taskBean, this);
            mRecyclerView.setIAdapter(mAdapter);
        } else {
            progressBar.setVisibility(View.VISIBLE);
            getTaskList(pageId, taskUserId);
        }
    }

    private void getTaskList(String pageId, String task_user_id) {
        serviceDao.getTaskList(ShareData.getUserProfile(), pageId, task_user_id, this);
    }

    private void initInstance(View rootview) {
        serviceDao = new ServiceDao(WebAPI.URL);
        mRecyclerView = (IRecyclerView) rootview.findViewById(R.id.task_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        backButton = (ImageView) rootview.findViewById(R.id.leftMenu);
        progressBar = (ProgressBar) rootview.findViewById(R.id.taskProgressBar);
        coordinatorLayout = (CoordinatorLayout) rootview.findViewById(R.id.coordinatorLayout);
        header = new RefreshView(getActivity());
        footer = new RefreshView(getActivity());
        mRecyclerView.setRefreshHeaderView(header);
        mRecyclerView.setLoadMoreFooterView(footer);
        mRecyclerView.setRefreshEnabled(true);
        mRecyclerView.setLoadMoreEnabled(true);
        mRecyclerView.setOnRefreshListener(this);
        mRecyclerView.setOnLoadMoreListener(this);
        mRecyclerView.setRefreshing(false);
        defaultDisplayview = new DefaultDisplayView(getActivity());
        toolbartitle = (TextView) rootview.findViewById(R.id.toolbar_title);
        backButton.setOnClickListener(this);
        backButton.setImageResource(R.drawable.ic_arrow_back_white_24dp);
        if(ShareData.getUserProfile().getUser_type().equals("E")) {
            backButton.setVisibility(View.VISIBLE);
            toolbartitle.setText(R.string.task_history_employee_toolbar_title);
        }
        else {
            backButton.setVisibility(View.GONE);
            taskUserId = ShareData.getUserProfile().getUser_id();
            toolbartitle.setText(R.string.task_history_student_toolbar_title);
        }

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

    @Override
    public void onLoadMore(View view) {
        isLoadmore = true;
        mRecyclerView.getLoadMoreFooterView().setVisibility(View.VISIBLE);
        int itemTotal = taskBean.getItemTotal();
        int pageId = itemTotal/10;
        getTaskList(String.valueOf(pageId), taskUserId);
    }

    @Override
    public void onRefresh() {
        isRefresh = true;
        getTaskList(pageId, taskUserId);
    }

    @Override
    public void onSuccess(String result) {

        try {
            progressBar.setVisibility(View.GONE);
            JSONObject response = new JSONObject(result);
            if(response.getString("error").equals("0")) {
                if(!isLoadmore) {
                    JSONObject resultObject = response.getJSONObject("result");
                    if(taskBean == null) {
                        taskBean = new TaskBean(resultObject);
                        mAdapter = new TaskHistoryRecyclerViewAdapter(getActivity(), taskBean, this);
                        mRecyclerView.setIAdapter(mAdapter);
                        AnimationSet slideupAnimation = AnimationUtil.animationSlideUp(getActivity());
                        mRecyclerView.startAnimation(slideupAnimation);
                    } else {
                        isRefresh = false;
                        taskBean.AddTaskFromFront(resultObject);
                    }
                } else {
                    taskBean.AddTask(response.getJSONObject("result"));
                    isLoadmore = false;
                }
                if(!WindowsUtil.isRecyclerScrollable(mRecyclerView)){
                    mRecyclerView.setLoadMoreEnabled(false);
                } else {
                    mRecyclerView.setLoadMoreEnabled(true);
                }
            }
            if(taskBean.getTaskSize() == 0){
                progressBar.setVisibility(View.GONE);
                RelativeLayout rootview = (RelativeLayout) this.getView();
                defaultDisplayview.setImage(getResources().getDrawable(R.drawable.ic_content_copy_black_48dp));
                defaultDisplayview.setImageOnclicklistener(defaultImageListener);
                defaultDisplayview.setText(getResources().getString(R.string.content_empty));
                mRecyclerView.setVisibility(View.GONE);
                rootview.addView(defaultDisplayview);

            } else {
                mRecyclerView.setRefreshEnabled(true);
                mRecyclerView.setVisibility(View.VISIBLE);
            }
            mRecyclerView.setRefreshing(false);
            mRecyclerView.getLoadMoreFooterView().setVisibility(View.GONE);
            mAdapter.notifyDataSetChanged();
            String toolsbarTitle = String.format(toolbartitle.getText().toString(), taskBean.getTotalHours());
            toolbartitle.setText(toolsbarTitle);
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }


    }

    View.OnClickListener defaultImageListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            RelativeLayout view = (RelativeLayout) getView();
            view.removeView(defaultDisplayview);
            getTaskList(pageId, ShareData.getUserProfile().getUser_id());
        }
    };

    @Override
    public void onLostConnection() {
        if(isLoadmore || isRefresh) {
            mRecyclerView.setRefreshing(false);
            View f = mRecyclerView.getLoadMoreFooterView();
            f.setVisibility(View.GONE);
            coordinatorLayout.setVisibility(View.VISIBLE);
            Snackbar.make(coordinatorLayout, getActivity().getString(R.string.no_internet_connection), Snackbar.LENGTH_LONG)
                    .show();
        } else {
            progressBar.setVisibility(View.GONE);
            RelativeLayout rootview = (RelativeLayout) this.getView();
            defaultDisplayview.setImage(getResources().getDrawable(R.drawable.ic_refresh_black_48dp));
            defaultDisplayview.setImageOnclicklistener(defaultImageListener);
            defaultDisplayview.setText(getActivity().getString(R.string.no_internet_connection));
            rootview.addView(defaultDisplayview);
        }
    }

    @Override
    public void onUnreachHost() {
        if(isLoadmore || isRefresh) {
            mRecyclerView.setRefreshing(false);
            View f = mRecyclerView.getLoadMoreFooterView();
            f.setVisibility(View.GONE);
            coordinatorLayout.setVisibility(View.VISIBLE);
            Snackbar.make(coordinatorLayout, getActivity().getString(R.string.no_internet_connection), Snackbar.LENGTH_LONG)
                    .show();
        } else {
            progressBar.setVisibility(View.GONE);
            RelativeLayout rootview = (RelativeLayout) this.getView();
            defaultDisplayview.setImage(getResources().getDrawable(R.drawable.ic_refresh_black_48dp));
            defaultDisplayview.setImageOnclicklistener(defaultImageListener);
            defaultDisplayview.setText(getActivity().getString(R.string.no_internet_connection));
            rootview.addView(defaultDisplayview);
        }
    }

    @Override
    public void onItemClickListener(RecyclerView.ViewHolder caller, View view) {

    }
}
