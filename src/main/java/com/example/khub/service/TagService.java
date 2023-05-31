package com.example.khub.service;

import com.example.khub.model.Tag;
import com.example.khub.repository.TagRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
@Slf4j
public class TagService {

    @Autowired
    private TagRepository repository;

    public Set<Tag> findAllByName(String name) {
        log.info("Searching for ids {}", name);
        return repository.findAllByName(name);
    }

    public List<Tag> findAllByIds(List<Integer> ids) {
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
