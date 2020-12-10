package main.Controller;

import main.Model.Post;
import main.Response.ListPostsResponse;
import main.Response.dto.PostResponse;
import main.Service.PostService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;

@RestController
@RequestMapping("/api/post")
public class PostController {

    private PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping("")
    public ListPostsResponse getPosts(@RequestParam int offset, @RequestParam int limit, @RequestParam String mode)
    {
        return postService.getPostsByTime();

        /*ListPostsResponse listPostsResponse = new ListPostsResponse();
        listPostsResponse.setCount(0);
        listPostsResponse.setPosts(new ArrayList<PostResponse>());
        return listPostsResponse;*/
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
