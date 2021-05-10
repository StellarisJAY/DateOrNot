package com.jay.date.mapper;

import com.jay.date.model.MatchedUserDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

/**
 * @author Jay
 */
@Mapper
@Repository
public interface MatchingMapper {

    /**
     * 获取匹配到用户的信息
     * @param userId 用户id
     * @return MatchedUserDTO
     */
    @Select("SELECT user_id, user_name, birthday, sex, introduction, head_image_url FROM User WHERE user_id=#{userId}")
    MatchedUserDTO getMatchedUserInfo(@Param("userId") Integer userId);
}
