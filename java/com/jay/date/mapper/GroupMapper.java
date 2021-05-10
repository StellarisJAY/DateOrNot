package com.jay.date.mapper;

import com.jay.date.model.GroupInfoUserVO;
import com.jay.date.model.GroupNoTagDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Jay
 */
@Mapper
@Repository
public interface GroupMapper {

    /**
     * 搜索群聊
     * @param searchText 搜索文本
     * @param limit 数量上限
     * @return 无标签搜索结果
     */
    @Select("SELECT group_id, group_name, member_count, create_time, description, head_image_url " +
            "FROM GroupChat " +
            "WHERE group_name LIKE #{searchText} " +
            "LIMIT #{limit}")
    List<GroupNoTagDTO> searchGroupByName(@Param("searchText") String searchText, @Param("limit") int limit);


    /**
     * 为用户推荐群聊
     * @param userId 用户id
     * @param limit 推荐数量上限
     * @return 无标签推荐结果
     */
    @Select("SELECT g.group_id, g.group_name, g.member_count, g.create_time, g.description, g.head_image_url " +
            "FROM GroupChat g, GroupTag gt " +
            "WHERE " +
            "g.status = 0 AND " +
            "gt.group_id=g.group_id AND " +
            "gt.tag_id IN (SELECT tag_id FROM UserTag WHERE user_id=#{userId}) " +
            "GROUP BY g.group_id HAVING COUNT(gt.tag_id) >= 1 " +
            "ORDER BY COUNT(gt.tag_id) DESC, g.member_count DESC " +
            "LIMIT #{limit};")
    List<GroupNoTagDTO> recommendGroup(@Param("userId") Integer userId, @Param("limit") Integer limit);

    /**
     * 获取群聊信息中群成员的信息
     * @param groupId 群聊id
     * @return 群成员信息
     */
    @Select("SELECT u.birthday, u.sex FROM User u, GroupMember ug WHERE ug.group_id=#{groupId} AND u.user_id=ug.user_id")
    List<GroupInfoUserVO> getGroupInfoMembers(@Param("groupId") Integer groupId);
}
