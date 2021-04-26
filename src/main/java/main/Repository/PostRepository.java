package main.Repository;

import java.util.List;
import main.Dto.IPostCount;
import main.Model.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends JpaRepository<Post, Integer> {

    // загружаем страницу постов по времени новые
    @Query(value = "select * FROM posts where moderation_status = 'ACCEPTED' order by time desc ", nativeQuery = true)
    Page<Post> findAllByTimeOnPageDesc(Pageable pageable);

    // загружаем страницу постов по времени старые
    @Query(value = "select * FROM posts where moderation_status = 'ACCEPTED' order by time asc", nativeQuery = true)
    Page<Post> findAllByTimeOnPageAsc(Pageable pageable);

    // загружаем страницу постов по комментам
    @Query(value = "select * FROM posts where moderation_status = 'ACCEPTED' order by " +
        "(select count(*) from post_comments where post_id = posts.id) desc", nativeQuery = true)
    Page<Post> findAllByCommentOnPage(Pageable pageable);

    // загружаем страницу постов по лайкам
    @Query(value = "select * from posts where moderation_status = 'ACCEPTED' order by " +
        "(select count(*) from post_votes where post_id = posts.id and value = 1) desc", nativeQuery = true)
    Page<Post> findAllByLikesOnPage(Pageable pageable);


    @Query(nativeQuery = true, value = "select * from posts where user_id = :id "
        + "and is_active = 0")
    Page<Post> findAllInactivePosts(int id, Pageable pageable);

    @Query(value =
        "select * from posts where user_id = :id and moderation_status = :moderationStatus"
            + " and is_active = :isActive", nativeQuery = true)
    Page<Post> findAllMyPosts(int id, Pageable pageable, String moderationStatus, int isActive);

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

    @Query(value = "select date(time) as datePost, "
        + "count(date(time)) as countPost from posts  group by date(time) order by time desc;",
    nativeQuery = true)
    List<IPostCount> countPostByDate();

    // список постов по времени старые

    @Query(value = "select * FROM posts where moderation_status = 'ACCEPTED' order by time ", nativeQuery = true)
    List<Post> findAllByTimeAsc();

    @Query(value = "select * " +
            "from posts join tag2post " +
            "on id = post_id " +
            "join tags on " +
            "tag_id = tags.id " +
            "where moderation_status = 'ACCEPTED' and name = :tag and is_active = 1 and moderation_status = \"ACCEPTED\""
            , nativeQuery = true)
    Page<Post> findAllByTagOnPage(String tag, Pageable pageable);

    @Query(value = "select * from posts where date(time) = :date", nativeQuery = true)
    Page<Post> findAllByDateByPage(String date, Pageable pageable);

    @Query(value = "select * from posts where is_active = 1 and moderation_status = 'ACCEPTED'", nativeQuery = true)
    Page<Post> findAllActivePosts(Pageable pageable);

    @Query(value = "select * from posts where moderation_status = 'ACCEPTED' and "
        + "(title like %:query% or text like %:query%)", nativeQuery = true)
    Page<Post> findAllByQuery(Pageable pageable, String query);

    List<Post> findAllByUserId(int id);

    @Query(value = "select * from posts where moderation_status = :status and is_active = 1", nativeQuery = true)
    Page<Post> findAllToModerate(String status, Pageable pageable);

    @Query(value = "select * from posts where moderation_status = :status and moderator_id = :id", nativeQuery = true)
    Page<Post> findAllToModerateByModerator(String status, Pageable pageable, int id);

    @Query(value = "select count(*) from posts where moderation_status = :status", nativeQuery = true)
    int countAllToModerate(String status);

    @Query(value = "select count(*) from posts where moderation_status = :status "
        + "and moderator_id = :id and is_active = 1", nativeQuery = true)
    int countAllToModerateByModerator(String status, int id);

}
