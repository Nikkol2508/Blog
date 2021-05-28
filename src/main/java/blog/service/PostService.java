package blog.service;

import blog.api.response.PostResponse;
import blog.dto.PostsDTO;
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

        List<PostsDTO> posts = new ArrayList<>();
        Object[] res = getActivePostsForPage(offset, limit, mode);
        int count = (int) res[1];

        if(count != 0) {
            for (Post post : (Page<Post>)res[0]) {
                posts.add(post.convertToPostDTO(post));
            }
        }
        postResponse.setCount(count);
        postResponse.setPosts(posts);
        return postResponse;
    }

    public Object[] getActivePostsForPage(Integer offset, Integer limit, String mode) {
        long time = System.currentTimeMillis();
        int count = 0;

        Pageable pageable = PageRequest.of(offset / limit, limit, Sort.by("time").descending());
        Page<Post> activePostsForPage = postRepository.findAllActiveByIsActiveAndModerationStatusAndTimeLessThan((byte) 1, ModerationStatus.ACCEPTED, time, pageable);
        switch (mode) {
            case "early":
                pageable = PageRequest.of(offset / limit, limit, Sort.by("time").ascending());
                activePostsForPage = postRepository.findAllActiveByIsActiveAndModerationStatusAndTimeLessThan((byte) 1, ModerationStatus.ACCEPTED, time, pageable);
                count = postRepository.countActiveByIsActiveAndModerationStatusAndTimeLessThan((byte) 1, ModerationStatus.ACCEPTED, time);
                break;
            case "popular":
                pageable = PageRequest.of(offset / limit, limit);
                activePostsForPage = postRepository.findAllActiveSortComments((byte) 1, "ACCEPTED", time, pageable);
                count = postRepository.countSelected((byte) 1, "ACCEPTED", time);
                break;
            case "best":
                //sort = Sort.by("postVoters").ascending();  //необходимо реализовать !!!!
                break;
            case "recent":

            default:
                pageable = PageRequest.of(offset / limit, limit, Sort.by("time").descending());
                activePostsForPage = postRepository.findAllActiveByIsActiveAndModerationStatusAndTimeLessThan((byte) 1, ModerationStatus.ACCEPTED, time, pageable);
                count = postRepository.countActiveByIsActiveAndModerationStatusAndTimeLessThan((byte) 1, ModerationStatus.ACCEPTED, time);

        }

        Object[] res = new Object[2];
        res[0] = activePostsForPage;
        res[1] = count;
        return res;
    }

}