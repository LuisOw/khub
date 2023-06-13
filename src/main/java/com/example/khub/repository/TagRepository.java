package com.example.khub.repository;

import com.example.khub.model.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface TagRepository extends JpaRepository<Tag, Long> {

    Set<Tag> findAllByName(String name);
}
