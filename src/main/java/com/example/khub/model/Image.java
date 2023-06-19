package com.example.khub.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;
import java.util.Set;
import java.util.UUID;


@Data
@Entity
@Table(name = "image")
public class Image {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(name = "description")
    private String description;

    @NotNull
    @Column(name = "type")
    private ImageType type = ImageType.ORIGINAL;

    @ManyToMany
    @JoinTable(name = "image_tag",
            joinColumns = @JoinColumn(name = "image_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id"))
    private Set<Tag> tags;

    public Image(String description, List<Tag> tags) {
        this.id = UUID.randomUUID();
        this.description = description;
        this.tags.addAll(tags);
    }

    public Image id(UUID id) {
        this.id = id;
        return this;
    }

    public Image description(String description) {
        this.description = description;
        return this;
    }

    public Image tags(List<Tag> tags) {
        this.tags = Set.copyOf(tags);
        return this;
    }

    public void addTags(List<Tag> tags) {
        this.tags.addAll(tags);
    }
}
