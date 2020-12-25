package main.Repository;

import main.Model.Vote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface VoteRepository extends JpaRepository<Vote, Integer> {


    @Query(value = "select count(*) from post_votes where value = 1", nativeQuery = true)
    int countLikes();

    @Query(value = "select count(*) from post_votes where value = -1", nativeQuery = true)
    int countDislikes();
}
