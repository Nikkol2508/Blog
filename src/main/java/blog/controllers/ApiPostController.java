package blog.controllers;

import blog.api.response.PostResponse;
import blog.api.response.TagResponse;
import blog.service.PostService;
import blog.service.TagService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class ApiPostController {

    private final PostService postService;
    private final TagService tagService;

    public ApiPostController(PostService postService, TagService tagService) {
        this.postService = postService;
        this.tagService = tagService;
    }

    @GetMapping("/post")
    private PostResponse getPosts() {
        return postService.getPosts();
    }

    @GetMapping("/tag")
    private TagResponse getTags() {
        return tagService.getTags();
    }
}
