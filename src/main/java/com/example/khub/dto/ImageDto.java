package com.example.khub.dto;

import com.example.khub.model.Image;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.util.List;
import java.util.UUID;

@Data
public class ImageDto {
    private UUID id;
    private String description;
    private List<TagDto> tags;
    private String presignedUrl;

    public static ImageDto fromSource(Image image) {
        ImageDto imageDto = new ImageDto();
        BeanUtils.copyProperties(image, imageDto);
        return imageDto;
    }

    public static Image toSource(ImageDto imageDto) {
        Image image = new Image();
        BeanUtils.copyProperties(imageDto, image);
        return image;
    }
}
