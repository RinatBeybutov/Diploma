package main.Response;

import main.Model.Post;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ListPostsResponse {
    private int count;
    private List<Post> posts;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public List<Post> getPosts() {
        return posts;
    }

    public void setPosts(List<Post> posts) {
        this.posts = posts;
    }
}
