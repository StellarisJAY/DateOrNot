package com.jay.date.service;

import com.jay.date.model.TagDO;

import java.util.List;

/**
 * @author Jay
 */
public interface TagService {
    /**
     * 随机获取一定数量的标签
     * @param limit 数量
     * @return 随机标签
     */
    List<TagDO> getRandomTags(Integer limit);

    /**
     * 添加用户自定义标签
     * @param content 标签内容
     * @return 添加状态
     */
    Integer saveNewTag(String content);
}
