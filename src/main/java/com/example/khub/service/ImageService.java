package com.example.khub.service;

import com.example.khub.error.exceptions.ImageNotFoundException;
import com.example.khub.model.Image;
import com.example.khub.model.Tag;
import com.example.khub.repository.ImageRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@Slf4j
public class ImageService {

    @Autowired
    private ImageRepository repository;

    @Autowired
    private ImageStore imageStore;

    public List<Image> findAll() {
        return repository.findAll();
    }

    public Image findById(UUID id) {
        return repository.findById(id)
                .orElseThrow(() -> new ImageNotFoundException(String.format(ImageNotFoundException.MESSAGE, id)));
    }

    public Image createImage(String description, List<Tag> tags, MultipartFile file) {
        Image image = new Image(description, tags);
        Map<String, String> metadata = new HashMap<>();
        metadata.put(HttpHeaders.CONTENT_TYPE, file.getContentType());
        metadata.put(HttpHeaders.CONTENT_LENGTH, String.valueOf(file.getSize()));
        try {
            imageStore.upload(image.getId().toString(), metadata, file.getInputStream());
        } catch (IOException e) {
            throw new IllegalStateException("Failed to upload to S3", e);
        }
        return repository.saveAndFlush(image);
    }

}
