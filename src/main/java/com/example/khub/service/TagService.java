package com.example.khub.service;

import com.example.khub.model.Tag;
import com.example.khub.repository.TagRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class TagService {

    @Autowired
    private TagRepository repository;

    public List<Tag> findAllByIds(List<Long> ids) {
        log.info("Searching for tags with ids {}", ids);
        return repository.findAllById(ids);
    }

    public void saveAllTags(List<Tag> tags) {
        repository.saveAll(tags);
    }

    public Tag saveTag(Tag tag) {
        log.info("Saving tag = {}", tag);
        return repository.save(tag);
    }

}
