package blog.api.response;

import blog.dto.TagDTO;

import java.util.List;

public class TagResponse {

    private List<TagDTO> tags;

    public TagResponse() {
    }

    public List<TagDTO> getTags() {
        return tags;
    }

    public void setTags(List<TagDTO> tags) {
        this.tags = tags;
    }
}
