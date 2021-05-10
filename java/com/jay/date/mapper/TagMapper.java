package com.jay.date.mapper;


import com.jay.date.model.TagDO;
import org.apache.ibatis.annotations.Insert;
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
public interface TagMapper {

    /**
     * 随机获取limit个标签
     * @param limit 数量
     * @return 获取的随机标签
     */
    @Select("SELECT * FROM Tag ORDER BY RAND() LIMIT #{limit}")
    List<TagDO> randomTags(@Param("limit") Integer limit);

    /**
     * 添加用户自定义tag
     * @param content tag内容
     * @return 添加状态
     */
    @Insert("INSERT INTO Tag VALUES(null, #{content})")
    Integer saveTag(@Param("content") String content);


    /**
     * 获取用户的tag内容
     * @param userId 用户id
     * @return tag内容集合
     */
    @Select("SELECT content FROM Tag, UserTag WHERE UserTag.user_id=#{userId} AND Tag.tag_id=UserTag.tag_id")
    List<String> getUserTags(@Param("userId") Integer userId);

    /**
     * 获取群聊tag内容
     * @param groupId 群聊id
     * @return tag内容集合
     */
    @Select("SELECT content FROM Tag, GroupTag WHERE GroupTag.group_id=#{groupId} AND GroupTag.tag_id=Tag.tag_id")
    List<String> getGroupTags(@Param("groupId") Integer groupId);

    /**
     * 获取群聊tag的id
     * @param groupId 群聊id
     * @return tag_id 集合
     */
    @Select("SELECT tag_id FROM Tag, GroupTag WHERE GroupTag.group_id=#{groupId} AND GroupTag.tag_id=Tag.tag_id")
    List<Integer> getGroupTagIds(@Param("groupId") Integer groupId);
}
