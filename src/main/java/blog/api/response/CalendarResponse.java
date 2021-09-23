package blog.api.response;

import lombok.Data;

import java.util.HashMap;
import java.util.List;

@Data
public class CalendarResponse {

    private List years;
    private HashMap<String, Integer> posts;

}
