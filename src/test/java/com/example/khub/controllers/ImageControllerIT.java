package com.example.khub.controllers;

import com.example.khub.dto.ImageDto;
import com.example.khub.error.exceptions.ImageNotFoundException;
import com.example.khub.model.Image;
import com.example.khub.repository.ImageRepository;
import com.example.khub.service.ImageService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public class ImageControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ImageRepository imageRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ImageService service;

    @BeforeEach
    @AfterEach
    void cleanDb() {
        imageRepository.deleteAllInBatch();
    }

    @Test
    public void getImages() throws Exception {
        Image image1 = new Image().id(UUID.randomUUID()).description("Test");
        image1 = imageRepository.saveAndFlush(image1);

        mockMvc.perform(get("/api/images")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("[*].id").value(image1.getId().toString()))
                .andExpect(jsonPath("[*].description").value(image1.getDescription()));
    }

    @Test
    public void getImageById() throws Exception {
        Image image1 = new Image().id(UUID.randomUUID()).description("Test");
        image1 = imageRepository.saveAndFlush(image1);

        mockMvc.perform(get("/api/images/{id}", image1.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(image1.getId().toString()))
                .andExpect(jsonPath("$.description").value(image1.getDescription()));
    }

    @Test
    public void getImageByNonexistentId() throws Exception {
        Image image1 = new Image().id(UUID.randomUUID()).description("Test");
        imageRepository.saveAndFlush(image1);
        UUID randomId = UUID.randomUUID();
        mockMvc.perform(get("/api/images/{id}", randomId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message")
                        .value(String.format(ImageNotFoundException.MESSAGE, randomId)));
    }

    @Test
    public void postImage() throws Exception {
        ImageDto imageDto = ImageDto.fromSource(new Image().description("Description"));
        imageDto.setTags(List.of(1, 2));
        String inputJson = objectMapper.writeValueAsString(imageDto);
        mockMvc.perform(post("/api/images")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(inputJson))
                .andExpect(status().isCreated())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));

        List<Image> allImages = imageRepository.findAll();
        assertThat(allImages.size()).isEqualTo(1);
        Image createdImage = allImages.get(0);
        assertThat(createdImage.getDescription()).isEqualTo(imageDto.getDescription());
    }
}
