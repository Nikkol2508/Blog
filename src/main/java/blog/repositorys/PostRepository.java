package blog.repositorys;

import blog.dto.CalendarDTOInterface;
import blog.model.ModerationStatus;
import blog.model.Post;
import org.hibernate.annotations.SQLUpdate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.PostUpdate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public interface PostRepository extends PagingAndSortingRepository<Post, Integer> {

    List<Post> findAllActiveByIsActiveAndModerationStatusAndTimeLessThan(byte isActive,
                                                                         ModerationStatus moderationStatus, long time,
                                                                         Pageable pageable);

    int countActiveByIsActiveAndModerationStatusAndTimeLessThan(byte isActive, ModerationStatus moderationStatus,
                                                                long time);

    Post findById(int id);

    @Query(value = "SELECT posts.* FROM posts LEFT JOIN post_comments ON posts.id = post_comments.post_id " +
            "WHERE is_active = :isActive AND moderation_status = :moderationStatus AND posts.time < :time " +
            "GROUP BY posts.id ORDER BY COUNT(1) DESC", nativeQuery = true)
    List<Post> findAllActiveSortComments(@Param("isActive") byte isActive, @Param("moderationStatus")
            String moderationStatus, @Param("time") long time, Pageable pageable);


    @Query(value = "SELECT posts.* FROM posts LEFT JOIN post_voters ON post_voters.post_id = posts.id " +
            "WHERE is_active = :isActive AND moderation_status = :moderationStatus AND posts.time < :time " +
            "GROUP BY posts.id ORDER BY COUNT(IF(post_voters.value = 1,1,NULL)) DESC", nativeQuery = true)
    List<Post> findAllActiveSortVoters(@Param("isActive") byte isActive, @Param("moderationStatus")
            String moderationStatus, @Param("time") long time, Pageable pageable);

    @Query(value = "SELECT posts.* FROM posts JOIN tag2post ON posts.id = tag2post.post_id " +
            "JOIN tags ON tags.id = tag2post.tag_id WHERE is_active = :isActive " +
            "AND moderation_status = :moderationStatus AND posts.time < :time " +
            "AND tags.name LIKE CONCAT('%', :query, '%')", nativeQuery = true)
    List<Post> searchByRequest(@Param("isActive") byte isActive, @Param("moderationStatus")
            String moderationStatus, @Param("time") long time, @Param("query") String query, Pageable pageable);

    @Query(value = "SELECT DISTINCT YEAR(FROM_UNIXTIME(time)) FROM posts " +
            "WHERE is_active = 1 AND moderation_status = 'ACCEPTED' " +
            "AND posts.time < UNIX_TIMESTAMP(NOW())", nativeQuery = true)
    List<Integer> getYears();

    @Query(value = "SELECT DATE(FROM_UNIXTIME(time)) AS date, COUNT(time) AS count FROM posts " +
            "WHERE YEAR(FROM_UNIXTIME(`time`)) = :year AND is_active = 1 AND moderation_status = 'ACCEPTED' " +
            "AND posts.time < UNIX_TIMESTAMP(NOW()) GROUP BY date", nativeQuery = true)
    List<CalendarDTOInterface> calendarPosts(@Param("year") int year);

    @Query(value = "SELECT posts.* FROM posts WHERE DATE(FROM_UNIXTIME(time)) = :date " +
            "AND is_active = 1 AND moderation_status = 'ACCEPTED' " +
            "AND posts.time < UNIX_TIMESTAMP(NOW())\n", nativeQuery = true)
    List<Post> getPostsByDate(@Param("date") String date, Pageable pageable);

    @Query(value = "SELECT posts.* FROM posts JOIN tag2post ON posts.id = tag2post.post_id " +
            "JOIN tags ON tags.id = tag2post.tag_id WHERE tags.name = :tag " +
            "AND is_active = 1 AND moderation_status = 'ACCEPTED' " +
            "AND posts.time < UNIX_TIMESTAMP(NOW())", nativeQuery = true)
    List<Post> getPostsByTag(@Param("tag") String tag, Pageable pageable);

    @Modifying
    @Transactional
    @Query(value = "UPDATE posts SET view_count = :viewCount WHERE id = :id", nativeQuery = true)
    void setViewCount(@Param("id") int id, @Param("viewCount") int viewCount);

}
