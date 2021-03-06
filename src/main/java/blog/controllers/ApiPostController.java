package blog.controllers;

import blog.api.response.PostResponse;
import blog.api.response.TagResponse;
import blog.dto.PostsDTO;
import blog.service.PostService;
import blog.service.TagService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    private ResponseEntity<PostResponse> getPosts(@RequestParam Integer offset,
                                                  @RequestParam Integer limit,
                                                  @RequestParam String mode) {
        return new ResponseEntity<>(postService.getPosts(offset, limit, mode), HttpStatus.OK);
    }

    @GetMapping("/post/{ID}")
    private ResponseEntity<PostsDTO> getPost(@PathVariable("ID") int id) {
        return new ResponseEntity<>(postService.getPost(id), HttpStatus.OK);
    }

    @GetMapping("/tag")
    private ResponseEntity<TagResponse> getTags() {
        return new ResponseEntity<>(tagService.getTags(), HttpStatus.OK);
    }

    @GetMapping("/post/search")
    private ResponseEntity<PostResponse> getSearchPosts(@RequestParam Integer offset,
                                                        @RequestParam Integer limit,
                                                        @RequestParam String query) {
        return new ResponseEntity<>(postService.getSearchPosts(offset, limit, query), HttpStatus.OK);
    }

    @GetMapping("/post/byDate")
    private ResponseEntity<PostResponse> getPostsByDate(@RequestParam Integer offset,
                                                        @RequestParam Integer limit,
                                                        @RequestParam String date) {
        return new ResponseEntity<>(postService.getPostsByDate(offset, limit, date), HttpStatus.OK);
    }

    @GetMapping("/post/byTag")
    private ResponseEntity<PostResponse> getPostsByTag(@RequestParam Integer offset,
                                                        @RequestParam Integer limit,
                                                        @RequestParam String tag) {
        return new ResponseEntity<>(postService.getPostsByTag(offset, limit, tag), HttpStatus.OK);
    }
}
