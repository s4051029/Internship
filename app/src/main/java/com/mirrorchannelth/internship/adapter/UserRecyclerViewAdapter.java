package com.mirrorchannelth.internship.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mirrorchannelth.internship.R;
import com.mirrorchannelth.internship.listener.RecyclerViewItemClickListener;
import com.mirrorchannelth.internship.model.UserBean;
import com.mirrorchannelth.internship.model.UserItem;

/**
 * Created by boss on 4/19/16.
 */
public class UserRecyclerViewAdapter extends RecyclerView.Adapter<UserRecyclerViewAdapter.ViewHolder> {

    private Context context;
    private UserBean userBean;
    private RecyclerViewItemClickListener listener;
    public UserRecyclerViewAdapter(Context context, UserBean userBean, RecyclerViewItemClickListener listener) {

        this.listener = listener;
        this.context = context;
        this.userBean = userBean;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_user_list, parent, false);

        // set the view's size, margins, paddings and layout parameters
        ViewHolder vh = new ViewHolder(view, listener);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        UserItem userItem = userBean.getUser(position);
        holder.titleTextView.setText(userItem.getTitle());
        holder.nameTextView.setText(userItem.getName());
        holder.userId = userItem.getUserId();

    }

    @Override
    public int getItemCount() {
        return userBean.getUserListSize();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public ImageView profileImageview;
        public RecyclerViewItemClickListener listener;
        public TextView nameTextView;
        public TextView titleTextView;
        public String userId;
        public ViewHolder(View itemView, RecyclerViewItemClickListener listener) {
            super(itemView);
            this.listener = listener;
            profileImageview = (ImageView) itemView.findViewById(R.id.profileImageview);
            nameTextView = (TextView) itemView.findViewById(R.id.nametextView);
            titleTextView = (TextView) itemView.findViewById(R.id.titleTextview);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            listener.onItemClickListener(this, v);
        }


    }
}
