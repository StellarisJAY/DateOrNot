package com.jay.date.model;

/**
 * @author Jay
 */
public class MatchedUserDTO {
    private Integer user_id;
    private String user_name;
    private String birthday;
    private Integer sex;
    private String introduction;
    private String head_image_url;

    public MatchedUserDTO(Integer user_id, String user_name, String birthday, Integer sex, String introduction, String head_image_url) {
        this.user_id = user_id;
        this.user_name = user_name;
        this.birthday = birthday;
        this.sex = sex;
        this.introduction = introduction;
        this.head_image_url = head_image_url;
    }

    public Integer getUser_id() {
        return user_id;
    }

    public void setUser_id(Integer user_id) {
        this.user_id = user_id;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public Integer getSex() {
        return sex;
    }

    public void setSex(Integer sex) {
        this.sex = sex;
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public String getHead_image_url() {
        return head_image_url;
    }

    public void setHead_image_url(String head_image_url) {
        this.head_image_url = head_image_url;
    }

    @Override
    public String toString() {
        return "MatchedUserDTO{" +
                "user_id=" + user_id +
                ", user_name='" + user_name + '\'' +
                ", birthday='" + birthday + '\'' +
                ", sex=" + sex +
                ", introduction='" + introduction + '\'' +
                ", head_image_url='" + head_image_url + '\'' +
                '}';
    }
}
