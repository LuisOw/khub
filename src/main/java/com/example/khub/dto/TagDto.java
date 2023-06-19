package com.example.khub.dto;

import com.example.khub.model.Tag;
import lombok.Data;
import org.springframework.beans.BeanUtils;

@Data
public class TagDto {

    private String name;
    private Long id;

    public static Tag toSource(TagDto tagDto) {
        Tag tag = new Tag();
        BeanUtils.copyProperties(tagDto, tag);
        return tag;
    }
}
