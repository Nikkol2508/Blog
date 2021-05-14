package blog.api.response;

import blog.dto.PostsDTO;

import java.util.List;

public class PostResponse {

    private int count;
    private List<PostsDTO> posts;

    public List<PostsDTO> getPosts() {
        return posts;
    }

    public void setPosts(List<PostsDTO> posts) {
        this.posts = posts;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

}
