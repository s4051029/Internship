package com.mirrorchannelth.internship.adapter;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mirrorchannelth.internship.R;
import com.mirrorchannelth.internship.fragment.ActivityHistoryFragment;
import com.mirrorchannelth.internship.listener.RecyclerViewItemClickListener;
import com.mirrorchannelth.internship.model.ActivityBean;
import com.mirrorchannelth.internship.model.ActivityItem;
import com.mirrorchannelth.internship.view.DateView;

/**
 * Created by boss on 4/24/16.
 */
public class ActivityRectclerViewAdapter extends RecyclerView.Adapter<ActivityRectclerViewAdapter.ViewHolder>{
    private ActivityBean activityBean;
    private Context context;
    private RecyclerViewItemClickListener itemClickListener;
    public ActivityRectclerViewAdapter(Context context, ActivityBean activityBean, RecyclerViewItemClickListener itemClickListener) {
        this.activityBean = activityBean;
        this.context = context;
        this.itemClickListener = itemClickListener;
    }

    @Override
    public ActivityRectclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_activity_history, parent, false);

        // set the view's size, margins, paddings and layout parameters


        ViewHolder vh = new ViewHolder(view, itemClickListener);
        return vh;
    }

    @Override
    public void onBindViewHolder(ActivityRectclerViewAdapter.ViewHolder holder, int position) {
        ActivityItem item = activityBean.getActivity(position);
        holder.activityTitleTextview.setText(item.getActivityTitle());
        holder.activityDescriptionTextview.setText(item.getActivityDescription());
        holder.activityDate.setDate(item.getActivityDate());

    }

    @Override
    public int getItemCount() {
        return activityBean.getActivitySize();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private DateView activityDate;
        private TextView activityTitleTextview;
        private TextView activityDescriptionTextview;
        private ImageView iconActivityImageview;
        private RecyclerViewItemClickListener itemClickListener;
        public ViewHolder(View itemView, RecyclerViewItemClickListener itemClickListener) {
            super(itemView);
            activityDate = (DateView) itemView.findViewById(R.id.activityDate);
            activityTitleTextview = (TextView) itemView.findViewById(R.id.activityTitleTextview);
            activityDescriptionTextview = (TextView) itemView.findViewById(R.id.activityDetailDescriptionTextview);
            iconActivityImageview = (ImageView) itemView.findViewById(R.id.activityIconImageview);
            this.itemClickListener = itemClickListener;
        }

        @Override
        public void onClick(View v) {
            itemClickListener.onItemClickListener(this, v);
        }
    }
}
