package main.Repository;

import main.Model.Post;
import main.Response.dto.PostResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Integer> {

    // загружаем страницу постов по времени новые

    @Query(value = "select * FROM posts order by time desc", nativeQuery = true)
    Page<Post> findAllByTimeOnPageDesc(Pageable pageable);

    // загружаем страницу постов по времени старые

    @Query(value = "select * FROM posts order by time asc", nativeQuery = true)
    Page<Post> findAllByTimeOnPageAsc(Pageable pageable);

    // загружаем страницу постов по комментам

    @Query(value = "select * FROM posts order by " +
            "(select count(*) from post_comments where post_id = posts.id) desc", nativeQuery = true)
    Page<Post> findAllByCommentOnPage(Pageable pageable);

    // загружаем страницу постов по лайкам

    @Query(value = "select * from posts order by " +
            "(select count(*) from post_votes where post_id = posts.id and value = 1) desc", nativeQuery = true)
    Page<Post> findAllByLikesOnPage(Pageable pageable);




    //кол-во лайков у поста

    @Query(value = "select count(*) from post_votes where post_id = :id and value = 1 ", nativeQuery = true)
    int countLikesOnPost(Integer id);

    //кол-во дизлайков у поста

    @Query(value = "select count(*) from post_votes where post_id = :id and value = -1 ", nativeQuery = true)
    int countDislikesOnPost(Integer id);

    //кол-во комментов у поста

    @Query(value = "select count(*) from post_comments where post_id = :id", nativeQuery = true)
    int countCommentsOnPost(Integer id);

    // кол-во постов

    @Query(value = "select count(*) from posts", nativeQuery = true)
    int countPost();





    // список постов по времени новые

    @Query(value = "select * FROM posts order by time desc limit :limit offset 0", nativeQuery = true)
    List<Post> findAllByTimeDesc(Integer limit);

    // список постов по времени старые

    @Query(value = "select * FROM posts order by time limit 10 offset 0", nativeQuery = true)
    List<Post> findAllByTimeAsc();

    // список постов по количеству комментов

    @Query(value = "select * from posts order by " +
            "(select count(*) from post_comments where post_id = posts.id) desc", nativeQuery = true)
    List<Post> findAllByComments();

    // список постов по лайкам

    @Query(value = "select * from posts order by " +
            "(select count(*) from post_votes where post_id = posts.id and value = 1) desc", nativeQuery = true)
    List<Post> findAllByLikes();






}
