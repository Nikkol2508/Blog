package blog.service;

import blog.api.response.CalendarResponse;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
public class CalendarService {
    public CalendarResponse getCalendar(Integer year) {
        CalendarResponse calendarResponse = new CalendarResponse();
        List<Integer> years = new ArrayList<>();
        years.add(LocalDate.now().getYear());
        years.add(2020);
        calendarResponse.setYears(years);
        HashMap<String, Integer> posts = new HashMap<>();
        posts.put("2021-07-18", 5);
        posts.put("2021-07-20", 12);
        posts.put("2021-07-25", 3);
        posts.put("2021-07-28", 8);
        calendarResponse.setPosts(posts);
        return calendarResponse;
    }
}
