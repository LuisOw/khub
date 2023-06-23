package com.example.khub.controllers;

import com.example.khub.dto.ImageDto;
import com.example.khub.model.Image;
import com.example.khub.model.Tag;
import com.example.khub.service.ImageService;
import com.example.khub.service.TagService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/images")
@Slf4j
public class ImageController {
    @Autowired
    private ImageService imageService;

    @Autowired
    private TagService tagService;

    @Autowired
    private ModelMapper modelMapper;

    @GetMapping
    public ResponseEntity<List<ImageDto>> getImages() {
        log.info("Get request to images");
        var dtoList = imageService.findAll()
                .stream()
                .map(image -> modelMapper.map(image, ImageDto.class))
                .map(imageService::addPresignedUrl)
                .toList();
        return ResponseEntity.ok(dtoList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ImageDto> getImage(@PathVariable UUID id) {
        log.info("Get request to image with id = {}", id);
        Image image = imageService.findById(id);
        return ResponseEntity.ok(modelMapper.map(image, ImageDto.class));
    }

    @PostMapping
    public ResponseEntity<ImageDto> postImage(@RequestParam("file") MultipartFile file,
                                              @RequestParam("description") String description,
                                              @RequestParam("tags") List<Long> tagsIds) {
        log.info("Post request to images");
        List<Tag> tags = tagService.findAllByIds(tagsIds);
        ImageDto returnImage = modelMapper.map(imageService.createImage(description, tags, file), ImageDto.class);
        return new ResponseEntity<>(returnImage, HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteImage(@PathVariable UUID id) {
        log.info("Deleting image with id {}", id);
        imageService.deleteImage(id);
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }
}