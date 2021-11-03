package blog.service;

import blog.api.response.CalendarResponse;
import blog.dto.CalendarDTOInterface;
import blog.repositorys.PostRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CalendarService {

    private final PostRepository postRepository;

    public CalendarService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    public CalendarResponse getCalendar(int year) {
        CalendarResponse calendarResponse = new CalendarResponse();
        List<Integer> years = postRepository.getYears();
        calendarResponse.setYears(years);
        List<CalendarDTOInterface> calendarPosts= postRepository.calendarPosts(year);
        HashMap<String, Integer> dateCount = new HashMap<>();
        for(CalendarDTOInterface calendarPost : calendarPosts) {
            dateCount.put(calendarPost.getDate(), calendarPost.getCount());
        }

        calendarResponse.setPosts(dateCount);
        return calendarResponse;
    }
}
