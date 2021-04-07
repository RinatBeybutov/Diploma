package main.Service;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.TreeMap;
import main.Dto.IPostCount;
import main.Model.Comment;
import main.Model.ModerationStatus;
import main.Model.Post;
import main.Model.Tag;
import main.Model.UserModel;
import main.Model.Vote;
import main.Repository.CommentRepository;
import main.Repository.PostRepository;
import main.Repository.TagRepository;
import main.Repository.UserRepository;
import main.Repository.VoteRepository;
import main.Request.RequestComment;
import main.Request.RequestDecisionModeration;
import main.Request.RequestPost;
import main.Request.RequestPostLike;
import main.Response.CalendarResponse;
import main.Response.ListPostsResponse;
import main.Response.PostWrongResponse;
import main.Response.ResponseComment;
import main.Response.SinglePostResponse;
import main.Response.StatisticsResponse;
import main.Dto.CommentDto;
import main.Dto.ErrorsPostDto;
import main.Dto.PostDto;
import main.Dto.ResultDto;
import main.Dto.UserDtoThreeFields;
import main.Dto.UserDtoTwoFields;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class PostService {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private VoteRepository voteRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TagRepository tagRepository;

    private TreeMap<String, Integer> authorisatedId = new TreeMap<>();

    public ListPostsResponse getPageablePostsByTimeDesc(int offset, int itemPerPage) {

        Pageable pageable = PageRequest.of(offset / itemPerPage, itemPerPage);
        ArrayList<PostDto> responseList = new ArrayList<PostDto>();
        postRepository.findAllByTimeOnPageDesc(pageable).forEach(e ->
            responseList.add(this.convertFromPostToPostDto(e,
                postRepository.countLikesOnPost(e.getId()),
                postRepository.countDislikesOnPost(e.getId()),
                postRepository.countCommentsOnPost(e.getId()))));
        return new ListPostsResponse(postRepository.countPost(), responseList);
    }

    public ListPostsResponse getPageablePostsByTimeAsc(int offset, int itemPerPage) {

        Pageable pageable = PageRequest.of(offset / itemPerPage, itemPerPage);
        ArrayList<PostDto> responseList = new ArrayList<PostDto>();
        postRepository.findAllByTimeOnPageAsc(pageable).forEach(e ->
            responseList.add(this.convertFromPostToPostDto(e,
                postRepository.countLikesOnPost(e.getId()),
                postRepository.countDislikesOnPost(e.getId()),
                postRepository.countCommentsOnPost(e.getId()))));
        return new ListPostsResponse(postRepository.countPost(), responseList);
    }

    public ListPostsResponse getPageablePostsByComments(int offset, int itemPerPage) {

        Pageable pageable = PageRequest.of(offset / itemPerPage, itemPerPage);
        ArrayList<PostDto> responseList = new ArrayList<PostDto>();
        postRepository.findAllByCommentOnPage(pageable).forEach(e ->
            responseList.add(this.convertFromPostToPostDto(e,
                postRepository.countLikesOnPost(e.getId()),
                postRepository.countDislikesOnPost(e.getId()),
                postRepository.countCommentsOnPost(e.getId()))));
        return new ListPostsResponse(postRepository.countPost(), responseList);
    }

    public ListPostsResponse getPageablePostsByLikes(int offset, int itemPerPage) {

        Pageable pageable = PageRequest.of(offset / itemPerPage, itemPerPage);
        ArrayList<PostDto> responseList = new ArrayList<PostDto>();
        postRepository.findAllByLikesOnPage(pageable).forEach(e ->
            responseList.add(this.convertFromPostToPostDto(e,
                postRepository.countLikesOnPost(e.getId()),
                postRepository.countDislikesOnPost(e.getId()),
                postRepository.countCommentsOnPost(e.getId()))));
        return new ListPostsResponse(postRepository.countPost(), responseList);
    }

    public ListPostsResponse getPostsByTimeDesc() {

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

    public ListPostsResponse getPostsByTimeAsc() {
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

    public ListPostsResponse getPostsByComments() {
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

    public ListPostsResponse getPostsByLikes() {
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

    public ResponseEntity<SinglePostResponse> getSinglePost(int id) {
        Optional<Post> optionalPost = postRepository.findById(id);

        if (!optionalPost.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        SinglePostResponse singlePostResponse =
            convertFromPostToCurrentPostResponse(optionalPost.get(),
                postRepository.countLikesOnPost(optionalPost.get().getId()),
                postRepository.countDislikesOnPost((optionalPost.get().getId())),
                commentRepository.findAllByPostId(optionalPost.get().getId()));

        optionalPost.get().setCountView(optionalPost.get().getCountView() + 1);
        postRepository.save(optionalPost.get());

        return new ResponseEntity(singlePostResponse, HttpStatus.OK);
    }

    public CalendarResponse getCalendarResponse(int yearId) {

        CalendarResponse calendarResponse = new CalendarResponse();
        ArrayList<IPostCount> listCountPostDto = (ArrayList<IPostCount>) postRepository.countPostByDate();
        for(int i=0; i<listCountPostDto.size(); i++) {
            calendarResponse.getPosts().put(listCountPostDto.get(i).getDatePost(),
                listCountPostDto.get(i).getCountPost());
            calendarResponse.getYears().add(
                Integer.valueOf(listCountPostDto.get(i).getDatePost().substring(0,4)));
        }
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
            responseList), HttpStatus.OK);
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
            responseList), HttpStatus.OK);
    }

    public ResponseEntity<ListPostsResponse> getPostsByQuery(int offset, int limit, String query) {
        Pageable pageable = PageRequest.of(offset / limit, limit);
        Page<Post> list;
        ArrayList<PostDto> responseList = new ArrayList<>();
        if (query.equals("")) {
            list = postRepository.findAllActivePosts(pageable);
        } else {
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

    public ResponseEntity<StatisticsResponse> getGlobalStatistics() {
        StatisticsResponse statisticsResponse = new StatisticsResponse();

        List<Post> listPosts = postRepository.findAllByTimeAsc();
        statisticsResponse.setPostsCount(listPosts.size());

        statisticsResponse
            .setViewsCount(postRepository.findAll().stream().mapToInt(e -> e.getCountView()).sum());
        statisticsResponse.setLikesCount(voteRepository.countAllLikes());
        statisticsResponse.setDislikesCount(voteRepository.countAllDislikes());
        statisticsResponse.setFirstPublication(listPosts.get(0).getTime().getTime() / 1000);

        return ResponseEntity.ok(statisticsResponse);
    }

    public ResponseEntity<StatisticsResponse> getMyStatistics(Principal principal) {
        StatisticsResponse statisticsResponse = new StatisticsResponse();

        UserModel user = userRepository.findAllByEmail(principal.getName())
            .orElseThrow(() -> new UsernameNotFoundException(principal.getName()));

        List<Post> listMyPosts = postRepository.findAllByUserId(user.getId());
        int totalViewCount = 0;

        listMyPosts.sort(Comparator.comparing(Post::getTime));

        for (Post post : listMyPosts) {
            totalViewCount += post.getCountView();
        }

        statisticsResponse.setPostsCount(listMyPosts.size());
        statisticsResponse.setViewsCount(totalViewCount);
        statisticsResponse.setFirstPublication(listMyPosts.get(0).getTime().getTime() / 1000);
        statisticsResponse.setLikesCount(voteRepository.countLikesByUser(user.getId()));
        statisticsResponse.setDislikesCount(voteRepository.countDislikesByUser(user.getId()));

        return ResponseEntity.ok(statisticsResponse);
    }

    public ResponseEntity<ListPostsResponse> getMainPagePosts(int offset, int limit, String mode) {
        switch (mode) {
            case "recent":
                return new ResponseEntity<>(this.getPageablePostsByTimeDesc(offset, limit),
                    HttpStatus.OK);
            case "early":
                return new ResponseEntity<>(this.getPageablePostsByTimeAsc(offset, limit),
                    HttpStatus.OK);
            case "popular":
                return new ResponseEntity<>(this.getPageablePostsByComments(offset, limit),
                    HttpStatus.OK);
            case "best":
                return new ResponseEntity<>(this.getPageablePostsByLikes(offset, limit),
                    HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    public ResponseEntity<ListPostsResponse> getMyPosts(int offset, int limit, String status,
        Principal principal) {

        UserModel user = userRepository.findAllByEmail(principal.getName())
            .orElseThrow(() -> new UsernameNotFoundException(principal.getName()));

        Pageable pageable = PageRequest.of(offset / limit, limit);

        Page<Post> listMyPosts = null;

        switch (status) {
            case "inactive":
                listMyPosts = postRepository.findAllInactivePosts(user.getId(), pageable);
                break;
            case "pending":
                listMyPosts = postRepository
                    .findAllMyPosts(user.getId(), pageable, ModerationStatus.NEW.toString(), 1);
                break;
            case "declined":
                listMyPosts = postRepository
                    .findAllMyPosts(user.getId(), pageable, ModerationStatus.DECLINED.toString(),
                        1);
                break;
            case "published":
                listMyPosts = postRepository
                    .findAllMyPosts(user.getId(), pageable, ModerationStatus.ACCEPTED.toString(),
                        1);
                break;
        }

        ArrayList<PostDto> responseList = new ArrayList<>();

        listMyPosts.forEach(e ->
            responseList.add(this.convertFromPostToPostDto(e,
                postRepository.countLikesOnPost(e.getId()),
                postRepository.countDislikesOnPost(e.getId()),
                postRepository.countCommentsOnPost(e.getId()))));

        return new ResponseEntity<>(new ListPostsResponse(responseList.size(),
            responseList), HttpStatus.OK);
    }

    public ResponseEntity<?> post(RequestPost requestPost, Principal principal) {

        boolean isFulledTitle = true;
        boolean isLongText = true;

        PostWrongResponse wrongResponse = new PostWrongResponse();

        if (requestPost.getText().length() < 50) {
            isLongText = false;
            wrongResponse.getErrors().setText("Текст публикации слишком короткий");
        }

        if (requestPost.getTitle().length() < 3) {
            isFulledTitle = false;
            wrongResponse.getErrors().setTitle("Заголовок не установлен");
        }

        if (isFulledTitle && isLongText) {
            ResultDto resultDto = new ResultDto(true);
            postRepository.save(convertFromPostRequestToPost(requestPost, principal));
            return ResponseEntity.ok(resultDto);
        }

        return ResponseEntity.ok(wrongResponse);
    }

    private Post convertFromPostRequestToPost(RequestPost requestPost,
        Principal principal) {
        Post post = new Post();

        post.setTitle(requestPost.getTitle());
        post.setText(getNormalPostText(requestPost.getText()));
        post.setTime(new Date(requestPost.getTimestamp() * 1000));
        post.setIsActive((byte) requestPost.getActive());

        ArrayList<Tag> listTags = new ArrayList<>();

        requestPost.getTags().forEach(e ->
        {
            Tag tag = tagRepository.findAllByName(e.trim()).orElse(new Tag(e));
            listTags.add(tag);
        });

        post.setTags(listTags);
        post.setModerationStatus(ModerationStatus.NEW);
        post.setCountView(0);
        post.setUser(userRepository.findAllByEmail(principal.getName()).get());
        post.setModerator(userRepository.findAllByEmail("tarakan@mail.ru").get());
        return post;

    }

    private String getNormalPostText(String text) {

        text = text.replaceAll("<.+?>", "");
        return text.substring(0, Math.min(254, text.length()));
    }

    private SinglePostResponse convertFromPostToCurrentPostResponse(Post post,
        int countLikes,
        int countDislike,
        List<Comment> listComments) {

        SinglePostResponse singlePostResponse = new SinglePostResponse();
        singlePostResponse.setId(post.getId());
        singlePostResponse.setTimestamp(post.getTime().getTime() / 1000);
        singlePostResponse.setActive(post.getIsActive() == 1 ? true : false);
        singlePostResponse
            .setUser(new UserDtoTwoFields(post.getUser().getId(), post.getUser().getName()));
        singlePostResponse.setTitle(post.getTitle());
        singlePostResponse.setText(post.getText());
        singlePostResponse.setLikeCount(countLikes);
        singlePostResponse.setDislikeCount(countDislike);
        singlePostResponse.setViewCount(post.getCountView());

        ArrayList<CommentDto> comments = new ArrayList<>();
        listComments.forEach(e -> comments.add(this.convertFromCommentToCommentDto(e)));
        singlePostResponse.setComments(comments);

        singlePostResponse.setTags(new ArrayList<String>());

        return singlePostResponse;
    }

    private CommentDto convertFromCommentToCommentDto(Comment comment) {
        CommentDto commentResponse = new CommentDto();
        commentResponse.setId(comment.getId());
        commentResponse.setTimestamp(comment.getTime().getTime() / 1000);
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
        postDto.setTimestamp(e.getTime().getTime() / 1000);
        postDto.setAnnounce(e.getText().substring(0, Math.min(150, e.getText().length())));
        postDto.setViewCount(e.getCountView());

        postDto.setCommentCount(countComments);
        postDto.setDislikeCount(countDislike);
        postDto.setLikeCount(countLikes);

        postDto.setUser(new UserDtoTwoFields(e.getUser().getId(), e.getUser().getName()));
        return postDto;

    }


    public ResponseEntity<?> like(RequestPostLike requestPostLike, Principal principal) {

        UserModel user = userRepository.findAllByEmail(principal.getName())
            .orElseThrow(() -> new UsernameNotFoundException(principal.getName()));

        Post post = postRepository.findById(requestPostLike.getPost_id())
            .orElseThrow(() -> new UsernameNotFoundException(
                "not found this post " + requestPostLike.getPost_id()));

        Vote vote = voteRepository.findAllByUserIdAndPostId(requestPostLike.getPost_id(),
            user.getId()).orElse(new Vote(user, post, (byte) 1));

        if (vote.getValue() == 1) {
            if (vote.getTime() != null) {
                return ResponseEntity.ok(new ResultDto(false));
            } else {
                vote.setTime(new Date());
            }
        } else {
            vote.setValue((byte) 1);
        }
        voteRepository.save(vote);
        return ResponseEntity.ok(new ResultDto(true));
    }

    public ResponseEntity<?> dislike(RequestPostLike requestPostLike, Principal principal) {
        UserModel user = userRepository.findAllByEmail(principal.getName())
            .orElseThrow(() -> new UsernameNotFoundException(principal.getName()));

        Post post = postRepository.findById(requestPostLike.getPost_id())
            .orElseThrow(() -> new UsernameNotFoundException(
                "not found this post " + requestPostLike.getPost_id()));

        Vote vote = voteRepository.findAllByUserIdAndPostId(requestPostLike.getPost_id(),
            user.getId()).orElse(new Vote(user, post, (byte) -1));

        if (vote.getValue() == -1) {
            if (vote.getTime() != null) {
                return ResponseEntity.ok(new ResultDto(false));
            } else {
                vote.setTime(new Date());
            }
        } else {
            vote.setValue((byte) -1);
        }
        voteRepository.save(vote);
        return ResponseEntity.ok(new ResultDto(true));
    }

    public ResponseEntity<?> editPost(int id, RequestPost requestPost, Principal principal) {

        Post post = postRepository.findById(id)
            .orElseThrow(() -> new UsernameNotFoundException("post not found, id = " + id));

        boolean isFulledTitle = true;
        boolean isLongText = true;

        PostWrongResponse wrongResponse = new PostWrongResponse();

        if (requestPost.getText().length() < 50) {
            isLongText = false;
            wrongResponse.getErrors().setText("Текст публикации слишком короткий");
        }

        if (requestPost.getTitle().length() < 3) {
            isFulledTitle = false;
            wrongResponse.getErrors().setTitle("Заголовок не установлен");
        }

        if (isFulledTitle && isLongText) {
            post.setText(getNormalPostText(requestPost.getText()));
            post.setTitle(requestPost.getTitle());

            List<Tag> listTags = post.getTags();

            requestPost.getTags().forEach(e ->
            {
                Tag tag = tagRepository.findAllByName(e.trim()).orElse(new Tag(e));
                if (!listTags.contains(tag)) {
                    listTags.add(tag);
                }
            });

            post.setTags(listTags);
            post.setIsActive((byte) requestPost.getActive());
            if (requestPost.getTimestamp() * 1000 < new Date().getTime()) {
                post.setTime(new Date());
            } else {
                post.setTime(new Date(requestPost.getTimestamp() * 1000));
            }
            postRepository.save(post);
            return ResponseEntity.ok(new ResultDto(true));
        }

        return ResponseEntity.ok(wrongResponse);
    }

    public ResponseEntity<?> getModerationPosts(Principal principal, int offset, int limit,
        String status) {

        Pageable pageable = PageRequest.of(offset / limit, limit);
        Page<Post> list;
        int countPosts = 0;
        int idModerator = userRepository.findAllByEmail(principal.getName()).get().getId();

        switch (status) {
            case "new":
                list = postRepository.findAllToModerate("NEW", pageable);
                countPosts = postRepository.countAllToModerate("NEW");
                break;
            case "accepted":
                list = postRepository
                    .findAllToModerateByModerator("ACCEPTED", pageable, idModerator);
                countPosts = postRepository.countAllToModerateByModerator("ACCEPTED", idModerator);
                break;
            case "declined":
                list = postRepository
                    .findAllToModerateByModerator("DECLINED", pageable, idModerator);
                countPosts = postRepository.countAllToModerateByModerator("DECLINDE", idModerator);
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + status);
        }

        ArrayList<PostDto> responseList = new ArrayList<>();

        list.forEach(e ->
            responseList.add(this.convertFromPostToPostDto(e,
                postRepository.countLikesOnPost(e.getId()),
                postRepository.countDislikesOnPost(e.getId()),
                postRepository.countCommentsOnPost(e.getId()))));

        ListPostsResponse listPostsResponse = new ListPostsResponse(countPosts, responseList);

        return ResponseEntity.ok(listPostsResponse);
    }

    public ResponseEntity<?> moderateNewPosts(Principal principal,
        RequestDecisionModeration requestDecisionModeration) {

        UserModel user = userRepository.findAllByEmail(principal.getName())
            .orElseThrow(()-> new UsernameNotFoundException(principal.getName()));

        if(user.getIsModerator() != 1)
        {
            return ResponseEntity.ok(new ResultDto(false));
        }

        Post post = postRepository.findById(requestDecisionModeration.getPostId())
            .orElseThrow(()-> new UsernameNotFoundException("not found post " + requestDecisionModeration.getPostId()));

        if(requestDecisionModeration.getDecision().equals("accept"))
        {
            post.setModerationStatus(ModerationStatus.ACCEPTED);
        }
        if(requestDecisionModeration.getDecision().equals("decline"))
        {
            post.setModerationStatus(ModerationStatus.DECLINED);
        }
        post.setModerator(user);
        postRepository.save(post);

        return ResponseEntity.ok(new ResultDto(true));
    }

    public ResponseEntity<?> comment(RequestComment requestComment, Principal principal) {

        if(!postRepository.findById(requestComment.getPostId()).isPresent())
        {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }

        if(requestComment.getText().length() < 5)
        {
            PostWrongResponse postWrongResponse = new PostWrongResponse();
            postWrongResponse.setResult(false);
            ErrorsPostDto errorsPostDto = new ErrorsPostDto();
            errorsPostDto.setText("Текст комментария не задан или слишком короткий");
            postWrongResponse.setErrors(new ErrorsPostDto());
            return ResponseEntity.ok(postWrongResponse);
        }

        Comment comment = new Comment();
        comment.setParentComment( requestComment.getParentId() == 0 ? null :
            commentRepository.findById(requestComment.getParentId()).get());
        comment.setText(getNormalPostText(requestComment.getText()));
        comment.setPostId(postRepository.findById(requestComment.getPostId()).get());
        comment.setTime(new Date());
        comment.setUser(userRepository.findAllByEmail(principal.getName())
            .orElseThrow(()->new UsernameNotFoundException(principal.getName())));

        commentRepository.save(comment);

        ResponseComment responseComment = new ResponseComment();
        responseComment.setId(commentRepository.countComments()+1);
        return ResponseEntity.ok(responseComment);
    }
}
