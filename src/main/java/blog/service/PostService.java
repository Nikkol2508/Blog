package blog.service;

import blog.api.response.PostResponse;
import blog.dto.PostsDTO;
import blog.dto.PostsSelectedDTO;
import blog.dto.UserDTO;
import blog.model.ModerationStatus;
import blog.model.Post;
import blog.repositorys.PostRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PostService {

    private final PostRepository postRepository;
    public static final int MAX_ANNOUNCE_LENGTH = 120;

    public PostService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    public PostResponse getPosts(Integer offset, Integer limit, String mode) {
        PostResponse postResponse = new PostResponse();

        List<PostsDTO> posts = new ArrayList<>();
        PostsSelectedDTO res = getActivePostsForPage(offset, limit, mode);
        int count = res.getCount();

        if(count != 0) {
            for (Post post : res.getPosts()) {
                posts.add(convertToPostDTO(post));
            }
        }
        postResponse.setCount(count);
        postResponse.setPosts(posts);
        return postResponse;
    }

    public PostsSelectedDTO getActivePostsForPage(Integer offset, Integer limit, String mode) {
        long time = System.currentTimeMillis();
        int count = postRepository.countActiveByIsActiveAndModerationStatusAndTimeLessThan((byte) 1, ModerationStatus.ACCEPTED, time);

        Pageable pageable;
        List<Post> activePostsForPage;
        switch (mode) {
            case "early":
                pageable = PageRequest.of(offset / limit, limit, Sort.by("time").ascending());
                activePostsForPage = postRepository.findAllActiveByIsActiveAndModerationStatusAndTimeLessThan((byte) 1, ModerationStatus.ACCEPTED, time, pageable);
                break;
            case "popular":
                pageable = PageRequest.of(offset / limit, limit);
                activePostsForPage = postRepository.findAllActiveSortComments(time, pageable);
                //activePostsForPage = postRepository.findAllActiveSortComments(time, pageable);
                //activePostsForPage = postRepository.findAllActiveSortComments((byte) 1, "ACCEPTED", time, pageable);
                break;
            case "best":
                pageable = PageRequest.of(offset / limit, limit);
                activePostsForPage = postRepository.findAllActiveSortVoters((byte) 1, "ACCEPTED", time, pageable);
                break;
            case "recent":
            default:
                pageable = PageRequest.of(offset / limit, limit, Sort.by("time").descending());
                activePostsForPage = postRepository.findAllActiveByIsActiveAndModerationStatusAndTimeLessThan((byte) 1, ModerationStatus.ACCEPTED, time, pageable);
        }

        PostsSelectedDTO res = new PostsSelectedDTO();
        res.setPosts(activePostsForPage);
        res.setCount(count);
        return res;
    }

    public PostsDTO convertToPostDTO(Post post) {
        PostsDTO postsDTO = new PostsDTO();
        postsDTO.setId(post.getId());
        postsDTO.setTimestamp(post.getTime());
        UserDTO userDTO = new UserDTO();
        userDTO.setId(post.getUserId());
        userDTO.setName(post.getUser().getName());
        postsDTO.setUser(userDTO);
        postsDTO.setTitle(post.getTitle());
        int lengthAnnounce = Math.min(post.getText().length(), MAX_ANNOUNCE_LENGTH);
        String announce = post.getText().substring(0, lengthAnnounce) + "...";
        postsDTO.setAnnounce(announce.replaceAll("\\<[^>]*>", ""));
        postsDTO.setLikeCount(post.getLikeCount());
        postsDTO.setDislikeCount(post.getDisLikeCount());
        postsDTO.setCommentCount(post.getCommentCount());
        postsDTO.setViewCount(post.getViewCount());
        return postsDTO;
    }

}