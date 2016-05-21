package com.mirrorchannelth.internship.adapter;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mirrorchannelth.internship.R;
import com.mirrorchannelth.internship.fragment.TaskHistoryFragment;
import com.mirrorchannelth.internship.listener.RecyclerViewItemClickListener;
import com.mirrorchannelth.internship.model.ShareData;
import com.mirrorchannelth.internship.model.TaskBean;
import com.mirrorchannelth.internship.model.TaskItem;
import com.mirrorchannelth.internship.view.DateView;

import org.w3c.dom.Text;

import java.util.Date;

/**
 * Created by boss on 4/19/16.
 */
public class TaskHistoryRecyclerViewAdapter extends RecyclerView.Adapter<TaskHistoryRecyclerViewAdapter.ViewHolder> {

    private Context context;
    private TaskBean taskBean;
    private RecyclerViewItemClickListener itemClickListener;

    public TaskHistoryRecyclerViewAdapter() {
    }

    public TaskHistoryRecyclerViewAdapter(Context context, TaskBean taskBean, RecyclerViewItemClickListener itemClickListener) {
        this.context = context;
        this.taskBean = taskBean;
        this.itemClickListener = itemClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_task_history, parent, false);

        // set the view's size, margins, paddings and layout parameters


        ViewHolder vh = new ViewHolder(view, itemClickListener);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if(ShareData.getUserProfile().getUser_type().equals("E")){
            holder.approveButtonGroup.setVisibility(View.VISIBLE);
        } else {
            holder.approveButtonGroup.setVisibility(View.GONE);
        }
        TaskItem item = taskBean.getActivity(position);
        holder.taskTitleTextview.setText(item.getTaskTitle());
        holder.taskHourTextview.setText(item.getTaskHours());
        holder.taskDescriptionTextview.setText(item.getTaskDescription());
        holder.taskDate.setDate(item.getTaskDate());

    }

    @Override
    public int getItemCount() {
        return taskBean.getTaskSize();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        LinearLayout approveButtonGroup;
        public TextView taskTitleTextview;
        public TextView taskDescriptionTextview;
        public TextView taskHourTextview;
        private RecyclerViewItemClickListener itemClickListener;
        public DateView taskDate;
        public ViewHolder(View itemView, RecyclerViewItemClickListener itemClickListener) {
            super(itemView);
            approveButtonGroup = (LinearLayout) (itemView).findViewById(R.id.approveButtonGroup);
            taskDescriptionTextview = (TextView) itemView.findViewById(R.id.taskDescriptionTextview);
            taskTitleTextview = (TextView) itemView.findViewById(R.id.taskTitleTextview);
            taskHourTextview = (TextView) itemView.findViewById(R.id.taskHoursTextview);
            taskDate = (DateView) itemView.findViewById(R.id.taskDate);
            this.itemClickListener = itemClickListener;
        }

        @Override
        public void onClick(View v) {

        }
    }
}
