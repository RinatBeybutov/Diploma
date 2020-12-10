package main.Repository;

import main.Model.Post;
import main.Response.dto.PostResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Integer> {

    // по времени

    //@Query(value = "SELECT ALL FROM posts post order by time desc limit 10 offset 0")
    //List<Post> findAllByTime();

    //по лайкам

    /*@Query(value = "SELECT id , title, text, time \n" +
            "(select count(*) from post_votes where post_id = posts.id and value = 1) as likeCount,\n" +
            "(select count(*) from post_votes where post_id = posts.id and value = -1) as dislikeCount " +
            "FROM posts order by time desc limit 3 offset 0")
    List<PostResponse> findByLikes();*/

    // кол-во постов



}
