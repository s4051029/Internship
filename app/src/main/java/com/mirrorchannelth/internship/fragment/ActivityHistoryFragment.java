package com.mirrorchannelth.internship.fragment;


import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.res.ResourcesCompat;
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
import com.mirrorchannelth.internship.adapter.ActivityRectclerViewAdapter;
import com.mirrorchannelth.internship.adapter.NewsRecyclerViewAdapter;
import com.mirrorchannelth.internship.config.WebAPI;
import com.mirrorchannelth.internship.listener.RecyclerViewItemClickListener;
import com.mirrorchannelth.internship.model.ActivityBean;
import com.mirrorchannelth.internship.model.ShareData;
import com.mirrorchannelth.internship.model.UserProfile;
import com.mirrorchannelth.internship.net.Connection;
import com.mirrorchannelth.internship.service.ServiceDao;
import com.mirrorchannelth.internship.util.AnimationUtil;
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
    private ActivityRectclerViewAdapter mAdapter;
    private TextView toolbarTitleTextview;
    private ImageView backImageview;
    private ServiceDao serviceDao;
    private boolean isLoadmore;
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
        bindWidget(rootview);
        initWidget();
        setWidgetListener();
        return rootview;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if(savedInstanceState != null) {

            activityBean = savedInstanceState.getParcelable("activity");
            mAdapter.setActivityBean(activityBean);
            mRecyclerView.setIAdapter(mAdapter);
            if(activityBean.getActivitySize() == 0){
                showDefaultView(getResources().getString(R.string.content_empty),
                        ResourcesCompat.getDrawable(getResources(), R.drawable.ic_content_copy_black_48dp, null), refreshClickListener);
            } else {
                mRecyclerView.setRefreshEnabled(true);
                mRecyclerView.setVisibility(View.VISIBLE);
            }
        }

        else {
                progressBar.setVisibility(View.VISIBLE);
                getActivityList(pageId);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {

        outState.putParcelable("activity", activityBean);
        super.onSaveInstanceState(outState);
    }

    private void getActivityList(String pageId) {
        UserProfile userProfile = ShareData.getUserProfile();
        serviceDao = new ServiceDao(WebAPI.URL);
        serviceDao.getActivityList(userProfile, pageId, this);
    }

    private void bindWidget(View rootview) {
        mRecyclerView = (IRecyclerView) rootview.findViewById(R.id.activity_recycler_view);
        toolbarTitleTextview = (TextView) rootview.findViewById(R.id.toolbar_title);
        backImageview = (ImageView) rootview.findViewById(R.id.leftMenu);
        progressBar = (ProgressBar) rootview.findViewById(R.id.progressBar);
        coordinatorLayout = (CoordinatorLayout) rootview.findViewById(R.id.coordinatorLayout);

    }

    private void setWidgetListener() {
        mRecyclerView.setOnRefreshListener(this);
        mRecyclerView.setOnLoadMoreListener(this);
        backImageview.setOnClickListener(this);
    }

    private void initWidget() {
        defaultDisplayview = new DefaultDisplayView(getActivity());
        backImageview.setImageResource(R.drawable.ic_arrow_back_white_24dp);
        toolbarTitleTextview.setText(R.string.activity_history_toolbar_title);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        header = new RefreshView(getActivity());
        footer = new RefreshView(getActivity());
        mAdapter = new ActivityRectclerViewAdapter(getActivity(), this);
        mRecyclerView.setRefreshHeaderView(header);
        mRecyclerView.setLoadMoreFooterView(footer);
        mRecyclerView.setLoadMoreEnabled(true);
        mRecyclerView.setRefreshEnabled(true);
        mRecyclerView.getLoadMoreFooterView().setVisibility(View.GONE);
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
                        mAdapter.setActivityBean(activityBean);
                        mRecyclerView.setIAdapter(mAdapter);
                        AnimationSet slideupAnimation = AnimationUtil.animationSlideUp(getActivity());
                        mRecyclerView.startAnimation(slideupAnimation);
                    } else {
                        activityBean.insertActivity(resultResponse);
                    }
                } else {
                        activityBean.AddActivity(resultResponse);
                        isLoadmore = false;
                }
                if(activityBean.getActivitySize() == 0){
                    showDefaultView(getResources().getString(R.string.content_empty),
                            ResourcesCompat.getDrawable(getResources(), R.drawable.ic_content_copy_black_48dp, null), refreshClickListener);
                } else {
                    mRecyclerView.setRefreshEnabled(true);
                    mRecyclerView.setVisibility(View.VISIBLE);
                }
                mRecyclerView.setRefreshing(false);
                mRecyclerView.getLoadMoreFooterView().setVisibility(View.GONE);
                mAdapter.notifyDataSetChanged();
            } else {
                showDefaultView(getResources().getString(R.string.default_message_dialog), ResourcesCompat.getDrawable(getResources(),
                        R.drawable.ic_refresh_black_48dp, null) , refreshClickListener);
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
        if (activityBean != null && activityBean.getActivitySize() !=0) {
            showSnackbar(getResources().getString(R.string.no_internet_connection));
        } else  {
            showDefaultView(getResources().getString(R.string.no_internet_connection), ResourcesCompat.getDrawable(getResources(), R.drawable.ic_refresh_black_48dp, null) , refreshClickListener);
        }
    }
    View.OnClickListener refreshClickListener = new View.OnClickListener() {
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
        progressBar.setVisibility(View.GONE);
        if (activityBean != null && activityBean.getActivitySize() !=0) {
            showSnackbar(getResources().getString(R.string.no_internet_connection));
        } else  {
            showDefaultView(getResources().getString(R.string.no_internet_connection), ResourcesCompat.getDrawable(getResources(), R.drawable.ic_refresh_black_48dp, null) , refreshClickListener);
        }
    }
    @Override
    public void onItemClickListener(RecyclerView.ViewHolder caller, View view) {
    }
    @Override
    public void onRefresh() {
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
    private void showDefaultView(String text, Drawable drawable, View.OnClickListener onClickListener) {
        progressBar.setVisibility(View.GONE);
        RelativeLayout rootview = (RelativeLayout) this.getView();
        defaultDisplayview.setImage(drawable);
        defaultDisplayview.setImageOnclicklistener(onClickListener);
        defaultDisplayview.setText(text);
        mRecyclerView.setVisibility(View.GONE);
        rootview.addView(defaultDisplayview);
    }
    private void showSnackbar(String text) {
        mRecyclerView.setRefreshing(false);
        View f = mRecyclerView.getLoadMoreFooterView();
        f.setVisibility(View.GONE);
        coordinatorLayout.setVisibility(View.VISIBLE);
        Snackbar.make(coordinatorLayout, text, Snackbar.LENGTH_LONG)
                .show();
    }
}
