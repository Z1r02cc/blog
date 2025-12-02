package com.zero.blog.entity;

import lombok.Data;

@Data
public class Video {
    private String id;
    private String videoUrl;      // 视频文件地址
    private String coverUrl;      // 封面图
    private String authorName;    // 作者名
    private String authorAvatar;  // 作者头像
    private String description;   // 视频文案
    private int likeCount;        // 点赞数
    private int commentCount;     // 评论数
    private int shareCount;       // 分享数

    public Video(String id, String videoUrl, int shareCount, int commentCount, int likeCount, String description, String authorAvatar, String authorName, String coverUrl) {
        this.id = id;
        this.videoUrl = videoUrl;
        this.shareCount = shareCount;
        this.commentCount = commentCount;
        this.likeCount = likeCount;
        this.description = description;
        this.authorAvatar = authorAvatar;
        this.authorName = authorName;
        this.coverUrl = coverUrl;
    }
}