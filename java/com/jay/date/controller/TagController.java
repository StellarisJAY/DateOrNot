package com.jay.date.controller;

import com.jay.date.model.TagDO;
import com.jay.date.service.TagService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author Jay
 */
@RestController
@CrossOrigin
public class TagController {
    private final TagService tagService;
    private final Logger logger;

    @Autowired
    public TagController(TagService tagService) {
        this.tagService = tagService;
        this.logger = LoggerFactory.getLogger(this.getClass());
    }

    @GetMapping("/tag/random")
    public List<TagDO> getRandomTags(@RequestParam("limit") Integer limit){
        return tagService.getRandomTags(limit);
    }

    @PostMapping("/tag")
    public Boolean saveNewTag(String content){
        try{
            return tagService.saveNewTag(content) > 0;
        }catch (IllegalArgumentException e){
            logger.info("添加新标签出错：" + e.getMessage());
            return false;
        }
    }
}
