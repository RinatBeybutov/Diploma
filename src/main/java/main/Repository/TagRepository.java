package main.Repository;

import main.Model.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface TagRepository extends JpaRepository<Tag, Integer> {

    @Query(nativeQuery = true, value = "" +
            "select (select count(*) from tag2post where tag_id = :id) / (select count(*) from posts)")
    float getWeightById(int id);
}
