package com.jay.date.model;

/**
 * @author Jay
 */
public class GroupInfoUserVO {
    private String birthday;
    private Integer sex;

    public GroupInfoUserVO(String birthday, Integer sex) {
        this.birthday = birthday;
        this.sex = sex;
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

    @Override
    public String toString() {
        return "GroupInfoUserVO{" +
                "birthday='" + birthday + '\'' +
                ", sex=" + sex +
                '}';
    }

    public void setSex(Integer sex) {
        this.sex = sex;
    }
}
