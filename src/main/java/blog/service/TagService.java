package blog.service;

import blog.api.response.TagResponse;
import blog.dto.TagDTO;
import blog.model.ModerationStatus;
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
        TagResponse tagResponse = new TagResponse();
        List<TagDTO> tagList = new ArrayList<>();
        Iterable<Tag> tags = tagRepository.findAll();
        tags.forEach(tag -> {
            TagDTO tagDTO = new TagDTO();
            tagDTO.setName(tag.getName());
            tagDTO.setWeight(getWeight(tag.getName()));
            tagList.add(tagDTO);
        });
        tagResponse.setTags(tagList);
        return tagResponse;
    }

    public double getWeight(String tag) {
        String tagPopular = postRepository.getPopularTagName();
        int postCount = postRepository.countActiveByIsActiveAndModerationStatusAndTimeLessThan((byte) 1, ModerationStatus.ACCEPTED, System.currentTimeMillis());
        int countPostsWithTag = postRepository.postsWithTag(tag);
        double dWeightTag = (double) countPostsWithTag / postCount;
        double dWeightMax = (double) postRepository.postsWithTag(tagPopular) / postCount;
        double k = (double) 1 / dWeightMax;

        return dWeightTag * k ;
    }
}

