package blog.service;

import blog.api.response.TagResponse;
import blog.dto.TagDTO;
import blog.repositorys.PostRepository;
import blog.model.Tag;
import blog.repositorys.TagRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TagService {

    private final TagRepository tagRepository;
    private final PostRepository postRepository;

    public TagService(TagRepository tagRepository, PostRepository postRepository) {
        this.tagRepository = tagRepository;
        this.postRepository = postRepository;
    }

    public TagResponse getTags() {
        long time = System.currentTimeMillis();
        TagResponse tagResponse = new TagResponse();
        List<TagDTO> tagList = new ArrayList<>();
        Iterable<Tag> tags = tagRepository.findAllActiveTags((byte) 1, "ACCEPTED", time);
        tags.forEach(tag -> {
            TagDTO tagDTO = new TagDTO();
            tagDTO.setName(tag.getName());
            tagDTO.setWeight(postRepository.getWeightTag(tag.getName()));
            tagList.add(tagDTO);
        });
        tagResponse.setTags(tagList);
        return tagResponse;
    }

}

