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

    @Query(value = "WITH t2 AS (SELECT COUNT(posts.id) AS max_count FROM tags JOIN tag2post ON tags.id = tag2post.tag_id JOIN posts ON posts.id = tag2post.post_id WHERE is_active = 1 AND moderation_status = 'ACCEPTED' AND posts.time < UTC_TIMESTAMP() GROUP BY tags.id ORDER BY max_count DESC LIMIT 1), \n" +
            "t1(t_name, p_count) AS (SELECT tags.name AS t_name, COUNT(posts.id) AS p_count FROM tags JOIN tag2post ON tags.id = tag2post.tag_id JOIN posts ON posts.id = tag2post.post_id WHERE is_active = 1 AND moderation_status = 'ACCEPTED' AND posts.time < UTC_TIMESTAMP() GROUP BY tags.id),\n" +
            "t3 AS (SELECT COUNT(posts.id) AS active_posts FROM posts WHERE is_active = 1 AND moderation_status = 'ACCEPTED' AND posts.time < UTC_TIMESTAMP())\n" +
            "SELECT (p_count / active_posts) * (1 / (max_count / active_posts)) AS res FROM t1, t2, t3 WHERE t_name = :tagName", nativeQuery = true)
    double getWeightTag(@Param("tagName") String tagName);

}
