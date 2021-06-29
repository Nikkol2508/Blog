package blog.repositorys;

import blog.dto.TagDTOInter;
import blog.model.Tag;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface TagRepository extends CrudRepository<Tag, Integer> {

    @Query(value = "WITH t2 AS (SELECT COUNT(posts.id) AS max_count FROM tags JOIN tag2post ON tags.id = tag2post.tag_id JOIN posts ON posts.id = tag2post.post_id WHERE is_active = 1 AND moderation_status = 'ACCEPTED' AND posts.time < UTC_TIMESTAMP() GROUP BY tags.id ORDER BY max_count DESC LIMIT 1), \n" +
            "t1(t_name, p_count) AS (SELECT tags.name AS t_name, COUNT(posts.id) AS p_count FROM tags JOIN tag2post ON tags.id = tag2post.tag_id JOIN posts ON posts.id = tag2post.post_id WHERE is_active = 1 AND moderation_status = 'ACCEPTED' AND posts.time < UTC_TIMESTAMP() GROUP BY tags.id),\n" +
            "t3 AS (SELECT COUNT(posts.id) AS active_posts FROM posts WHERE is_active = 1 AND moderation_status = 'ACCEPTED' AND posts.time < UTC_TIMESTAMP())\n" +
            "SELECT t_name AS name, (p_count / active_posts) * (1 / (max_count / active_posts)) AS weight FROM t1, t2, t3", nativeQuery = true)
    List<TagDTOInter> getWeightTag();
}
