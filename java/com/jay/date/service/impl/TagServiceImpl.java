package com.jay.date.service.impl;

import com.jay.date.mapper.TagMapper;
import com.jay.date.model.TagDO;
import com.jay.date.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Jay
 */
@Service
public class TagServiceImpl implements TagService {

    private final TagMapper tagMapper;

    private static final int DEFAULT_RANDOM_LIMIT = 15;
    private static final int TAG_CONTENT_MAX_LENGTH = 6;
    @Autowired
    public TagServiceImpl(TagMapper tagMapper) {
        this.tagMapper = tagMapper;
    }

    @Override
    public List<TagDO> getRandomTags(Integer limit) {
        if(limit == null || limit <= 0 || limit > DEFAULT_RANDOM_LIMIT){
            limit = DEFAULT_RANDOM_LIMIT;
        }
        return tagMapper.randomTags(limit);
    }

    @Override
    public Integer saveNewTag(String content) {
        if(content == null || content.length() == 0 || content.length() > TAG_CONTENT_MAX_LENGTH){
            throw new IllegalArgumentException("添加新标签参数错误");
        }
        return tagMapper.saveTag(content);
    }
}
