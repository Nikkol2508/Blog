package blog.model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

@Entity
@Table(name = "posts")
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "is_active", nullable = false)
    private byte isActive;

    @Enumerated(EnumType.STRING)
    @Column(name = "moderation_status", columnDefinition = "enum('NEW', 'ACCEPTED', 'DECLINED') ", nullable = false)
    private ModerationStatus moderationStatus = ModerationStatus.NEW;

    @Column(name = "moderator_id")
    private int moderatorId;

    @Column(name = "user_id", nullable = false, insertable = false, updatable = false)
    private int userId;

    @Column(nullable = false)
    private long time;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String text;

    @Column(name = "view_count", nullable = false)
    private int viewCount;

    @ManyToOne
    private User user;

    public Post() {}

    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(name = "tag2post", joinColumns = {@JoinColumn(name = "post_id", columnDefinition = "INT NOT NULL")},
            inverseJoinColumns = {@JoinColumn(name = "tag_id", columnDefinition = "INT NOT NULL")})
    private List<Tag> tags = new ArrayList<>();

    public List<Tag> getTags() {
        return tags;
    }

    @OneToMany(mappedBy = "post")
    private Set<PostVoter> postVoters;

    @OneToMany(mappedBy = "post")
    private Set<PostComment> postComment;

    public Set<PostComment> getPostComment() {
        return postComment;
    }

    public int getLikeCount() {
        AtomicInteger res = new AtomicInteger();
        postVoters.iterator().forEachRemaining(pv -> {
            int voter = pv.getValue();
            if(voter == 1) res.getAndIncrement();
        });
        return res.get();
    }

    public int getDisLikeCount() {
        AtomicInteger res = new AtomicInteger();
        postVoters.iterator().forEachRemaining(pv -> {
            int voter = pv.getValue();
            if(voter == -1) res.getAndIncrement();
        });
        return res.get();
    }

    public int getCommentCount() {
        return postComment.size();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public byte getIsActive() {
        return isActive;
    }

    public void setIsActive(byte isActive) {
        this.isActive = isActive;
    }

    public ModerationStatus getModerationStatus() {
        return moderationStatus;
    }

    public void setModerationStatus(ModerationStatus moderationStatus) {
        this.moderationStatus = moderationStatus;
    }

    public int getModeratorId() {
        return moderatorId;
    }

    public void setModeratorId(int moderatorId) {
        this.moderatorId = moderatorId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getViewCount() {
        return viewCount;
    }

    public void setViewCount(int viewCount) {
        this.viewCount = viewCount;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
