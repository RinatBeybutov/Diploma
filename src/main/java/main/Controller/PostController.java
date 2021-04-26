package main.Controller;

import java.security.Principal;
import main.Request.RequestDecisionModeration;
import main.Request.RequestPost;
import main.Request.RequestPostLike;
import main.Response.ListPostsResponse;
import main.Response.SinglePostResponse;
import main.Service.PostService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/post")
public class PostController {

    private PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping("")
    //@PreAuthorize("hasAuthority('user:write')")
    public ResponseEntity<ListPostsResponse> getPosts(@RequestParam(defaultValue = "0") int offset,
        @RequestParam(defaultValue = "10") int limit,
        @RequestParam(defaultValue = "recent") String mode) {
        return postService.getMainPagePosts(offset, limit, mode);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SinglePostResponse> getOpenPost(@PathVariable int id) {
        return postService.getSinglePost(id);
    }

    @GetMapping("/byTag")
    public ResponseEntity<ListPostsResponse> getPostsByTag(
        @RequestParam(defaultValue = "0") int offset,
        @RequestParam(defaultValue = "10") int limit,
        @RequestParam String tag) {
        return postService.getPostsByTag(offset, limit, tag);
    }

    @GetMapping("/byDate")
    public ResponseEntity<ListPostsResponse> getPostsByDate(
        @RequestParam(defaultValue = "0") int offset,
        @RequestParam(defaultValue = "10") int limit,
        @RequestParam String date) {
        return postService.getPostsByDate(offset, limit, date);
    }

    @GetMapping("/search")
    //@PreAuthorize("hasAuthority('user:moderate')")
    public ResponseEntity<ListPostsResponse> getPostsByQuery(
        @RequestParam(defaultValue = "0") int offset,
        @RequestParam(defaultValue = "10") int limit,
        @RequestParam(defaultValue = "") String query) {
        return postService.getPostsByQuery(offset, limit, query);
    }

    @GetMapping("/my")
    public ResponseEntity<ListPostsResponse> getMyPosts(@RequestParam(defaultValue = "0") int offset,
        @RequestParam(defaultValue = "10") int limit,
        @RequestParam(defaultValue = "inactive") String status,
        Principal principal) {
        return postService.getMyPosts(offset, limit, status, principal);
    }

    @PostMapping("")
    public ResponseEntity<?> post(@RequestBody RequestPost requestPost, Principal principal)
    {
        return postService.post(requestPost, principal);
    }

    @PostMapping("/like")
    @PreAuthorize("hasAuthority('user:write')")
    public ResponseEntity<?> like(@RequestBody RequestPostLike requestPostLike, Principal principal)
    {
        return postService.like(requestPostLike, principal);
    }

    @PostMapping("/dislike")
    @PreAuthorize("hasAuthority('user:write')")
    public ResponseEntity<?> dislike(@RequestBody RequestPostLike requestPostLike, Principal principal)
    {
        return postService.dislike(requestPostLike, principal);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> editPost(@PathVariable int id, @RequestBody RequestPost requestPost, Principal principal)
    {
        return postService.editPost(id, requestPost, principal);
    }

    @GetMapping("/moderation")
    public ResponseEntity<?>  getModerationPosts(Principal principal,
        @RequestParam(defaultValue = "0") int offset,
        @RequestParam(defaultValue = "10") int limit,
        @RequestParam(defaultValue = "new") String status)
    {
        return postService.getModerationPosts(principal, offset, limit, status);
    }


}
