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
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aspsine.irecyclerview.IRecyclerView;
import com.aspsine.irecyclerview.OnLoadMoreListener;
import com.aspsine.irecyclerview.OnRefreshListener;
import com.mirrorchannelth.internship.R;
import com.mirrorchannelth.internship.adapter.NewsRecyclerViewAdapter;
import com.mirrorchannelth.internship.config.WebAPI;
import com.mirrorchannelth.internship.listener.RecyclerViewItemClickListener;
import com.mirrorchannelth.internship.model.NewsBean;
import com.mirrorchannelth.internship.model.ShareData;
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
public class NewsFragment extends Fragment implements Connection.OnConnectionCallBackListener, RecyclerViewItemClickListener, OnRefreshListener, OnLoadMoreListener {

    private IRecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private NewsRecyclerViewAdapter mAdapter;
    private TextView toolbartitle;
    private ServiceDao serviceDao;
    private NewsBean newsBean;
    private boolean isLoadmore = false;
    private boolean isRefresh = false;
    private RefreshView header ;
    private RefreshView footer;
    private ProgressBar progressBar;
    private CoordinatorLayout coordinatorLayout;
    private  DefaultDisplayView defaultDisplayview;
    private View rootview;
    private String defaultPageId = "1";

    public static NewsFragment newInstance(){
        NewsFragment newsFragment = new NewsFragment();
        Bundle bundle = new Bundle();
        newsFragment.setArguments(bundle);
        return newsFragment;
    }

    public NewsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
         rootview = inflater.inflate(R.layout.fragment_news, container, false);
        initInstance(rootview);
        return rootview;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if(newsBean != null){
            mAdapter = new NewsRecyclerViewAdapter(getActivity(), newsBean, this);
            mRecyclerView.setIAdapter(mAdapter);
            if(newsBean.getNewsSize() == 0){
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

        }else {
            getNewsList(defaultPageId);
        }
    }

    private void getNewsList(String pageId) {
        progressBar.setVisibility(View.VISIBLE);
        serviceDao = new ServiceDao(WebAPI.URL);
        serviceDao.requestNews(pageId, this, ShareData.getUserProfile() );
    }

    private void initInstance(View rootview) {

        mRecyclerView = (IRecyclerView) rootview.findViewById(R.id.rcNews);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        toolbartitle = (TextView) rootview.findViewById(R.id.toolbar_title);
        progressBar = (ProgressBar) rootview.findViewById(R.id.progressBar);
        coordinatorLayout = (CoordinatorLayout) rootview.findViewById(R.id.coordinatorLayout);
        toolbartitle.setText(R.string.news_toolbar_title);
        header = new RefreshView(getActivity());
        footer = new RefreshView(getActivity());
        defaultDisplayview = new DefaultDisplayView(getActivity());

        mRecyclerView.setRefreshHeaderView(header);
        mRecyclerView.setLoadMoreFooterView(footer);
        mRecyclerView.setRefreshEnabled(true);
        mRecyclerView.setLoadMoreEnabled(true);
        mRecyclerView.setOnRefreshListener(this);
        mRecyclerView.setOnLoadMoreListener(this);
        mRecyclerView.setRefreshing(false);
    }

    @Override
    public void onSuccess(String result) {
        try {
            progressBar.setVisibility(View.GONE);
            JSONObject response = new JSONObject(result);
            if(response.getString("error").equals("0")) {
                if(!isLoadmore) {
                    JSONObject resultObject = response.getJSONObject("result");
                    if(newsBean == null) {
                        newsBean = new NewsBean(resultObject);
                        mAdapter = new NewsRecyclerViewAdapter(getActivity(), newsBean, this);
                        mRecyclerView.setIAdapter(mAdapter);
                        AnimationSet slideupAnimation = AnimationUtil.animationSlideUp(getActivity());
                        mRecyclerView.startAnimation(slideupAnimation);
                    } else {
                        isRefresh = false;
                        newsBean.AddNewsFromFront(resultObject);
                    }
                } else {
                    newsBean.AddNews(response.getJSONObject("result"));
                    isLoadmore = false;
                }
                if(!WindowsUtil.isRecyclerScrollable(mRecyclerView)){
                    mRecyclerView.setLoadMoreEnabled(false);
                } else {
                    mRecyclerView.setLoadMoreEnabled(true);
                }
                if(newsBean.getNewsSize() == 0){
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
    View.OnClickListener defaultImageListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            RelativeLayout view = (RelativeLayout) getView();
            view.removeView(defaultDisplayview);
            getNewsList(defaultPageId);
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
    public void onItemClickListener(RecyclerView.ViewHolder caller, View v) {

    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onLoadMore(View view) {
        View f = mRecyclerView.getLoadMoreFooterView();
        f.setVisibility(View.VISIBLE);
        isLoadmore = true;
        int pageId = Integer.parseInt(newsBean.getPageId());
        serviceDao.requestNews(String.valueOf(pageId+1), this, ShareData.getUserProfile());

    }

    @Override
    public void onRefresh() {
        isRefresh = true;
        serviceDao.requestNews(defaultPageId, this, ShareData.getUserProfile());
    }
}
