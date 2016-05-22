package com.mirrorchannelth.internship.model;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by boss on 5/14/16.
 */
public class NewsItem {

    private String newsId;
    private String newsTitle;
    private String newsShortDescription;
    private String newsFullDescription;
    private String[] newsPictureUrls;
    private Date newsDate;

    public NewsItem(JSONObject jsonObject){
        newsId = jsonObject.optString("news_id");
        newsTitle = jsonObject.optString("news_title");
        newsShortDescription = jsonObject.optString("news_short_description");
        newsFullDescription = jsonObject.optString("news_full_description");
        String date =jsonObject.optString("news_date");
        SimpleDateFormat dfm = new SimpleDateFormat("yyyy-MM-dd");
        try {
            newsDate = dfm.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        JSONArray pictureUrls = jsonObject.optJSONArray("news_picture");
        newsPictureUrls = new String[pictureUrls.length()];
        for (int i = 0;i<pictureUrls.length(); i++){
            newsPictureUrls[i] = pictureUrls.optString(i);
        }
        newsPictureUrls = new String[3];
        newsPictureUrls[0] = "http://image.dek-d.com/25/1089566/110621445";
        newsPictureUrls[1] = "http://e2.365dm.com/15/10/768x432/manchester-united-old-trafford-premier-league_3369609.jpg?20151028164244";
        newsPictureUrls[2] = "http://i1.manchestereveningnews.co.uk/incoming/article9882472.ece/ALTERNATES/s1227b/JS70280311.jpg";
    }

    public String getNewsId() {
        return newsId;
    }

    public void setNewsId(String newsId) {
        this.newsId = newsId;
    }

    public String getNewsTitle() {
        return newsTitle;
    }

    public void setNewsTitle(String newsTitle) {
        this.newsTitle = newsTitle;
    }

    public Date getNewsDate() {
        return newsDate;
    }

    public void setNewsDate(Date newsDate) {
        this.newsDate = newsDate;
    }

    public String getNewsShortDescription() {
        return newsShortDescription;
    }

    public void setNewsShortDescription(String newsShortDescription) {
        this.newsShortDescription = newsShortDescription;
    }

    public String getNewsFullDescription() {
        return newsFullDescription;
    }

    public void setNewsFullDescription(String newsFullDescription) {
        this.newsFullDescription = newsFullDescription;
    }

    public String[] getNewsPictureUrls() {
        return newsPictureUrls;
    }

    public void setNewsPictureUrls(String[] newsPictureUrls) {
        this.newsPictureUrls = newsPictureUrls;
    }

    @Override
    public boolean equals(Object o) {
        NewsItem temp = (NewsItem) o;
        if(temp.newsId.equalsIgnoreCase(newsId)){
            return true;
        } else {
            return false;
        }
    }
}
