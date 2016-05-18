package com.mirrorchannelth.internship.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mirrorchannelth.internship.R;
import com.mirrorchannelth.internship.model.ShareData;

/**
 * Created by boss on 4/19/16.
 */
public class TaskHistoryRecyclerViewAdapter extends RecyclerView.Adapter<TaskHistoryRecyclerViewAdapter.ViewHolder> {


    public TaskHistoryRecyclerViewAdapter() {
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_task_history, parent, false);

        // set the view's size, margins, paddings and layout parameters


        ViewHolder vh = new ViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if(ShareData.getUserProfile().getUser_type().equals("E")){
            holder.approveButtonGroup.setVisibility(View.VISIBLE);
        } else {
            holder.approveButtonGroup.setVisibility(View.GONE);

        }

    }

    @Override
    public int getItemCount() {
        return 200;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        LinearLayout approveButtonGroup;
        public ViewHolder(View itemView) {
            super(itemView);
            approveButtonGroup = (LinearLayout) (itemView).findViewById(R.id.approveButtonGroup);
        }
    }
}
