package com.example.khub.service;

import com.example.khub.model.Tag;
import com.example.khub.repository.TagRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


@ExtendWith(SpringExtension.class)
@SpringBootTest
public class TagServiceIT {

    @Autowired
    private TagRepository tagRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private TagService service;

    @BeforeEach
    void cleanDb() {
        tagRepository.deleteAllInBatch();
    }

    @Test
    void checkSave() {
        Tag tag1 = new Tag().name("tag 1");
        Tag tag2 = new Tag().name("tag 2");
        service.saveAllTags(List.of(tag1, tag2));
        var allTags = tagRepository.findAll();
        assertThat(allTags.size()).isEqualTo(2);
        assertThat(allTags.stream().anyMatch(t -> t.getName().equals(tag1.getName()))).isTrue();
        assertThat(allTags.stream().anyMatch(t -> t.getName().equals(tag2.getName()))).isTrue();
    }

}
