package blog.service;

import blog.api.response.PostResponse;
import blog.dto.PostsDTO;
import blog.dto.UserDTO;
import blog.model.ModerationStatus;
import blog.model.Post;
import blog.model.PostRepository;;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PostService {

    private final PostRepository postRepository;

    public PostService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    public PostResponse getPosts(Integer offset, Integer limit, String mode) {
        PostResponse postResponse = new PostResponse();
        long time = System.currentTimeMillis();
        int count = postRepository.countActiveByIsActiveAndModerationStatusAndTimeLessThan((byte) 1, ModerationStatus.ACCEPTED, time);

        Sort sort = Sort.by("time").descending();
        switch (mode) {
            case "early":
                sort = Sort.by("time").ascending();
                break;
            case "popular":
                sort = Sort.by("postComment").ascending(); //необходимо реализовать !!!!
                break;
            case "best":
                sort = Sort.by("postVoters").ascending();  //необходимо реализовать !!!!
                break;
            case "recent":
                sort = sort;
                break;
            default:
                sort = sort;

        }
        Pageable pageable = PageRequest.of(offset/limit, limit, sort);
        Page<Post> activePostsForPage = postRepository.findAllActiveByIsActiveAndModerationStatusAndTimeLessThan((byte) 1, ModerationStatus.ACCEPTED, time, pageable);

        List<PostsDTO> posts = new ArrayList<>();
        for (Post post : activePostsForPage) {
            PostsDTO postsDTO = new PostsDTO();
            postsDTO.setId(post.getId());
            postsDTO.setTimestamp(post.getTime());
            UserDTO userDTO = new UserDTO();
            userDTO.setId(post.getUserId());
            userDTO.setName(post.getUser().getName());
            postsDTO.setUser(userDTO);
            postsDTO.setTitle(post.getTitle());
            int lengthAnnounce = (post.getText().length() > 120) ? 120 : post.getText().length();
            String announce = post.getText().substring(0, lengthAnnounce) + "...";
            postsDTO.setAnnounce(announce.replaceAll("\\<[^>]*>",""));
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
