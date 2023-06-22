package com.example.khub.controllers;

import com.example.khub.error.exceptions.ImageNotFoundException;
import com.example.khub.model.Image;
import com.example.khub.repository.ImageRepository;
import com.example.khub.service.ImageService;
import com.example.khub.service.ImageStore;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.oauth2.core.user.OAuth2UserAuthority;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.oauth2Login;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
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

    @MockBean
    private ImageStore imageStore;

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
    public void getImageWithNonexistentId() throws Exception {
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
    public void postImageAuthenticated() throws Exception {
        MockMultipartFile file = new MockMultipartFile("file", "hello.txt",
                MediaType.MULTIPART_FORM_DATA_VALUE, "Testing with String".getBytes());
        Map<String, Object> map = new HashMap<>();
        map.put("login", "user");
        String description = "Description of image";
        mockMvc.perform(multipart("/api/images")
                        .file(file)
                        .param("description", description)
                        .param("tags", "1", "2")
                        .with(oauth2Login().authorities(new OAuth2UserAuthority(map)))
                )
                .andExpect(status().isCreated())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));

        List<Image> allImages = imageRepository.findAll();
        assertThat(allImages.size()).isEqualTo(1);
        Image createdImage = allImages.get(0);
        assertThat(createdImage.getDescription()).isEqualTo(description);
    }

    @Test
    public void postImageUnauthenticated() throws Exception {
        MockMultipartFile file = new MockMultipartFile("file", "hello.txt",
                MediaType.MULTIPART_FORM_DATA_VALUE, "Testing with String".getBytes());
        String description = "Description of image";
        mockMvc.perform(multipart("/api/images")
                        .file(file)
                        .param("description", description)
                        .param("tags", "1", "2")
                )
                .andExpect(status().isFound());

        List<Image> allImages = imageRepository.findAll();
        assertThat(allImages.size()).isZero();
    }
}
