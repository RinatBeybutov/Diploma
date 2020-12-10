package main.Service;

import main.Model.Post;
import main.Repository.PostRepository;
import main.Response.ListPostsResponse;
import main.Response.dto.PostResponse;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class PostService {

    private PostRepository postRepository;

    public ListPostsResponse getPostsByTime()
    {

     Post post = postRepository.findById(1).get();

//        System.out.println("\n  " +post.getText() + "   \n");
     //   ArrayList<Post> list = (ArrayList<Post>) postRepository.findAll();
        //list.stream().forEach(e -> System.out.println(e.getId() + " " + e.getText()));

        ListPostsResponse listPostsResponse = new ListPostsResponse();
        listPostsResponse.setPosts(new ArrayList<PostResponse>() );
        return listPostsResponse;
    }

}
