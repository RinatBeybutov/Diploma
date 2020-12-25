package main.Service;

import main.Model.Comment;
import main.Model.Post;
import main.Repository.CommentRepository;
import main.Repository.PostRepository;
import main.Repository.VoteRepository;
import main.Response.CalendarResponse;
import main.Response.CurrentPostResponse;
import main.Response.ListPostsResponse;
import main.Response.StatisticsResponse;
import main.Response.dto.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PostService {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private VoteRepository voteRepository;

    public ListPostsResponse getPageablePostsByTimeDesc(int offset, int itemPerPage)
    {

        Pageable pageable = PageRequest.of(offset / itemPerPage, itemPerPage);
        ArrayList<PostDto> responseList = new ArrayList<PostDto>();
        postRepository.findAllByTimeOnPageDesc(pageable).forEach(e ->
                responseList.add(this.convertFromPostToPostDto(e,
                        postRepository.countLikesOnPost(e.getId()),
                        postRepository.countDislikesOnPost(e.getId()),
                        postRepository.countCommentsOnPost(e.getId()))));
        return new ListPostsResponse(postRepository.countPost(),responseList);
    }

    public ListPostsResponse getPageablePostsByTimeAsc(int offset, int itemPerPage)
    {

        Pageable pageable = PageRequest.of(offset / itemPerPage, itemPerPage);
        ArrayList<PostDto> responseList = new ArrayList<PostDto>();
        postRepository.findAllByTimeOnPageAsc(pageable).forEach(e ->
                responseList.add(this.convertFromPostToPostDto(e,
                        postRepository.countLikesOnPost(e.getId()),
                        postRepository.countDislikesOnPost(e.getId()),
                        postRepository.countCommentsOnPost(e.getId()))));
        return new ListPostsResponse(postRepository.countPost(),responseList);
    }

    public ListPostsResponse getPageablePostsByComments(int offset, int itemPerPage)
    {

        Pageable pageable = PageRequest.of(offset / itemPerPage, itemPerPage);
        ArrayList<PostDto> responseList = new ArrayList<PostDto>();
        postRepository.findAllByCommentOnPage(pageable).forEach(e ->
                responseList.add(this.convertFromPostToPostDto(e,
                        postRepository.countLikesOnPost(e.getId()),
                        postRepository.countDislikesOnPost(e.getId()),
                        postRepository.countCommentsOnPost(e.getId()))));
        return new ListPostsResponse(postRepository.countPost(),responseList);
    }

    public ListPostsResponse getPageablePostsByLikes(int offset, int itemPerPage)
    {

        Pageable pageable = PageRequest.of(offset / itemPerPage, itemPerPage);
        ArrayList<PostDto> responseList = new ArrayList<PostDto>();
        postRepository.findAllByLikesOnPage(pageable).forEach(e ->
                responseList.add(this.convertFromPostToPostDto(e,
                        postRepository.countLikesOnPost(e.getId()),
                        postRepository.countDislikesOnPost(e.getId()),
                        postRepository.countCommentsOnPost(e.getId()))));
        return new ListPostsResponse(postRepository.countPost(),responseList);
    }

    public ListPostsResponse getPostsByTimeDesc()
    {

     ArrayList<Post> list = (ArrayList<Post>) postRepository.findAllByTimeDesc(10);
     ArrayList<PostDto> responseList = new ArrayList<PostDto>();
     list.stream().forEach(e ->
         responseList.add(this.convertFromPostToPostDto(e,
                 postRepository.countLikesOnPost(e.getId()),
                 postRepository.countDislikesOnPost(e.getId()),
                 postRepository.countCommentsOnPost(e.getId()))
     ));

     return new ListPostsResponse(postRepository.countPost(), responseList);
    }

    public ListPostsResponse getPostsByTimeAsc()
    {
        ArrayList<Post> list = (ArrayList<Post>) postRepository.findAllByTimeAsc();
        ArrayList<PostDto> responseList = new ArrayList<PostDto>();
        list.stream().forEach(e ->
                responseList.add(this.convertFromPostToPostDto(e,
                        postRepository.countLikesOnPost(e.getId()),
                        postRepository.countDislikesOnPost(e.getId()),
                        postRepository.countCommentsOnPost(e.getId()))
                ));

        return new ListPostsResponse(postRepository.countPost(), responseList);
    }

    public ListPostsResponse getPostsByComments()
    {
        ArrayList<Post> list = (ArrayList<Post>) postRepository.findAllByComments();
        ArrayList<PostDto> responseList = new ArrayList<PostDto>();
        list.stream().forEach(e ->
                responseList.add(this.convertFromPostToPostDto(e,
                        postRepository.countLikesOnPost(e.getId()),
                        postRepository.countDislikesOnPost(e.getId()),
                        postRepository.countCommentsOnPost(e.getId()))
                ));

        return new ListPostsResponse(postRepository.countPost(), responseList);
    }

    public ListPostsResponse getPostsByLikes()
    {
        ArrayList<Post> list = (ArrayList<Post>) postRepository.findAllByLikes();
        ArrayList<PostDto> responseList = new ArrayList<PostDto>();
        list.stream().forEach(e ->
                responseList.add(this.convertFromPostToPostDto(e,
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
           }
        );

        return calendarResponse;
    }

    public ResponseEntity<ListPostsResponse> getPostsByTag(int offset, int limit, String tag) {

        Pageable pageable = PageRequest.of(offset / limit, limit);
        ArrayList<PostDto> responseList = new ArrayList<>();
        postRepository.findAllByTagOnPage(tag, pageable).forEach(e ->
                responseList.add(this.convertFromPostToPostDto(e,
                        postRepository.countLikesOnPost(e.getId()),
                        postRepository.countDislikesOnPost(e.getId()),
                        postRepository.countCommentsOnPost(e.getId()))));

        return new ResponseEntity<ListPostsResponse>(new ListPostsResponse(responseList.size(),
                responseList),HttpStatus.OK);
    }

    public ResponseEntity<ListPostsResponse> getPostsByDate(int offset, int limit, String date) {

        Pageable pageable = PageRequest.of(offset / limit, limit);
        ArrayList<PostDto> responseList = new ArrayList<>();
        postRepository.findAllByDateByPage(date, pageable).forEach(e ->
                responseList.add(this.convertFromPostToPostDto(e,
                        postRepository.countLikesOnPost(e.getId()),
                        postRepository.countDislikesOnPost(e.getId()),
                        postRepository.countCommentsOnPost(e.getId()))));

        return new ResponseEntity<ListPostsResponse>(new ListPostsResponse(responseList.size(),
                responseList),HttpStatus.OK);
    }


    public ResponseEntity<ListPostsResponse> getPostsByQuery(int offset, int limit, String query) {
        Pageable pageable = PageRequest.of(offset/limit, limit);
        Page<Post> list;
        ArrayList<PostDto> responseList = new ArrayList<>();
        if(query.equals(""))
        {
            list = postRepository.findAllActivePosts(pageable);
        }
        else
        {
            list = postRepository.findAllByQuery(pageable, query);
        }

        list.forEach(e ->
                responseList.add(this.convertFromPostToPostDto(e,
                        postRepository.countLikesOnPost(e.getId()),
                        postRepository.countDislikesOnPost(e.getId()),
                        postRepository.countCommentsOnPost(e.getId()))));

        return new ResponseEntity<>(new ListPostsResponse(responseList.size(),
                responseList), HttpStatus.OK);
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

    private PostDto convertFromPostToPostDto(Post e, int countLikes, int countDislike,
                                             int countComments) {

        PostDto postDto = new PostDto();
        postDto.setId(e.getId());
        postDto.setTitle(e.getTitle());
        postDto.setTimestamp(e.getTime().getTime()/1000);
        postDto.setAnnounce(e.getText().substring(0,Math.min(150, e.getText().length())));
        postDto.setViewCount(e.getCountView());

        postDto.setCommentCount(countComments);
        postDto.setDislikeCount(countDislike);
        postDto.setLikeCount(countLikes);

        postDto.setUser(new UserDtoTwoFields(e.getUser().getId(), e.getUser().getName()));
        return postDto;

    }

    public StatisticsResponse getGlobalStatistics() {
        StatisticsResponse statisticsResponse = new StatisticsResponse();


        List<Post> listPosts = postRepository.findAllByTimeAsc();
        statisticsResponse.setPostsCount(listPosts.size());

        statisticsResponse.setViewsCount(postRepository.findAll().stream().mapToInt(e->e.getCountView()).sum());
        statisticsResponse.setLikesCount(voteRepository.countLikes());
        statisticsResponse.setDislikesCount(voteRepository.countDislikes());
        statisticsResponse.setFirstPublication(listPosts.get(0).getTime().getTime()/1000);

        return statisticsResponse;
    }
}
