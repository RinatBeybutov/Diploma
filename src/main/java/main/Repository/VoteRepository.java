package main.Repository;

import java.util.Optional;
import main.Model.Vote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface VoteRepository extends JpaRepository<Vote, Integer> {


    @Query(value = "select count(*) from post_votes where value = 1", nativeQuery = true)
    int countAllLikes();

    @Query(value = "select count(*) from post_votes where value = -1", nativeQuery = true)
    int countAllDislikes();

    @Query(value = "select count(*) from post_votes where value = 1 and user_id = :id", nativeQuery = true)
    int countLikesByUser(int id);

    @Query(value = "select count(*) from post_votes where value = -1 and user_id = :id", nativeQuery = true)
    int countDislikesByUser(int id);

    @Query(value = "select * from post_votes where post_id = :postId and user_id = :userId", nativeQuery = true)
    Optional<Vote> findAllByUserIdAndPostId(int postId, int userId);
}
