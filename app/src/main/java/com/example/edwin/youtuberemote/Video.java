package com.example.edwin.youtuberemote;

/**
 * Created by Edwin on 6/3/2017.
 */
public class Video {
    private String title, URL, views, uploader, duration;

    public Video(String title, String URL, String views, String uploader, String duration){
        this.title = title;
        this.URL = URL;
        this.views = views;
        this.uploader = uploader;
        this.duration = duration;
    }

    public String getTitle() {
        return title;
    }

    public String getURL() {
        return URL;
    }

    public String getViews() {
        return views;
    }

    public String getUploader() {
        return uploader;
    }

    public String getDuration() {
        return duration;
    }
}
