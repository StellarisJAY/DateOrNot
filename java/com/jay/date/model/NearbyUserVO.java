package com.jay.date.model;

/**
 * @author Jay
 */
public class NearbyUserVO {
    private Integer userId;
    private String username;
    private Integer gender;
    private Integer age;
    private String description;
    private Double distance;
    private Double latitude;
    private Double longitude;

    public NearbyUserVO(Integer userId, String username, Integer gender, Integer age, String description, Double distance, Double latitude, Double longitude) {
        this.userId = userId;
        this.username = username;
        this.gender = gender;
        this.age = age;
        this.description = description;
        this.distance = distance;
        this.latitude = latitude;
        this.longitude = longitude;
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

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getDistance() {
        return distance;
    }

    public void setDistance(Double distance) {
        this.distance = distance;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    @Override
    public String toString() {
        return "NearbyUserVO{" +
                "userId=" + userId +
                ", username='" + username + '\'' +
                ", distance=" + distance +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                '}';
    }
}
