package blog.controllers;

import blog.api.response.CalendarResponse;
import blog.service.CalendarService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class ApiCalendarController {
    private final CalendarService calendarService;

    public ApiCalendarController(CalendarService calendarService) {
        this.calendarService = calendarService;
    }

    @GetMapping("/calendar")
    private ResponseEntity<CalendarResponse> getCalendar(@RequestParam int year) {
        return new ResponseEntity<>(calendarService.getCalendar(year), HttpStatus.OK);
    }


}
