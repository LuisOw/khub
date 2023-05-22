package com.example.khub.service;

import com.example.khub.error.exceptions.ImageNotFoundException;
import com.example.khub.model.Image;
import com.example.khub.repository.ImageRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@Slf4j
public class ImageService {

    @Autowired
    private ImageRepository repository;

    public List<Image> findAll() {
        return repository.findAll();
    }

    public Image findById(UUID id) {
        return repository.findById(id)
                .orElseThrow(() -> new ImageNotFoundException(String.format(ImageNotFoundException.MESSAGE, id)));
    }

    public Image createImage(Image image) {
        return repository.saveAndFlush(image.id(UUID.randomUUID()));
    }

}
