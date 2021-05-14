package blog.service;

import blog.api.response.PostResponse;
import blog.dto.PostsDTO;
import blog.dto.UserDTO;
import blog.model.Post;
import blog.model.PostRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PostService {

    private final PostRepository postRepository;

    public PostService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    public PostResponse getPosts() {
        PostResponse postResponse = new PostResponse();
        int count = (int) postRepository.count();
        Iterable<Post> postIterable = postRepository.findAll();
        List<PostsDTO> posts = new ArrayList<>();
        for (Post post : postIterable) {
            PostsDTO postsDTO = new PostsDTO();
            postsDTO.setId(post.getId());
            postsDTO.setTimestamp(post.getTime());
            UserDTO userDTO = new UserDTO();
            userDTO.setId(post.getUserId());
            userDTO.setName(post.getUser().getName());
            postsDTO.setUser(userDTO);
            postsDTO.setTitle(post.getTitle());
            int lengthAnnounce = (post.getText().length() > 120) ? 120 : post.getText().length();
            postsDTO.setAnnounce(post.getText().substring(0, lengthAnnounce));
            postsDTO.setLikeCount(post.getLikeCount());
            postsDTO.setDislikeCount(post.getDisLikeCount());
            postsDTO.setCommentCount(post.getCommentCount());
            postsDTO.setViewCount(post.getViewCount());
            posts.add(postsDTO);
        }
        postResponse.setCount(count);
        postResponse.setPosts(posts);
        return postResponse;
    }
}
