package com.mirrorchannelth.internship.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.aspsine.irecyclerview.IRecyclerView;
import com.aspsine.irecyclerview.OnLoadMoreListener;
import com.aspsine.irecyclerview.OnRefreshListener;
import com.mirrorchannelth.internship.R;
import com.mirrorchannelth.internship.adapter.NewsRecyclerViewAdapter;
import com.mirrorchannelth.internship.config.WebAPI;
import com.mirrorchannelth.internship.listener.RecyclerViewItemClickListener;
import com.mirrorchannelth.internship.model.NewsBean;
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
public class NewsFragment extends Fragment implements Connection.OnConnectionCallBackListener, RecyclerViewItemClickListener, OnRefreshListener, OnLoadMoreListener {

    private IRecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private RecyclerView.Adapter mAdapter;
    private TextView toolbartitle;
    private ServiceDao serviceDao;
    private NewsBean newsBean;
    private boolean isLoadmore = false;
    private RefreshView header ;
    private LoadmoreView footer;

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
        View rootview = inflater.inflate(R.layout.fragment_news, container, false);

        initInstance(rootview);
        return rootview;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        serviceDao = new ServiceDao(WebAPI.URL);
        UserProfile profile = ShareData.getUserProfile();

        serviceDao.requestNews(profile.getToken_key(), profile.getUser_id(), profile.getUser_type(), profile.getUser_group(), "1", this);

    }

    private void initInstance(View rootview) {

        mRecyclerView = (IRecyclerView) rootview.findViewById(R.id.rcNews);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        toolbartitle = (TextView) rootview.findViewById(R.id.toolbar_title);
        toolbartitle.setText(R.string.news_toolbar_title);
        header = new RefreshView(getActivity());
        footer = new LoadmoreView(getActivity());

        mRecyclerView.setRefreshEnabled(true);
        mRecyclerView.setLoadMoreEnabled(true);
        mRecyclerView.setRefreshHeaderView(header);
        mRecyclerView.setLoadMoreFooterView(footer);
        mRecyclerView.setOnRefreshListener(this);
        mRecyclerView.setOnLoadMoreListener(this);

    }

    @Override
    public void onSuccess(String result) {
        try {
            JSONObject response = new JSONObject(result);
            if(response.getString("error").equals("0")) {
                if(!isLoadmore) {
                    JSONObject resultObject = response.getJSONObject("result");
                    if(newsBean == null) {
                        newsBean = new NewsBean(resultObject);
                        mAdapter = new NewsRecyclerViewAdapter(getActivity(), newsBean, this);
                        mRecyclerView.setIAdapter(mAdapter);
                    } else {
                        newsBean.AddNewsFromFront(resultObject);
                    }
                    mRecyclerView.setRefreshing(false);
                } else {
                    newsBean.AddNews(response.getJSONObject("result"));
                    View f = mRecyclerView.getLoadMoreFooterView();
                    f.setVisibility(View.GONE);
                    isLoadmore = false;
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
        Toast.makeText(getActivity(), "UnReachHost", Toast.LENGTH_SHORT).show();
    }



    @Override
    public void onItemClickListener(RecyclerView.ViewHolder caller, View v) {
        NewsRecyclerViewAdapter.ViewHolder holder = (NewsRecyclerViewAdapter.ViewHolder) caller;
        UserProfile profile = ShareData.getUserProfile();
        serviceDao.requestNews(profile.getToken_key(), profile.getUser_id(), profile.getUser_type(), profile.getUser_group(), "2", this);

    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onLoadMore(View view) {
        View f = mRecyclerView.getLoadMoreFooterView();
        f.setVisibility(View.VISIBLE);
        isLoadmore = true;
        UserProfile profile = ShareData.getUserProfile();
        int pageId = Integer.parseInt(newsBean.getPageId());
        serviceDao.requestNews(profile.getToken_key(), profile.getUser_id(), profile.getUser_type(), profile.getUser_group(), String.valueOf(pageId+1), this);

    }

    @Override
    public void onRefresh() {
        UserProfile profile = ShareData.getUserProfile();
        serviceDao.requestNews(profile.getToken_key(), profile.getUser_id(), profile.getUser_type(), profile.getUser_group(), "1", this);
    }
}
