package com.jay.date.service.impl;

import com.jay.date.mapper.GroupMapper;
import com.jay.date.mapper.TagMapper;
import com.jay.date.model.GroupInfoUserVO;
import com.jay.date.model.GroupNoTagDTO;
import com.jay.date.model.MatchedGroupVO;
import com.jay.date.service.GroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Jay
 */
@Service
public class GroupServiceImpl implements GroupService {

    private final GroupMapper groupMapper;
    private final TagMapper tagMapper;
    private final RedisTemplate<String, String> redisTemplate;
    private static final int DEFAULT_LIMIT = 20;
    private static final int RECOMMEND_LIMIT = 20;
    @Autowired
    public GroupServiceImpl(GroupMapper groupMapper, TagMapper tagMapper, RedisTemplate<String, String> redisTemplate) {
        this.groupMapper = groupMapper;
        this.tagMapper = tagMapper;
        this.redisTemplate = redisTemplate;
    }

    @Override
    public List<MatchedGroupVO> searchGroupByName(String groupName, int limit) {
        if(groupName == null || limit <= 0 || groupName.length() == 0){
            throw new IllegalArgumentException("搜索群聊参数错误");
        }
        String searchText = "%" + groupName + "%";
        List<GroupNoTagDTO> groups = groupMapper.searchGroupByName(searchText, Math.min(limit, DEFAULT_LIMIT));

        return getGroupVO(groups);
    }

    @Override
    public List<MatchedGroupVO> recommendGroup(Integer userId, Integer limit) {
        if(userId == null || userId < 0 || limit == null || limit <= 0){
            throw new IllegalArgumentException("推荐群聊参数错误");
        }
        List<GroupNoTagDTO> groupNoTagDTOS = groupMapper.recommendGroup(userId, Math.min(limit, RECOMMEND_LIMIT));

        return getGroupVO(groupNoTagDTOS);
    }

    @Override
    public List<String> getGroupTags(Integer groupId) {
        if(groupId == null || groupId < 0){
            throw new IllegalArgumentException("获取群标签参数错误");
        }
        return tagMapper.getGroupTags(groupId);
    }

    @Override
    public List<GroupInfoUserVO> getGroupMembersForChart(Integer groupId) {
        if(groupId == null || groupId < 0){
            throw new IllegalArgumentException("获取群成员参数错误");
        }
        return groupMapper.getGroupInfoMembers(groupId);
    }

    private List<MatchedGroupVO> getGroupVO(List<GroupNoTagDTO> groupNoTagDTOS){
        List<MatchedGroupVO> result = new ArrayList<>(groupNoTagDTOS.size());
        groupNoTagDTOS.forEach((groupNoTagDTO -> {
            List<String> tags = tagMapper.getGroupTags(groupNoTagDTO.getGroupId());
            result.add(new MatchedGroupVO(
                    groupNoTagDTO.getGroupId(),
                    groupNoTagDTO.getGroupName(),
                    groupNoTagDTO.getMemberCount(),
                    groupNoTagDTO.getCreateTime(),
                    groupNoTagDTO.getDescription(),
                    tags,
                    groupNoTagDTO.getHeadImageUrl()));
        }));
        return result;
    }
}
