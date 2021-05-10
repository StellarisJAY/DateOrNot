package com.jay.date.model;

/**
 * @author Jay
 */
public class MatchedUserVO {
    private Integer userId;
    private String username;
    private Integer gender;
    private String birthday;
    private String description;
    private String headImageUrl;

    public MatchedUserVO(Integer userId, String username, Integer gender, String birthday, String description, String headImageUrl) {
        this.userId = userId;
        this.username = username;
        this.gender = gender;
        this.birthday = birthday;
        this.description = description;
        this.headImageUrl = headImageUrl;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Integer getGender() {
        return gender;
    }

    public void setGender(Integer gender) {
        this.gender = gender;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getHeadImageUrl() {
        return headImageUrl;
    }

    public void setHeadImageUrl(String headImageUrl) {
        this.headImageUrl = headImageUrl;
    }

    @Override
    public String toString() {
        return "MatchedUserVO{" +
                "userId=" + userId +
                ", username='" + username + '\'' +
                ", gender=" + gender +
                ", birthday='" + birthday + '\'' +
                ", description='" + description + '\'' +
                ", headImageUrl='" + headImageUrl + '\'' +
                '}';
    }
}
