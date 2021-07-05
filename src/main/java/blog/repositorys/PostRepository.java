package blog.repositorys;

import blog.model.ModerationStatus;
import blog.model.Post;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends PagingAndSortingRepository<Post, Integer> {

    List<Post> findAllActiveByIsActiveAndModerationStatusAndTimeLessThan(byte isActive, ModerationStatus moderationStatus, long time, Pageable pageable);

    int countActiveByIsActiveAndModerationStatusAndTimeLessThan(byte isActive, ModerationStatus moderationStatus, long time);

    @Query(value = "SELECT posts.* FROM posts LEFT JOIN post_comments ON posts.id = post_comments.post_id WHERE is_active = :isActive AND moderation_status = :moderationStatus AND posts.time < :time GROUP BY posts.id ORDER BY COUNT(1) DESC", nativeQuery = true)
    List<Post> findAllActiveSortComments(@Param("isActive") byte isActive, @Param("moderationStatus") String moderationStatus, @Param("time") long time, Pageable pageable);

    @Query(value = "SELECT posts.* FROM posts LEFT JOIN post_voters ON post_voters.post_id = posts.id WHERE is_active = :isActive AND moderation_status = :moderationStatus AND posts.time < :time GROUP BY posts.id ORDER BY COUNT(IF(post_voters.value = 1,1,NULL)) DESC", nativeQuery = true)
    List<Post> findAllActiveSortVoters(@Param("isActive") byte isActive, @Param("moderationStatus") String moderationStatus, @Param("time") long time, Pageable pageable);

    @Query(value = "SELECT posts.* FROM posts JOIN tag2post ON posts.id = tag2post.post_id JOIN tags ON tags.id = tag2post.tag_id WHERE is_active = :isActive AND moderation_status = :moderationStatus AND posts.time < :time AND tags.name LIKE CONCAT('%', :query, '%')", nativeQuery = true)
    List<Post> searchByRequest(@Param("isActive") byte isActive, @Param("moderationStatus") String moderationStatus, @Param("time") long time, @Param("query") String query, Pageable pageable);

}
