package blog.service;

import blog.api.response.PostResponse;
import blog.dto.PostsDTO;
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
import java.util.stream.Collectors;

@Service
public class PostService {

    private final PostRepository postRepository;
    public static final int MAX_ANNOUNCE_LENGTH = 120;
    public static final byte IS_ACTIVE = 1;
    public static final String MODERATION_STSTUS = "ACCEPTED";

    public PostService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    public PostResponse getPosts(Integer offset, Integer limit, String mode) {

        long time = System.currentTimeMillis() / 1000;
        int count = postRepository.countActiveByIsActiveAndModerationStatusAndTimeLessThan(IS_ACTIVE, ModerationStatus.ACCEPTED, time);

        List<Post> activePostsForPage = new ArrayList<>();

        if(count != 0) {
            Pageable pageable;
            switch (mode) {
                case "early":
                    pageable = PageRequest.of(offset / limit, limit, Sort.by("time").ascending());
                    activePostsForPage = postRepository.findAllActiveByIsActiveAndModerationStatusAndTimeLessThan(IS_ACTIVE, ModerationStatus.ACCEPTED, time, pageable);
                    break;
                case "popular":
                    pageable = PageRequest.of(offset / limit, limit);
                    activePostsForPage = postRepository.findAllActiveSortComments(IS_ACTIVE, MODERATION_STSTUS, time, pageable);
                    break;
                case "best":
                    pageable = PageRequest.of(offset / limit, limit);
                    activePostsForPage = postRepository.findAllActiveSortVoters(IS_ACTIVE, MODERATION_STSTUS, time, pageable);
                    break;
                case "recent":
                default:
                    pageable = PageRequest.of(offset / limit, limit, Sort.by("time").descending());
                    activePostsForPage = postRepository.findAllActiveByIsActiveAndModerationStatusAndTimeLessThan(IS_ACTIVE, ModerationStatus.ACCEPTED, time, pageable);
            }

        }

        PostResponse postResponse = new PostResponse();
        postResponse.setCount(count);
        postResponse.setPosts(activePostsForPage.stream().map(this::convertToPostDTO).collect(Collectors.toList()));
        return postResponse;
    }

    public PostResponse getSearchPosts(Integer offset, Integer limit, String query) {
        long time = System.currentTimeMillis();
        Pageable pageable = PageRequest.of(offset / limit, limit);
        PostResponse postResponse = new PostResponse();
        List<Post> postsForPage = new ArrayList<>();
        if(query.matches("\\s*")) {
            postResponse = getPosts(offset, limit, "recent");
        }
        else {
            postsForPage = postRepository.searchByRequest(IS_ACTIVE, MODERATION_STSTUS, time, query, pageable);
            postResponse.setPosts(postsForPage.stream().map(this::convertToPostDTO).collect(Collectors.toList()));
            postResponse.setCount(postsForPage.size());
        }
        return postResponse;
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
        postsDTO.setAnnounce(announce.replaceAll("<[^>]*>", ""));
        postsDTO.setLikeCount(post.getLikeCount());
        postsDTO.setDislikeCount(post.getDisLikeCount());
        postsDTO.setCommentCount(post.getCommentCount());
        postsDTO.setViewCount(post.getViewCount());
        return postsDTO;
    }

}