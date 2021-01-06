package main.Response;

import main.dto.PostDto;

import java.util.List;

public class ListPostsResponse {
    private int count;
    private List<PostDto> posts;

    public ListPostsResponse(int count, List<PostDto> posts) {
        this.count = count;
        this.posts = posts;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public List<PostDto> getPosts() {
        return posts;
    }

    public void setPosts(List<PostDto> posts) {
        this.posts = posts;
    }
}
