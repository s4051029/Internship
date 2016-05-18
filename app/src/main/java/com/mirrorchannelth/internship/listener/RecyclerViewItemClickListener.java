package com.mirrorchannelth.internship.listener;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import com.mirrorchannelth.internship.adapter.NewsRecyclerViewAdapter;

/**
 * Created by boss on 4/28/16.
 */
public interface RecyclerViewItemClickListener extends View.OnClickListener {

   public void onItemClickListener(RecyclerView.ViewHolder caller, View view);
}
