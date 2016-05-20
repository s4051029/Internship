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
import android.widget.Toast;

import com.aspsine.irecyclerview.IRecyclerView;
import com.aspsine.irecyclerview.OnLoadMoreListener;
import com.aspsine.irecyclerview.OnRefreshListener;
import com.mirrorchannelth.internship.R;
import com.mirrorchannelth.internship.adapter.ActivityRectclerViewAdapter;
import com.mirrorchannelth.internship.config.WebAPI;
import com.mirrorchannelth.internship.listener.RecyclerViewItemClickListener;
import com.mirrorchannelth.internship.model.ActivityBean;
import com.mirrorchannelth.internship.model.ShareData;
import com.mirrorchannelth.internship.model.UserProfile;
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
public class ActivityHistoryFragment extends Fragment implements View.OnClickListener, Connection.OnConnectionCallBackListener, RecyclerViewItemClickListener, OnRefreshListener, OnLoadMoreListener {

    private IRecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private RecyclerView.Adapter mAdapter;
    private TextView toolbarTitleTextview;
    private ImageView backImageview;
    private ServiceDao serviceDao;
    private boolean isLoadmore;
    private boolean isRefresh;
    private ActivityBean activityBean;
    private RefreshView header ;
    private RefreshView footer;
    private final String pageId = "1";
    private CoordinatorLayout coordinatorLayout;
    private DefaultDisplayView defaultDisplayview;
    private ProgressBar progressBar;
    public ActivityHistoryFragment() {
        // Required empty public constructor
    }
    public static ActivityHistoryFragment newInstance(){
        ActivityHistoryFragment activityHistoryFragment = new ActivityHistoryFragment();
        return activityHistoryFragment;

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootview = inflater.inflate(R.layout.fragment_activity_history, container, false);
        initInstance(rootview);
        return rootview;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if(activityBean != null) {
            mAdapter = new ActivityRectclerViewAdapter(getActivity(), activityBean, this);
            mRecyclerView.setIAdapter(mAdapter);
        } else {
                progressBar.setVisibility(View.VISIBLE);
                getActivityList(pageId);
        }
    }

    private void getActivityList(String pageId) {

        UserProfile userProfile = ShareData.getUserProfile();
        serviceDao = new ServiceDao(WebAPI.URL);
        serviceDao.getActivityList(userProfile, pageId, this);
    }

    private void initInstance(View rootview) {
        mRecyclerView = (IRecyclerView) rootview.findViewById(R.id.activity_recycler_view);
        toolbarTitleTextview = (TextView) rootview.findViewById(R.id.toolbar_title);
        backImageview = (ImageView) rootview.findViewById(R.id.leftMenu);
        progressBar = (ProgressBar) rootview.findViewById(R.id.progressBar);

        coordinatorLayout = (CoordinatorLayout) rootview.findViewById(R.id.coordinatorLayout);
        defaultDisplayview = new DefaultDisplayView(getActivity());
        backImageview.setImageResource(R.drawable.ic_arrow_back_white_24dp);
        toolbarTitleTextview.setText(R.string.activity_history_toolbar_title);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        header = new RefreshView(getActivity());
        footer = new RefreshView(getActivity());
        mRecyclerView.setRefreshHeaderView(header);
        mRecyclerView.setLoadMoreFooterView(footer);
        mRecyclerView.setOnRefreshListener(this);
        mRecyclerView.setOnLoadMoreListener(this);
        backImageview.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.leftMenu:
                closeFrament();
                break;
        }
    }

    private void closeFrament() {
        getActivity().getSupportFragmentManager()
                .popBackStack("HistoryFragment",  FragmentManager.POP_BACK_STACK_INCLUSIVE);
    }

    @Override
    public void onSuccess(String result) {
        try {
            progressBar.setVisibility(View.GONE);
            JSONObject response = new JSONObject(result);
            if(response.getString("error").equals("0")) {
                JSONObject resultResponse = response.getJSONObject("result");
                if(!isLoadmore) {
                    if(activityBean == null) {
                        activityBean = new ActivityBean(resultResponse);
                        mAdapter = new ActivityRectclerViewAdapter(getActivity(), activityBean, this);
                        mRecyclerView.setIAdapter(mAdapter);
                        AnimationSet slideupAnimation = AnimationUtil.animationSlideUp(getActivity());
                        mRecyclerView.startAnimation(slideupAnimation);
                    } else {
                        isRefresh = false;
                        activityBean.AddActivityFromFront(resultResponse);
                    }
                } else {
                        activityBean.AddActivity(resultResponse);
                        isLoadmore = false;
                }
                if(!WindowsUtil.isRecyclerScrollable(mRecyclerView)){
                    mRecyclerView.setLoadMoreEnabled(false);
                } else {
                    mRecyclerView.setLoadMoreEnabled(true);
                }
                if(activityBean.getActivitySize() == 0){
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
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onLostConnection() {
        progressBar.setVisibility(View.GONE);

        if(isLoadmore || isRefresh) {
            mRecyclerView.setRefreshing(false);
            mRecyclerView.getLoadMoreFooterView().setVisibility(View.GONE);
            coordinatorLayout.setVisibility(View.VISIBLE);
            Snackbar.make(coordinatorLayout, getActivity().getString(R.string.no_internet_connection), Snackbar.LENGTH_LONG)
                    .show();
        } else {
            RelativeLayout rootview = (RelativeLayout) this.getView();
            defaultDisplayview.setImage(getResources().getDrawable(R.drawable.ic_refresh_black_48dp));
            defaultDisplayview.setImageOnclicklistener(defaultImageListener);
            defaultDisplayview.setText(getActivity().getString(R.string.no_internet_connection));
            rootview.addView(defaultDisplayview);
        }
    }
    View.OnClickListener defaultImageListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            RelativeLayout view = (RelativeLayout) getView();
            view.removeView(defaultDisplayview);
            progressBar.setVisibility(View.VISIBLE);
            getActivityList(pageId);
        }
    };
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
        Toast.makeText(getActivity(), "On item click", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRefresh() {
        isRefresh = true;
        getActivityList(pageId);
    }

    @Override
    public void onLoadMore(View view) {
        isLoadmore = true;
        mRecyclerView.getLoadMoreFooterView().setVisibility(View.VISIBLE);
        int itemTotal = activityBean.getItemTotal();
        int pageId = itemTotal/10;
        getActivityList(String.valueOf(pageId));
    }


}
