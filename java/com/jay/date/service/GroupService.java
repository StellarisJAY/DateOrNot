package com.jay.date.service;

import com.jay.date.model.GroupInfoUserVO;
import com.jay.date.model.MatchedGroupVO;
import com.jay.date.model.TagDO;

import java.util.List;

/**
 * @author Jay
 */
public interface GroupService {
    /**
     * 通过名称搜索群聊
     * @param groupName 群聊名称
     * @param limit 上限
     * @return 搜索结果
     */
    List<MatchedGroupVO> searchGroupByName(String groupName, int limit);

    /**
     * 为用户推荐群聊
     * @param userId 用户id
     * @Param limit 推荐数量上限
     * @return 推荐结果
     */
    List<MatchedGroupVO> recommendGroup(Integer userId, Integer limit);

    /**
     * 获取group的标签
     * @param groupId 群聊id
     * @return 标签集合
     */
    List<String> getGroupTags(Integer groupId);

    /**
     * 获取group成员信息
     * @param groupId 群聊id
     * @return 群成员信息
     */
    List<GroupInfoUserVO> getGroupMembersForChart(Integer groupId);
}
