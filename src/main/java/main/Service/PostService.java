package main.Service;

import main.Model.Comment;
import main.Model.Post;
import main.Repository.CommentRepository;
import main.Repository.PostRepository;
import main.Response.CalendarResponse;
import main.Response.ListPostsResponse;
import main.Response.dto.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class PostService {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private CommentRepository commentRepository;

    public ListPostsResponse getPageablePostsByTimeDesc(int offset, int itemPerPage)
    {

        Pageable pageable = PageRequest.of(offset / itemPerPage, itemPerPage);
        ArrayList<PostResponse> responseList = new ArrayList<PostResponse>();
        postRepository.findAllByTimeOnPageDesc(pageable).forEach(e ->
                responseList.add(this.convertFromPostToPostResponse(e,
                        postRepository.countLikesOnPost(e.getId()),
                        postRepository.countDislikesOnPost(e.getId()),
                        postRepository.countCommentsOnPost(e.getId()))));
        return new ListPostsResponse(postRepository.countPost(),responseList);
    }

    public ListPostsResponse getPageablePostsByTimeAsc(int offset, int itemPerPage)
    {

        Pageable pageable = PageRequest.of(offset / itemPerPage, itemPerPage);
        ArrayList<PostResponse> responseList = new ArrayList<PostResponse>();
        postRepository.findAllByTimeOnPageAsc(pageable).forEach(e ->
                responseList.add(this.convertFromPostToPostResponse(e,
                        postRepository.countLikesOnPost(e.getId()),
                        postRepository.countDislikesOnPost(e.getId()),
                        postRepository.countCommentsOnPost(e.getId()))));
        return new ListPostsResponse(postRepository.countPost(),responseList);
    }

    public ListPostsResponse getPageablePostsByComments(int offset, int itemPerPage)
    {

        Pageable pageable = PageRequest.of(offset / itemPerPage, itemPerPage);
        ArrayList<PostResponse> responseList = new ArrayList<PostResponse>();
        postRepository.findAllByCommentOnPage(pageable).forEach(e ->
                responseList.add(this.convertFromPostToPostResponse(e,
                        postRepository.countLikesOnPost(e.getId()),
                        postRepository.countDislikesOnPost(e.getId()),
                        postRepository.countCommentsOnPost(e.getId()))));
        return new ListPostsResponse(postRepository.countPost(),responseList);
    }

    public ListPostsResponse getPageablePostsByLikes(int offset, int itemPerPage)
    {

        Pageable pageable = PageRequest.of(offset / itemPerPage, itemPerPage);
        ArrayList<PostResponse> responseList = new ArrayList<PostResponse>();
        postRepository.findAllByLikesOnPage(pageable).forEach(e ->
                responseList.add(this.convertFromPostToPostResponse(e,
                        postRepository.countLikesOnPost(e.getId()),
                        postRepository.countDislikesOnPost(e.getId()),
                        postRepository.countCommentsOnPost(e.getId()))));
        return new ListPostsResponse(postRepository.countPost(),responseList);
    }

    public ListPostsResponse getPostsByTimeDesc()
    {

     ArrayList<Post> list = (ArrayList<Post>) postRepository.findAllByTimeDesc(10);
     ArrayList<PostResponse> responseList = new ArrayList<PostResponse>();
     list.stream().forEach(e ->
         responseList.add(this.convertFromPostToPostResponse(e,
                 postRepository.countLikesOnPost(e.getId()),
                 postRepository.countDislikesOnPost(e.getId()),
                 postRepository.countCommentsOnPost(e.getId()))
     ));

     return new ListPostsResponse(postRepository.countPost(), responseList);
    }

    public ListPostsResponse getPostsByTimeAsc()
    {
        ArrayList<Post> list = (ArrayList<Post>) postRepository.findAllByTimeAsc();
        ArrayList<PostResponse> responseList = new ArrayList<PostResponse>();
        list.stream().forEach(e ->
                responseList.add(this.convertFromPostToPostResponse(e,
                        postRepository.countLikesOnPost(e.getId()),
                        postRepository.countDislikesOnPost(e.getId()),
                        postRepository.countCommentsOnPost(e.getId()))
                ));

        return new ListPostsResponse(postRepository.countPost(), responseList);
    }

    public ListPostsResponse getPostsByComments()
    {
        ArrayList<Post> list = (ArrayList<Post>) postRepository.findAllByComments();
        ArrayList<PostResponse> responseList = new ArrayList<PostResponse>();
        list.stream().forEach(e ->
                responseList.add(this.convertFromPostToPostResponse(e,
                        postRepository.countLikesOnPost(e.getId()),
                        postRepository.countDislikesOnPost(e.getId()),
                        postRepository.countCommentsOnPost(e.getId()))
                ));

        return new ListPostsResponse(postRepository.countPost(), responseList);
    }

    public ListPostsResponse getPostsByLikes()
    {
        ArrayList<Post> list = (ArrayList<Post>) postRepository.findAllByLikes();
        ArrayList<PostResponse> responseList = new ArrayList<PostResponse>();
        list.stream().forEach(e ->
                responseList.add(this.convertFromPostToPostResponse(e,
                        postRepository.countLikesOnPost(e.getId()),
                        postRepository.countDislikesOnPost(e.getId()),
                        postRepository.countCommentsOnPost(e.getId()))
                ));

        return new ListPostsResponse(postRepository.countPost(), responseList);
    }

    public ResponseEntity getCurrentPost(int id) {
        Optional<Post> optionalPost = postRepository.findById(id);

        if(!optionalPost.isPresent())
        {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        CurrentPostResponse currentPostResponse =
                convertFromPostToCurrentPostResponse(optionalPost.get(),
                        postRepository.countLikesOnPost(optionalPost.get().getId()),
                        postRepository.countDislikesOnPost((optionalPost.get().getId())),
                        commentRepository.findAllByPostId(optionalPost.get().getId()));


        optionalPost.get().setCountView(optionalPost.get().getCountView() + 1);
        postRepository.save(optionalPost.get());

        return new ResponseEntity(currentPostResponse, HttpStatus.OK);
    }

    private CurrentPostResponse convertFromPostToCurrentPostResponse(Post post,
                                                                     int countLikes,
                                                                     int countDislike,
                                                                     List<Comment> listComments) {

        CurrentPostResponse currentPostResponse = new CurrentPostResponse();
        currentPostResponse.setId(post.getId());
        currentPostResponse.setTimestamp(post.getTime().getTime()/1000);
        currentPostResponse.setActive(post.getIsActive() == 1 ? true : false);
        currentPostResponse.setUser(new UserDtoTwoFields(post.getUser().getId(), post.getUser().getName()));
        currentPostResponse.setTitle(post.getTitle());
        currentPostResponse.setText(post.getText());
        currentPostResponse.setLikeCount(countLikes);
        currentPostResponse.setDislikeCount(countDislike);
        currentPostResponse.setViewCount(post.getCountView());

        ArrayList<CommentDto> comments = new ArrayList<>();
        listComments.forEach(e->comments.add(this.convertFromCommentToCommentDto(e)));
        currentPostResponse.setComments(comments);

        currentPostResponse.setTags(new ArrayList<String>());

        return currentPostResponse;
    }

    private CommentDto convertFromCommentToCommentDto(Comment comment)
    {
        CommentDto commentResponse = new CommentDto();
        commentResponse.setId(comment.getId());
        commentResponse.setTimestamp(comment.getTime().getTime()/1000);
        commentResponse.setText(comment.getText());
        commentResponse.setUser(new UserDtoThreeFields(comment.getUser().getId(),
                comment.getUser().getName(),
                comment.getUser().getPhoto()));

        return commentResponse;
    }


    private PostResponse convertFromPostToPostResponse(Post e, int countLikes, int countDislike,
                                                       int countComments) {

        PostResponse postResponse = new PostResponse();
        postResponse.setId(e.getId());
        postResponse.setTitle(e.getTitle());
        postResponse.setTimestamp(e.getTime().getTime()/1000);
        postResponse.setAnnounce(e.getText().substring(0,Math.min(150, e.getText().length())));
        postResponse.setViewCount(e.getCountView());

        postResponse.setCommentCount(countComments);
        postResponse.setDislikeCount(countDislike);
        postResponse.setLikeCount(countLikes);

        postResponse.setUser(new UserDtoTwoFields(e.getUser().getId(), e.getUser().getName()));
        return postResponse;

    }


    public CalendarResponse getCalendarResponse(int yearId) {

        CalendarResponse calendarResponse = new CalendarResponse();
        ArrayList<Post> listPosts = (ArrayList<Post>)postRepository.findAllByTimeAsc();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        listPosts.forEach(e->
        {
            String currentDate = dateFormat.format(e.getTime());
            if(!calendarResponse.getPosts().containsKey(currentDate))
            {
                calendarResponse.getPosts().put(currentDate, 1);
            }
            else
            {
               calendarResponse.getPosts().put(currentDate,
                       calendarResponse.getPosts().get(currentDate)+1);
            }
            calendarResponse.getYears().add(e.getTime().getYear()+1900);
            //System.out.println(e.getId() + "  " + dateFormat.format(e.getTime()) + "  " + (e.getTime().getYear()+1900));
        }
        );

        return calendarResponse;
    }
}
