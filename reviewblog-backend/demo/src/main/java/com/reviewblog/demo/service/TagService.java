package com.reviewblog.demo.service;


import com.reviewblog.demo.entity.Tag;
import com.reviewblog.demo.repository.TagRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class TagService {

    private TagRepository tagRepository;

    public List<Tag> getAll() {
        return tagRepository.findAll();
    }

    public Set<Tag> findOrCreateTags(List<String> names) {
        Set<Tag> tags = new HashSet<>();
        if (names == null) return tags;
        for (String name : names) {
            String trimmed = name.trim();
            if (trimmed.isEmpty()) continue;
            Tag tag = tagRepository.findByName(trimmed)
                    .orElseGet(() -> tagRepository.save(Tag.builder().name(trimmed).build()));
            tags.add(tag);
        }
        return tags;
    }
}
