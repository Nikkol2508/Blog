package blog.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PostsDTO {

    private int id;
    private long timestamp;
    private UserDTO user;
    private String title;
    private String announce;
    private int likeCount;
    private int dislikeCount;
    private int commentCount;
    private int viewCount;

    private Boolean active;
    private String text;
    private ArrayList<CommentDTO> comments;
    private List<String> tags;

    public PostsDTO() {
    }

}
