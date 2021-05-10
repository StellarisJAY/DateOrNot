package com.jay.date.model;

/**
 * @author Jay
 */
public class GroupNoTagDTO {
    private Integer groupId;
    private String groupName;
    private Integer memberCount;
    private String createTime;
    private String description;
    private String headImageUrl;

    public GroupNoTagDTO(Integer groupId, String groupName, Integer memberCount, String createTime, String description, String headImageUrl) {
        this.groupId = groupId;
        this.groupName = groupName;
        this.memberCount = memberCount;
        this.createTime = createTime;
        this.description = description;
        this.headImageUrl = headImageUrl;
    }

    public String getHeadImageUrl() {
        return headImageUrl;
    }

    public void setHeadImageUrl(String headImageUrl) {
        this.headImageUrl = headImageUrl;
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

    @Override
    public String toString() {
        return "GroupNoTagDTO{" +
                "groupId=" + groupId +
                ", groupName='" + groupName + '\'' +
                ", memberCount=" + memberCount +
                ", createTime='" + createTime + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
