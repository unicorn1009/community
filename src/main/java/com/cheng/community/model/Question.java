package com.cheng.community.model;

import lombok.Data;

@Data
public class Question {
    private Integer id;
    private String title;
    private String tag;
    private String description;
    private Long gmtCreate;
    private Long gmtModified;
    private Integer creator;
    private Integer viewCount;
    private Integer likeCount;
    private Integer commentCount;
}
