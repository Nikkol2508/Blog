package blog.controllers;

import blog.api.response.PostResponse;
import blog.api.response.TagResponse;
import blog.service.PostService;
import blog.service.TagService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
    private ResponseEntity<PostResponse> getPosts(@RequestParam Integer offset, @RequestParam Integer limit, @RequestParam String mode) {
        return new ResponseEntity<>(postService.getPosts(offset, limit, mode), HttpStatus.OK);
    }

    @GetMapping("/tag")
    private ResponseEntity<TagResponse> getTags() {
        return new ResponseEntity<>(tagService.getTags(), HttpStatus.OK);
    }

    @GetMapping("/post/search")
    private ResponseEntity<PostResponse> getSearchPosts(@RequestParam Integer offset, @RequestParam Integer limit, @RequestParam String query) {
        return new ResponseEntity<>(postService.getSearchPosts(offset, limit, query), HttpStatus.OK);
    }
}
