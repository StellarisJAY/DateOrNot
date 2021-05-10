package com.jay.date.match.pool;

import java.util.List;

/**
 * @author Jay
 */
public class MatchPoolUserInfo {
    private Integer userId;
    private List<Integer> tags;
    private String birthday;
    private Long timestamp;

    public MatchPoolUserInfo(Integer userId, List<Integer> tags, String birthday) {
        this.userId = userId;
        this.tags = tags;
        this.birthday = birthday;
        this.timestamp = System.currentTimeMillis();
    }


    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public List<Integer> getTags() {
        return tags;
    }

    public void setTags(List<Integer> tags) {
        this.tags = tags;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "MatchPoolUserInfo{" +
                "userId=" + userId +
                ", tags=" + tags +
                '}';
    }
}
