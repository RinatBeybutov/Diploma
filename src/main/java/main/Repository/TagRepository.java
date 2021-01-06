package main.Repository;

import java.util.Optional;
import main.Model.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface TagRepository extends JpaRepository<Tag, Integer> {

  @Query(nativeQuery = true, value = "" +
      "select (select count(*) from tag2post join posts on post_id = id where tag_id = :id and moderation_status = 'ACCEPTED') / "
      + "(select count(*) from posts where is_active = 1 and moderation_status = 'ACCEPTED')")
  float getWeightById(int id);

  Optional<Tag> findAllByName(String e);
}
