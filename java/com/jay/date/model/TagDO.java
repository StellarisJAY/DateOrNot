package com.jay.date.model;

/**
 * @author Jay
 */
public class TagDO {
    private Integer tag_id;
    private String content;

    public TagDO(Integer tag_id, String content) {
        this.tag_id = tag_id;
        this.content = content;
    }

    public Integer getTag_id() {
        return tag_id;
    }

    public void setTag_id(Integer tag_id) {
        this.tag_id = tag_id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "TagDO{" +
                "tag_id=" + tag_id +
                ", content='" + content + '\'' +
                '}';
    }
}
