package com.mirrorchannelth.internship.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SlidingPaneLayout;
import android.support.v7.widget.RecyclerView;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.DefaultSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.inthecheesefactory.thecheeselibrary.widget.AdjustableImageView;
import com.mirrorchannelth.internship.R;
import com.mirrorchannelth.internship.listener.RecyclerViewItemClickListener;
import com.mirrorchannelth.internship.model.NewsBean;
import com.mirrorchannelth.internship.model.NewsItem;
import com.mirrorchannelth.internship.view.DateView;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.Date;

/**
 * Created by boss on 4/19/16.
 */
public class NewsRecyclerViewAdapter extends RecyclerView.Adapter<NewsRecyclerViewAdapter.ViewHolder> {

    private NewsBean newsBean;
    private Context context;
    private RecyclerViewItemClickListener itemListener;
    public NewsRecyclerViewAdapter(Context context, NewsBean newsBean, RecyclerViewItemClickListener itemListener) {
        this.newsBean = newsBean;
        this.context = context;
        this.itemListener = itemListener;
    }
    public void setDataSource(NewsBean newsBean){
        this.newsBean = newsBean;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_news, parent, false);
        // set the view's size, margins, paddings and layout parameters

        ViewHolder vh = new ViewHolder(view, context);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

            NewsItem news = newsBean.getNews(position);
            holder.headlineTextview.setText(news.getNewsTitle());
            holder.shortDescriptionTextview.setText(news.getNewsShortDescription());
            Date newsDate = news.getNewsDate();
            holder.dateView.setDate(newsDate);
            holder.sliderLayout.removeAllSliders();
        for (int i = 0; i< news.getNewsPictureUrls().length; i++) {
            DefaultSliderView textSliderView = new DefaultSliderView(context);
            textSliderView
                    .image(news.getNewsPictureUrls()[i])
                    .setScaleType(BaseSliderView.ScaleType.CenterCrop)
                    .getPicasso();

            holder.sliderLayout.addSlider(textSliderView);
        }
        holder.sliderLayout.setDuration(6000);

    }


    @Override
    public int getItemCount() {
        return newsBean.getNewsSize();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView headlineTextview;
        public TextView shortDescriptionTextview;
        public DateView dateView;
        public LinearLayout mainView;
        public SliderLayout sliderLayout;

        public ViewHolder(View itemView, Context context) {
            super(itemView);
            headlineTextview = (TextView) itemView.findViewById(R.id.headlineTextview);
            shortDescriptionTextview = (TextView) itemView.findViewById(R.id.shortDescriptionTextview);
            dateView = (DateView) itemView.findViewById(R.id.dateView);
            mainView = (LinearLayout) itemView.findViewById(R.id.main);
            sliderLayout = (SliderLayout) itemView.findViewById(R.id.slider);

        }
    }
}
