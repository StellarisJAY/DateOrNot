package com.jay.date.model;

import java.util.List;

/**
 * @author Jay
 */
public class MatchedGroupVO {
    private Integer groupId;
    private String groupName;
    private Integer memberCount;
    private String createTime;
    private String description;
    private List<String> tags;
    private String headImageUrl;

    public MatchedGroupVO(Integer groupId, String groupName, Integer memberCount, String createTime, String description, List<String> tags, String headImageUrl) {
        this.groupId = groupId;
        this.groupName = groupName;
        this.memberCount = memberCount;
        this.createTime = createTime;
        this.description = description;
        this.tags = tags;
        this.headImageUrl = headImageUrl;
    }

    public String getHeadImageUrl() {
        return headImageUrl;
    }

    public void setHeadImageUrl(String headImageUrl) {
        this.headImageUrl = headImageUrl;
    }

    public MatchedGroupVO() {
    }

    public Integer getGroupId() {
        return groupId;
    }

    public void setGroupId(Integer groupId) {
        this.groupId = groupId;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public Integer getMemberCount() {
        return memberCount;
    }

    public void setMemberCount(Integer memberCount) {
        this.memberCount = memberCount;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    @Override
    public String toString() {
        return "MatchedGroupVO{" +
                "groupId=" + groupId +
                ", groupName=" + groupName +
                ", memberCount=" + memberCount +
                ", createTime='" + createTime + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
