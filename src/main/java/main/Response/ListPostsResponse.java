package main.Response;

import main.Model.Post;
import main.Response.dto.PostResponse;

import java.util.List;

public class ListPostsResponse {
    private int count;
    private List<PostResponse> posts;

    public ListPostsResponse(int count, List<PostResponse> posts) {
        this.count = count;
        this.posts = posts;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public List<PostResponse> getPosts() {
        return posts;
    }

    public void setPosts(List<PostResponse> posts) {
        this.posts = posts;
    }
}
