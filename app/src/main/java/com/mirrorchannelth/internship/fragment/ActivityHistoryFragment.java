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
import com.mirrorchannelth.internship.util.WindowsUtil;
import com.mirrorchannelth.internship.view.LoadmoreView;
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
    private ActivityBean activityBean;
    private RefreshView header ;
    private LoadmoreView footer;

    public ActivityHistoryFragment() {
        // Required empty public constructor
    }
    public static ActivityHistoryFragment newInstance(){
        ActivityHistoryFragment activityHistoryFragment = new ActivityHistoryFragment();
        return activityHistoryFragment;

    }

    @Override
    public void onResume() {
        super.onResume();
        getActivityList("1");



    }

    private void getActivityList(String pageId) {
        UserProfile userProfile = ShareData.getUserProfile();
        serviceDao = new ServiceDao(WebAPI.URL);
        serviceDao.getActivityList(userProfile, pageId, this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootview = inflater.inflate(R.layout.fragment_activity_history, container, false);
        initInstance(rootview);
        return rootview;
    }

    private void initInstance(View rootview) {
        mRecyclerView = (IRecyclerView) rootview.findViewById(R.id.activity_recycler_view);
        toolbarTitleTextview = (TextView) rootview.findViewById(R.id.toolbar_title);
        backImageview = (ImageView) rootview.findViewById(R.id.leftMenu);

        backImageview.setImageResource(R.drawable.ic_arrow_back_white_24dp);
        toolbarTitleTextview.setText(R.string.activity_history_toolbar_title);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        header = new RefreshView(getActivity());
        footer = new LoadmoreView(getActivity());
        mRecyclerView.setRefreshEnabled(true);
        mRecyclerView.setLoadMoreEnabled(true);
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
            JSONObject response = new JSONObject(result);
            if(response.getString("error").equals("0")) {
                if(!isLoadmore) {
                    JSONObject resultResponse = response.getJSONObject("result");
                    if(activityBean == null) {
                        activityBean = new ActivityBean(resultResponse);
                        mAdapter = new ActivityRectclerViewAdapter(getActivity(), activityBean, this);
                        mRecyclerView.setIAdapter(mAdapter);
                        if(WindowsUtil.isRecyclerScrollable(mRecyclerView)){
                            View f = mRecyclerView.getLoadMoreFooterView();
                            f.setVisibility(View.GONE);
                        }
                    } else {
                        activityBean.AddActivityFromFront(resultResponse);
                        if(WindowsUtil.isRecyclerScrollable(mRecyclerView)){
                            View f = mRecyclerView.getLoadMoreFooterView();
                            f.setVisibility(View.GONE);
                            mRecyclerView.setLoadMoreEnabled(false);
                        }
                    }

                    mRecyclerView.setRefreshing(false);
                } else {
                        activityBean.AddActivityFromFront(response.getJSONObject("result"));
                        View f = mRecyclerView.getLoadMoreFooterView();
                        f.setVisibility(View.GONE);
                        mRecyclerView.setRefreshing(false);
                }

                mAdapter.notifyDataSetChanged();
            } else {
                WindowsUtil.defaultAlertDialog(getString(R.string.default_dialog_header), getString(R.string.login_username_incorrect), getString(R.string.default_label_dialog_button), getActivity());
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onLostConnection() {
        Toast.makeText(getActivity(), "Lost connection", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onUnreachHost() {
        Toast.makeText(getActivity(), "Lost onUnreachHost", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onItemClickListener(RecyclerView.ViewHolder caller, View view) {
        Toast.makeText(getActivity(), "On item click", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRefresh() {
        getActivityList("1");
    }

    @Override
    public void onLoadMore(View view) {
        if(WindowsUtil.isRecyclerScrollable(mRecyclerView)){
            View f = mRecyclerView.getLoadMoreFooterView();
            f.setVisibility(View.GONE);
            mRecyclerView.setLoadMoreEnabled(false);
        } else {
            View f = mRecyclerView.getLoadMoreFooterView();
            f.setVisibility(View.VISIBLE);
        }
        int itemTotal = activityBean.getItemTotal();
        int pageId = itemTotal/10;
        getActivityList(String.valueOf(pageId));
    }


}
