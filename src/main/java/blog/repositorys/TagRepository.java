package blog.repositorys;

import blog.model.Tag;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TagRepository extends CrudRepository<Tag, Integer> {

    @Query(value = "SELECT tags.* FROM tags JOIN tag2post ON tags.id = tag2post.tag_id JOIN posts ON posts.id = tag2post.post_id WHERE is_active = :isActive AND moderation_status = :moderationStatus AND posts.time < :time GROUP BY tags.id", nativeQuery = true)
    List<Tag> findAllActiveTags(@Param("isActive") byte isActive, @Param("moderationStatus") String moderationStatus, @Param("time") long time);

}
