package blog.service;

import blog.api.response.TagResponse;
import blog.dto.TagDTO;
import blog.model.Tag;
import blog.model.TagRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TagService {

    private final TagRepository tagRepository;

    public TagService(TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }

    public TagResponse getTags() {
        TagResponse tagResponse = new TagResponse();
        List<TagDTO> tagList = new ArrayList<>();
        Iterable<Tag> tags = tagRepository.findAll();
        tags.forEach(tag -> {
            TagDTO tagDTO = new TagDTO();
            tagDTO.setName(tag.getName());
            tagDTO.setWeight(0.26); // необходимо реализовать расчёт значения
            tagList.add(tagDTO);
        });
        tagResponse.setTags(tagList);
        return tagResponse;
    }
}
