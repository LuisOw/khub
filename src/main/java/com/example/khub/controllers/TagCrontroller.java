package com.example.khub.controllers;

import com.example.khub.dto.TagDto;
import com.example.khub.service.TagService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/tag")
@Slf4j
public class TagCrontroller {

    @Autowired
    private TagService service;

    @Autowired
    private ModelMapper modelMapper;

    @PostMapping
    public ResponseEntity<TagDto> postTag(@RequestBody TagDto tagDto) {
        log.info("Request to post new tag with = {}", tagDto);
        TagDto tagToReturn = modelMapper.map(service.saveTag(TagDto.toSource(tagDto)), TagDto.class);
        return new ResponseEntity<>(tagToReturn, HttpStatus.CREATED);
    }
}
