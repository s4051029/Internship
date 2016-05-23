package com.mirrorchannelth.internship.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.load.engine.Resource;
import com.mirrorchannelth.internship.R;
import com.mirrorchannelth.internship.model.Image;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.List;

/**
 * Created by boss on 5/22/16.
 */
public class GalleryRecyclerViewAdapter extends RecyclerView.Adapter<GalleryRecyclerViewAdapter.ViewHolder>{
    private List<Image> imageList;
    private Context context;

    public GalleryRecyclerViewAdapter(Context context, List imageList){
        this.imageList = imageList;
        this.context = context;

    }

    @Override
    public GalleryRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_gallery, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Image image = imageList.get(position);
        Resources res = context.getResources();

        Picasso.with(context).load(image.getUrl()).resize((int)res.getDimension(R.dimen.image_gallery_item),(int)res.getDimension(R.dimen.image_gallery_item)).centerCrop().into(holder.imageView);
    }


    @Override
    public int getItemCount() {
        return imageList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageView;
        public ViewHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.image);
        }
    }
}
