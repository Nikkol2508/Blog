package blog.model;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends PagingAndSortingRepository<Post, Integer> {

    Page<Post> findAllActiveByIsActiveAndModerationStatusAndTimeLessThan(byte isActive, ModerationStatus moderationStatus, long time, Pageable pageable);

    int countActiveByIsActiveAndModerationStatusAndTimeLessThan(byte isActive, ModerationStatus moderationStatus, long time);

    @Query(value = "SELECT posts.* FROM posts JOIN post_comments ON post_comments.post_id = posts.id WHERE is_active = :isActive AND moderation_status = :moderationStatus AND posts.time < :time GROUP BY posts.id ORDER BY COUNT(1) DESC", nativeQuery = true)
    Page<Post> findAllActiveSortComments(@Param("isActive") byte isActive, @Param("moderationStatus") String moderationStatus, @Param("time") long time, Pageable pageable);

    @Query(value = "SELECT COUNT(DISTINCT posts.id) FROM posts JOIN post_comments ON post_comments.post_id = posts.id WHERE is_active = :isActive AND moderation_status = :moderationStatus AND posts.time < :time", nativeQuery = true)
    int countSelected(@Param("isActive") byte isActive, @Param("moderationStatus") String moderationStatus, @Param("time") long time);
}
