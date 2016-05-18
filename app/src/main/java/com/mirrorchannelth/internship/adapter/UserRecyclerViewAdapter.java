package com.mirrorchannelth.internship.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.mirrorchannelth.internship.R;
import com.mirrorchannelth.internship.listener.RecyclerViewItemClickListener;

/**
 * Created by boss on 4/19/16.
 */
public class UserRecyclerViewAdapter extends RecyclerView.Adapter<UserRecyclerViewAdapter.ViewHolder> {

    private RecyclerViewItemClickListener listener;
    public UserRecyclerViewAdapter(RecyclerViewItemClickListener listener) {

        this.listener = listener;
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
    }

    @Override
    public int getItemCount() {
        return 200;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public ImageView profileImageview;
        public RecyclerViewItemClickListener listener;
        public ViewHolder(View itemView, RecyclerViewItemClickListener listener) {
            super(itemView);
            this.listener = listener;
            profileImageview = (ImageView) itemView.findViewById(R.id.profileImageview);
            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            listener.onItemClickListener(this, v);
        }


    }
}
