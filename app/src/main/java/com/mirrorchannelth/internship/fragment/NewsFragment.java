package com.mirrorchannelth.internship.fragment;


import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.content.res.ResourcesCompat;
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
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if(newsBean != null){
            mAdapter = new NewsRecyclerViewAdapter(getActivity(), newsBean, this);
            mRecyclerView.setIAdapter(mAdapter);
            if(newsBean.getNewsSize() == 0){
                showDefaultView(getResources().getString(R.string.content_empty),
                        ResourcesCompat.getDrawable(getResources(), R.drawable.ic_content_copy_black_48dp, null), refreshClickListener);
            } else {
                mRecyclerView.setRefreshEnabled(true);
                mRecyclerView.setVisibility(View.VISIBLE);
            }
        }else {
            getNewsList(defaultPageId);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
         rootview = inflater.inflate(R.layout.fragment_news, container, false);
        bindWidget(rootview);
        initWidget();
        return rootview;
    }

    private void bindWidget(View rootview) {
        mRecyclerView = (IRecyclerView) rootview.findViewById(R.id.newsRecyclerview);
        toolbartitle = (TextView) rootview.findViewById(R.id.toolbar_title);
        progressBar = (ProgressBar) rootview.findViewById(R.id.progressBar);
        coordinatorLayout = (CoordinatorLayout) rootview.findViewById(R.id.coordinatorLayout);
    }

    private void initWidget() {

        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        toolbartitle.setText(R.string.news_toolbar_title);
        header = new RefreshView(getActivity());
        footer = new RefreshView(getActivity());
        defaultDisplayview = new DefaultDisplayView(getActivity());
        mRecyclerView.setRefreshHeaderView(header);
        mRecyclerView.setLoadMoreFooterView(footer);
        mRecyclerView.setRefreshEnabled(true);
        mRecyclerView.setRefreshing(false);
        mRecyclerView.setLoadMoreEnabled(true);
        mRecyclerView.setOnRefreshListener(this);
        mRecyclerView.setOnLoadMoreListener(this);

    }

    private void getNewsList(String pageId) {
        progressBar.setVisibility(View.VISIBLE);
        serviceDao = new ServiceDao(WebAPI.URL);
        serviceDao.requestNews(pageId, this, ShareData.getUserProfile() );
    }

    @Override
    public void onSuccess(String result) {
        try {
            progressBar.setVisibility(View.GONE);
            JSONObject response = new JSONObject(result);
            if(response.getString("error").equals("0")) {
                JSONObject resultObject = response.getJSONObject("result");
                if(!isLoadmore) {
                    if(newsBean == null) {
                        newsBean = new NewsBean(resultObject);
                        mAdapter = new NewsRecyclerViewAdapter(getActivity(), newsBean, this);
                        mRecyclerView.setIAdapter(mAdapter);
                        AnimationSet slideupAnimation = AnimationUtil.animationSlideUp(getActivity());
                        mRecyclerView.startAnimation(slideupAnimation);
                    } else {
                        newsBean.insertNews(resultObject);
                    }
                } else {
                    newsBean.AddNews(resultObject);
                    isLoadmore = false;
                }
                if(newsBean.getNewsSize() == 0){
                    showDefaultView(getResources().getString(R.string.content_empty),
                            ResourcesCompat.getDrawable(getResources(), R.drawable.ic_content_copy_black_48dp, null), refreshClickListener);
                } else {
                    mRecyclerView.setRefreshEnabled(true);
                    mRecyclerView.setVisibility(View.VISIBLE);
                }
                mRecyclerView.setRefreshing(false);
                mRecyclerView.getLoadMoreFooterView().setVisibility(View.GONE);
                mAdapter.notifyDataSetChanged();
            } else{

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
        if (newsBean != null && newsBean.getNewsSize() !=0) {
            showSnackbar(getResources().getString(R.string.no_internet_connection));
        } else  {
            showDefaultView(getResources().getString(R.string.no_internet_connection), ResourcesCompat.getDrawable(getResources(), R.drawable.ic_refresh_black_48dp, null) , refreshClickListener);
        }
    }

    private void showSnackbar(String text) {
        mRecyclerView.setRefreshing(false);
        View f = mRecyclerView.getLoadMoreFooterView();
        f.setVisibility(View.GONE);
        coordinatorLayout.setVisibility(View.VISIBLE);
        Snackbar.make(coordinatorLayout, text, Snackbar.LENGTH_LONG)
                .show();
    }

    View.OnClickListener refreshClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            RelativeLayout view = (RelativeLayout) getView();
            view.removeView(defaultDisplayview);
            getNewsList(defaultPageId);
        }
    };

    @Override
    public void onUnreachHost() {
        if (newsBean != null && newsBean.getNewsSize() !=0) {
            showSnackbar(getResources().getString(R.string.no_internet_connection));
        } else  {
            showDefaultView(getResources().getString(R.string.no_internet_connection), ResourcesCompat.getDrawable(getResources(), R.drawable.ic_refresh_black_48dp, null) , refreshClickListener);
        }
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


    @Override
    public void onItemClickListener(RecyclerView.ViewHolder caller, View v) {

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
        serviceDao.requestNews(defaultPageId, this, ShareData.getUserProfile());
    }
}
