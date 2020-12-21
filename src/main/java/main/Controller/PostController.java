package main.Controller;

import main.Response.ListPostsResponse;
import main.Response.dto.CurrentPostResponse;
import main.Service.PostService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/post")
public class PostController {

    private PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping("")
    public ResponseEntity<ListPostsResponse> getPosts(@RequestParam int offset,
                                                     @RequestParam int limit,
                                                     @RequestParam String mode)
    {
        switch (mode){
        case "recent": return new ResponseEntity<>(postService.getPageablePostsByTimeDesc(offset, limit), HttpStatus.OK);
        case "early": return new ResponseEntity<>(postService.getPageablePostsByTimeAsc(offset, limit), HttpStatus.OK);
        case "popular": return new ResponseEntity<>(postService.getPageablePostsByComments(offset, limit), HttpStatus.OK);
        case "best": return new ResponseEntity<>(postService.getPageablePostsByLikes(offset, limit), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }


    @GetMapping("/{id}")
    public ResponseEntity<CurrentPostResponse> getOpenPost(@PathVariable int id)
    {
        return postService.getCurrentPost(id);
    }

    // GET /api/post/search/

    // GET /api/post/byTag

    // GET /api/post/moderation

    // GET /api/post/my

    // GET /api/post/{ID}

    // POST /api/post

    // POST /api/image

    // PUT /api/post/{ID}

    // POST /api/comment/

    // GET /api/tag/

    // POST /api/moderation

    // GET /api/calendar/

    // GET /api/statistics/my

    // GET /api/statistics/all

    // POST /api/post/like

    // POST /api/post/dislike



}
