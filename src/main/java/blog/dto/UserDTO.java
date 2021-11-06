package blog.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
public class UserDTO {

    private int id;
    private String name;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String photo;

    public UserDTO() {
    }

}
