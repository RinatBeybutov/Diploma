package main.Repository;

import main.Model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Integer> {

    // поиск всех комментариев по id поста

    @Query(nativeQuery = true, value = "select * from post_comments where post_id = :id")
    List<Comment> findAllByPostId(int id);


}
