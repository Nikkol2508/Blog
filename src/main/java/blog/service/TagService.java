package blog.service;

import blog.api.response.TagResponse;
import blog.repositorys.TagRepository;
import org.springframework.stereotype.Service;

@Service
public class TagService {

    private final TagRepository tagRepository;

    public TagService(TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }

    public TagResponse getTags() {
        TagResponse tagResponse = new TagResponse();
        tagResponse.setTags(tagRepository.getWeightTag());
        return tagResponse;
    }

}

