package com.mirrorchannelth.internship.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
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
import com.mirrorchannelth.internship.adapter.UserRecyclerViewAdapter;
import com.mirrorchannelth.internship.config.WebAPI;
import com.mirrorchannelth.internship.listener.RecyclerViewItemClickListener;
import com.mirrorchannelth.internship.model.ShareData;
import com.mirrorchannelth.internship.model.UserBean;
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
public class UserFragment extends Fragment implements RecyclerViewItemClickListener, OnRefreshListener, Connection.OnConnectionCallBackListener {

    private TextView toolbarTitleTextview;
    private IRecyclerView mRecyclerView;
    private UserRecyclerViewAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ImageView backButton;
    private boolean isRefresh = false;
    private RefreshView header ;
    private RefreshView footer;
    private ProgressBar progressBar;
    private CoordinatorLayout coordinatorLayout;
    private DefaultDisplayView defaultDisplayview;
    private String pageId = "1";
    private String taskUserId;
    private ServiceDao serviceDao;
    private UserBean userBean;

    public UserFragment() {
        // Required empty public constructor
    }

    public static UserFragment newInstance(){
        UserFragment userFragment = new UserFragment();
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

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (userBean != null) {
            mAdapter = new UserRecyclerViewAdapter(getActivity(), userBean, this);
            mRecyclerView.setIAdapter(mAdapter);
        } else {
            progressBar.setVisibility(View.VISIBLE);
            serviceDao.getUserList(ShareData.getUserProfile(), this);
        }


    }

    private void initInstance(View rootview) {
        toolbarTitleTextview = (TextView) rootview.findViewById(R.id.toolbar_title);
        mRecyclerView = (IRecyclerView) rootview.findViewById(R.id.userRecycleview);
        mLayoutManager = new LinearLayoutManager(getContext());
        toolbarTitleTextview.setText(R.string.user_toolbar_title);
        mRecyclerView.setLayoutManager(mLayoutManager);
        serviceDao = new ServiceDao(WebAPI.URL);

        progressBar = (ProgressBar) rootview.findViewById(R.id.taskProgressBar);
        coordinatorLayout = (CoordinatorLayout) rootview.findViewById(R.id.coordinatorLayout);
        header = new RefreshView(getActivity());
//        footer = new RefreshView(getActivity());
        mRecyclerView.setRefreshHeaderView(header);
        mRecyclerView.setLoadMoreFooterView(footer);
        mRecyclerView.setRefreshEnabled(true);
        mRecyclerView.setLoadMoreEnabled(true);
        mRecyclerView.setOnRefreshListener(this);
//        mRecyclerView.setOnLoadMoreListener(this);
        mRecyclerView.setRefreshing(false);
        defaultDisplayview = new DefaultDisplayView(getActivity());

    }




    @Override
    public void onItemClickListener(RecyclerView.ViewHolder caller, View v) {
       UserRecyclerViewAdapter.ViewHolder holder = (UserRecyclerViewAdapter.ViewHolder) caller;
        getActivity().getSupportFragmentManager()
                .beginTransaction()
                .setCustomAnimations(R.anim.from_right, R.anim.to_left, R.anim.from_left, R.anim.to_right)
                .replace(R.id.fragmentContainer, TaskHistoryFragment.newInstance(holder.userId))
                .addToBackStack("taskHistory")
                .commit();
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onRefresh() {
        isRefresh = true;
        serviceDao.getUserList(ShareData.getUserProfile(), this);
    }

//    @Override
//    public void onLoadMore(View view) {
//
//    }

    @Override
    public void onSuccess(String result) {
        try {
            progressBar.setVisibility(View.GONE);
            JSONObject response = new JSONObject(result);
            if(response.getString("error").equals("0")) {
               // JSONObject resultResponse = response.getJSONObject("result");
                    if(userBean == null) {
                        userBean = new UserBean(response);
                        mAdapter = new UserRecyclerViewAdapter(getActivity(), userBean, this);
                        mRecyclerView.setIAdapter(mAdapter);
                        AnimationSet slideupAnimation = AnimationUtil.animationSlideUp(getActivity());
                        mRecyclerView.startAnimation(slideupAnimation);
                    } else {
                        isRefresh = false;
                        userBean.AddUserItemFromFront(response);
                    }

                if(userBean.getUserListSize() == 0){
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
        if(userBean == null || userBean.getUserListSize() == 0) {
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
        if(userBean == null || userBean.getUserListSize() == 0) {
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

    View.OnClickListener defaultImageListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            RelativeLayout view = (RelativeLayout) getView();
            view.removeView(defaultDisplayview);
            progressBar.setVisibility(View.VISIBLE);
            serviceDao.getUserList(ShareData.getUserProfile(), UserFragment.this);
        }
    };
}
