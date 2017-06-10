package com.example.rakshit.spiderginternship;

public class YouTubeVideoDataHolder
{
    private String videoId;
    private String videoName;
    private int tag = 2;
    private String videoDesc;
    //if tag is true - only thumbnail
    //if tag is false, only text

    public YouTubeVideoDataHolder(String videoId, String videoName, int tag, String videoDesc)
    {
        this.videoId = videoId;
        this.videoName = videoName;
        this.videoDesc = videoDesc;
        this.tag = tag;
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

    public int getTaggggg()
    {
        return tag;
    }
}
