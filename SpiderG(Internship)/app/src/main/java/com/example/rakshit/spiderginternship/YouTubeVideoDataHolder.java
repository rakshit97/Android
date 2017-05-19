package com.example.rakshit.spiderginternship;

public class YouTubeVideoDataHolder
{
    private String videoId;
    private String videoName;
    private String videoDesc;

    public YouTubeVideoDataHolder(String videoId, String videoName, String videoDesc)
    {
        this.videoId = videoId;
        this.videoName = videoName;
        this.videoDesc = videoDesc;
    }

    public String getVideoId()
    {
        return videoId;
    }

    public String getVideoName()
    {
        return videoName;
    }

    public String getVideoDesc()
    {
        return videoDesc;
    }
}
