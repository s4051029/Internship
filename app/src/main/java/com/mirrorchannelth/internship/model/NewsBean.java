package com.mirrorchannelth.internship.model;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by boss on 5/14/16.
 */
public class NewsBean implements Parcelable {
    private List<NewsItem> newsList = new ArrayList<NewsItem>();
    private String pageId;
    private int limit;
    public NewsBean(JSONObject jsonObject){
        pageId = jsonObject.optString("page_id");
        limit = jsonObject.optInt("limit");
        JSONArray news = jsonObject.optJSONArray("news");
        addNewsList(news);
    }

    public void AddNews(JSONObject jsonObject){
        pageId = jsonObject.optString("page_id");
        limit = jsonObject.optInt("limit");
        JSONArray news = jsonObject.optJSONArray("news");
        addNewsList(news);

    }

    private void addNewsList(JSONArray news) {
        if(news !=null) {
            for (int i = 0; i < news.length(); i++) {
                JSONObject newsTmp = news.optJSONObject(i);
                NewsItem newsItem = new NewsItem(newsTmp);
                if(!newsList.contains(newsItem)) {
                    newsList.add(newsItem);
                }

            }
        }
    }

    public void insertNews(JSONObject jsonObject){
        pageId = jsonObject.optString("page_id");
        limit = jsonObject.optInt("limit");
        JSONArray news = jsonObject.optJSONArray("news");
        if(news !=null) {
            for (int i = 0; i < news.length(); i++) {
                JSONObject newsTmp = news.optJSONObject(i);
                NewsItem newsItem = new NewsItem(newsTmp);
                if(!newsList.contains(newsItem)) {
                    newsList.add(i, newsItem);
                }
            }
        }
    }

    public NewsItem getNews(int position){
        return newsList.get(position);
    }

    public int getNewsSize(){
        return newsList.size();

    }

    public String getPageId(){

        return pageId;
    }

    protected NewsBean(Parcel in) {
        if (in.readByte() == 0x01) {
            newsList = new ArrayList<NewsItem>();
            in.readList(newsList, NewsItem.class.getClassLoader());
        } else {
            newsList = null;
        }
        pageId = in.readString();
        limit = in.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (newsList == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(newsList);
        }
        dest.writeString(pageId);
        dest.writeInt(limit);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<NewsBean> CREATOR = new Parcelable.Creator<NewsBean>() {
        @Override
        public NewsBean createFromParcel(Parcel in) {
            return new NewsBean(in);
        }

        @Override
        public NewsBean[] newArray(int size) {
            return new NewsBean[size];
        }
    };
}
