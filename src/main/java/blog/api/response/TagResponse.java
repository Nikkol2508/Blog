package blog.api.response;

import blog.dto.TagDTOInter;

import java.util.List;

public class TagResponse {

    private List<TagDTOInter> tags;

    public TagResponse() {
    }

    public List<TagDTOInter> getTags() {
        return tags;
    }

    public void setTags(List<TagDTOInter> tags) {
        this.tags = tags;
    }
}
